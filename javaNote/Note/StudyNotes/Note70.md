# 클라이언트가 보낸 데이터 읽기
## 여러개의 데이터
**1. 다른 이름으로 값을 보낼 경우**
각각의 값에 대해 getParameter를 호출하여 확인한다.
```java
String genre1 = req.getParameter("genre1");
String genre2 = req.getParameter("genre2");
String genre3 = req.getParameter("genre3");
String genre4 = req.getParameter("genre4");
String genre5 = req.getParameter("genre5");
String genre6 = req.getParameter("genre6");
```
**2. 같은 이름으로 값을 보낼 경우**
값을 배열 형태로 한번에 입력받는다. 
```java
String[] genres = req.getParameterValues("genre");\
```
## 빈값과 null
파라미터 이름만 넘어갈 때 getParameter()의 리턴 값은 빈 문자열 객체이다. 입력 상자에 값을 입력하지 않아도 빈 문자열이 서버에 전송된다.  
만약 파라미터 이름 자체가 없으면 getParameter()는 null을 리턴한다.

# doGet과 doPost
HttpServlet 클래스는 클라이언트로부터 service(ServletRequest, ServletResponse) 메서드 요청이 들어오면 HTTP 프로토콜을 다룰 수 있도록 service() 메서드를 오버로딩하여 형변환해주는 역할을 한다. 다만 이 방식은 HTTP 요청 종류가 무엇인지에 따라 어떤 로직을 처리할지 코드를 작성하는데 불편함이 있다. 

그래서 service() 메서드에는 요청 종류에 따라 개별적인 메서드를 다룰 수 있도록 에 doGet(), doPost(), doPut() 등의 메서드로 분기하는 로직이 포함되어 있다. 이 메서드는 this를 호출하기 때문에 HttpServlet을 상속받는 서브클래스에서 오버라이딩하여 구현해서 사용할 수 있다.

이 방식을 사용하면 개발자는 HTTP 요청이 무엇인지에 따라 각각에 메서드에 필요한 로직을 명확하게 작성할 수 있다.



# 서블릿 객체 자동생성
Servlet은 Listener, Filter와 다르게 클라이언트가 실행을 요청해야만 객체를 생성한다. 클라이언트가 실행을 요청하지 않아도 서블릿을 미리 생성하고 싶다면 loadOnStartup 프로퍼티 값을 지정하여 미리 객체를 생성할 수 있다. 즉 Servlet Container가 실행될때 바로 Servlet의 init()이 실행된다.

보통 서블릿이 작업할 때 사용할 자원을 준비하는데 시간이 오래 걸리는 경우에 웹 애플리케이션을 시작할 때 미리 서블릿 객체를 준비하는 것이 좋다.

## 방법
1. 애노테이션
loadOnStartup=실행순서이다. 미리 생성할 서블릿이 여러 개라면, 지정한 순서대로 생성된다. 
```java
@WebServlet(value="/ex06/s1", loadOnStartup = 1)
```

2. DD 파일(web.xml)에 지정
```xml
<servlet>
  ...
  <load-on-startup>1</load-on-startup>
</servlet>
```

# 서블릿 초기화 파라미터
## init() 오버라이딩
서블릿 객체가 생성될 때 뭔가 준비해야 하는 작업을 해야 한다면, init(ServletConfig config) 메서드를 오버라이딩해서 사용하는 방법은 권장하지 않는다.  
메서드가 호출될 때 넘어오는 값 `config`를 나중에 사용할 수 있도록 인스턴스 필드에 저장하는데 매번 이런식으로 코딩하는 방법은 불편하다.  
이미 GenericServlet에서 파라미터 값을 받지 않는 init()를 구현해놓았다.

그러므로 개발자는 서블릿 객체가 생성될 때 뭔가 작업을 수행하고 싶다면, init()를 오버라이딩하는 것이 좋다. 

```java
ServletConfig config;

// 오버라이딩 X
@Override
public void init(ServletConfig config) throws ServletException {...} 

// 오버라이딩
@Override
public void init() throws ServletException {...}
// 만약 이 객체가 생성될 때 DB에 연결된다고 가정해보자
// 자바 소스 코드에 JDBC Driver 이름과 URL, 사용자 아이디, 암호를 작성할 경우
// 나중에 DB 연결정보가 바뀔 때마다 소스를 변경해주어야 하는 상황이 발생한다. 
// 보통 이렇게 값을 직접 작성하는 것을 "하드(Hard) 코딩"이라 부른다.
// 따라서 변경 값들을 외부에 두는 것이 관리에 편하다.
```

