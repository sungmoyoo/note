# 보관소에 값 넣기
## Web App 보관소

<img src="../../img/WebAppRepo.png">


## ServletContext에 값 넣기
ServletContext 객체는 웹 애플리케이션이 시작될 때 생성된다.
애플리케이션이 종료될 때까지 유지된다.
**컨텍스트 값 넣기**
```java
ServletContext sc = this.getServletContext();
sc.setAttribute("v1", "aaa");
```

## HttpSession에 값 넣기
Servlet Container가 HttpSession을 만들 때 세션ID가 생성된다.
타임아웃 무효화 또는 클라이언트가 종료될 때까지 유지된다. 

정확히는 클라이언트가 getSession()을 요청할 때 세션ID를 안보내면 그때 HttpSession 객체를 생성한다. 만약 웹 브라우저에서 세션ID를 제공하면 getSession()을 호출할 때 기존 생성했던 세션 객체를 리턴한다.

```java
HttpSession session = request.getSession();
session.setAttribute("v2", "bbb");
```
*브라우저와 클라이언트, HttpSession
- 브라우저별로 다른 쿠키 테이블을 가짐
- 같은 브라우저가 여러 창을 띄워도 같은 쿠키테이블을 공유함
- 브라우저에서 서버에 요청하면 HttpSession으로부터 세션ID를 발급받아 쿠키테이블에 보관한다.
- Servlet Container는 클라이언트가 보낸 세션ID를 통해 같은 클라이언트인지 구분한다.

## ServletRequest에 값 넣기
ServletRequest 객체는 클라이언트가 요청할 때마다 생성된다.
응답이 끝나면 사라진다.
```java
request.setAttribute("v3", "ccc");
```


# 보관소에서 값 꺼내기

## ServletContext에서 저장된 값 꺼내기
```java
    ServletContext sc = this.getServletContext();
    String v1 = (String) sc.getAttribute("v1");
```

## HttpSession에서 저장된 값 꺼내기
이 요청을 한 클라이언트의 HttpSession 객체가 없다면 만들어준다.  
이미 이 클라이언트를 위해 만든 객체가 있다면 그 객체를 리턴한다.
```java
HttpSession session = request.getSession();
String v2 = (String) session.getAttribute("v2");
```

## ServletRequest에서 저장된 값 꺼내기
```java
String v3 = (String) request.getAttribute("v3");
```


# Cookie 
> 웹서버가 웹브라우저에게 맡기는 데이터
응답할 때 응답 헤더에 포함시켜 보낸다. 웹브라우저는 응답헤더로 받은 쿠키 데이터를 보관하고 있다가 지정된 URL을 요청할 때 요청 헤더에 포함시켜 웹 서버에게 쿠키를 다시 보낸다.

