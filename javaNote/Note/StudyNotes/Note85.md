# Spring MVC 1

# 요청 핸들러의 아규먼트

## 프론트 컨트롤러로부터 받을 수 있는 파라미터 값
Spring Documentation - WebServlet - Method Argument에 받을 수 있는 모든 파라미터가 정의되어 있다.

그 중 실무에서 가장 많이 사용되는 파라미터는 
- ServletRequest
- ServletResponse
- HttpServletRequest
- HttpServletResponse
- HttpSession
- Map<String, Object>
- Model
- PrintWriter
가 있다. 

**주의**
ServletContext는 파라미터로 받을 수 없다. `@Autowired` 애노테이션을 통해 의존 객체로 주입 받아야 한다.


## @RequestParam
클라이언트가 보낸 파라미터 값을 파라미터 위치에서 값을 바로 받을 수 있는 애노테이션이다. 파라미터 앞에 붙이고 클라이언트가 보낸 파라미터의 지정하여 받을 수 있다. 

```java
@GetMapping("h1")
@ResponseBody
public void handler1(
      PrintWriter out,
      ServletRequest request,
      @RequestParam(value = "name") String name1,

      // value와 name은 같은 일을 한다.
      @RequestParam(name = "name") String name2, 
      
      // value 이름을 생략할 수 있다.
      @RequestParam("name") String name3, 

      /* @RequestParam("name") */ String name 
      // 요청 파라미터 이름과 메서드 파라미터(아규먼트)의 이름이 같다면 애노테이션을 생략해도 된다.
      ) {
        ...
      }

@GetMapping("h2")
  @ResponseBody
  public void handler2(
      PrintWriter out,

      // 애노테이션을 붙이면 필수 항목으로 간주한다.
      // 따라서 파라미터 값이 없으면 예외가 발생한다.
      @RequestParam("name1") String name1, 
      
      // 애노테이션을 붙이지 않으면 선택 항목으로 간주한다.
      // 따라서 파라미터 값이 없으면 null을 받는다.
      String name2, 
      
      // required 프로퍼티를 false로 설정하면 선택 항목으로 간주한다.
      @RequestParam(value = "name3", required = false) String name3,
      
      // 기본 값을 지정하면 파라미터 값이 없어도 된다.
      @RequestParam(value = "name4", defaultValue = "ohora") String name4
      ) {

    out.printf("name1=%s\n", name1);
    out.printf("name2=%s\n", name2);
    out.printf("name3=%s\n", name3);
    out.printf("name4=%s\n", name4);
  }
```

## 도메인 객체로 요청 파라미터 값 받기
아규먼트가 값 객체이면 요청 파라미터 중에서 값 객체의 프로퍼티 이름과 일치하는 항목에 대해 값을 넣어준다. 

값 객체 안에 또 값 객체가 있을 때는 `OGNL 방식`으로 요청 파라미터 값을 지정하면 된다. ex) ...&engine.model=ok&engine.cc=1980&engine.valve=16

```java
@GetMapping("h1")
  @ResponseBody
  public void handler1(
    PrintWriter out,

    String model,

    String maker,

    @RequestParam(defaultValue = "100") int capacity, // 프론트 컨트롤러가 String 값을 int로 변환해 준다.
      // 단 변환할 수 없을 경우 예외가 발생한다.

    boolean auto,
    // 프론트 컨트롤러가 String 값을 boolean으로 변환해 준다.
    // 단 변환할 수 없을 경우 예외가 발생한다.
    // "true", "false"는 대소문자 구분없이 true, false로 변환해 준다.
    // 1 ==> true, 0 ==> false 로 변환해 준다. 그 외 숫자는 예외 발생!

    Car car
  ) {
    ...
  }
```

<br>

```
*필드와 프로퍼티
"Property"
getFirstName(){-} firstName (read only)
setName(){-} name  | (read/write)
getName(){-} name  |
setAge(){-} age (write only)

get, set 날리고 첫대문자를 소문자로 변환하면 프로퍼티이름

class Score{
  String name <-- "Field"
}
```

## 프로퍼티 에디터 사용
클라이언트가 보낸 파라미터 값은 기본적으로 String이다 Request Handler의 아규먼트 타입(String, int 등)의 값으로 바꿀 때 `primitive type`에 대해서만 바꿀 수 있다. 그 외의 타입은 그냥 변환하려고 하면 예외가 발생한다.

이때 다른 타입에 대해서 처리할 수 있는 것이 바로 프로퍼티 에디터(타입 변환기)이다. 