## 방법
1. DD 파일 초기화 설정
일반적으로 초기화 값을 DD 파일에 둔다. ServletConfig와 ServletContext에 저장하는 2가지 방법이 있다.

- ServletConfig에 저장: init-param 태그.
```xml

<servlet>
  <servlet-name>Servlet03</servlet-name>
  <servlet-class>com.eomcs.web.ex06.Servlet03</servlet-class>

  <init-param>
    <param-name>jdbc.driver</param-name>
    <param-value>com.mysql.jdbc.Driver</param-value>
  </init-param>
  <init-param>
    <param-name>jdbc.url</param-name>
    <param-value>jdbc:mysql://localhost:3306/studydb</param-value>
  </init-param>
  <init-param>
    <param-name>jdbc.username</param-name>
    <param-value>study</param-value>
  </init-param>
  <init-param>
    <param-name>jdbc.password</param-name>
    <param-value>1111</param-value>
  </init-param>
  <load-on-startup>1</load-on-startup>
</servlet>
```
위 방법은 해당 서블릿에서만 DB 정보를 사용할 수 있다.

- ServletContext에 저장: context-param 태그
```xml
<context-param>
  <param-name>jdbc.driver</param-name>
  <param-value>com.mysql.jdbc.Driver</param-value>
</context-param>
<context-param>
  <param-name>jdbc.url</param-name>
  <param-value>jdbc:mysql://localhost:3306/studydb</param-value>
</context-param>
<context-param>
  <param-name>jdbc.username</param-name>
  <param-value>study</param-value>
</context-param>
<context-param>
  <param-name>jdbc.password</param-name>
  <param-value>1111</param-value>
</context-param>
```
ServletContext에 저장하면 해당 Servlet-Container에서 모든 서블릿이 값을 공유할 수 있다. 

값을 가져올 때는 ServletConfig 또는 ServletContext 객체를 가져와 getInitParameter(파라미터이름)을 호출하여 사용한다. 
```java
@Override
public void init() throws ServletException {
  // ServletConfig config = this.getServletConfig();
  ServletContext config = this.getServletContext();

  String jdbcDriver = config.getInitParameter("jdbc.driver");
  String jdbcUrl = config.getInitParameter("jdbc.url");
  String username = config.getInitParameter("jdbc.username");
  String password = config.getInitParameter("jdbc.password");
  ...
}
```

2. 애노테이션 초기화 설정
@WebServlet 애노테이션으로도 초기화 할 수 있다. 단 많이 사용하지는 않는다.
```java
@WebServlet(
  value = "/ex06/s3",
  loadOnStartup = 1,
  initParams = {
      @WebInitParam(name = "jdbc.driver", value = "org.mariadb.jdbc.Driver"),
      @WebInitParam(name = "jdbc.url", value = "jdbc:mariadb://localhost/studydb"),
      @WebInitParam(name = "jdbc.username", value = "study"),
      @WebInitParam(name = "jdbc.password", value = "1111")})
```

# Forwarding
`forwarding`이란 서블릿 실행을 위임하는 기술로 forward를 호출한 서블릿에게 넘겨 요청에 대한 작업을 수행하고 응답을 받는다. 

forward를 호출하여 실행을 위임한 서블릿은 기존에 작업을 수행하여 버퍼에 출력한 모든 내용은 삭제하며 리턴된 후에도 출력이 모두 무시된다. 

보통 RequestDispatcher 인터페이스를 사용하여 forward를 수행한다. RequestDispatcher 객체는 다른 서블릿의 url을 받아 요청을 다른 서블릿으로 넘기는 역할을 한다. 

```java
@Override
protected void service(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    response.setContentType("text/plain;charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.println("forward 호출 전");

    RequestDispatcher 요청배달자 = request.getRequestDispatcher(다른 서블릿 url);
    요청배달자.forward(request, response);

    out.println("forward 호출 후");
    }
```
위와 같이 forward를 호출하면 기존 버퍼가 모두 삭제되고 앞으로의 출력도 모두 무시되는 로직이 적용되어 결과는 s2의 응답만 출력된다. 


