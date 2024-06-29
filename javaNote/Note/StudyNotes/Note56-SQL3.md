# Entity RelationShip

## 1 : 0 또는 1

primary key로 쓰이는 foreign key


# DQL
---

## distinct 와 all
모든 데이터를 가져올 때는 all(생략 가능, default)  
중복값 조건을 붙일 때는 distinct
```sql
/* 모든 데이터를 가져온다.*/
select all loc from room; -- all 생략 가능

/* 중복 값을 한 개만 추출할 때 distinct 를 붙인다.*/
select distinct loc from room;  

/* 컬럼이 2 개 이상일 때 
    그 컬럼들의 값이 중복될 경우만 한 개로 간주한다.*/
select distinct loc, name from room;
```
## order by
실무에서는 반드시 order by를 준다.
- asc(ascending): 오름차순
- desc(descending): 내림차순
```sql
/* 오름차순 정렬, asc는 생략 가능*/
select rno, loc, name
from room
-- order by name asc;
order by name;

/* 내림차순 정렬 */
select rno, loc, name
from room
order by name desc;

/* 이름은 오름차순, 지점명은 내림차순으로 정렬*/
select rno, loc, name
from room
order by name asc, loc desc;
```


## 라벨명
출력 시 라벨명을 지정하여 컬럼명을 변경할 수 있다.

```sql
/*라벨명을 지정하지 않으면 컬럼명이 라벨명이 된다, as는 생략 가능*/
select rno as room_no, loc as location, name
from room;

/* 라벨명에 공백을 넣고 싶으면 '' 안에 작성한다(문자열).*/
select rno 'room no', loc location, name
from room;

/* 복잡한 형식으로 출력할 경우 라벨명(별명)을 부여한다.*/
select concat(name, '(', loc, ')') title
from room;

select count(*) cnt
from room;

/* count()를 호출할 때 컬럼 이름을 지정하면 
   해당 컬럼의 값이 null 이 아닌 데이터만 카운트한다. */
select count(mno) cnt
from lect;
```

## union / union all (합집합)
select 결과를 합쳐 출력한다.
```sql
/* union: 중복 값 자동 제거*/
select distinct bank from stnt
union
select distinct bank from tcher;

/* union all: 중복 값 제거 안함*/
select distinct bank from stnt
union all
select distinct bank from tcher;

/* 차집합
   mysql 은 차집합 문법을 지원하지 않는다. (minus)
   따라서 다음과 기존의 SQL 문법(where not .. in)을 사용해서 처리해야 한다.
*/
select distinct bank
from stnt
where not bank in (select distinct bank from tcher);

/* 교집합
   mysql 은 교집합 문법을 지원하지 않는다. (intersect)
   따라서 다음과 기존의 SQL 문법(where .. in)을 사용해서 처리해야 한다.
*/
select distinct bank
from stnt
where bank in (select distinct bank from tcher);
```

## Join
서로 관련된 테이블의 데이터를 연결하여 추출하는 방법

### CROSS 조인(Cartesian product) 
두 테이블의 데이터를 1:1로 모두 연결한다. 모든 데이터를 출력하는 단순무식한 join으로서 잘 사용하지 않는다.
```sql
// 테이블 생성
create table board1 (
  bno int primary key auto_increment,
  title varchar(255) not null,
  content text
);

create table attach_file1 (
  fno int primary key auto_increment,
  filepath varchar(255) not null,
  bno int not null 
);

/*부모 테이블 board1, 자식테이블 attach_file1
  PK = FK, bno*/
alter table attach_file1
  add constraint attach_file1_fk foreign key (bno) references board1 (bno);

-- bno 컬럼이 두 테이블에 모두 존재한다.
-- 따라서 어떤 테이블의 컬럼인지 지정하지 않으면 실행 오류!
select bno, title, content, fno, filepath
from board1 cross join attach_file1;

-- 컬럼명 앞에 테이블명을 점으로 구분하여 명시한다. 
-- 라벨명이 되는 것은 아니다. 지정만 하는 역할이다. 
select board1.bno, title, content, fno, filepath, attach_file1.bno
from board1 cross join attach_file1;

-- 일반적으로 테이블명을 지정한 컬럼명에 별명 붙여서 사용
select b.bno, title, content, fno, filepath, a.bno
from board1 b cross join attach_file1 a;

-- 고전(클래식 문법)
select b.bno, title, content, fno, filepath, a.bno
from board1 b, attach_file1 a;
```

