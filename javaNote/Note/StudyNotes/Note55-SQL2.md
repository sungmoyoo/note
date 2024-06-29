# DML

## insert
데이터 입력 시 사용하는 명령.
>insert into 테이블명(컬럼) values(값)

```sql
/* 컬럼을 지정하지 않으면 테이블을 생성할 때 선언한 컬럼 순서대로 값을 지정해야 한다.*/
insert into test1 values(null,'aaa','111','222','10101','seoul');

/* 컬럼을 명시할 경우, 순서는 상관없다. 대신 순서에 맞게 값을 지정해야 한다.*/
insert into test1(name,fax,tel,no,pstno,addr) values('bbb','222','111',null,'10101','seoul');

/* 자동 증가 컬럼은 생략 가능 */
-- no 컬럼이 primary key일 때
insert into test1(name,tel) values('ccc','333');

/* 여러 개의 값을 한번에 insert */
insert into test1(name,tel) values
('aaa', '1111'),
('bbb', '2222'),
('ccc', '3333');

/*select 결과를 테이블에 insert*/
insert into test2(fullname,phone)
  select name, tel from test1 where addr='seoul';
```

## update
등록된 데이터를 변경할 때 사용하는 명령.

> update 테이블명 set 컬럼명=값, 컬럼명=값, ... where 조건...;

```sql
update test1 set tel='3030', fax='1212' where no=2;

/*조건을 지정하지 않으면 지정한 모든 데이터 변경*/
update test1 set fax='333';
```

## delete
데이터를 삭제할 때 사용하는 명령.

> delete from 테이블명 where 조건

```sql
delete from test1 where no=2;

/*주의! 조건을 지정하지 않으면 모든 데이터가 삭제된다.*/
delete from test1;
```

## autocommit
mysql은 autocommit의 기본값이 true이다. 따라서 명령창에서 SQL을 실행하면 바로 실제 테이블에 적용된다. DML 사용 시에는 데이터가 모두 날라갈 수 있는 위험이 존재하므로 autocommit을 false로 설정하여 수동으로 변경사항을 적용하는 방식을 사용하는 것이 좋다. 

예를 들면 실수로 delete문에 조건을 지정하지 않은 경우 복구가 불가능한데, autocommit을 false로 설정한 경우 rollback 명령으로 쉽게 복구가 가능하다. 

```sql
-- 수동 커밋 설정
set autocommit=false;
-- 변경사항 커밋
commit;
-- 마지막 커밋 기준 변경사항 모두 취소
rollback;
```

## Transaction(트랜잭션) 
하나의 업무가 여러 개의 작업으로 이루어진 경우 한 단위로 다룰 수 있도록 여러 작업을 묶은 것이다. 트랜잭션은 데이터베이스를 일관된 상태로 유지하고, 무결성을 보장하는데 사용된다. 

autocommit을 false로 설정하면 여러 SQL문을 하나의 트랜잭션으로 묶을 수 있고 모든 SQL 성공적으로 실행되었을 때 커밋하고, 오류가 발생하면 롤백하여 이전으로 되돌리는 방식이다. 

**동작 방식**
트랜잭션 내에서 데이터를 변경하면 이 변경사항이 바로 데이터베이스 테이블에 반영되지 않고, 임시로 사용되는 영역(DB)에 저장된다. 이 임시 DB는 트랜잭션 내에서만 유효하며, 
트랜잭션이 성공적으로 커밋되면 변경사항이 실제 데이터베이스에 영구적으로 저장된다. 반대로 롤백되면 임시 DB의 변경사항은 취소되어 실제 데이터베이스에는 아무런 영향을 미치지 않게 되는 방식이다. 

따라서 autocommit을 false로 설정한 후 commit하기 전 select로 볼 수 있는 변경사항은 임시 DB에 저장된 변경사항이고 다른 클라이언트에서 select할 경우 변경사항이 적용이 안된 데이터를 조회할 것이다. 

---

