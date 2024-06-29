# Use-case Modeling
사용자의 관점에서 시스템을 모델링하기 위한 소프트웨어 개발 기법

따라서 사용자의 목표, 사용자와 시스템 간의 상호작용, 이러한 목표를 충족하는데 필요한 시스템 동작을 나타낸다.
그 결과물이 요구사항 정의서이다.

대개 의뢰인과 개발팀이 참조하는 설계 문서의 한 부분으로 사용된다. 

한번에 구체적인 것을 작성하는 것이 아니라 큰 주제부터 하나씩 상세화시켜나간다. 즉 Use-case는 사용자가 바라는 점을 이해함으로써 개발자가 만드는 시스템이 "어떻게" 사용될 것인가를 결정하는데 도움을 준다. 

**작성 순서**
1. Actor 식별
  - 시스템을 사용하는 사람, 프로세스
2. Use-case 식별
  - 액터가 시스템을 통해 달성하고자 하는 업무 목표
3. Relation 정의
  - 액터/유스케이스 간 관계

## Actor 식별
액터는 시스템을 사용하는 사람, 프로세스, 또는 시스템인 주 액터(Primary Actor)와 시스템이 의존하는 외부 시스템인 보조 액터()로 나누어 식별한다. 


## Use-case 식별 가이드라인 1
2주~4주 기간의 개발 단위에서 관리하기에 적절한 크기로 식별하는 방법을 식별 가이드라인이라고 한다. 

업무를 자르는 기준은 EBP 
1. 한 사람이 한번에 수행하는 업무(한장소에서 한번 수행)
2. 시스템을 사용해서 처리하는 업무
3. 카운트 가능한 업무 (시작과 끝이 명확)

## Use-case 식별 가이드라인 2
###  CRUD에 해당하는 경우 한 개의 Use-case로 합치는 것이 관리하기 편하다. 
예를 들어 학생관리 시스템에서 학생에 대한 과제 등록, 과제 조회, 과제 수정, 과제 삭제 기능이 있다면 이를 합쳐 과제 관리라는 하나의 유스케이스로 만들어 보다 수월하게 관리할 수 있다. 

### 서로 관련된 Use-case일 경우 한 개의 Use-case로 합친다. 
로그인, 로그아웃과 같이 관련된 기능 같은 경우에도 사용자 권한처럼 하나의 유스케이스로 만들면 복잡성을 줄일 수 있다.

### 여러 Use-case에 중복되는 시나리오가 있다면 별도의 Use-case로 분리한다.
여러 Use-case에 포함(include)하는 공통 시나리오는 Use-case가 될 수 없어도 관리 및 구현의 편의상 별도의 Use-case로 추출하는 것이 좋다. 확장(Extend)되는 공통시나리오도 마찬가지이다.

>**Use-case의 Include(포함관계)와 Extend(확장관계)**  
>Include 관계는 하나의 유스케이스 실행 시 다른 유스케이스를 포함하여 반드시 실행시켜야 할 때 사용하는 관계이다. 즉 필수적으로 실행되어야 하는 하위 작업(유스케이스)를 의미한다. 화살표는 Base가 되는 유스케이스에서 포함되는 유스케이스를 향하는 방향으로 표현한다.

Extend는 Include와 반대로 필수가 아닌 선택의 개념이다. 특정 조건에 따라 다른 유스케이스가 실행 여부가 결정된다. 해당 특정 조건이 있는 지점을 extend point(확장점)라고 하며 조건이 맞을 때 extention이 실행된다. 

회원가입과 주소입력을 예로 들면
- 회원가입 시 주소입력이 필수이면 -> Include
- 회원가입 시 주소입력을 클릭 할 때 실행되면 -> Extend


---


# Spring WebMVC
<img src=>

# XML로 IoC 설정

## DispatcherServlet
Spring WebMVC 구조에서는 Front Controller가 모든 요청을 받는다. 스프링 프레임워크에서 이 역할을 해주는 서블릿이 DispatcherServlet이다. 

DispatcherServlet은 자체적으로 WebApplicationContext라는 IoC 컨테이너를 보유하고 있다. IoC 컨테이너를 사용하기 위해서는 XML 또는 Java Config로 설정 파일을 지정해주어야 한다. 
```xml
<servlet>
  <servlet-name>app</servlet-name>
  <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  <init-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/app-servlet.xml</param-value>
  </init-param>
  <!-- 서블릿을 요청하지 않아도 웹 애플리케이션을 시작시킬 때 자동 생성되어
       IoC 컨테이너를 준비할 수 있도록
       다음 옵션을 붙인다. -->
  <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
  <servlet-name>app</servlet-name>
  <url-pattern>/app/*</url-pattern>
</servlet-mapping>

  <!-- 웹 애플리케이션이 시작될 때 사용자에게 표시될 기본 환영 페이지를 정의하는 코드 -->
<welcome-file-list>
  <welcome-file>index.html</welcome-file>
  <welcome-file>index.htm</welcome-file>
  <welcome-file>default.htm</welcome-file>
</welcome-file-list>
```

### `<load-on-startup>`을 지정하는 이유?
보통의 서블릿은 해당 서블릿에 요청이 들어와야만 설정을 초기화하고 인스턴스를 생성한다.  

Spring Framework에서 IoC 컨테이너는 요청이 들어올때 생성되고 초기화되는 것이 아니라 웹 애플리케이션이 시작될 때 생성된다. 


