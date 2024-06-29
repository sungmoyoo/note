# Spring WebMVC 2

# HttpSession
## 세션 값 사용
HttpSession 객체를 사용하려면 아규먼트로 받아야 한다.

값 보관은 `setAttribute()` 값을 꺼낼 때는 `getAttribute()`를 사용한다. 
```java
@GetMapping(value = "h1", produces = "text/plain;charset=UTF-8")
@ResponseBody
public String handler1(HttpSession session) {

  session.setAttribute("name", "홍길동");
  session.setAttribute("age", "20");

  return "세션에 값을 보관했음!";
}

@GetMapping(value = "h2", produces = "text/plain;charset=UTF-8")
@ResponseBody
public String handler2(HttpSession session) {

   return String.format("name=%s, age=%s, name2=%s, age2=%s, tel2=%s",
      session.getAttribute("name"),
      session.getAttribute("age"),
      session.getAttribute("name2"),
      session.getAttribute("age2"),
      session.getAttribute("tel2"));
}
```

세션을 무효화시키고 싶다면 `invalidate()`를 호출하면 된다. 
```java
@GetMapping(value = "h3", produces = "text/plain;charset=UTF-8")
@ResponseBody
public String handler3(HttpSession session) {
  session.invalidate();

  return "세션을 무효화시켰음!";
}
```

## @ModelAttribute와 @SessionAttribute
request handler가 뷰 컴포넌트(jsp)에 전달하는 값 중에서 세션에 보관할 값의 이름을 지정하면 그 값이 세션에 보관된다. 

@ModelAttribute가 현재 페이지 컨트롤러의 @SessionAttributes 에 지정된 이름이 아니라면, 프론트 컨트롤러는 요청 파라미터에서 해당 이름의 값을 찾아 넘겨준다.
만약 요청 파라미터에 값이 없다면 빈 문자열을 넘겨준다.
```java
@GetMapping(value = "h4", produces = "text/plain;charset=UTF-8")
@ResponseBody
public String handler4(
    @ModelAttribute("name") String name,
    @ModelAttribute("age") String age,
    @ModelAttribute("name2") String name2,
    @ModelAttribute("age2") String age2) {

  return String.format("name=%s, age=%s, name2=%s, age2=%s",
      name, age, name2, age2);
}
```

Model 객체에 값을 담으면 프론트 컨트롤러는 ServletRequest 보관소에 값을 보관한다. 만약 @SessionAttributes 에서 지정한 이름의 값이라면
HttpSession 객체에'도' 보관된다.

위와 달리 현재 페이지 컨트롤러의 @SessionAttributes에 지정된 이름이 존재하는 경우 @ModelAttribute는 요청 파라미터가 아닌 세션에서 값을 찾는다. 만약 지정된 이름으로 보관된 값이 없을 경우 에러를 출력한다. 

```java
@Controller
@RequestMapping("/c03_2")
@SessionAttributes({"name2","age2"})
public class Controller03_2 {

  @GetMapping(value = "h1", produces = "text/plain;charset=UTF-8")
  @ResponseBody
  public String handler1(Model model) {
    model.addAttribute("name2", "임꺽정");
    model.addAttribute("age2", "30");

    model.addAttribute("tel2", "1111-2222");

    return "세션에 값 보관했음!";
  }

  // 세션 조회
  @GetMapping(value = "h2", produces = "text/plain;charset=UTF-8")
  @ResponseBody
  public String handler2(HttpSession session) {
    return String.format("name=%s, age=%s, name2=%s, age2=%s, tel2=%s",
        session.getAttribute("name"),
        session.getAttribute("age"),
        session.getAttribute("name2"),
        session.getAttribute("age2"),
        session.getAttribute("tel2"));
  }

  // 세션 초기화
  @GetMapping(value = "h3", produces = "text/plain;charset=UTF-8")
  @ResponseBody
  public String handler3(HttpSession session) {
    session.invalidate();
    return "세션 무효화되었음!";
  }

  // 세션 값 꺼내기
  @GetMapping(value = "h4", produces = "text/plain;charset=UTF-8")
  @ResponseBody
  public String handler4(
      @ModelAttribute("name") String name,
      @ModelAttribute("age") String age,
      @ModelAttribute("name2") String name2,
      @ModelAttribute("age2") String age2,

      @ModelAttribute("tel2") String tel2) {


    return String.format("name=%s, age=%s, name2=%s, age2=%s, tel2=%s",
        name, age, name2, age2, tel2);
  }
}
```
여기서 주의해야 할 점은 세션을 무효화한 후 새 세션을 사용할 때 문제가 발생할 수 있다는 것이다. 

