# Thymeleaf
타임리프란 자바에서 HTML 뷰 페이지를 작성하기 위한 템플릿이다. 뷰 템플릿은 컨트롤러가 전달하는 데이터를 받아 HTML로 변환하며, 동적으로 구성해주는 속성이다. 

타임리프는 



## Thymeleaf vs JSP






## Java Config로 타임리프 설정
공식문서 상에 나온 타임리프 설정 방법이다. 
- ThymeleafViewResolver, SpringResourceTemplateResolver, SpringTemplateEngine를 빈에 등록한다.
- viewResolver에 설정 정보를 담아 리턴하면 controller에서 리턴된 경로에서 html을 찾아 변환하는 방식으로 동작한다.

```java
// AppConfig
@Bean
public ViewResolver viewResolver() {
  InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
  viewResolver.setPrefix("/WEB-INF/jsp/");
//    viewResolver.setSuffix(".jsp");
  viewResolver.setViewNames("*.jsp");
  viewResolver.setOrder(1); // 우선순위 1번째
  return viewResolver;
}

@Bean
public ThymeleafViewResolver thymeleafViewResolver(ISpringTemplateEngine springTemplateEngine) {
  ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
  viewResolver.setTemplateEngine(springTemplateEngine);
  viewResolver.setViewNames(new String[]{"*.html", "*.xhtml", "*"});
  viewResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
  viewResolver.setOrder(2);
  return viewResolver;
}

@Bean
public SpringResourceTemplateResolver templateResolver(ApplicationContext applicationContext) {
  SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
  templateResolver.setApplicationContext(applicationContext);
  templateResolver.setPrefix("/WEB-INF/templates/");
  templateResolver.setSuffix(".html");
  templateResolver.setTemplateMode(TemplateMode.HTML);

    // HTML 템플릿을 Thymeleaf 엔진이 사용할 수 있도록 특정 형식에 맞춰 컴파일 해 둘 것인가?
    // 컴파일? => 어떤 형식의 데이터를 다른 형식의 데이터로 바꾸는 것도 컴파일이다.
    // 개발하는 동안 템플릿 파일의 내용을 자주 바꿀 수 있기 실행할 때마다 컴파일 하는 것이 좋다.
    // 개발이 완료된 다음에는 템플릿 파일을 컴파일한 결과를 보관해두는 것이 성능을 높인다.

    // cacheable => 컴파일 결과를 캐시에 보관할지 여부를 설정한다.
    // true인 경우 한번만 컴파일 하고 서버를 종료하고 다시 실행해야 컴파일한다.
    // false는 매번 실행할 때마다 컴파일하기 때문에 서버를 종료하지 않아도 컴파일한다.
  templateResolver.setCacheable(false);
  return templateResolver;
  }

@Bean
public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
  SpringTemplateEngine templateEngine = new SpringTemplateEngine();
  templateEngine.setTemplateResolver(templateResolver);
  templateEngine.setEnableSpringELCompiler(true);
  return templateEngine;
}
```

## Thymeleaf의 동작원리
타임리프의 장점 중 하나는 순수 HTML 문서를 사용하여 컴파일한다는 것이다. 

예를 들면 HTML5 공식 문법으로 지원하는 사용자옵션(Data-th-..) 태그를 인식하여 컴파일하는 방식이다. 




