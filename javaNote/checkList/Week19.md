# 학습점검목록 19주차

# 85일차(2024-03-18, 월)
### 1. request handler의 파라미터로 다룰 수 있는 타입을 설명할 수 있는가?
ServletRequest, HttpServletRequest, ServletResponse, HttpServletResponse, HttpSession, Map, Model 등이 있다. 단 ServletContext, ApplicationContext는 파라미터로 다룰 수 없어 의존 객체로 주입받아야 한다.

### 2. @RequestParam 사용법을 설명할 수 있는가?
클라이언트가 보낸 파라미터 값을 지정한 이름으로 받을 수 있는 애노테이션이다. 요청 파라미터 이름과 메서드 파라미터(아규먼트) 이름이 같은 경우 생략할 수 있다.

### 3. request handler에서 요청 파라미터 값을 객체로 받는 방법을 아는가?

### 4. 객체가 포함하고 있는 객체에 요청 파라미터 값을 받는 방법을 아는가?

### 5. 프로퍼티 에디터를 사용하여 요청 파라미터의 문자열을 다른 타입으로 변환할 수 있는가?

### 6. @ControllerAdvice, @InitBinder 사용법을 설명할 수 있는가?

### 7. @RequestHeader 사용법을 아는가?

### 8. User-Agent 요청 헤더를 다룰 수 있는가?

### 9. @CookieValue를 사용하여 쿠키를 다룰 수 있는가?
쿠키를 클라이언트로 보내는 방법은 response 객체를 주입받아 addCookie를 통해 추가하는 방법밖에 없다.

### 10. multipart/form-data 형식의 요청 파라미터 값을 다룰 수 있는가?

### 11. @RequestBody/@ResponseBody 사용법을 아는가?
@RequestBody 클라이언트가 보낸 데이터를 통째로 받을 때 사용하는 애노테이션이다.  
@ResponseBody는 클라이언트로 view URL이 아닌 클라이언트에게 데이터를 직접 보낼 때 사용한다.

### 12. request handler의 리턴 타입으로 String, HttpEntity, ResponseEntity를 사용하는 방법을 아는가?

### 13. request handler에서 View URL을 리턴하는 방법을 아는가?
@ResponseBody를 붙이지 않고 String을 보내면 프론트 컨트롤러가 view URL로 인식한다


# 86일차(2024-03-19, 화)
### 1. request handler에서 redirect, forward, include를 처리하는 방법을 설명할 수 있는가?

### 2. request handler에서 뷰 컴포넌트가 사용할 값을 리턴하는 방법을 설명할 수 있는가?

### 3. ViewResolver의 역할과 동작과정을 설명할 수 있는가?

### 4. view name이 절대 경로일 때와 상대 경로일 때에 따라 URL의 계산 달라지는 것을 설명할 수 있는가?

### 5. @PathVariable을 사용하여 URL에서 값을 추출하는 방법을 설명할 수 있는가?

### 6. @MatrixVariable 사용법을 설명할 수 있는가?


# 87일차(2024-03-20, 수)
### 1. @SessionAttribute, @ModelAttribute를 사용하여 세션 값을 제어할 수 있는가?

### 2. 인터셉터의 구동에 대해 설명하고 구현할 수 있는가?
프론트컨트롤러와 페이지컨트롤러 또는 프론트컨트롤러와 JSP 사이에 코드(기능) 삽입하고 싶을 때 인터셉터를 사용한다. HandlerInterceptor 인터셉터를 구현하여 언제 인터셉터를 실행할지에 대한 메서드를 오버라이딩한 후 IoC 컨테이너에 인터셉터를 등록한다

### 3. HttpMessageConverter에 대해 설명할 수 있는가?

### 4. 요청으로 보낸 JSON 데이터를 객체로 자동 변환하여 request handler의 파라미터로 받을 수 있는가?

### 5. request handler가 리턴한 객체를 JSON 데이터로 자동 변환하여 응답할 수 있는가?

### 6. 서블릿 또는 request handler에서 예외가 발생했을 때 오류를 출력할 페이지를 설정할 수 있는가?


# 88일차(2024-03-21, 목)
### 1. 웹 프로젝트에 Spring WebMVC를 설정할 수 있는가?

### 2. XML 설정 및 Java Config 설정을 모두 다룰 수 있는가?

### 3. Mybatis Persistence Framework의 구동 원리를 설명할 수 있는가?

### 4. Mybatis를 Spring WebMVC 프로젝트에 적용할 수 있는가?


# 89일차(2024-03-22, 금)
### 1. Mybatis의 <resultMap/>, <association/>, <collection/> 태그를 설명할 수 있는가?

### 2. Mybatis의 <typeAliases/> 태그를 설명할 수 있는가? 
import랑 유사한 역할

### 3. Mybatis 설정을 Java Config로 바꿀 수 있는가?

### 4. Spring 프레임워크의 @Transactional 애노테이션을 트랜잭션을 다룰 수 있도록 설정할 수 있는가?

### 5. Spring 프레임워크에서 @Transactional을 어떻게 처리하는지 구동 원리를 설명할 수 있는가?