## 쿠키 생성
- 이름과 값으로 생성한다. 
- 쿠키의 유효기간을 설정하지 않으면 웹브라우저가 실행되는 동안만 유지된다.
- 쿠키의 사용범위를 지정하지 않으면 현재 경로에 한정한다. 예를 들어 쿠키를 보낼 때의 URL이 /ex10/s1 이라면, 웹브라우저는 /ex10/* 경로를 요청할 때만 웹서버에게 쿠키를 보낸다. 즉 사용범위에 해당하는 요청을 할 때만 쿠키를 보낸다.


```java
// 프로토콜 예 => Set-Cookie: name=hong
Cookie c1 = new Cookie("name", "hong");

// 프로토콜 예 => Set-Cookie: name2=홍길동
// Cookie c2 = new Cookie("name2", "홍길동"); URL 인코딩 필요

// 프로토콜 예 => Set-Cookie: name3=%ED%99%8D%EA%B8%B8%EB%8F%99
Cookie c3 = new Cookie("name3", URLEncoder.encode("홍길동", "UTF-8"));

// 응답 헤더에 포함
response.addCookie(c1);
response.addCookie(c3);
```

## 쿠키 읽기
- 쿠키를 이름으로 한 개씩 추출할 수 없다. 한 번에 배열로 받아야 한다.
- 요청 헤더에 쿠키가 없으면 리턴 되는 것은 빈 배열이 아니라 null이다.
- 따라서 무조건 반복문을 돌리면 안된다.
```java
// 응답 프로토콜 예:
// Cookie: name=hong; age=20; working=true; name2=홍길동; name3=%ED%99%8D%EA%B8%B8%EB%8F%99
Cookie[] cookies = request.getCookies();

response.setContentType("text/plain;charset=UTF-8");
PrintWriter out = response.getWriter();

if (cookies != null) {
  for (Cookie c : cookies) {
    // 쿠키 값이 'URL 인코딩'한 값이라면
    // 개발자가 직접 디코딩 해서 사용해야 한다.
    // 쿠키 값에 대해서는 서버가 자동으로 디코딩 해주지 않는다.
    out.printf("%s=%s,%s\n",
        c.getName(),
        c.getValue(),
        URLDecoder.decode(c.getValue(), "UTF-8"));
      }
    }
```

## 쿠키 유효기간
- 유효기간을 설정하면 웹브라우저는 그 기간 동안 보관하고 있다가 웹서버에게 쿠키를 보낸다.
- 유효기간을 설정하면 웹브라우저를 종료해도 삭제되지 않는다. 단 유효기간이 지나면 웹서버에 보내지 않고 삭제한다.
- 웹 브라우저는 로컬 디스크에 쿠키를 보관한다.
- 유효기간은 초(second) 단위로 설정된다.
```java
// 응답 프로토콜 예: Set-Cookie: v=ccc; Max-Age=60; Expires=Wed, 08-Apr-2020 02:42:13 GMT
Cookie c = new Cookie("v", "ccc");
c.setMaxAge(60); // 쿠키를 보낸 이후 60초 동안 만 유효

response.addCookie(c);
```

## 쿠키 사용범위
- 쿠키의 사용 범위를 지정하지 않으면 쿠키를 발행한 URL 범위에 한정된다. 즉 같은 URL로 요청할 때만 쿠키를 보낸다.

```java
// 사용 범위를 지정하지 않은 쿠키
// => 쿠키를 발급한 서블릿과 같은 경로이거나 하위 경로의 서블릿을 요청할 때만 웹 브라우저가 서버에 쿠키를 보낸다.
// => 기본: /ex10/* : ex10/ 하위 경로에 모두 보낸다.
Cookie c1 = new Cookie("v1", "aaa");

// 사용 범위 지정
// => 쿠키를 발급한 서블릿의 경로에 상관없이 지정된 경로의 서블릿을 요청할 때
// 웹 브라우저가 서버에 쿠키를 보낸다.
// => /ex10/a/* : ex/a/ 하위 경로만 보낸다.
Cookie c2 = new Cookie("v2", "bbb");
c2.setPath("/ex10/a");

// => / : 어떤 URL이던 요청 시 쿠키를 보낸다.
Cookie c3 = new Cookie("v3", "ccc");
c3.setPath("/");

// 쿠키를 응답 헤더에 포함시키기
response.addCookie(c1);
response.addCookie(c2);
response.addCookie(c3);
```

*쿠키의 경로를 적을 때 웹 애플리케이션 루트(컨텍스트 루트)까지 적는 이유?
- 쿠키 경로는 서블릿 컨테이너가 사용하는 경로가 아니다. 웹 브라우저가 사용하는 경로다. 
- 웹 브라우저에서 '/' 은 서버 루트를 의미한다. 따라서 웹 브라우저가 사용하는 경로를 지정할 때는 조심해야 한다. '/'가 서버 루트를 의미하기 때문이다.  
- => 그래서 쿠키의 경로를 지정할 때는 웹 애플리케이션 루트(컨텍스트 루트)를 정확하게 지정해야 한다.


# 세션
> 클라이언트를 식별하는 기술

## HttpSession과 Cookie의 관계
HTTP 프로토콜은 Stateless 방식으로 통신을 한다. 연결한 후 요청하고 응답을 받으면 연결을 끊는다. 그래서 서버는 클라이언트가 요청할 때 마다 누구인지 알 수 없다.  

클라이언트가 접속하면 웹 서버는 그 클라이언트를 위한 고유 번호를 발급(쿠키 이용)한다. 이 고유 번호를 '세션 아이디'라 부른다.

세션 아이디는 새 세션을 생성할 때 세션 아이디를 발급하는데, 클라이언트 요청에 세션이 없거나, 유효기간이 지난 경우, request.getSession()을 호출하면 세션을 생성한다. 즉 클라이언트가 세션이 없다고 무조건 세션을 생성하는 것이 아니다. getSession() 메서드가 호출되어야 세션을 생성한다. 이렇게 생성된 세션의 세션ID를 응답할 때 쿠키로 보낸다. 

## getSession() 호출
1) 클라이언트가 세션 아이디를 보내지 않은 경우
  - 서버에서 새 HttpSession을 만든 후 클라이언트에게 알려주기 위해 세션ID를 쿠키로 보낸다.
2) 클라이언트가 세션 아이디를 쿠키로 보낸 경우
  - 요청 헤더에 쿠키에 저장된 세션아이디를 확인해 클라이언트를 식별한다.
  - 기존 세션 아이디가 유효하다면 응답할 때 세션아이디를 보내지 않는다.

```java
// 응답 헤더 예
Set-Cookie: JSESSIONID=5801C115615A2C9074AC0B78E31C5F21; Path=/컨텍스트루트; HttpOnly
```

## 세션의 활용
세션은 로그인 정보 저장 외에도 transation에서 입력받는 데이터나 중간 계산결과를 임시 보관할 때 사용할 수 있다. 

<img src="../../img/UsesSession.png">

세션의 특징은 유효시간 이내에 요청을 받으면 동일한 세션을 공유한다는 것이다.따라서  위 그림과 같이 쿠키에 데이터를 입력받을 때 여러 서블릿을 걸쳐 세션에 저장하고 그 값을 꺼내 사용할 수 있다.

## 세션 타임아웃 설정
```java
HttpSession session = request.getSession();
session.setMaxInactiveInterval(10); // 유효시간 설정
```
- 클라이언트가 요청하는 순간부터 세션 시간을 카운트한다. 파라미터는 초(second) 단위이다. 
- 만약 유효 시간 내에 다시 클라이언트 요청이 있다면 카운트를 0부터 다시 시작한다. 
- 지정된 시간동안 서버에 아무런 요청을 하지 않으면 해당 세션을 무효화된다. 
- 무효화된 세션을 사용할 수 없기 때문에 getSession()은 새 세션을 만들어 리턴한다. 

## 세션 무효화하기
```java
HttpSession session = request.getSession();
session.invalidate(); // 세션 무효화
```
- 직접 invalidate() 메서드를 호출하여 유효시간이 남았더라도 무효화할 수 있다.


# 서블릿 배치
## 1. DD file에 배치 정보를 등록
```xml
<servlet>
  <servlet-name>ex12.Servlet01</servlet-name>
  <servlet-class>com.eomcs.web.ex12.Servlet01</servlet-class>
</servlet>
<servlet-mapping>
  <servlet-name>ex12.Servlet01</servlet-name>
  <url-pattern>/ex12/s01</url-pattern>
</servlet-mapping>
```

## 2. 애노테이션으로 배치 정보를 등록
```java
@WebServlet("/ex12/s02")
public class Servlet00 extends HttpServlet {
  ...
}
```

## 3. 객체를 직접 생성하여 등록: programmatic deployment
```java
@WebListener
public class Listener01 implements ServletContextListener {
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    System.out.println("웹애플리케이션 시작!!");
    System.out.println("서블릿 배치!");

    // 1) 서블릿 컨테이너에 등록할 서블릿 객체를 준비한다.
    Servlet03 servlet = new Servlet03();

    // 2) 서블릿 정보를 관리하는 객체를 꺼낸다.
    ServletContext sc = sce.getServletContext();

    // 3) ServletContext 객체를 통해 서블릿 객체를 등록한다. ServletRegistration 내 Dynamic
    Dynamic 서블릿설정정보 = sc.addServlet("ex12.s03", servlet); //<servlet>...</servlet>

    // 4) 등록된 서블릿의 배치 정보를 설정한다.
    서블릿설정정보.addMapping("/ex12/s03"); // <servlet-mapping>...</servlet-mapping>
  }
}
```