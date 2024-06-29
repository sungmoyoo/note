# Servlet 
## Servlet 객체 생성 과정
1. 서블릿 컨테이너에 등록된 배치 파일 경로를 통해 실행
2. 클라이언트 요청이 들어올 때 서블릿 컨테이너에서 new를 통해 인스턴스를 생성 -> 생성자 호출
3. init() 호출
  - 서블릿이 작업할 때 사용할 자원 준비
  - 파라미터로 받은 ServletConfig 객체는 인스턴스 변수에 보관해 두었다가 필요할 때 사용한다.
4. new 생성과 init()호출은 서블릿 객체가 존재하지 않을 때 실행한다는 조건문이 걸려 있다. 따라서 서블릿을 최초로 실행할 때 한번씩만 호출된다.
5. service() 호출
  - 클라이언트가 서블릿의 실행을 요청할 때마다 호출된다.

*destroy() : 웹 애플리케이션을 종료할 때(서버 종료 포함) 호출된다.

*getServletConfig() : 서블릿에서 작업을 수행하는 중에 서블릿 관련 설정 정보를 꺼낼 때 이 메서드를 호출한다. 보통 init()의 파라미터 값을 리턴받아 ServletConfig 객체를 사용한다.

*getServletInfo() : 서블릿 컨테이너가 관리자 화면을 출력할 때 이 메서드를 호출한다. 리턴값은 서블릿을 소개하는 간단한 문자열이다.

## 서블릿 배치 방법
**1. 애노테이션**
```java
@WebServlet(value={"/ex01/first","/ex/first","/first"})
@WebServlet(value={"/ex01/s2"})
@WebServlet(urlPatterns="/ex01/s2")
@WebServlet(value="/ex01/s2")
@WebServlet("/ex01/first")
...
```

**2. XML**
WEB-INF/web.xml(DD File)에 등록
```
<servlet>
    <servlet-name>서블릿별명</servlet-name>
    <servlet-class>서블릿 클래스의 전체이름(패키지명 포함)</servlet-class>
</servlet>

<servlet-mapping>
    <servlet-name>서블릿별명</servlet-name>
    <url-pattern>클라이언트에서 요청할 때 사용할 URL(/로 시작해야 한다.)</url-pattern>
</servlet-mapping>
```

# GenericServlet
GenericServlet은 Servlet, ServletConfig, Serializable 인터페이스를 구현한 추상클래스이다.

- Servlet:  
Servlet의 service() 메서드만 남겨두고 나머지 메서드들은 모두 구현하였다. 따라서 이 클래스를 상속받는 서브클래스는 service() 메서드만 구현하면 된다. 

- ServletConfig:  

- Serializable:  
서버에 문제가 발생했을 때 다른 서버로 현재 실행 상태(서블릿 객체)를 그대로 옮겨야 하는 상황에서 Serialize/Deserialize를 사용하기 위해 구현하였다. 
SerialVersionUID 변수를 설정해야 한다. 

*@WebServlet 애노테이션
- web.xml 에 서블릿 정보를 설정하는 대신에 이 애노테이션을 사용하여 서블릿을 설정할 수 있다.
- 이 애노테이션을 활성화시키려면 web.xml의 <web-app> 태그에 다음 속성을 추가해야 한다. metadata-complete="false" 속성의 값은 false 여야 한다.


# HttpServlet
- GenericServlet은 다양한 프로토콜을 고려해 상위 레퍼런스인 ServletRequest와 ServletResponse를 파라미터로 받는다. 
- HttpServlet 은 GenericServlet의 파라미터를 형변환하지 않고 사용할 수 있도록 HttpServletRequest와 HttpServletResponse를 파라미터로 받는다. 
- HTTP 프로토콜의 메서드를 다루고 사용하려면 HttpServlet 추상클래스를 상속받아 service(HttpServletRequest, HttpServletResponse)를 오버라이딩하여 사용하는 것이 좋다. 


# doGet(), doPost(), doHead() ..
HTTP 요청 프로토콜에서 특정 명령에 대해 처리하고 싶다면 service(HttpServletRequest,HttpServletResponse)를 오버라이딩 하는 대신에 doGet(), doPost() 등을 오버라이딩하여 사용한다. 

