# SQL 삽입 공격(SQL Injection)
입력 문자열에 SQL 명령을 삽입하여 프로그램의 의도와 다른 SQL문을 실행되게 함으로써 데이터베이스를 조작하는 코드 인젝션 공격 방법이다. 

예를 들어 다음과 같이 SQL 문장이 있다고 가정하자
```java
int count = stmt.executeUpdate(
          "insert into x_board(title, contents) values('" + title + "','" + contents + "')");
```

여기서 사용자가 title 입력값으로 `aaaa`,  
contents 입력값으로 `bbbb'), ('haha', 'hoho'), ('hehe', 'puhul` 을 주었을 때

전달되는 실행 문장은 다음과 같이 들어간다.
```sql
insert into x_board(title, contents) values('aaaa','bbbb'), ('haha', 'hoho'), ('hehe', 'puhul')
```

SQL 구조를 안다는 가정하에 개인정보를 조작/악용할 수 있어 실무에서는 1순위 보안사항으로 구분된다. 

update

## 대책
> 값을 가지고 SQL을 만들면 절대 안된다. SQL과 값을 분리하여 실행한다. 

Statement말고 PreparedStatement를 사용하여 SQL 삽입 공격을 차단할 수 있다. 

PreparedStatement는 값이 놓일 자리에 in-parameter를 설정하고 들어갈 값의 타입에 따라 적절한 setXxx() 메서드를 호출하는 방식이다.

```java
PreparedStatement pstmt = con.prepareStatement(
  "insert into x_board(title, content) values(?,?)");

pstmt.setString(1, 'aaa');
pstmt.setString(2, 1111);
```
- prepareStatement() 메서드는 주어진 SQL문을 값과 분리하여 DBMS가 이해할 수 있는 형식으로 미리 변환(Compile)한다. 
- 값이 들어갈 자리는 ?로 정해두고 따로 전달한다. 이를 in-parameter라고 한다. 
- SetXxx(a, b) 메서드는 in-parameter 순서와, 들어갈 값을 파라미터로 받는다. [a = in-parameter 순서, b = 값]
- 이미 SQL을 준비한 상태이기 때문에 실행할 때는 SQL문을 전달할 필요가 없고 SetXxx()로 설정된 값은 단순 텍스트로 처리한 후 전달되기 때문에 SQL 인젝션을 차단할 수 있다. 


# 자식 테이블의 데이터를 함께 입력할 때 문제점
부모 테이블에 값을 입력함과 동시에 자식 테이블에 값을 입력하려고 하면, 해당 부모 테이블의 PK값을 알아야 한다. PK가 Foreign Key로서 자식 테이블과 연결되어 있기 때문이다.

문제는 작성한 부모 테이블의 PK값이 자동생성되는 컬럼이라면 입력한 후 PK값이 뭔지 알 수가 없다. 더구나 여러명이 동시에 입력하는 멀티 스레드 환경이라면 더더욱 select로 PK값을 조회하면 안된다. 

## 대책
PreparedStatement 객체를 얻을 때 자동 생성된 PK값 리턴 여부 옵션을 파라미터로 지정한다. 
```java
PreparedStatement stmt = con.prepareStatement( 
  "insert into x_board(title,contents) values(?,?)", 
  Statement.RETURN_GENERATED_KEYS);
```

insert 수행 후 자동생성된 PK값은 따로 요구해야 한다. getGeneratedKeys() 메서드를 통해 ResultSet 객체로 받을 수 있다. 
```java
try (ResultSet rs = stmt.getGeneratedKeys()) {
  rs.next(); 
  // Insert를 여러번 하면 반복문으로 PK값을 여러번 받아야 한다.

  int pk = rs.getInt(1); 
  // int pk = rs.getInt("board_id");
  ```

# auto commit의 문제점(트랜잭션 적용)
DriverManager가 리턴한 Connection은 autocommit의 기본 상태가 true로 설정되어 있다. 따라서 DML(insert, update, delete)을 실행하면 즉시 실제 테이블에 적용되어 버린다.

이는 여러 DML이 한 작업 단위로 묶이는 경우 문제가 발생한다. insert/update/delete 작업을 수행하다가 작업이 실패해도 이전에 수행한 작업이 유효한 상태로 남아있기 때문이다. 

예를 들면 제품을 주문하는 경우에, 주문 정보를 주문 테이블에 입력한 후 결제 정보를 결제 테이블에 입력할 때 실패한다면, 주문 정보도 같이 취소되어야 하는데 주문 정보를 그대로 남기 때문에 돈을 내지 않고 주문이 완료될 수 있기 때문이다. 

따라서 연속적으로 수행하는 여러 개 DML 작업을 한 작업 단위로 묶고, 작업을 모두 성공했을 때 한꺼번에 적용하고 실패하면 모두 취소하는 방식인 `트랜잭션(transaction)`을 적용해야 한다. 

## 대책
작업을 트랜잭션으로 다루려면 autocommit을 false로 설정하여 수동 상태로 만들고 작업이 완료된 후 commit()하면 된다.
```java
con.setAutoCommit(false);

stmt.setString(...)
stmt.executeUpdate();
con.commit();
```
작업에 실패하여 commit()을 호출하지 않고 커넥션 객체를 닫으면 모든 작업 결과를 취소하기 때문에 rollback()할 필요는 없다. 만약 커넥션을 공유하는 상황이라면 예외가 발생할 때 rollback()을 명시적으로 호출해야 한다.  


