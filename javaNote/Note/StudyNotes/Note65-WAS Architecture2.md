# Servlet Container가 관리하는 객체(Component)
*관리 대상*
1. 서블릿(Servlet): 클라이언트 요청을 처리
2. 필터(Filter): 서블릿 실행 전후에 보조 작업을 수행
3. 리스너(Listener): 특정 상태(event)에서 작업을 수행
  - 특정 상태 ex) 웹 앱의 시작/종료, 요청을 받거나 응답 완료, 세션이 시작/종료
*저장소*
4. ServletContext: 웹 애플리케이션에 1개, 서블릿/필터/리스너가 공유하는 저장소
  - ex) DB커넥션 객체, DAO 객체, 트랜잭션 관리 객체
5. HTTPSession: 클라이언트당 1개, 서블릿/필터/리스너가 공유하는 저장소
  - ex) 로그인 사용자 정보,
6. ServletRequest: 요청당 1개, 서블릿/필터/리스너가 공유하는 저장소
  - ex) 

## Web Server와 Web Browser Client
클라이언트는 하나의 브라우저를 의미한다. 브라우저 탭이나 창을 여러개 띄우더라도 하나의 클라이언트로 간주한다. 단 시크릿 창은 예외로 다른 클라이언트다. 다른 브라우저를 띄울 경우에도 다른 클라이언트다.  
즉 클라이언트는 사람을 지칭하는 것이 아니다. 브라우저가 클라이언트 단위이다.

## Servlet Container와 Web Application, ServletContext

## Servlet과 요청, ServletRequest
Servlet에 요청이 들어오면 ServletRequest가 생성되고 응답하면 소멸된다. 즉 1 요청 = 1 ServletRequest


# 서블릿 컨테이너/서블릿 프로토콜

서블릿 컨테이너가 service()를 호출할 때 넘겨주는 값은 HttpServletRequest와 HttpServletResponse이다.(ServletRequest/Response의 서브타입)  

service()의 파라미터는 Http 프로토콜 외 다른 프로토콜도 받을 수 있도록 의도하여 상위 인터페이스인 ServletRequest/Response로 설정하였다.

다만 getSession() 메서드는 파라미터로 넘어온 HttpServletRequest와 HttpServletResponse에 존재하기에
이를 사용하려면 원래 타입으로 형변환해주어야 한다.

```java
public void service(ServletRequest servletRequest, ServletResponse servletResponse)
      throws ServletException, IOException {
  HttpServletRequest request = (HttpServletRequest) servletRequest;
  HttpServletResponse response = (HttpServletResponse) servletResponse;
      }
```

# HttpServlet
매번 위처럼 형변환하면 중복 코드가 발생한다. 따라서 GenericServlet을 상속받아 형변환한 파라미터를 오버로딩한 HttpServlet 추상클래스를 통해 오버라이딩하여 형변환 없이 사용할 수 있다.  

# URL (Uniform Resource Locator)
https://localhost:8888/auth/login?email=xxx&password=1111

- https : schema
- localhost : host
- 8888: port
- auth/login: resource path
  - static
  - dynamic
- email=xxx&password=1111: query string

getParameter('email') -> xxx

# 정적 자원과 동적 자원
- static resource: 실행 결과가 고정
  - ex) .html, .css, .javascript, .pdf, .gif ..
  https://localhost:8888/auth/form.html

- dynamic resouce: 실행 결과가 가변적
  - ex) servlet, jsp 등 프로그램 실행
  https://localhost:8888/auth/login

# URL 인코딩(퍼센트[%] 인코딩)
URL을 작성할 때 특별한 의미를 지니는 단어의 경우(예: /, :, ? #, &, =, @ 등) 일반 문자로 사용해서는 안되기 때문에 특별한 형식으로 변환하는 것.  
ex) ! => %21, @ => %40 ...

# URL 인코딩/디코딩
웹브라우저가 웹서버에게 데이터를 보내기 전에 URL 인코딩을 수행하고 웹서버는 웹브라우저가 보낸 데이터를 URL 디코딩해서 리턴한다. 따라서 개발자는 URL 인코딩/디코딩을 직접 수행할 필요가 없다. 

