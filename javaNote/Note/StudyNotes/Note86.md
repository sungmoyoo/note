# Spring WebMVC 2

# View Resolver 사용
## 절대경로와 상대경로
```java
  // 테스트:
  // http://localhost:8888/app2/c01_1/h1 
  @GetMapping("h1")
  public String handler1(Model model) {

    model.addAttribute("name", "홍길동");
    model.addAttribute("age", 20);

    return "/jsp/c01_1.jsp";
    // return "jsp/c01_1.jsp" 오류 발생!
  }
```

기본 ViewResolver는 리턴 값으로 받은 view name으로 JSP를 찾는다. 
1) 절대경로로 시작할 때 - 앞에가 '/' 로 시작할 때  
  ex) return "/jsp/c01_1.jsp  
      view URL = /웹애플리케이션루트경로 + view name  
               = /jsp/c01_1.jsp  
2) 상대경로로 시작할 떄 - 앞에 '/' 가 없는 경우  
  ex) return "jsp/c01_1.jsp  
      view URL = 현재 URL 경로 + view name  
               = /웹애플리케이션경로/app2/c01_1 + jsp/c01_1.jsp  
               = /웹애플리케이션경로/app2/c01_1/jsp/c01_1.jsp  

즉 view name을 JSP URL로 간주한다.

따라서 위의 return 문의 view name은 다음 JSP 경로와 같다. 
```
/컨텍스트경로/jsp/c01_1.jsp
```



## view 이름 리턴 안할 때 발생하는 문제
```java
  // 테스트:
  // http://localhost:8888/app2/c01_1/h2
  @GetMapping("h2")
  public void handler2(Model model) {
    model.addAttribute("name", "홍길동2");
    model.addAttribute("age", 30);
  }
```

기본 ViewResolver를 사용할 때, 뷰 이름을 리턴하지 않으면 request handler의 URL을 상대 경로 view name으로 사용한다.  
즉 다음 리턴문과 같다.  
```java
return "c01_1/h2

계산방법
ex) 현재 URL = /c01_1/h2
    view URL = 현재 URL 경로 + view name
             = 웹 애플리케이션 경로 
             = /app2/c01_1/c01_1/h2
```

따라서 잘못 계산된 view URL로 JSP를 찾으니까 오류가 발생한다. 

## WEB-INF
MVC 모델에서 JSP는 뷰 컴포넌트로서 출력이라는 역할을 담당하고 출력한 데이터를 준비하는 일은 페이지 컨트롤러가 담당한다. 그래서 JSP를 실행할 때는 직접 JSP를 요청하지 않고 항상 페이지 컨트롤러를 통해 실행해야 한다. 

그런데 웹 디렉토리에 JSP를 두면 클라이언트에서 JSP를 직접 요청할 수 있다. 이와 같은 직접 요청을 막기 위해 JSP파일을 /WEB-INF 폴더 아래 두는 것을 권장한다.
```java
@GetMapping("h3")
public String handler3(Map<String, Object> map) {
  map.put("name", "홍길동3");
  map.put("age", 40);

  return "/WEB-INF/jsp/c01_1.jsp";
}
```



JSP URL을 자동 변환? 
InternalResourceViewResolver로 교체한 다음의 JSP URL은?
" /WEB-INF/jsp2//jsp/c01_1.jsp.jsp "

## InternalResourceViewResolver
view name을 직접 리턴하는 경우 --------

```java
@ComponentScan("bitcamp.app2")
public class App2Config {
  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver vr = new InternalResourceViewResolver(
      "/WEB-INF/jsp/", // prefix
      ".jsp" // suffix
    );

    return vr;
  }
}
```
---------------------

```java
@GetMapping("h1")
public String handler1(Model model) {

  model.addAttribute("name", "홍길동");
  model.addAttribute("age", 20);

  return "c01_1";
}
```
---------------------


```java
@GetMapping("h2")
public void handler2(Model model) {
  model.addAttribute("name", "홍길동2");
  model.addAttribute("age", 30);

  // view name = /c01_2/h2
  // view URL = /WEB-INF/jsp/c01_2/h2.jsp
}
```
--------------------



## 클라이언트로부터 값을 받는 일반적인 방법
Query String 으로 받는 방법이다. 아규먼트 앞에 @RequestParam을 붙여도 되고 아규먼트이 이름이 요청 파라미터의 이름과 같다면 @RequestParam을 생략해도 된다.
```java
@GetMapping
@ResponseBody
public String handler1(String name, int age) {
  return String.format("name=%s, age=%d", name, age);
}
```

## @PathVariable
**{변수명}/{변수명}**
request handler의 URL을 설정할 때 "{변수명}/{변수명}"을 선언하고 아규먼트로 @PathVariable(변수명) String 아규먼트 를 선언하여 받을 수 있다.
```java
@GetMapping("{name}/{age}")
@ResponseBody
public String handler2(
    @PathVariable String name,
    @PathVariable int age
    ) {

  return String.format("name=%s, age=%d", name, age);
}
```

**{변수명}_{변수명}**
```java
@GetMapping("{name}_{age}")
@ResponseBody
public String handler3(
    @PathVariable String name,
    @PathVariable int age
    ) {
  return String.format("name=%s, age=%d", name, age);
}
```

## @MatrixVariable
value값 중에서 name 항목의 값을 받고 싶을 때 @MatrixVariable 을 사용한다.  
이때 value의 형식은 "이름=값;이름=값;이름=값" 형태여야 한다.
```java
@GetMapping(value = "{value}", produces = "text/plain;charset=UTF-8")
@ResponseBody
public String handler2(//
    @PathVariable("value") String value,
    @MatrixVariable(required = false) String name,
    @MatrixVariable(defaultValue = "20") int age
) {
  return String.format("value:%s \n name:%s, age:%d", value, name, age);
}
```

위 request handler를 실행시키면 value는 출력되는데 @MatrixVariable 애노테이션을 설정한 name과 age는 요청한 대로 출력되지 않는다. 

why? @MatrixVariable 애노테이션을 사용하려면 IoC 컨테이너에서 이 애노테이션을 활성화시키는 설정을 추가해야 한다.
```java
// Java Config 설정
@EnableWebMvc
@ComponentScan("bitcamp.app2")
public class App2Config implements WebMvcConfigurer {
  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver vr = new InternalResourceViewResolver(
      "/WEB-INF/jsp/", // prefix
      ".jsp" // suffix
    );
    return vr;
  }

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    UrlPathHelper urlPathHelper = new UrlPathHelper();
    urlPathHelper.setRemoveSemicolonContent(false);
    configurer.setUrlPathHelper(urlPathHelper);
  }
}
```