# DQL
## select
테이블의 데이터를 조회할 때 사용하는 명령
```sql
/*모든 컬럼 값 조회*/
select * from test1;

/*특정 컬럼의 값만 조회 => 프로젝션(Projection)이라 부른다.*/ 
select no, name, tel from test1;

/*가상의 컬럼 값 조회 => concat 함수가 적용된 값이 들어간다. 컬럼명은 식 그대로 들어간다.*/
select no, concat(name, '(',class,')') from test1;
```

**컬럼 별명**
별명을 붙이지 않으면 원래의 컬럼명이 조회 결과의 컬럼명으로 사용된다.  
만약 복잡한 식으로 표현한 컬럼이거나 긴 이름을 가진 컬럼인 경우 별명을 붙여 조회결과를 보기 쉽게 할 수 있다.

> select 컬럼명 [as] 별명
```sql
select no as num, concat(name, '(',class,')') as title from test1;

/*as는 생략가능*/
select no num, concat(name, '(',class,')') title from test1;
```

**조회할 때 조건 지정**
where 절과 연산자를 이용하여 조회시 조건을 지정할 수 있다. 조건을 지정하여 행(레코드) 결과를 선택하는 것을 셀렉션(Selection)이라 부른다. 

> select ... from ... where 조건...
```sql
select * from test1 where no > 5;
```

**연산자**
OR, AND, NOT
- OR  : 두 조건 중에 참인 것이 있으면 조회 결과에 포함시킨다.
- AND : 두 조건 모두 참일 때만 조회 결과에 포함시킨다.
- NOT : 조건에 일치하지 않을 때만 결과에 포함시킨다.

```sql
-- or
select no, name, class, working 
from test1 
where working='Y' or class='java100';

-- and
select no, name, class, working
from test1
where working='Y' and class='java100';

-- not, 모두 동일
select no, name, class, working
from test1
where not working = 'Y';

select no, name, class, working
from test1
where working != 'Y';

select no, name, class, working
from test1
where working <> 'Y';
```

사칙연산/비교연산
- +, -, *, /, % 
- =, !=, >, >=, <, <=, <>

```sql
update test1 set
    tel = '1111'
where (no % 2) = 0;

-- 사칙연산 
-- MySQL은 from 을 지정하지 않아도 select 할 수 있다. Oracle은 불가능
select (4 + 5), (4 - 5), (4 * 5), (4 / 5), (4 % 5);

-- 비교연산
select (4=5), (4!=5), (4>5), (4>=5), (4<5), (4<=5), (4<>5);

-- 주의!
-- null에는 != 연산자를 사용하면 안된다. 
select *
from test1
where tel != null;

/* null인지 여부를 가릴 때는 is 또는 is not 연산자를 사용한다*/
select *
from test1
where tel is not null;

select *
from test1
where not tel is null;
```

두 값 사이(두 값도 포함)에 있는지 검사하는 조건
- between 값1 and 값2
```sql
select 5 between 3 and 5;
```

문자열 비교
- like, %, _를 사용하여 문자열을 비교, 포함관계를 조건으로 사용할 수 있다.
```sql
/*동일한 문자열이면 조회*/
select *
from test1
where class like 'java';

/*%앞의 문자열로 시작하면 조회, %를 앞에 붙이면 문자열로 끝날 경우 조회*/
select *
from test1
where class like 'java%';

/*문자열을 포함하면 조회, 맨앞뒤도 해당된다.*/
select *
from test1
where class like '%java%';

/*_ 문자열 뒤에 딱 1자만 올 경우 조회, 여러개를 붙이면 붙인 개수만큼 조건이 설정된다 ex) __ = 2자.*/
select *
from test1
where name like 's0_';
```

**날짜**
날짜 값 비교
```sql
/* 특정 날짜의 게시글 찾기 */
select *
from test1
where regdt = '2022-6-17';

/* 특정 기간의 게시글 조회 */
select *
from test1
where regdt between '2022-11-1' and '2022-12-31';
```