java.sql.Date 타입을 통해 예를 들어보자
```java
@Controller
@RequestMapping("/c04_4")
public class Controller04_4 {
  @GetMapping("h1")
  @ResponseBody
  public void handler1(
    PrintWriter out,
    String model,
    @RequestParam(defaultValue = "5") int capacity, // String ===> int : Integer.parseInt(String)
    boolean auto, // String ===> boolean : Boolean.parseBoolean(String)
    Date createdDate // 프로퍼티 에디터를 설정하지 않으면 변환 오류 발생
    ) {
      ...
    }
}
```

위 코드만 가지고는 Date 타입을 처리할 수 없어 예외가 발생한다. 이를 처리하려면
타입을 변환해주는 프로퍼티에디터를 생성하고 `@InitBinder` 애노테이션으로 지정한 메서드를 호출하여 형변환시키는 도구를 설정한다. 
```java
// 프로퍼티 에디터 생성
public class DatePropertyEditor extends PropertyEditorSupport {

  @Override
  public void setAsText(String text) throws IllegalArgumentException {
    this.setValue(Date.valueOf(text)); //"yyyy-MM-dd" ===> java.sql.Date 객체로 변환
  }
}
```
```java
// 프로퍼티에디터를 호출할 메서드 지정 (Page Controller)
@InitBinder
public void initBinder(WebDataBinder 데이터변환등록기) {
  System.out.println("ok(),,,");

  데이터변환등록기.registerCustomEditor(
      java.sql.Date.class, // String 값을 어떤 타입으로 바꿀 것인지 지정
      new DatePropertyEditor()); // String 값을 해당 타입으로 변환해줄 변환기 지정
}
```

## 글로벌 프로퍼티 에디터 
다른 페이지 컨트롤러에서 등록한 프로퍼티 에디터는 사용할 수 없다. 각 페이지 컨트롤러마다 자신이 사용할 프로퍼티 에디터를 등록하면 중복이 발생하게 된다. 따라서 만약 여러 페이지 컨트롤러에서 공통으로 사용하는 프로퍼티 에디터가 있다면 이를 글로벌 프로퍼티 에디터로 등록하는 것이 편하다. 

글로벌 프로퍼티 에디터는 공통으로 사용할 `@InitBinder`, 즉 변환 등록기를 `@ControllerAdvice` 애노테이션이 적용된 별도의 클래스로 만들면 된다.

@ControllerAdvice는 페이지 컨트롤러를 실행할 때 충고하는 역할을 수행한다. 프론트 컨트롤러가 페이지 컨트롤러의 request handler를 호출하기 전에 이 애노테이션이 붙이 클래스를 참고하여 적절한 메서드를 호출한다. 

## @RequestHeader
클라이언트의 HTTP 요청 헤더를 받고 싶다면, request handler의 아규먼트 앞에 `@RequestHeader(헤더명)`  애노테이션을 붙인다. 


## @Cookie
쿠키를 클라이언트로 보내는 방법은 HttpServletResponse의 addCookie만 존재한다.
자동으로 리턴해주는 기능은 없다.
```java
@GetMapping("h1")
  @ResponseBody
  public void handler1(
      PrintWriter out,
      HttpServletResponse response // 쿠키값을 리턴하는 유일한 방법
  ) {
    // 이 메서드에서 쿠키를 클라이언트로 보낸다.
    try {
      // 쿠키의 값이 ASCII가 아니라면 URL 인코딩 해야만 데이터가 깨지지 않는다.
      // URL 인코딩을 하지 않으면 ? 문자로 변환된다.
      response.addCookie(new Cookie("name1", "AB가각"));
      response.addCookie(new Cookie("name2", URLEncoder.encode("AB가각", "UTF-8")));
      response.addCookie(new Cookie("name3", "HongKildong"));
      response.addCookie(new Cookie("age", "30"));

    } catch (Exception e) {
      e.printStackTrace();
    }

    out.println("send cookie!");
  }
```

클라이언트가 보낸 쿠키를 @CookieValue(쿠키명) 애노테이션을 request handler의 아규먼트 앞에 붙여 꺼낼 수 있다. 이 때 쿠키의 값이 ASCII가 아니라면 URL 인코딩 해야만 데이터가 깨지지 않는다. URL 인코딩을 하지 않으면 ? 문자로 변환된다.

