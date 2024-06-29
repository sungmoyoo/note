# DB커넥션과 프록시 패턴
DB 커넥션풀을 그대로 사용하는 기존의 코드는 Business Logic에서 Connection 객체를 사용한다. 이는 단순히 Connection Pool에 Connection 객체를 반납하기 위해서이다. 

프록시 패턴을 사용하면 기존 구현체의 역할을 그대로 하되 추가 로직 처리한 뒤 기존 구현체의 메소드를 실행할 수 있다. 

이때 우리가 원하는 로직은 커넥션 구현체를 close()할 때 커넥션풀에 커넥션 객체를 반환하는 것이다.

따라서 close()에 조건문을 걸어 close()를 호출할 때 커넥션풀에 반납하도록 하면 된다.

단 프록시에서 커넥션풀을 사용한다면 쌍방 참조가 되어 강결합되는 문제가 발생할 수 있다.

이는 ConnectionPool 인터페이스를 통해 쌍방참조 문제를 해결할 수 있다.
- 프록시: ConnectionPool 사용
- 커넥션풀: 인터페이스 구현, 프록시 사용 

```java
// Proxy
public class ConnectionProxy implements Connection {
  private Connection original;
  private ConnectionPool connectionPool;

  public ConnectionProxy(Connection original, ConnectionPool connectionPool) {
    this.original = original;
    this.connectionPool = connectionPool;
  }

  @Override
  public void close() throws SQLException {
    if (original.getAutoCommit()) {
      // DB 커넥션풀에 반납한다.
      connectionPool.returnConnection(this);
    }
  }
  // 아래는 위임 메서드 구현
  ...
  ...
}
```

# 트랜잭션 제어 기능 분리
Business Logic에서 트랜잭션을 제어하기 위해 Connection 객체를 사용하고 있는데 이를 분리하여 트랜잭션 매니저로 만들면 보다 간결해진다.

```java
// 트랜잭션 매니저
public class TransactionManager {

  ConnectionPool connectionPool;

  public TransactionManager(ConnectionPool connectionPool) {
    this.connectionPool = connectionPool;
  }

  public void startTransaction() throws Exception {
    connectionPool.getConnection().setAutoCommit(false);
    System.out.printf("[%s] 트랜잭션 시작\n", Thread.currentThread().getName());
  }

  public void commit() throws Exception {
    connectionPool.getConnection().commit();
    complete();
  }

  public void rollback() throws Exception {
    connectionPool.getConnection().rollback();
    complete();
  }

  private void complete() throws Exception{
    Connection con = connectionPool.getConnection();
    con.setAutoCommit(true);
    con.close();
    System.out.printf("[%s] 트랜잭션 종료\n", Thread.currentThread().getName());
  }
}
```
위와 같이 분리된 트랜잭션 매니저를 Business Logic에서 TransactionManager를 생성자로 받으면 메서드 호출만으로 제어할 수 있는 장점이 있다.