날짜 다루는 연산자
```sql
/* 현재 날짜 및 시간 알아내기 */
select now();

/* 현재 날짜 알아내기 */
select curdate();

/* 현재 시간 알아내기 */
select curtime();

/* 주어진 날짜, 시간에서 날짜만 뽑거나 시간만 뽑기 */
select regdt, date(regdt), time(regdt) from test1;

/* 특정 날짜에 시,분,초,일,월,년을 추가하거나 빼기*/
select date_add(now(), interval 11 day); -- month, year, microsecond 등등
select date_sub(now(), interval 11 day);

/* 두 날짜 사이의 간격을 알아내기 */
select datediff(curdate(), '2023-2-10');

/* 날짜에서 특정 형식으로 값을 추출하기 */
date_format(날짜, 형식)
select regdt, date_format(regdt, '%m/%e/%Y') from test1; /* 09/7/2022 */
select regdt, date_format(regdt, '%M/%d/%y') from test1; /* September/07/17 */
select regdt, date_format(regdt, '%W %w %a') from test1; /* Thursday 4 Thu */
select regdt, date_format(regdt, '%M %b') from test1; /* September Sep */
select now(), date_format(now(), '%p %h %H %l'); /* PM 01 13 1 */
select now(), date_format(now(), '%i %s'); /* 05 45 */

/* 날짜 값을 저장할 때 기본 형식은 yyyy-MM-dd이다. */
insert into test1 (title, regdt) values('aaaa', '2022-11-22');

/* 다음 형식의 문자열을 날짜 값으로 지정할 수 없다.*/
insert into test1 (title, regdt) values('bbbb', '11/22/2022');

/* 특정 형식으로 입력된 날짜를 date 타입의 컬럼 값으로 변환하면 입력할 수 있다.*/
insert into test1 (title, regdt) values('bbbb', str_to_date('11/22/2022', '%m/%d/%Y'));
```

## SQL 쿼리 실행 순서
일반적으로 select문은 여러 다양한 절(clause)들을 포함할 수 있다. 일반적인 쿼리 실행 순서는 다음과 같다. 
> FROM -> WHERE -> GROUPBY -> HAVING -> SELECT -> ORDER BY -> LIMIT 

간단한 예제를 통해 대략적인 실행과정을 살펴보도록 하자.
```sql
select 
  번호, 이름, 제품명, 총액
from 
  고객 join 주문 
    on 고객.번호 = 주문.고객번호
where 
  가격 >= 10000
order by 
  가격 desc
```
**1. FROM**
쿼리가 실행되면 가장 처음으로 조회할 테이블을 지정한다. 이후 Join을 실행하면 가상의 테이블로 결합하게 된다. 

- Join  
Join이 실행되면 두 테이블 간 `catesian(카테시안 곱)`이 만들어진다. 카테시안 곱은 첫번째 테이블의 각 레코드에 대해 두번재 테이블의 레코드와 결합된 결과이다. 예를 들어 고객테이블이 3개의 레코드 주문테이블이 5개의 레코드를 가지고 있다면 각 행들이 결합된 15개(3 x 5)의 새 테이블이 생성된다는 것이다.  
이후 on 절이 실행되면 조건에 일치하는 행만 선택하고 조건에 맞지 않는 행은 결과에서 제외된다. 이를 `inner join`이라고 한다.

**2. WHERE**
조건에 맞는 결과 데이터(레코드)를 먼저 `선택`한다. 이를 셀렉션(Selection)이라 한다. 예제에 따르면 가격이 10000원 이상인 레코드들이 `선택`된다. 여기서 주의할 점은 on절과 달리 셀렉션은 값을 제외하지 않고 `선택`만 한다는 것이다.  
결국 쿼리 절이 모두 실행된 다음 실제 데이터를 출력할 때 선택된 값만 출력되는 형식이다.

**3. SELECT** 
결과 데이터에서 가져올(출력할) 컬럼명을 `선택`한다. 이 또한 마찬가지로 실행 시 나머지 컬럼들이 제외되는 것이 아니다.

**4. ORDER BY**
값을 특정 컬럼을 기준으로 오름차, 또는 내림차순으로 정렬한다. 이때 SELECT로 선택되지 않은 컬럼으로도 정렬할 수 있다. inner join 테이블에서 제외되지 않았기 때문이다. 

**5. 결과값 조회**
WHERE와 SELECT로 선택 표시가 된 값만 출력된다. 

---