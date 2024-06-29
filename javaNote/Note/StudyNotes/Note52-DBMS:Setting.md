# DBMS
**Database ManageMent System**

**Database**
조직화된 데이터 모음 또는 데이터 저장소 유형, DBMS에 의해 관리된다.

## DBMS 통신 방법

Client Application과 DBMS가 데이터를 주고 받기 위해 통신을 하려면 DBMS 전용 프로토콜을 사용해서 소켓통신을 해야한다. 문제는 DBMS사에서 전용 프로토콜을 공개하지 않는다.프로토콜도 회사가 가진 기술이자 자산이기도 하고 보안적인 측면에서 위험하기 때문이다. 

따라서 각 DBMS에서는 프로토콜을 공개하지 않으면서 DBMS와 통신할 수 있는 전용 Client API를 제공한다.  
DBMS마다 DBMS Client API가 다르며 (ex/ Oracle Client API) 이를 `Vendor API` 또는 `Native API`라고 한다.  
<img src="../../img/VendorAPI.png">


Vender API 사용법 또한 다르기 때문에 여러 DBMS를 사용한다면 DBMS마다 따로 App을 작성해야 하는 문제는 그대로이다. 이는 프로그래밍 일관성을 떨어뜨리고 개발비를 증가하는 문제가 발생한다.

그래서 등장한 것이 MS사에서 개발한 ODBC API이다. ODBC는 데이터베이스의 접근하는 방법을 통일시킨 API(문서, 규격)이다. MS사는 DBMS사들과 컨소시엄을 구성했고 DBMS사들은 이 API에 따라서 자사의 Vendor API와 통신할 수 있는 ODBC Driver를 만들어 제공하였다. 클라이언트는 Driver만 설치하면 호출 규칙이 모두 동일하기 때문에 App 코드를 한번만 작성하면 된다. 
<img src="../../img/ODBCAPI.png">

이 Vendor API와 ODBC API는 C와 C++ 기반으로 제작되었다. 자바에서는 C/C++ API 규격에 따라 만들어진 Driver를 직접 호출할 수 없다. 
그래서 자바 애플리케이션이 데이터베이스와 통신할 수 있도록 만든 API가 바로 JDBC이다. 

### JDBC Driver types
JDBC Driver는 크게 4가지 type으로 나뉜다.

**Type1 Driver**
Type1 Driver는 JDBC Driver가 ODBC Driver에 접근하는 통로를 제공하는 구조이다.  
JDBC Driver가 App과 ODBC Driver 사이에 중간 다리 역할을 하기 때문에 ODBC-JDBC라고도 한다.
<img src="../../img/Type1Driver.png">

*장점
- Local에 설치된 ODBC 드라이버를 사용하기 때문에 기존 ODBC 드라이버 어느 것이라도 연결할 수 있다.
- JRE에 기본 포함되어 있어 따로 라이브러리를 설치할 필요가 없다.

*단점
- 실행속도가 느리다.

**Type2 Driver**
Type2 Driver는 JDBC API 규격에 따라 만든 각 DBMS사의 JDBC Driver가 기존 ODBC의 역할을 하는 구조이다. 

<img src="../../img/Type2Driver.png">

*장점
- local에 설치된 Native API를 사용하므로 Type1에 비해 빠르다.

*단점
- Vendor에서 제공하는 JDBC Driver를 클라이언트에 따로 설치해야 한다.
- DBMS를 교체하면 Local에 설치된 JDBC도 교체해야 한다. 


**Type3 Driver**
Type3 Driver는 Local에서 중계 서버의 JDBC Driver를 호출하면 중계 서버가 JDBC Driver 호출을 대신해주는 구조이다. Middleware server라고 부른다. 

<img src="../../img/Type3Driver.png">

*장점
- DBMS를 교체할 때 미들웨어 서버측만 JDBC 드라이버 파일을 교체하면 되기 때문에 local에서는 관리할 필요가 없다. 즉 특정 DBMS에 종속되지 않는다.


*단점
- 3rd-party의 Driver를 local에 설치해야 한다.
- 미들웨어 서버 구매 비용이 발생한다. 

**Type4 Driver**
순수 자바 드라이버이자 Native Driver이다. 따라서 App에서 Driver를 호출하면 바로 DBMS와 통신하는 것이 가능한 구조이다. 
<img src="../../img/Type4Driver.png">

*장점
- Local에 c/c++ 기반의 Client API를 설치할 필요가 없다.
- 오버헤드가 발생하지 않아 가장 빠르다.