# Including
앞서 forward는 기존 작업 내용을 취소하고 요청을 다른 서블릿으로 전달한다면 include는 기존 작업 내용을 유지(포함)한 채 include로 호출한 서블릿의 작업 결과를 합친다. 
```java
@Override
protected void service(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    response.setContentType("text/plain;charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.println("forward 호출 전");

    RequestDispatcher 요청배달자 = request.getRequestDispatcher(다른 서블릿 url);
    요청배달자.include(request, response);

    out.println("forward 호출 후");
    }
```
forward와 달리 기존 버퍼를 삭제하지 않기 때문에 결과에는 "forward 호출 전", "forward 호출 후" 가 모두 출력된다. 

# forward/include 객체 공유
forward와 include를 통해 요청을 전달할 때 파라미터로 ServletRequest와 ServletResponse를 넘기는데 이는 전달하는 서블릿과 전달받는 서블릿이 같은 객체를 공유한다는 것을 의미한다. 따라서 forward/include 상관없이 ServletRequest와 ServletResponse가 가지고 있는 attribute를 모든 서블릿이 사용할 수 있다. 

# HttpServletRequest / HttpServletResponse Lifecycle
HttpServletRequest와 HttpServletResponse는 요청이 들어올 때 생성되고 응답할 때 소멸된다. 따라서 forward를 통해 다른 서블릿에게 전달이 되어도 결국 리턴된 후 응답하게 되는데 이 때 버퍼가 삭제되고 무시되는 원리는 자원이 해제되어서 그런 것이 아니라 RequestDispatcher에 forward를 호출한 서블릿의 버퍼를 무시하는 로직이 있기 때문이다. 

# Refresh 
> 클라이언트에게 다른 URL을 요청하라는 명령

서버로부터 응답을 받고 "내용을 출력한 후" 지정된 시간이 경과되면 특정 URL을 자동으로 요청하도록 만들 수 있다.

## Refresh 방법

**헤더에 명령 추가**
out.println()이 출력한 후 상태는 버퍼 스트림에 보관한 상태이고 service() 메서드 호출이 끝나면 버퍼를 message-body에 담아 응답한다. 따라서 순서 상관없이 응답 헤더에 Refresh 정보를 추가하여 리프래시를 실행할 수 있다. 
```java
// Refresh 응답 헤더에 설정된 시간이 지난 후 알려준 url을 자동 요청한다.
response.setHeader("Refresh", "3;url=s100");
```

**HTML 헤더에 Refresh 명령 추가**
HTML을 출력하는 경우 응답헤더가 아니라 HTML 헤더에 리프래시 명령을 설정할 수 있다. 
```java
// http-equiv : 응답 헤더와 유사한 동작을 하는데 사용
out.println("<meta http-equiv='Refresh' content='3;url=s100'>");
```

# Redirect
> 응답할 때 콘텐트를 보내지 않는다. 바로 다른 페이지를 요청하라고 명령한다.

Redirect는 Servlet에서 작업 후 응답할 때 다시 요청할 url만 포함하고 콘텐트는 포함하지 않는다. 

*Redirect 응답 예
```
HTTP/1.1 302 
Location: s100
Content-Type: text/html;charset=UTF-8
Content-Length: 0
Date: Fri, 23 Feb 2024 05:41:30 GMT
Keep-Alive: timeout=60
Proxy-Connection: keep-alive

// message-body가 없다.
```

```java
// 클라이언트에게 URL을 알려줄 때 상대 경로를 지정할 수 있다.
// forward/include 와 달리 '/'는 컨텍스트 루트(웹 애플리케이션 루트)가 아닌
// 웹 서버 루트를 의미한다.
response.sendRedirect("s100");
```

- 로그인 후 로그인 결과를 출력하지 않고 즉시 메인 화면으로 보내고 싶을 때
- 결제완료 후 결과를 출력하지 않고 즉시 결제 상태 페이지로 보내고 싶을 때
- 게시글 삭제 후 결과를 출력하지 않고 즉시 게시글 목록으로 보내고 싶을 때

### *버퍼와 응답
Refresh와 Redirect의 설정의 위치는 출력문이 어디에 있던 상관없다. 다만 출력 시 버퍼가 가득 차면 즉시 응답하기 때문에 리프래시가 동작하지 않는 것에 유의해야 한다(1요청=1응답). 물론 Redirect는 응답문을 작성하지 않는 것이 원칙이다.(어차피 모두 버려지기 때문)