```java
@GetMapping(value = "h2", produces = "text/plain;charset=UTF-8")
  @ResponseBody
  public String handler2(
      @CookieValue(value = "name1", required = false) String name1,
      @CookieValue(value = "name2", defaultValue = "") String name2,
      @CookieValue(value = "name3", defaultValue = "홍길동") String name3,
      @CookieValue(value = "age", defaultValue = "0") int age // String ===> int 자동 변환
  ) throws Exception {

    //
    // 1) URLEncoder.encode("AB가각", "UTF-8")
    // ==> JVM 문자열은 UTF-16 바이트 배열이다.
    //     0041 0042 ac00 ac01
    // ==> UTF-8 바이트로 변환한다.
    //     41 42 ea b0 80 ea b0 81
    // ==> 8비트 데이터가 짤리지 않도록 URL 인코딩으로 7비트화 시킨다.
    //     "AB%EA%B0%80%EA%B0%81"
    //     41 42 25 45 41 25 42 30 25 38 30 25 45 41 25 42 30 25 38 31
    // ==> 웹 브라우저에서는 받은 값을 그대로 저장
    //
    // 2) 쿠키를 다시 서버로 보내기
    // ==> 웹 브라우저는 저장된 값을 그대로 전송
    //     "AB%EA%B0%80%EA%B0%81"
    //     41 42 25 45 41 25 42 30 25 38 30 25 45 41 25 42 30 25 38 31
    // ==> 프론트 컨트롤러가 쿠키 값을 꺼낼 때 자동으로 URL 디코딩을 수행한다.
    //     즉 7비트 문자화된 코드를 값을 원래의 8비트 코드로 복원한다.
    //     41 42 ea b0 80 ea b0 81
    // ==> 디코딩 하여 나온 바이트 배열을 UTF-16으로 만든다.
    //     문제는 바이트 배열을 ISO-8859-1로 간주한다는 것이다.
    //     그래서 UTF-16으로 만들 때 무조건 앞에 00 1바이트를 붙인다.
    //     0041 0042 00ea 00b0 0080 00ea 00b0 0081
    //     그래서 한글이 깨진 것이다.
    //
    // 해결책:
    // => UTF-16을 ISO-8859-1 바이트 배열로 변경한다.
    //    41 42 ea b0 80 ea b0 81
    byte[] originBytes = name2.getBytes("ISO-8859-1");

    // => 다시 바이트 배열을 UTF-16으로 바꾼다.
    //    이때 바이트 배열이 UTF-8로 인코딩된 값임을 알려줘야 한다.
    //    0041 0042 ac00 ac01
    String namex = new String(originBytes, "UTF-8");

    return String.format(//
        "name1=%s\n name2=%s\n name2=%s\n name3=%s\n age=%d\n", //
        name1, name2, namex, name3, age);
  }
```


## multipart/form-data
클라이언트가 멀티파트 형식으로 전송한 데이터를 꺼내는 방식에는,
Servlet API에서 제공하는 Part를 사용하거나 Spring에서 제공하는 MultipartFile 타입의 아규먼트를 사용하는 방법이 있다.



## @RequestBody
클라이언트가 보낸 데이터를 통째로 받기 위해 사용하는 애노테이션이다.  
request handler의 아규먼트 앞에 @RequestBody를 붙이면 된다.
```java
@PostMapping(value="h1", produces="text/html;charset=UTF-8")
@ResponseBody
public String handler1(
    String name,
    int age,
    @RequestBody String data
  ) throws Exception {

    StringWriter out0 = new StringWriter();
    PrintWriter out = new PrintWriter(out0);
    out.println("<html><head><title>c04_9/h1</title></head><body>");
    out.println("<h1>결과</h1>");
    out.printf("<p>이름:%s</p>\n", name);
    out.printf("<p>나이:%s</p>\n", age);
    out.printf("<p>통데이터:%s</p>\n", data);
    out.println("</body></html>");
    return out0.toString();
  }
```


## @ResponseBody

**통째로 리턴**  
리턴 값이 클라이언트에게 보내는 콘텐트라면, 메서드 선언부에 이 애노테이션을 붙인다. 붙이지 않으면 JSP에게 보내는 view URL로 간주한다.
출력 콘텐트는 기본으로 HTML로 간주하는데, 한글은 ISO-8859-1 문자표에 정의된 코드가 아니기 때문에 ?로 변환되어 보내진다. 
```java
@GetMapping("h1")
@ResponseBody
public String handler1() {
  return "<html><body><h1>abc가각간</h1></body></html>";
}
```

**인코딩 문제 해결**  
한글 인코딩을 해결하고 싶으면 애노테이션의 produces 프로퍼티에 MIME 타입과 charset을 지정한다. HttpServletResponse에 대해 콘텐트 타입을 설정하는 것은 소용없다. 
```java
@GetMapping(value = "h2", produces = "text/html;charset=UTF-8")
@ResponseBody
public String handler2() {

  //response.setContentType("text/html;charset=UTF-8"); // X
  return "<html><body><h1>abc가각간<h1></body></html>";
}
```

