# JDBC API와 JDBC Driver
**JDBC API**
자바에서 제공하는 인터페이스 및 클래스로 Driver 클래스
```
ex) java.sql.x, javax.sql.x
Driver
Connection
Statement
PrepareStatement
ResultSet
```

**JDBC Driver(.jar)**
vendor에서 제공하는 인터페이스 구현체 및 보조클래스
```
ex) MySQL Driver
com.mysql.cj.jdbc.Driver
com.mysql.cj.jdbc.ConnectionImpl
com.mysql.cj.jdbc.StatementImpl
...
```

# JDBC 프로그래밍
<그림>JDBC 구현체 간의 관계

## 1. JDBC 드라이버 준비(등록)
JDBC Driver 파일(*.jar)은 JDBC API 규칙에 따라 작성되었으며, DBMS에 연결을 대행하는 클래스를 포함하고 있다. 이를 사용하려면 JDBC Driver 파일을 다운로드 받아 프로젝트의 CLASSPATH에 등록해야 한다. gradle에서는 의존 라이브러리를 사용하기 때문에 mvnrepository.com 또는 search.maven.org에서 라이브러리 정보를 dependencies에 추가만 해주면 된다. 

java.sql.Driver 규칙에 따라 정의된 클래스를 로딩되면 Driver 구현체를 생성하여 DriverManager에 등록해야 한다. DriverManager는 DBMS의 Driver 구현체를 찾아 작업을 위임한다.

### JDBC 드라이버 등록 방법1
**직접 Driver 구현 객체를 생성하고 직접 등록**  
```java
// Driver 구현체 생성
java.sql.Driver mysqlDriver = new com.mysql.jdbc.Driver();

// DriverManager에 인스턴스를 등록
DriverManager.registerDriver(mysqlDriver);
```

### JDBC 드라이버 등록 방법2
**Driver 구현 객체 생성과 자동 등록**  
- Driver 구현체가 로딩될 때 java.sql.Driver 인터페이스를 구현한 클래스라면 static 블록에서 인스턴스를 만들어 자동으로 등록해준다. 따라서 개발자가 직접 등록할 필요가 없다.
```java
new com.mysql.jdbc.Driver();

//Driver 클래스 로딩 시 실행되는 static block
  static {
    try {
      DriverManager.registerDriver(new Driver());
    } catch (SQLException e) {
      // eat
    }
  }
```
### JDBC 드라이버 등록 방법3
**Driver 구현 클래스 로딩과 자동 등록**  
- 2번과 동일하게 java.sql.Driver 인터페이스를 구현한 클래스를 로딩하면 해당 클래스에서 자신을 자동으로 DriverManager에게 등록할 것이다. Class.forName() 메서드로 클래스 로딩시키는 방법이다.
```java
Class.forName("com.mysql.jdbc.Driver");
```

### JDBC 드라이버 등록 방법4
**Driver 구현체 자동 로딩**  
DriverManager를 사용할 때, JDriverManager 는 다음 절차에 따라 Driver 구현체를 찾아서 자동으로 로딩한다.
```
1. java.sql.Driver 클래스의 서비스 제공자를 찾아 로딩한다.
2. jar 파일 안에 META-INF/services/java.sql.Driver 파일을 찾는다.
3. 이때 JVM은 'service-provider loading' 절차에 따라 이 파일에 등록된 클래스를 로딩한다.
```

jar 파일에 해당 정보가 있다면 개발자가 따로 java.sql.Driver 구현체를 명시적으로 등록하지 않아도 된다.


## 2. DBMS 연결(Connection)
DriverManager에게 연결할 DBMS의 정보(jdbc URL, username, password)를 주면 DBMS의 Driver의 객체를 찾아 connect()를 호출한다. 이때 정보를 넘겨주는 메서드는 `getConnetion()`이며 Connection 객체를 리턴한다. 
```java
java.sql.Connection con = DriverManager.getConnection(
  "jdbc:mysql://localhost:3306/studydb", // jdbcURL
  "study", // username
  "Bitcamp!@#123" // password
);
```

## 3. DBMS에 SQL문 보내기 
DBMS에 SQL문을 보내려면 Connection 객체로부터 java.sql.Statement 객체를 얻어야 한다. Statement 객체는 SQL문을 DBMS의 형식에 따라 인코딩하여 서버에 전달하는 일을 한다. 
```java
try (java.sql.Connection con = java.sql.DriverManager.getConnection(
  "jdbc:mysql://localhost:3306/studydb",
  "study","Bitcamp!@#123");
  java.sql.Statement stmt = con.createStatement();) {...}
```
### insert/update/delete
DML 관련 SQL문은 Statement의 executeUpdate() 메서드를 사용해 전송한다. 리턴값은 변경된 값의 개수(int)이다.
```java
//insert
int count = stmt.executeUpdate(
    "insert into x_board(title,contents) values('제목10','내용'),('제목11','내용')");

System.out.printf("%d 개 입력 성공!", count);

//update
int count = stmt.executeUpdate(
    "update x_board set"
    + " view_count = view_count + 1"
    + " where board_id = 4");
System.out.printf("%d 개 변경 성공!", count);

//delete
int count = stmt.executeUpdate(
    "delete from x_board where board_id = 7");
System.out.printf("%d 개 삭제 성공!", count);
```

### select
DQL 관련 SQL문은 Statement의 executeQuery() 메서드를 사용해 전송한다. 리턴값은 서버에서 데이터를 가져오는 일을 할 객체인 ResultSet이다.

ResultSet은 결과가 아니다. 서버에서 결과를 가져오기 위한 도구이다.  

가져온 값은 next()로 객체 단위로 읽을 수 있으며
데이터를 가져오는 일을 하는 getXxx() 메서드로 다양한 타입으로 데이터를 받을 수 있다.

```java
try (
  java.sql.Connection con = DriverManager
  .getConnection("jdbc:mysql://localhost:3306/studydb","study","Bitcamp!@#123");
  java.sql.Statement stmt = con.createStatement();
  java.sql.ResultSet rs = stmt.executeQuery(
  "select * from x_board order by board_id desc");) {
    if (rs.next) {
      System.out.printf("%d, %s, %s, %s, %d\n",
      rs.getInt("board_id"),
      rs.getString("title"),
      rs.getString("contents"),
      rs.getDate("created_date"),
      rs.getInt("view_count"));
    }
  }
```