*단점
- Vendor에서 제공하는 JDBC Driver를 따로 설치해야 한다.
- DBMS를 교체하면 Local에 설치된 JDBC도 교체해야 한다. 

# MySQL
## MySQL 설치
macOS 기준
1. brew 최신버전 업데이트
```
brew update 
```
2. MySQL 설치
```
brew install mysql
```

3. 서버 시작
brew service 명령어를 통해 서버를 시작하면 서버가 백그라운드에서도 실행되며 시스템 부팅 시에도 자동으로 시작한다.
```
brew services start mysql 
```
<br>

mysql.server 명령어는 MySQL에서 제공하는 스크립트로 서버를 수동으로 시작한다. 시스템 부팅 시 자동으로 시작되지 않는다.
```
mysql.server start
```

4. 초기 환경 세팅
초기 환경설정에 관한 질문을 묻는다.
```
mysql_secure_installation

// 작업 질문
1. VALIDATE PASSWORD COMPONENT
복잡한 비밀번호를 설정할 것인지 묻는다.

2. set the password
비밀번호 입력/확인 과정

3. Remove anonymous user?
익명사용자가 계정을 삭제할 것인지 묻는다.

4. Disallow root login remotely?
원격 접속을 허용할 것인지 묻는다.

5. Remove test database and access to it?
test DB를 삭제할 것인지 묻는다.

6. Reload privilege tables now? 
현재까지 변경한 내용을 적용하여 테이블을 다시 로드할지 여부를 묻는다.
```

## MySQL 사용자 추가 
1. root관리자로 접속
```
mysql -u root -p // 로컬 MySQL 서버에 접속
mysql -h 서버주소 -u root -p // 원격 MySQL 서버에 접속
```

2. 사용자 추가
```
CREATE USER '사용자아이디'@'서버주소' IDENTIFIED BY '암호';

// 로컬에서만 접속할 수 있는 사용자 추가 예
CREATE USER 'study'@'localhost' IDENTIFIED BY '12345678';
// 어떠한 호스트에서든 접속이 가능한 사용자 추가 예
CREATE USER 'study'@'%' IDENTIFIED BY '12345678';
```

3. 사용자 권한 부여
```
GRANT ALL ON 데이터베이스명.* TO '사용자아이디'@'서버주소';

GRANT ALL ON studydb.* TO 'study'@'localhost';
```

# DBMS 사용 
<img src="../../img/UseDBMS.png">

**1.직접 사용** 
터미널, 명령프롬프트에서 CLI를 사용하여 DBMS에 접속하고 쿼리를 실행하는 방법이 주로 사용된다. 사용자가 로컬에 Client Application을 설치하여 DBMS와 통신하는 구조, 즉 C/S Architecture이다. 

### JDBC API 프로그래밍
**1. JDBC 드라이버 준비**
java.sql.Driver 구현체를 생성하여 DriverManager에 등록한다. 
```java
Driver driver = new com.mysql.cj.jdbc.Driver();
DriverManager.registerDriver(driver);
```
- "ServiceProvider" 명세에 따라 JDBC Driver 파일에 들어있는 설정대로 Driver 객체를 자동으로 생성하고 등록한다. 즉 생략할 수 있다.

왜냐? jar를 만든 개발자가 META-INF에 Services 폴더에 로딩할 클래스를 적어놓았다. 그러면 DriverManager가 여기에 적혀있는 클래스를 로딩한다. 여기서 적혀있는 클래스가 로딩되면 Driver 객체를 생성하기 때문에 생략해도 되는 것이다. 

**2. DriverManager를 통해 DBMS 연결을 요청**
```java
Connection con = DriverManager.getConnection("jdbc:mysql://호스트주소/","아이디","패스워드");
```

**3. SQL을 DBMS에 전달해줄 객체 준비**
```java
Statement stml = con.creatStatement();
```
**4. SQL을 DBMS에 전달**
```java
ResultSet rs = stmt.executeQuery(SQL문)
```
이 때 ResultSet은 데이터를 가져올 때 필요한 정보를 가진다. 데이터 그 자체가 아니다. 

**5. 실행결과 데이터 중 한개의 데이터를 서버에서 가져오기**
여기서 한개의 데이터는 record 또는 row 또는 tuple이라고 한다. 
```java
rs.next();
```

**6. 서버에서 가져온 레코드에서 컬럼 값 꺼내기**
```java
rs.getInt();
rs.getString();
...
```