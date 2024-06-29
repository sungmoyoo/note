# Spring WebMVC 1

## 여러 개의 DispatcherServlet



# Java config로 IoC Container 설정

## XML에서 컨테이너 클래스 변경
Java config로 IoC 컨테이너로 지정하면 애노테이션을 통해 설정하므로 XML을 작성할 필요가 없다. 

기본 IoC 컨테이너는 XmlWebApplicationContext이다. 만약 Java config로 IoC 컨테이너를 지정하고 싶다면 다음과 같이 초기화 파라미터를 통해 DispatcherServlet이 사용할 IoC 컨테이너 클래스와 Java Config 클래스를 설정한다.

`<init-param>` 태그에서 contextClass를 `AnnotationConfigWebApplicationContext`로 설정해주고 contextConfigLocation의 값을 패키지 경로로 설정해주면 된다.
```xml
<!---->
<init-param>
  <param-name>contextClass</param-name>
  <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
</init-param>
```

## 애노테이션으로 설정 선언


## 자바 클래스에서 DispatcherServlet을 직접 만들어 배치

```java
@WebListener
public class WebInitListener implements ServletContextListener {
  private static Log log = LogFactory.getLog(WebInitListener.class);

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    log.debug("contextInitialized() 호출");

    ServletContext sc = sce.getServletContext();

    AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
    appContext.register(AppConfig.class);

    Dynamic 서블릿설정 = sc.addServlet("app", new DispatcherServlet(appContext));
    서블릿설정.addMapping("/app/*");
    서블릿설정.setLoadOnStartup(1);
  }
}
```

># 서블릿 배치 방법  
선언적 방법(Declarative)  
: 


프로그램적인 방법(Programmatic)  
:


# XML 없이 순수 자바클래스와 애노테이션을 이용한 실행
스프링 프레임워크에서는 XML을 사용하여 설정을 초기화하는 작업을 한다. 하지만 스프링 부트 방식에서는 XML을 아예 사용하지 않는다.



## 동작과정
웹 애플리케이션이 실행되면 톰캣서버가 .jar파일에 /WEB-INF/services 경로에 잇는 서비스 파일을 찾는다. 
파일을 찾으면 서블릿 컨테이너는 클래스 정보를 읽어
javax.servlet.ServletContainerInitializer를 구현하는 org.springframework.web.SpringServletContainerInitializer 객체를 생성한 후 WebApplicationInitializer 구현체를 모두 찾아 onStartup()에 전달해 메서드를 실행한다. 





