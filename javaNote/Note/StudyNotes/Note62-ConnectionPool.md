# Connection Pool
## 커넥션을 공유하면 발생하는 문제
여러 스레드가 같은 커넥션을 공유할 경우 스레드의 작업이 간섭받을 수 있다. 

같은 커넥션을 공유하는 경우 임시 DB, 데이터베이스 테이블도 공유하기 때문이다.

예를 들어 트랜잭션으로 묶어 작업을 관리한다면, 내 작업이 완료되어 커밋하기 전 다른 클라이언트에서 롤백하는 경우 같은 커넥션을 공유하기 때문에 내 작업도 같이 롤백되어버린다.

## 해결책 : 스레드별로 커넥션 생성
위와 같은 커넥션 공유의 문제점의 단순 해결책은 스레드마다 고유의 커넥션을 사용하는 것이다.  

**1. SQL 실행할 때마다 Connection 생성**
전송하는 SQL문 마다 DB Connection을 생성
```java
public void add(Member member) {
  Connection con = null;
  try {
    con = DriverManager.getConnection(
        "jdbc:mysql://localhost/studydb", "study", "Bitcamp!@#123");
    con.setAutoCommit(false);

  try (PreparedStatement pstmt = con.prepareStatement(
      "insert into members(email,name,password) values(?,?,sha2(?,256))")) {
    pstmt.setString(1, member.getEmail());
    pstmt.setString(2, member.getName());
    pstmt.setString(3, member.getPassword());
    pstmt.executeUpdate();
  }
  } catch (Exception e) {
    throw new DaoException("데이터 입력 오류", e);
  } finally {
    try {
      con.setAutoCommit(true);
  }  catch (Exception e2) {}
  } try {
    con.close();
  } catch (Exception e) {
  }
}
```
이 방법은 가장 다른 스레드의 작업에 간섭하지 않는 가장 단순한 방법이다.  

Connection 생성 시 사용자 인증(Authentication)과 권한 조회(Authorization)의 절차를 거친다.

위 방법의 경우 SQL문을 실행할 때마다 Connection을 생성하기 때문에 가비지가 많이 발생하고 또 매번 위의 Auth 과정을 반복한다면 연결에 시간이 지체된다.  

가장 핵심은 하나의 스레드에 여러 Connection을 사용한다는 것이다. 즉 트랜잭션으로 묶을 수가 없다.

<br>

**2. 스레드 당 1개의 Connection 유지**
ThreadLocal 변수와 스레드에 대해 Connection을 리턴해주는 클래스를 사용
```java
public class ThreadConnection {
  private String jdbcURL;
  private String username;
  private String password;

  // 개별 스레드용 DB 커넥션 저장소
  private static final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

  public ThreadConnection(String jdbcURL, String username, String password) {
    this.jdbcURL = jdbcURL;
    this.username = username;
    this.password = password;
  }

  public Connection get() throws Exception{
    // 현재 스레드에 보관중인 Connection 객체를 꺼낸다.
    Connection con = connectionThreadLocal.get();

    // 없다면 새로 만든다.
    if (con == null) {
      con = DriverManager.getConnection(jdbcURL, username, password);

      // 현재 스레드에서 나중에 또 사용할 수 있도록 현재 스레드에 보관한다.
      connectionThreadLocal.set(con);
      
      System.out.printf("&s: DB 커넥션 생성\n", Thread.currentThread().getName());
    } else {
      System.out.printf("&s: 기존에 보관한 DB 커넥션 사용\n", Thread.currentThread().getName());
    }
    return con;
  }

  public void remove() {
    // 현재 스레드에 보관중인 Connection 객체를 제거한다.
    Connection con =connectionThreadLocal.get();
    if (con != null) {
      try {
        con.close();
      } catch (Exception e) {
      }
      connectionThreadLocal.remove();
      
      System.out.printf("&s: DB 커넥션 제거\n", Thread.currentThread().getName());
    }
  }
}
```
ThreadLocal 변수는 현재 스레드에 대해 커넥션 객체를 보관하고 제거하는 등의 역할을 하는 객체이다. 