**HttpEntity**  
다른 방법으로는 HttpEntity 객체에 콘텐트에 담아 리턴할 수 있다. 이 경우에는 리턴타입으로 콘텐트 타입을 알 수 있기 때문에 `@ResponseBody` 애노테이션을 붙이지 않아도 된다. 이 경우도 마찬가지로 ISO-8859-1 문자표의 코드를 변환하기 때문에 produces 프로퍼티에 콘텐트타입을 지정해주어야 한다. 
```java
@GetMapping(value = "h4", produces = "text/html;charset=UTF-8")
public HttpEntity<String> handler4(HttpServletResponse response) {

  HttpEntity<String> entity = new HttpEntity<>(
      "<html><body><h1>abc가각간<h1></body></html>");

  return entity;
}
```

**HttpHeaders**  
`@GetMapping` 애노테이션에 지정하는 방법 말고 HttpHeaders에 직접 Content-type을 설정하는 방법도 있다. 임의의 응답 헤더를 추가해야 하는 경우에 사용한다. 
```java
// 테스트:
// http://localhost:9999/eomcs-spring-webmvc/app1/c05_1/h5
@GetMapping("h5")
public HttpEntity<String> handler5(HttpServletResponse response) {

  HttpHeaders headers = new HttpHeaders();
  headers.add("Content-Type", "text/html;charset=UTF-8");

  HttpEntity<String> entity = new HttpEntity<>(
      "<html><body><h1>abc가각간<h1></body></html>",
      headers);

   return entity;
}
```

**ResponseEntity**
HttpEntity의 자식클래스로 콘텐트 헤더 뿐만 아니라 응답 상태 코드를 편하게 설정할 수 있는 기능을 가지고 있다. 
```java
// 테스트:
// http://localhost:8888/eomcs-spring-webmvc/app1/c05_1/h6
@GetMapping("h6")
public ResponseEntity<String> handler6(HttpServletResponse response) {

  HttpHeaders headers = new HttpHeaders();
  headers.add("Content-Type", "text/html;charset=UTF-8");

    
  headers.add("BIT-OK", "ohora");

  ResponseEntity<String> entity = new ResponseEntity<>(
      "<html><body><h1>abc가각간<h1></body></html>",
      headers,
      HttpStatus.OK // 응답 상태 코드를 설정할 수 있다.
  ); 

  return entity;
}
```

## view URL 리턴하기, 리다이렉트, forward/include
**view URL**
메서드 선언부에 @ResponseBody를 붙이지 않으면 jsp로 보내는 프론트 컨트롤러는 view URL로 간주한다.
```java
@GetMapping("h1")
public String handler1() {
  //리턴 URL의 '/'는 웹 애플리케이션 루트를 의미한다.
  return "/jsp/c05_2.jsp";
}
```

MVC 패턴에서는 항상 Controller에 의해 View가 통제되어야 한다. 반드시 Controller를 경유하여 JSP를 실행해야 View에 대해 일관성 있는 제어가 가능하다.

문제는 jsp파일을 웹 애플리케이션 일반 폴더에 두게 되면 클라이언트에서 접근하여 직접 실행을 요청할 수 있다.

따라서 아예 접근도 못하게 /WEB-INF 폴더에 jsp파일을 둔다. 
주의! 반대로 직접 요청하는 스태틱 리소스는 /WEB-INF 폴더 밖에 둔다. 

```java
// 테스트:
// http://localhost:8888/eomcs-spring-webmvc/app1/c05_2/h1
@GetMapping("h2")
public String handler2() {
  return "/WEB-INF/jsp/c05_2.jsp";
}

// View URL을 리턴하는 다른 방법들
// 테스트:
// http://localhost:8888/eomcs-spring-webmvc/app1/c05_2/h2
@GetMapping("h3")
public View handler3() {
  return new JstlView("/WEB-INF/jsp/c05_2.jsp");
}

// 테스트:
// http://localhost:8888/eomcs-spring-webmvc/app1/c05_2/h3
@GetMapping("h4")
public ModelAndView handler4() {
  System.out.println("===> /app1/c05_2/h4");
  ModelAndView mv = new ModelAndView();
  mv.setViewName("/WEB-INF/jsp/c05_2.jsp");
  return mv;
}
```

**Redirect**  
리다이렉트를 지정할 때는 URL 앞에 "redirect:" 접두어를 붙인다.
```java
// 테스트:
// http://localhost:8888/eomcs-spring-webmvc/app1/c05_2/h5
@GetMapping("h5")
public String handler5() {
  return "redirect:h4";
}
```