예를 들어 handler3을 통해 세션을 무효화 한 후 바로 세션에 model.addAttribute() 메서드로 값을 보관해도 값이 보관되지 않는다. 

왜?
바로 세션이 무효화되고 나서 다시 세션을 사용하려면 request.getSession()을 호출하여 새 세션을 만들어 리턴해야 하기 때문이다. 즉 .invalidate()는 세션 자체를 없애버리는 것이고 세션에 값을 보관하려 해도 세션 자체가 없기 때문에 보관이 안되는 것이다. 

따라서 HttpSession을 파라미터로 받는 메서드를 먼저 호출하고 세션에 값을 보관한 후 세션 조회를 하면 정상적으로 값을 들어간 것을 확인할 수 있다.

실무에서는 이러한 문제를 해결하기 위해 HttpSession을 사용하지 않아도 값을 보관하는 handler1 메서드에 파라미터로 HttpSession을 넣기도 한다. 

## status.setComplete()
세션을 무효화시키는 방법은 session.invalidate() 를 사용한다. 이 메서드는 세션 자체를 제거해버리기 때문에 다시 getSession()이 호출되어야만 세션을 사용할 수 있다. 

세션은 특정 작업을 진행하는 동안 요청과 요청 사이에 작업을 공유하기 위해 사용된다. 컨텍스트는 모든 클라이언트가 공유하기에 각 클라이언트마다 실행되는 개별 작업에는 세션이 적합하다. 

작업이 끝난 경우 새 작업을 위해 기존 작업에 사용한 값을 제거해야 하는데 이를 위해 세션을 삭제하지 않고 값만 제거해주는 status.setComplete()를 사용한다. 

```java
  @GetMapping(value = "h3", produces = "text/plain;charset=UTF-8")
  @ResponseBody
  public String handler3(SessionStatus status) {
    status.setComplete();

    return "status.setComplete()";
  }
```

# 필터
필터란 기존 코드를 손대지 않고 기능을 추가하기 위해 특정 작업을 하는 코드를 클래스 사이에 삽입하는 기법/기술이다. 

서블릿 컨테이너와 프론트 컨트롤러 사이에 코드를 삽입하는 기술은 Servlet API의 Filter(doFilter()),  
프론트 컨트롤러와 페이지 컨트롤러 사이 또는 프론트 컨트롤러와 뷰 컴포넌트 사이에 코드를 삽입하는 기술은 인터셉터,  
페이지 컨트롤러와 DAO 사이에는 AOP Proxy 기술로 필터링 기법이 적용된다.


## 인터셉터 만들기
1) HandlerInterceptor 인터셉터를 구현한 후 
preHandle, postHandle, afterCompletion 메서드를 재정의한다. 
```java
public class Interceptor1 implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // 페이지 컨트롤러의 핸들러를 호출하기 전에 이 메서드가 먼저 호출된다.
    System.out.println("Interceptor1.preHandle()");

    // 다음 인터셉터나 페이지 컨트롤러를 계속 실행하고 싶다면 true를 리턴한다.
    // 여기서 요청 처리를 완료하고 싶다면 false를 리턴한다.
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    // 페이지 컨트롤러의 핸들러가 리턴한 후 이 메서드가 호출된다.
    System.out.println("Interceptor1.postHandle()");
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    // JSP를 실행한 후 이 메서드가 호출된다.
    System.out.println("Interceptor1.afterCompletion()");
  }
}
```