## /WEB-INF app-servlet.xml을 두는 이유?
설정파일을 다음과 같이 config 아래에 두는 회사는 진지하게 퇴사 고민해라.  
왜? 일반 웹 디렉토리는 클라이언트에서 해당 경로로 접근이 가능하기 때문에 설정 정보가 노출될 위험이 있다. 그래서 보안상 굉장이 취약하다. 그래서 접근이 불가한 /WEB-INF 경로 하위에 설정파일을 두는 것이 권장된다.

또한 서블릿 초기화 변수인 contextConfiguration이 없다면, 정해진 규칙에 따라 설정파일을 자동으로 찾는데 자동으로 찾는 그 경로가 /WEB-INF이다.

## contextConfiguration
서블릿 초기화 변수로 contextConfiguration을 넣지 않으면 /WEB-INF/서블릿이름-servlet.xml 파일을 자동으로 찾는다. 해당 파일을 찾지 못하면 예외가 발생한다.  
contextConfiguration 초기화 변수가 있다면, 지정한 설정 파일을 로딩하여 객체를 준비한다. 만약 변수의 값이 비어있다면 아무것도 생성하지 않는데, contextConfiguration가 없을 때와 달리 예외가 발생하지 않는다.  
즉 contextConfiguration 변수를 생략하는 것과 변수의 값을 비어두는 것은 다르다.

이를 활용하면 init-param 태그를 생략할 수도 있다. 개발자가 /WEB-INF 경로에 서블릿이름-servlet.xml 파일을 두면 굳이 `init-param` 태그로 `param-name` `param-value`를 지정하지 않아도 자동으로 찾아 IoC 설정을 초기화하기 때문이다. 


## ContextLoaderListener로 IoC 컨테이너 설정
```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/app-servlet.xml</param-value>
  </context-param>
  
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>

  <servlet>
    <servlet-name>app</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextConfigLocation</param-name>
    </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
```

## ContextLoaderListener - IoC 컨테이너 공유



## ApplicationContext와 WebApplicationContext의 관계
ContextLoaderListener가 생성된 후에 contextInitialized() 메서드가 호출되면 되면 내부적으로 IoC 컨테이너를 생성한다. 

다음의 xml 코드가 실행될 때 ContextLoaderListener가 생성된다.
```java
<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class> 
```

contextInitialized() -> initWebApplicationContext() -> createWebApplicationContext(servletContext) -> ConfigurableWebApplicationContext(IoC 컨테이너) 리턴 -> servletContext.setAttribute()로 보관


## mvc annotation driven

## log4j
로그는 애플리케이션 모니터링, 에러 추적 및 보고를 출력하는 개발에 필수적인 요소이다. 만약 이 로그를 System.out.print()를 사용하여 확인한다면 필요한 위치에서 발생하는 로그를 출력하도록 직접 모두 작성해야 한다. 

이러한 불편함을 해소하기 위해 만들어진 것이 로깅 유틸리티이다. 가장 보편적으로 사용하는 log4j는 xml 또는 프로퍼티 파일 형태로 작성되며, 로깅 레벨, 출력 대상, 출력 형식을 정의하여 사용할 수 있다. 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>

  <!-- 로그 출력 형태를 정의한다. -->
  <Appenders>

    <!-- 표준 출력 장치인 콘솔로 출력하는 방식을 정의한다. -->
    <Console name="stdout" target="SYSTEM_OUT">
      <PatternLayout pattern="[%-5level] %d{yyyy-MM-dd} [%t] %c{1} - %msg%n" />
    </Console>

    <File name="file" fileName="./logs/file/sample.log" append="false">
      <PatternLayout pattern="[%-5level] %d{yyyy-MM-dd} [%t] %c{1} - %msg%n" />
    </File>
  </Appenders>

  <!-- 로그 출력을 적용할 대상과 로그 출력 레벨을 지정한다. -->
  <Loggers>

    <!-- Root => 모든 대상에 적용할 기본 로그 출력 형식과 레벨 -->
    <Root level="debug" additivity="false">
      <AppenderRef ref="stdout" /> <!-- 로그를 출력할 때 사용할 출력 방식 지정 -->
    </Root>
  </Loggers>
</Configuration>
```

## Page Controller 
프론트 컨트롤러가 실행할 페이지 컨트롤러는 클래스 선언 앞에 @Controller 애노테이션을 붙인다. 

**@RequestMapping 애노테이션**  
클라이언트 요청이 들어왔을 때 호출될 메서드(request handler)를 표시할 때 사용하는 애노테이션이다.
```java
@RequestMapping(요청URL)
@RequestMapping(value="/hello")
@RequestMapping("/hello")
@RequestMapping(path="/hello")
@RequestMapping({"/hello", "/hello2", "/okok"})
// 요청 URL 앞에 "/"는 프론트 컨트롤러 경로 다음에 붙는 경로라는 의미이다.
```

**@ResponseBody 애노테이션**
리턴하는 String 값이 뷰 컴포넌트의 URL이 아닌 경우 @ResponseBody 애노테이션으로 표기한다.  

왜?  
@ResponseBody 애노테이션은 리턴하는 문자열이 클라이언트에게 보낼 콘텐트임을 표시하고 프론트 컨트롤러는 해당 리턴 값을 클라이언트에게 그대로 전송해주기 때문이다. 