- doGet() : HTTP GET 요청을 보냈을 때 호출되는 메서드, 주로 정보를 요청하고 응답을 반환하는데 사용
- doPost() : HTTP POST 요청을 보냈을 때 호출되는 메서드, 주로 클라이언트에서 서버로 데이터를 전송하고, 이를 처리하는 데 사용
- doHead() : HTTP HEAD 요청을 보냈을 때 호출되는 메서드, doGet()과 유사하지만 응답 본문을 제외한 헤더 정보만을 반환한다. 


# Filter
## Filter 생성 및 구동
1. 웹 애플리케이션 실행 시 바로 필터 객체가 생성되고 init() 메서드가 호출된다.
2. 요청이 들어올 때마다 doFilter() 가 호출된다. 서블릿이 실행되기 전 필터가 먼저 실행되며 서블릿을 실행한 후 다시 필터로 리턴한다. 
```java
// 다음 필터를 실행한다.
// 만약 다음 필터가 없으면,
// 요청한 서블릿의 service() 메서드를 호출한다.
// service() 메서드 호출이 끝나면 리턴된다.
chain.doFilter(request, response);
```
3. 웹 애플리케이션을 종료할 때 destroy() 가 호출된다.

## 필터의 용도
서블릿을 실행하기 전후에 기능을 추가할 때 사용한다. 

*기능 유형
- 암호 해제/암호화
- 압축 해제/압축
- 인코딩/디코딩
- 로깅(logging)
- 권한 검사



## 필터 구동 실체
Filter에는 GoF의 "Chain of Responsibility" 패턴이 적용되어 있다. 

>Chain of Responsibility 패턴:
>요청을 처리하는 객체들의 연결된 체인을 만들어 각각의 객체가 요청을 처리하거나 다음 객체로 전달하는 패턴. 기존 코드를 손대지 않고 기능을 추가하거나 제거할 수 있는 설계 기법이다

<img src="../../img/Filter.png">

1. 클라이언트 요청이 들어오면 Servlet Container는 FilterChain을 실행한다. 
2. FilterChain은 내부적으로 등록된 필터를 찾아 실행한다. 
3. chain.doFilter를 통해 다음 필터가 없을 때까지 반복한 후 없으면 service() 메서드를 호출한다. 
4. 종료할 때는 service() 부터 실행된 순서 반대로 종료해나간다. 

Chain of Responsibility는 클래스 간 Coupling이 맺지 않아 의존관계가 없고 독립적이기 때문에 유지보수가 쉽다. 


# Listener
## Listener 생성
1. 애노테이션 선언 시 경로 지정 없이 @WebListener만 선언하면 된다.
2. 웹 애플리케이션 실행 시 객체가 Filter보다 먼저 생성된다. 


## Listener 용도
시스템이 특정 상태에 놓일 때 알림을 받아 작업을 수행하는데 사용된다.  

Listner는 GoF의 "Observer" 패턴으로 만들어졌다. 

>Observer 패턴:
>한 객체(주제)의 상태가 바뀌면 그 객체에 의존하는 다른 객체들(옵저버)에게 알림이 가고 자동으로 작업을 수행하는 방식으로 일대다 관계를 정의한다. 

Listener에서 주제는 Servlet Container이며 Listener가 옵저버이다. Servlet Container의 상태가 변경되어 특정 상태가 되면 XxxListener 인터페이스를 구현한 옵저버인 Listener가 자신의 로직에 따라 작업을 수행하는 방식이다.

Listener는 다양한 인터페이스가 있으며 필요에 따라 인터페이스를 구현해 사용하면 된다. 
**Listener 인터페이스 대표적인 예**
```
ServletContextListener
- 서블릿 컨테이너를 시작하거나 종료할 때 보고 받고 싶을 때 구현.
ServletRequestListener
- 요청이 들어오거나 응답할 때 보고 받고 싶을 때 구현.
HttpSessionListener
- 세션이 생성되거나 종료될 때 보고 받고 싶을 때 구현.
```