2) IoC 컨테이너에 인터셉터 배치한다.
```java
@EnableWebMvc
@ComponentScan("bitcamp.app2")
public class App2Config implements WebMvcConfigurer {
  ...

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 인터셉터를 적용할 경로를 지정하지 않으면 모든 request handler에 대해 적용한다.
    registry
    .addInterceptor(new Interceptor1());
    
    // /c04_1/ 바로 아래에 있는 자원에 대해서만 인터셉터를 적용한다.
    registry
    .addInterceptor(new Interceptor2()).addPathPatterns("/c04_1/*");

    // /c04_1/ 의 모든 하위 경로에 있는 자원에 대해 인터셉터를 적용한다.
    registry
    .addInterceptor(new Interceptor3()).addPathPatterns("/c04_1/**");

    // 특정 하위 경로에 대해 인터셉터 적용을 제외한다. 
    registry
    .addInterceptor(new Interceptor4())
    .addPathPatterns("/c04_1/**")
    .excludePathPatterns("/c04_1/a/**");
  }

  ...
}
```
이처럼 인터셉터를 등록한 후 요청을 실행하면 request handler 실행 전, 실행 후 그리고 JSP 실행 후 인터셉터를 실행시킬 수 있다. 또 어떤 addPathPatterns(), excludePathPatterns() 등을 통해 request handler에 대해 인터셉터를 적용할지 제어할 수 있다. 

# JSON 콘텐트 출력하기
1) JSP에 작성
2) Gson 구글 라이브러리 사용
3) Jackson 라이브러리 사용

## HttpMessageConverter
JSON 형식을 다루는 라이브러리@Controller가 붙은 일반적인 페이지 컨트롤러의 요청 핸들러를 실행할 때 요청 파라미터의 문자열을 int나 boolean 등으로 바꾸기 위해
기본으로 장착된 변환기를 사용한다. 그 변환기는 HttpMessageConverter 규칙에 따라 만든 변환기이다.
    
또한 요청 핸들러가 리턴한 값을 문자열로 만들어 클라이언트로 출력할 때도
이 HttpMessageConverter를 사용한다. 즉 클라인트가 보낸 파라미터 값을 핸들러의 아규먼트 타입으로 바꿀 때도 이 변환기를 사용하고 핸들러의 리턴 값을 클라이언트로 보내기 위해 문자열로 바꿀 때도 이 변환기를 사용한다.
    
스프링이 사용하는 기본 데이터 변환기는 MappingJackson2HttpMessageConverter 이다.
만약 이 변환기가 없다면 Google의 Gson 변환기를 사용한다. 구글의 Gson 변환기 마저 없다면 컨버터가 없다는 예외를 발생시킨다. 컨버터가 하는 일은 JSON 데이터로 변환하는 것이다.  

>클라이언트가 보낸 JSON 요청 파라미터 ===> 자바 객체
>핸들러가 리턴하는 자바 객체 ===> JSON 형식의 문자열

Jackson 라이브러리와 Gson 라이브러리를 동시에 추가한다면 Jackson 라이브러리를 사용한다. 

## @RestController
페이지 컨트롤러를 @RestController로 선언하면, 리턴 값은 HttpMessageConverter에 의해 자동으로 변환된다. 따라서 @ResponseBody를 붙일 필요가 없어진다.

## @JsonFormat
클라이언트...

MappingJackson2HttpMessageConverter에만 적용되는 애노테이션으로 도메인 객체의 프로퍼티에 이 애노테이션을 붙이면 2019-05-01 이나 2019-5-1 모두 처리할 수 있다. 뿐만 아니라, 도메인 객체를 JSON 문자열로 변환할 때도 해당 형식으로 변환된다.



## 예외 처리

에러가 발생되는 @ExceptionHandler 애노테이션이 붙은 메서드가 호출되고 request handler가 던진 에러를 파라미터로 받는다.

이를 활용하면 톰캣의 기본 에러 페이지가 아닌 개발자가 직접 에러 페이지를 커스터마이징 할 수 있다.

이 방식은 페이지 컨트롤러가 100개 200개 많아질수록 직접 다 하나하나 설정해주어야 하는 치명적인 단점이 존재한다.

따라서 모든 페이지 컨트롤러에 공통적으로 적용하고자 한다면 에러 컨트롤러를  글로벌 컨트롤러로 적용시키거나 web.xml에 <error-page> 태그를 추가하여 설정할 수 있다. 

>*오류처리 순서
><img>



