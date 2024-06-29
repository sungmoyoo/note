# 학습점검목록 18주차

# 80일차(2024-03-11, 월)
### 1. IoC의 개념을 설명할 수 있는가?

### 2. DI를 설명할 수 있는가?

### 3. ApplicationContext 인터페이스를 설명할 수 있는가?

### 4. ApplicationContext 구현체를 설명할 수 있는가?

### 5. XML로 Bean 생성을 설정하는 방법을 아는가?

### 6. 객체를 생성할 때 호출될 생성자를 지정하고 파라미터를 넘기는 방법을 아는가?

### 7. 객체를 생성할 때 세터를 호출하여 인스턴스 필드를 초기화시키는 방법을 아는가?

### 8. 객체를 생성할 때 배열이나 컬렉션 프로퍼티의 값을 설정하는 방법을 아는가?

### 9. 객체에 의존 객체를 주입하는 방법을 아는가?

### 10. 팩토리 메서드를 호출하여 객체를 생성하는 방법을 아는가?
- 스태틱 메서드
- 인스턴스 메서드
- 

### 11. 프로퍼티 값을 설정할 때 String을 특정 타입의 값으로 변환하는 방법을 아는가?


# 81일차(2024-03-12, 화)
### 1. @Autowired를 이용하여 의존객체를 주입하는 방법을 설명할 수 있는가?

### 2. BeanPostProcessor 동작 원리와 사용법을 설명할 수 있는가?

### 3. 생성자를 통해 의존 객체를 주입하는 방법을 설명할 수 있는가?

### 4. 의존 객체를 주입할 때 생성자, 셋터, 필드 주입 방식의 특징을 설명할 수 있는가?

### 5. @Qualifie, @Resource 애노테이션을 사용할 수 있는가?

### 6. @Component를 이용하여 객체 생성을 자동화할 수 있는가?

### 7. @Controller, @Service, @Repository의 용도를 설명할 수 있는가?

### 8. XML 대신 Java config를 사용하여 IoC 컨테이너의 동작을 설명할 수 있는가?

### 9. @Bean, @Configuration의 용도를 설명할 수 있는가?

### 10. @PropertySource, @Value를 이용하여 .properties 파일의 값을 다룰 수 있는가?

### 11. @ComponentScan의 사용법을 설명할 수 있는가?

### 12. Actor와 Use-case의 개념을 설명할 수 있는가?

### 13. Actor를 식별할 수 있는가?


# 82일차(2024-03-13, 수)
### 1. 서블릿 기반 웹프로젝트에 Spring WebMVC를 적용할 수 있는가?
DispatcherServlet이라는 프론트 컨트롤러를 DD파일이라는 설정파일에 등록해준다. 

### 2. DispatcherServlet의 IoC 컨테이너 설정 방법을 설명할 수 있는가?
XML로 설정하는 방법과 Java config로 설정하는 방법으로 나누어진다. 
- 
- 

### 3. ContextLoaderListener의 IoC 컨테이너 설정 방법을 설명할 수 있는가?

### 4. DispatcherServlet의 IoC 컨테이너와 ContextLoaderListener의 IoC 컨테이너의 관계를 설명할 수 있는가?


# 83일차(2024-03-14, 목)
### 1. ServletContainerInitializer의 구동 원리를 설명할 수 있는가?

### 2. SpringContainerInitializer 클래스의 구동 원리를 설명할 수 있는가?

### 3. WebApplicationInitializer의 구동 원리를 설명할 수 있는가?

### 4. WebApplicationInitializer를 활용하여 ContextLoaderListener와 DispatcherServlet을 설정할 수 있는가?


# 84일차(2024-03-15, 금)
### 1. @Controller와 @RequestMapping을 설명할 수 있는가?

### 2. GET요청과 POST 요청을 구분하여 처리할 수 있는가?

### 3. @GetMapping, @PostMapping 애노테이션을 설명할 수 있는가?

### 4. 파라미터의 이름으로 요청을 구분할 수 있는가?

### 5. 요청 프로토콜의 헤더 이름으로 구분할 수 있는가?

### 6. 요청 프로토콜의 Accept 헤더의 값을 요청을 구분할 수 있는가?
클라이언트가 보낸 Accept 헤더의 값을 통해 구분하는 방법은 produces를 사용한다.

### 7. 요청 프로토콜의 Content-type 헤더의 값으로 요청을 구분할 수 있는가?
consumes

### 8. @RequestBody, @ResponseBody의 사용법을 설명할 수 있는가?
