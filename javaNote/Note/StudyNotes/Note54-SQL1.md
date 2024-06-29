# SQL(Structured Query Language)
DBMS에게 내릴 명령을 작성하는 프로그래밍 언어

>표준 문법 + DBMS 전용 문법 = 실무 SQL
- 표준문법은 DBMS에 따라 지원율이 다르다.
- DBMS 전용 문법은 DBMS마다 다르다.

## SQL 문법 유형
**DDL(Data Definition Language)**
DB 객체(테이블, 뷰, 함수, 프로시저, 트리거) 등을 정의/삭제/변경하는 문법

**DML(Data Manipulation Language)** 
데이터를 입력/변경/삭제하는 문법

**DQL(Data Query Language)**
데이터를 조회하는 문법

# DDL
## 테이블 생성
```sql
create table 테이블명 (
  컬럼명 타입 null 여부 옵션,
  컬럼명 타입 null 여부 옵션 default 옵션,
  ...
);
```
- null을 허용할 경우 데이터를 입력하지 않아도 된다. 즉 값을 null로 설정할 수 있다.
- not null로 지정하면 데이터를 입력하지 않았을 때 입력/변경이 거절되어 에러(cannot be null)가 발생한다. 
- 컬럼에 default 옵션이 있는 경우 생략하면 default로 지정한 값이 사용된다. null로 지정하면 기본값이 사용되지 않는다.

## 테이블 삭제
```sql
drop table 테이블명
```

## 컬럼 타입
**1. int**
- 4바이트 크기의 정수 값
- 기타: tinyint(1바이트), smallint(2바이트), mediumint(3바이트), bigint(8바이트)

**2. float**
- 부동소수점

**3. numeric**
- 전체 자릿수와 소수점 이하의 자릿수를 정밀하게 지정할 수 있는 타입
- numeric(n,e) : 전체 n자릿수 중에서 소수점은 e자릿수
- numeric : numetic(10, 0)과 같다.

**4. char(n)**
- 최대 n개의 문자를 저장 (0~255)
- 고정 크기이다. 
- 메모리 크기가 고정되어서 검색이 빠르다.

**5. varchar(n)**
- 최대 n개의 문자를 저장 (0~65535/바이트)
- n값은 문자집합에 따라 최대 값이 다르다. 
- 가변 크기이다.
- 한 문자를 저장하면 한 문자 만큼 크기의 메모리를 차지한다. 
- 메모리 크기가 가변적이라 검색이 오래 걸린다. 

>*DBMS 중에는 고정 크기인 컬럼의 값을 비교할 때 빈자리까지 검사하는 경우도 있다.
즉 c1='abc'에서는 데이터를 찾지 못하고, c1='abc  '여야만 데이터를 찾는 경우가 있다. 그러나 mysql은 고정크기 컬럼이더라도 빈자리를 무시하고 데이터를 찾는다.

**6. text**
- 긴 텍스트를 저장할 때 사용하는 타입
- 오라클의 경우 long, CLOB 타입이 있다.

**7. date**
- 날짜 정보를 저장할 때 사용
- 년, 월, 일 정보를 저장

**8. time**
- 시간 정보를 저장
- 시, 분, 초 정보를 저장

**9. datetime**
- 날짜와 시간 정보를 함께 저장

**10. boolean**
- 정수 1 또는 0으로 표현
- Y 또는 N으로도 표현
- true/false도 가능 => 저장할 때 1, 0으로 변경
- 실제 컬럼을 생성할 때 tinyint(1) 로 설정한다.

### key 컬럼
데이터를 구분할 때 사용하는 값(컬럼들의 집합), 즉 데이터베이스에서 레코드를 구별할 수 있는 기준이 되는 속성의 컬럼이다. 

**후보키(Candidate key)**
키들 중에서 유일성, 최소성, not null 조건을 만족하는 키

>*유일성: 중복값이 존재하지 않는 속성, 예를 들면 학번이 키라고 가정한다면, 각 학생이 고유한 학번을 가졌을 때 유일성이 있다고 할 수 있다.
>
>*최소성: 키를 구성하는 열의 수가 최소여야 하는 속성, 예를 들어 학생 테이블에서 학번과 학과 번호의 조합이 키인 경우 학번만으로 고유성을 만족하므로 학과 번호를 키에서 제외시켜 최소한의 컬럼 수로 줄인 것이 최소성이다. 

**복합키**
둘 이상의 컬럼으로 이루어진 키

**기본키(Primary key;주키)**
candidate key 중에서 관리자가 사용하기로 결정한 키,테이블에서 레코드를 유일하게 식별할 수 있는 키를 의미한다. 

**대안키(alternate key)**
candidate key 중에서 primary key로 선택된 키를 제외한 나머지 키. primary 키는 아니지만 primary key처럼 데이터를 구분하는 용도로 대신 사용할 수 있어서 `대안키`라고 부른다. 