**forward/include**
포워드를 지정할 때는 "forward:"접두어
인클루드를 지정할 때는 "include:"접두어
```java
// 테스트:
// http://localhost:9999/eomcs-spring-webmvc/app1/c05_2/h6
@GetMapping("h6")
public String handler6() {
  return "forward:h4";
}
```

```
- forward vs include
forward는 

- redirect vs refresh
redirect는 요청을 받으면 작업 후 리턴하여 새 요청을 한다. refresh는 첫 요청을 받을 때 콘텐트를 함께 받아 "출력"한 후 리턴하고 새 요청을 한다.
```

## 요청 핸들러에서 view 컴포넌트(JSP) 쪽에 데이터 전달하기
c05_3.jsp
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jsp</title>
</head>
<body>
<h1>/WEB-INF/jsp/c05_3.jsp</h1>
이름: ${name}<br>
나이: ${age}<br>
재직여부: ${working}<br>
</body>
</html>
```
`${값}` 이 명령은 EL로서 request, response, session, context에 저장된 속성 객체의 프로퍼티를 차례로 찾아 출력하는데 전부 다 찾아도 없을 시에는 빈 문자열을 리턴한다. 

일반 서블릿 기술을 사용하여 전달하는 방법
```java
// 테스트:
// http://localhost:9999/eomcs-spring-webmvc/app1/c05_3/h1
@GetMapping("h1")
public String handler1(
    ServletRequest request) {

  // JSP가 꺼내 쓸 수 있도록 ServletRequest 객체에 직접 담는다.
  request.setAttribute("name", "홍길동");
  request.setAttribute("age", 20); // auto-boxing: int ===> Integer 객체
  request.setAttribute("working", true); // auto-boxing: boolean ===> Boolean 객체

  return "/WEB-INF/jsp/c05_3.jsp";
}
```
실무에서는 서블릿 기술을 최소화하기 때문에 위와 같은 방식은 잘 사용하지 않는다.
따라서 Map 객체나 Model 또는 ModelAndView 객체를 선언하여 값을 담아 전달하는 방식을 사용한다.

```java
// 테스트:
// http://localhost:9999/eomcs-spring-webmvc/app1/c05_3/h2
@GetMapping("h2")
public String handler2(Map<String,Object> map) {

  // 아규먼트에 Map 타입의 변수를 선언하면
  // 프론트 컨트롤러는 빈 맵 객체를 만들어 넘겨준다.
  // 이 맵 객체의 용도는 JSP에 전달할 값을 담는 용이다.
  // 맵 객체에 값을 담아 놓으면 프론트 컨트롤러가 JSP를 실행하기 전에
  // ServletRequest로 복사한다.
  // 따라서 ServletRequest에 값을 담는 것과 같다.
  map.put("name", "홍길동");
  map.put("age", 20); // auto-boxing
  map.put("working", true); // auto-boxing

  return "/WEB-INF/jsp/c05_3.jsp";
}

// 테스트:
// http://localhost:9999/eomcs-spring-webmvc/app1/c05_3/h3
@GetMapping("h3")
public String handler3(Model model) {

  // 아규먼트에 Model 타입의 변수를 선언하면
  // 프론트 컨트롤러는 모델 객체를 만들어 넘겨준다.
  // 이 객체의 용도는 Map 객체와 같다.
  // Map에 비해 기능은 적고 가볍다. 
  // Model은 제네릭을 지정할 필요가 없어 더 간결하다.
  // 따라서 실제 업무에서는 Model 객체를 더 많이 사용한다.
  model.addAttribute("name", "홍길동");
  model.addAttribute("age", 20); // auto-boxing
  model.addAttribute("working", true); // auto-boxing

  return "/WEB-INF/jsp/c05_3.jsp";
}

// 테스트:
//   http://localhost:9999/eomcs-spring-webmvc/app1/c05_3/h4
@GetMapping("h4")
public ModelAndView handler4() {
  // 파라미터를 받지 않는다.
  // request handler에서 ModelAndView 객체를 만들어 리턴한다.
  // => 이 객체의 용도는 Model과 view URL을 함께 리턴하는 것이다.
  ModelAndView mv = new ModelAndView();

  // JSP가 사용할 데이터를 담고
  mv.addObject("name", "홍길동");
  mv.addObject("age", 20); // auto-boxing
  mv.addObject("working", true); // auto-boxing

  // JSP 주소도 담는다.
  mv.setViewName("/WEB-INF/jsp/c05_3.jsp");

  return mv;
}
```



