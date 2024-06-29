# Mybatis Persistence Framework(SQL Mapper)




## SqlSession과 Connection, Transaction
1) 트랜잭션을 사용하지 않을 때
SqlSessionFactory.openSession()을 할 때마다 각각 자신만의 sqlSession, Connection, thread을 갖게 되며 생성된 여러 sqlSession과 이에 연결된 Connection은 서로 간섭하지 않는다. 그리고 sqlSession 작업이 종료되면 Connection은 sqlSession이 close() 되었을 때 반납된다.

2) 트랜잭션을 시작했을 때
클라이언트는 자신만의 sqlSession만 가진다. 여러 클라이언트는 Connection을 공유하며 그에 연결된 thread를 통해 작업을 한다. 작업 중에는 임시DB에서 작업하고 commit 또는 rollback을 호출하여 스레드의 작업이 종료 되었을 때 그 스레드가 사용한 Connection이 반납된다. 



## <resultMap/>, <association/>, <collection/> 태그


## <typeAliases>
mybatis-config.xml에 이 태그를 통해 패키지 이름을 적으면 해당 패키지의 객체별명이 자동 부여된다. 

순서는 properties 태그 뒤에 온다.

```xml
  <typeAliases>
    <package name="bitcamp.myapp.vo"/>
  </typeAliases>
```

위처럼 패키지 이름을 주면
bitcamp.myapp.vo의 모든 객체를 사용할 때 앞에 패키지 이름을 붙이지 않아도 된다.
```xml
<!--전-->
<insert id="add" parameterType="bitcamp.myapp.voAssignment">
...
</insert>

<!--후-->
<insert id="add" parameterType="Assignment">
...
</insert>
```

# Mybatis-Spring Framework 연동 라이브러리
Mybatis와 Spring 프레임워크와 더 쉽게 연동하고자 할 때 사용하는 라이브러리

## SQLSessionFactory...


## @Transactional을 이용한 트랜잭션 제어
기존 코드를 손대지 않고 커밋과 롤백 코드를 삽입하기 위해 프록시 패턴을 사용