위 방법은 ThreadLocal을 통해 DAO에서 커넥션을 get() 메서드로 받아 하나의 스레드에 대해 같은 커넥션을 공유하도록 할 수 있다. 

위 방법도 아직 문제점이 남아있다. DAO에서 commit/rollback이 메서드 단위로 실행되기 때문에 트랜잭션을 제대로 다룰 수 없다. 

**3. DAO를 호출하는 쪽(Business Logic 객체)에서 트랜잭션 제어**
DAO의 메서드는 기능에 따라 여러 개를 하나의 트랜잭션으로 묶어 실행해야 할 때가 있다. 따라서 트랜잭션은 DAO를 호출하는 Business Logic 객체에서 제어해야 한다. 

**4. DB 커넥션 재사용: 커넥션풀**
3번의 방법은 스레드당 커넥션, 트랜잭션 문제를 해결했지만 자원의 낭비라는 문제가 남아있다. 

예를 들어 Thread 수만큼 DB Connection이 생성하고 Client가 DB 작업을 요청하지 않으면 사용하지 않는다. 즉 Connection을 그대로 유지하기 때문에 자원관리 측면에서 비효율적이다.

따라서 커넥션풀을 사용하여 커넥션을 사용한 후 재사용할 수 있도록 해야 한다.

```java
public class DBConnectionPool {
  private String jdbcURL;
  private String username;
  private String password;
  // DB 커넥션 목록
  ArrayList<Connection> connections = new ArrayList<>();

  // 개별 스레드용 DB 커넥션 저장소
  private static final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

  public DBConnectionPool(String jdbcURL, String username, String password) {
    this.jdbcURL = jdbcURL;
    this.username = username;
    this.password = password;
  }

  public Connection getConnection() throws Exception{
    // 현재 스레드에 보관중인 Connection 객체를 꺼낸다.
    Connection con = connectionThreadLocal.get();


    if (con == null) {
      // 스레드에 보관된 Connection이 없다면,

      if (connections.size() > 0) {
        // 스레드풀에 놀고 있는 Connection이 있다면,
        con = connections.remove(0);
        System.out.printf("%s: DB 커넥션풀에서 꺼냄\n", Thread.currentThread().getName());

      } else {
        // 스레드풀에도 놀고 있는 Connection이 없다면,
        // 새로 Connection을 만든다.
        con = DriverManager.getConnection(jdbcURL, username, password);
        System.out.printf("%s: DB 커넥션 생성\n", Thread.currentThread().getName());
      }

      // 현재 스레드에서 Connection을 보관한다.
      connectionThreadLocal.set(con);

    } else {
      System.out.printf("%s: 스레드에 보관된 DB 커넥션 리턴\n", Thread.currentThread().getName());
    }
    return con;
  }

  public void returnConnection(Connection con) {
    // 현재 스레드에 보관중인 Connection 객체를 제거한다.
    connectionThreadLocal.remove();

    // Connection을 커넥션풀에 반납한다.
    connections.add(con);

    System.out.printf("%s: DB 커넥션을 커넥션풀에 반납\n", Thread.currentThread().getName());
  }
}
```
위의 예제의 실행흐름을 설명해보자면 다음과 같다. 
- Business Logic 객체에서 처음 호출할 때는 현재 스레드의 커넥션이 null이기 때문에 커넥션이 없으면 생성하여 리턴하고 존재한다면 커넥션풀에서 꺼내 ThreadLocal에 보관하고 리턴한다. 
- 그 다음 DAO에서도 getConnection()을 호출하면 ThreadLocal에 저장되어 있는 커넥션을 리턴한다. 
- DAO에서 작업이 모두 끝나고 Business Logic 객체로 돌아오면 returnConnection()를 호출하여 커넥션풀에 커넥션을 반납한다. 

이와 같은 방법을 사용하면 각 스레드마다 고유의 커넥션을 생성하지 않고 재사용하여 자원을 효율적으로 관리할 수 있다. 
