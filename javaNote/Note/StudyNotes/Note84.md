# Spring MVC 1
# 페이지 컨트롤러 만드는 방법

## 페이지 컨트롤러와 핸들러
1. 애노테이션 설정
  - 클래스 선언에 @Controller 애노테이션을 붙인다.
  - @RequestMapping 애노테이션으로 컨트롤러에 URL을 매핑한다.

2. 메서드 설정
  - 메서드에 @RequestMapping을 붙여 요청이 들어왔을 때 호출될 메서드임을 표시한다.
  - 만약 메서드 리턴값이 view url이 아닌 클라이언트에게 출력할 내용이라면 @ResponseBody 애노테이션을 붙인다.
  - 요청이 들어 왔을 때 호출되는 메서드를 "request handler" 라 부른다.  
  - URL을 지정할 때 하나의 URL에 여러 메서드 매핑은 안된다. 반대로 request handler에 여러 개의 URL을 매핑은 가능하다.
  - 디렉토리 형식으로도 지정할 수 있다. 

## 기본 URL과 상세 URL 분리하여 설정


## GET, POST 구분
@RequestMapping에 아무런 파라미터도 주지 않으면 GET과 POST 모두 받는다.
method를 enumeration 상수로 정해주면 매핑 에러가 발생하지 않는다. 
```java
@Controller
@RequestMapping("/c02_1")
public class Controller02_1 {

  @RequestMapping(method = RequestMethod.GET) // GET 요청일 때만 호출된다.
  @ResponseBody
  public String handler1() {
    return "get";
  }

  @RequestMapping(method = RequestMethod.POST) // POST 요청일 때만 호출된다.
  @ResponseBody
  public String handler2() {
    return "post";
  }
}
```

## 파라미터 이름으로 구분하기
같은 GET 요청이라도 파라미터의 이름으로 요청을 구분하여 더 디테일하게 요청을 다룰 수 있다. 
```java
// Get 요청 메서드
@GetMapping(params = "name")
@ResponseBody
public String handler() {
  return "handler";
}
```

## 요청 헤더의 이름으로 구분하기

```java
  @GetMapping(headers="name")
  @RequestMapping(method = RequestMethod.GET, headers = "name")
  @ResponseBody
  public String handler() {
    return "handler";
  }

```

## Accept 요청 헤더의 값에 따라 구분하기
Accept 헤더는 HTTP 클라이언트에서에서 서버에 요청할 때 받고자 하는 Content type을 알려주는 헤더이다. 파라미터로 들어가는 produces를 설정하면 클라이언트가 Accept할 수 있는 헤더 값과 일치하는 메서드를 호출한다.
```java
  @GetMapping(produces = "text/html")
  @ResponseBody
  public String handler1() {
    return "handler1";
  }

  @GetMapping(produces = "application/json")
  @ResponseBody
  public String handler2() {
    return "{\"title\":\"text\"}";
  }

  @GetMapping(produces = "text/csv")
  @ResponseBody
  public String handler3() {
    return "1, hong, 20";
  }
```

```
// Accept 헤더 예
text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;

앞에 적힌 콘텐트 타입부터 확인하는데 만약 모든 헤더가 일치하지 않는다면, 와일드카드(*/*)에 따라 아무 타입이나 받는다.
```

## Content type 헤더의 값에 따라 구분하기
HTTP 클라이언트가 보내는 데이터의 콘텐트 타입이다.  
클라이언트가 POST로 요청할 때 보내는 데이터의 유형에 따라 호출될 메서드를 구분할 때 사용한다.
```java     
  @PostMapping(consumes = "application/x-www-form-urlencoded")
  @ResponseBody
  public String handler1() {
    return "handler1";
  }
```

## 자바 코드로 Multipart 지정
XML에서는 `<multipart-config>` 태그와 애노테이션으로 Multipart를 처리하였다. 

만약 XML을 사용하지 않고 WebApplicationInitializer를 통해 DispatcherServlet을 등록한다면 Multipart 형식을 따로 지정해주어야 한다.

WebApplicationInitializer 인터페이스 구현체를 사용한다면 직접 구현해서  DispatcherServlet에 Multipart 형식을 지정해야 한다. 

만약 AbstractDispatcherInitializer 인터페이스 구현체를 상속받은 경우 customizeRegistration() 메서드를 통해 설정할 수 있다.

```java
@Override
  protected void customizeRegistration(Dynamic registration) {
    registration.setMultipartConfig(new MultipartConfigElement(
        new File("./temp").getAbsolutePath(), // 파일을 다운로드 받을 경로
        1024 * 1024 * 10, // maxFileSize
        1024 * 1024 * 100, // maxRequestSize,
        1024 * 1024)); // fileSizeThreshold
  }
```

>*postman
>API 개발 및 테스트에 사용되는 협업 도구 및 개발 환경이다. 요청 응답 결과를 쉽게 확인할 수 있고 개발자가 임의로 Header, Accept 값을 변경하는 기능, 응답 결과를 검증하는 기능을 제공하여 편리하게 빌드 및 테스트를 할 수 있다. 