### Natural 조인
같은 이름을 가진 컬럼 값을 기준으로 레코드를 연결한다.
```sql
select b.bno, title, content, fno, filepath, a.bno
from board1 b natural join attach_file1 a;

-- 고전(클래식 문법)
select b.bno, title, content, fno, filepath, a.bno
from board1 b, attach_file1 a
where b.bno = a.bno;
```

**natural 조인의 문제점**
- 두 테이블의 조인 기준이 되는 컬럼 이름이 다를 때 연결되지 못한다.
- 상관 없는 컬럼과 이름이 같을 때 잘못 연결된다.
- 같은 이름의 컬럼이 여러 개 있을 경우 잘못 연결된다.

고전문법에서는 조인 기준이 되는 컬럼값을 검사하기 때문에 정상적으로 나온다. 


### Join ~ Using(컬럼명)
같은 이름을 가진 컬럼이 여러 개 있을 경우 USING을 컬럼을 명시할 수 있다.  
단 같은 이름을 가진 컬럼이 없을 경우 사용할 수 없다. 만약 두 테이블에 같은 이름의 컬럼이 없을 경우 연결하지 못한다. 

```sql
select b.bno, b.title, content, a.fno, a.title, a.bno
from board4 b join attach_file4 a using (bno);
```


### Join ~ On
Join Using과 달리 조인 조건을 직접 명시하기 때문에 같은 이름을 가진 컬럼이 없어도 연결할 수 있다. 실무에서 가장 많이 사용한다.

Join On은 조인 조건을 on 뒤에 명시한다. 조건이 일치하는 경우에만 두 테이블의 데이터를 연결한다.  
>이런 조인을 'inner join'이라 부른다.  
>inner join이라 기술해도 되고 생략해도 된다. 
```sql
select no, title, content, fno, filepath, bno
from board5 b inner join attach_file5 a on b.no=a.bno;
```

**[inner] join ~ on의 문제점**
- 반드시 on에서 지정한 컬럼의 값이 같을 경우에만 두 테이블의 데이터가 연결된다. 
- 같은 값을 갖는 데이터가 없다면 연결되지 않고, 결과로 출력되지 않는다.
- 예를 들어 강의 정보와 강의실 정보를 출력할 때 두 강의실이 배정되지 않은 강의 정보도 출력하고 싶은데 출력할 수 없다는 것이 문제이다. 반대로 강의실 정보도 출력하고 싶어도 전부 출력할 수 없다.

### Outer Join 
조인 조건에 일치하는 데이터가 없더라도 두 테이블 중에서 한 테이블의 데이터를 결과에 포함시키는 문법이다.  

- left outer join => 왼쪽 테이블의 데이터는 반드시 포함시키라는 뜻이다.
- right outer join => 오른쪽 테이블의 데이터를 반드시 포함시키는 뜻이다.

```sql
select no, title, content, fno, filepath, bno
from board5 b left outer join attach_file5 a on b.no=a.bno
order by no desc;

-- 실무 --
/* 실무에서는 위와 같이 작성하면 가독성이 떨어지기 때문에 아래와 같은 규칙대로 가독성있게 작성한다.*/
-- 1) 여러 테이블을 조인하여 컬럼을 projection 할 때 
--    각 컬럼이 어떤 테이블의 컬럼인지 명시한다.
-- 2) 컬럼을 나열할 때 한 줄에 한 컬럼을 나열한다.

select 
  b.no, 
  b.title, 
  b.content, 
  a.fno, 
  a.filepath, 
  a.bno
from board5 b 
  left outer join attach_file5 a on b.no=a.bno
order by 
  b.no desc;

select 
  la.lano, 
  la.rdt,
  l.titl, 
  m.name, 
  s.work,
  ifnull(r.name, '-') r_name,
  ifnull(m2.name, '-') m_name,
  ifnull(mg.posi, '-') posi
from lect_appl la
  join lect l on la.lno=l.lno
  join memb m on la.mno=m.mno
  join stnt s on la.mno=s.mno
  left outer join room r on l.rno=r.rno
  left outer join memb m2 on l.mno=m2.mno
  left outer join mgr mg on l.mno=mg.mno;
```

## 서브 쿼리
쿼리문 안에 쿼리문을 실행하는 기법, 조인