**인공키(artificial key = surrogate key)**
primary key로 사용하기에 
적절한 컬럼이 없다면(조건을 만족하는 키 존재x, 기본키가 복합키인 경우) 
또는 데이터 무결성을 유지하기 위해 
일련번호를 저장할 정수 타입의 컬럼을 추가하여 기본키로 사용한다.

아이디, 이메일 등과 같은 컬럼은 유일성을 가지지만 값이 변경, 삭제 될 수 있다. PK값을 변경하면 그 값을 사용한 모든 데이터에 영향을 끼치기 때문에 회원번호와 같이 기본키 역할을 담당하는 인공 키를 활용하여 무결성을 유지하는 게 바람직하다는 의미이다. 
 

### key 선언
```sql
-- primary key 기본 키 선언
create table test1(
  no int primary key, 
  name varchar(20),
  age int,
  kor int,
  eng int,
  math int
);

-- 두개 이상의 컬럼을 묶어서 키를 선언하고 싶다면 `constraint` 제약조건이름 primary key(컬럼명, 컬럼명)을 사용한다
-- constraint
create table test1(
  name varchar(20),
  age int,
  kor int,
  eng int,
  math int,
  constraint test1_pk primary key(name, age)
  );

-- unique 대안 키 선언
create table test1(
  no int primary key,
  name varchar(20),
  age int,
  kor int,
  eng int,
  math int,
  constraint test1_uk unique (name, age)
  );

-- 정의 다음에 제약
create table test1(
  no int,
  name varchar(20),
  age int,
  kor int,
  eng int,
  math int
);

alter table test1
  add constraint test1_pk primary key (no);

alter table test1
  add constraint test1_uk unique (name, age);
```

## index
SQL의 index는 테이블의 컬럼에 대한 검색(쿼리) 성능 향상을 위한 데이터베이스 객체이다. 쉽게 말해 검색 조건으로 사용되는 컬럼의 경우 따로 정렬해두어 보다 빨리 찾도록 하는 문법이다. 

index는 특정 컬럼의 값을 정렬한 데이터 정보를 별도의 파일로 생성한다. 인덱스로 지정된 컬럼의 값이 추가/변경/삭제되면 인덱스 정보도 같이 갱신되므로 입력/변경/삭제의 속도가 느려지는 문제가 있다. 

### 인덱스 유형
regular, fulltext , unique, ...

```sql
-- name 컬럼을 조건으로 검색 성능은 향샹, 추가/삭제/변경 속도는 하락
create table test1(
  no int primary key,
  name varchar(20),
  age int,
  kor int,
  eng int,
  math int,
  constraint test1_uk unique (name, age),
  fulltext index test1_name_idx (name)
);
```

## 테이블 변경
```sql
alter table 테이블명
  add column ... -- 컬럼 추가
  add constraint test1_pk primary key(...) -- 제약 추가
  add fulltext index idx (...) -- 인덱스 컬럼 지정

  modify column ...not null -- 컬럼 옵션 변경/추가
```

## 컬럼 값 자동 증가
특정 컬럼의 값을 자동으로 증가하게 선언할 수 있다. 단 반드시 primary key나 unique여야 한다. 
- 1씩 증가한다.
- 임의로 컬럼의 값을 지정할 수 있다. 단 중복x
- 컬럼의 값을 생략하면 마지막 값을 증가시킨다. 
- 중간에 컬럼을 삭제해도 마지막 값을 기준으로 증가한다. 
```sql
create table test1(
  no int not null auto_increment; 
)
```

## 뷰(view)
조회 결과를 테이블처럼 사용하는 문법, select 문장이 복잡할 때 뷰로 정의해놓고 사용하면 편하다. 
```sql
-- 테이블 생성
create table test1 (
  no int primary key auto_increment,
  name varchar(20) not null,
  class varchar(10) not null,
  working char(1) not null,
  tel varchar(20)
);

-- 데이터 입력
insert into test1(name,class,working) values('aaa','java100','Y');
insert into test1(name,class,working) values('bbb','java100','N');
insert into test1(name,class,working) values('ccc','java100','Y');
insert into test1(name,class,working) values('ddd','java100','N');
insert into test1(name,class,working) values('eee','java100','Y');

-- 뷰 생성
create view worker
  as select no, name, class from test1 where working = 'Y';

-- 조회
select * from worker;
```
as 를 통해 select문의 결과를 기반으로 뷰를 만들다겠다는 의미이지만, 미리 결과를 만들어놓는 것이 아니고 뷰를 조회할 때마다 select문장을 실행하기 때문에 일종의 조회 함수 역할을 한다고 볼 수 있다. 

## information schema
데이터베이스 시스템에서 제공하는 시스템 카탈로그 스키마로, 메타데이터를 조회하는데 사용한다. 예를 들면 테이블 정보 키 컬럼 정보 등을 조회함으로서 데이터베이스 객체 및 구조에 대한 정보를 얻을 수 있다. 



