# Cookie
쿠키는 클라이언트의 브라우저(Local)에 저장되는 작은 데이터 조각으로, 서버가 크라이언트의 브라우저에 정보를 유지하고 추적하는데 사용된다. 클라이언트 측에 저장되므로 서버로 다시 요청을 보낼 때 이전에 저장한 정보를 함께 보낼 수 있어 로그인 정보 저장 등에 활용할 수 있다.

## 쿠키 저장
쿠키 저장은 서버측에서 응답헤더에 쿠키 정보를 추가하여 보내면 브라우저가 이를 지정 디렉토리에 저장해놓는 방식이다. 

쿠키의 저장 위치는 Local이며 브라우저마다 지정 디렉토리가 다르다.

쿠키를 응답헤더에 추가할 땐 Cookie 객체를 생성하여 필요 정보를 다양한 set 메서드로 설정하고 addCookie를 사용하여 추가할 수 있다. 
```java
String email = request.getParameter("email");
String saveEmail = request.getParameter("saveEmail");
if (saveEmail != null) {
  Cookie cookie = new Cookie("email", email);
  cookie.setMaxAge(60 * 60 * 24 * 7);

  response.addCookie(cookie);
} else {
  Cookie cookie = new Cookie("email", "");
  cookie.setMaxAge(0);
  response.addCookie(cookie);
}
```

- 응답 헤더 예
```
HTTP/1.1 200 ok
...
Set-Cookie: email=user1@test.com; Max-Age=604800; Expires=Mon, 04 Mar 2024 06:33:52 GMT
...
message-body
```


## 쿠키 읽기
쿠키 정보를 읽어서 사용하는 방법은 쿠키 저장의 반대 순서이다. 브라우저에 저장된 쿠키를 로딩하여 요청 헤더에 추가한 후 보내면 쿠키 정보를 사용하여 작업을 수행한 후 응답한다. 

클라이언트는 웹서버에서 받은 쿠키를 해당 웹서버에 방문할 때마다 보낸다.
```java
String email = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("email")) {
          email = cookie.getValue();
          break;
        }
      }
    }
...

out.println("<div>");
out.printf("    이메일: <input name='email' type='text' value='%s'>\n", email); 
out.println("</div>");
...
```

요청 헤더 예
```
GET /auth/login HTTP/1.1
...
Cookie: email=user1@test.com; JSESSIONID=CBEECB9191885485634132CBA47D31C7
...
```
