# IoC Container 

# XML 설정
## 의존 객체 주입 자동화
- 기존 프로퍼티로 객체 수동 주입
```xml
<bean id="c1" class="com.eomcs.spring.ioc.ex08.a.Car">
    <property name="model" value="티코"/>
    <property name="maker" value="비트자동차"/>
    <property name="cc" value="890"/>
    <property name="auto" value="true"/>
    <!-- 의존 객체 주입(Dependency Injection; DI) -->
    <property name="engine" ref="e1"/>
</bean>

<bean id="e1" class="com.eomcs.spring.ioc.ex08.a.Engine">
    <property name="maker" value="비트자동차"/>
    <property name="valve" value="16"/>
    <property name="cylinder" value="4"/>
</bean>
```

```java
public static void main(String[] args) {
  ApplicationContext iocContainer = new ClassPathXmlApplicationContext(
      "com/eomcs/spring/ioc/ex08/a/application-context.xml");
}
```

- @Autowired를 사용하여 주입
1. BeanPostProcessor 인터페이스를 구현한 객체를 등록한다.
등록한 객체는 Spring이 자동으로 관리해주기 때문에 아이디를 지정할 필요가 없다.
```xml
<bean class="org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor"/>
```

2. 셋터 메서드 또는 필드에 @Autowired를 붙인다.
```java
@Autowired
public void setEngine(Engine engine) {
  System.out.println("Car.setEngine()");
  this.engine = engine;
}
```
@Autowired 애노테이션을 필드(인스턴스 변수)에 붙여도 된다. 직접 변수에 주입하기 때문에 세터를 호출하지 않는다. 하지만 인스턴스 변수에 직접 의존 객체를 주입한다는 것은 캡슐화를 위배하는 측면이 있기 때문에 객체지향적이지 않다는 비난을 받는다.

*private 필드에 값을 넣는 방법:  
Reflection API 사용하면 private 멤버도 접근할 수 있다.
```java
// Model은 private변수
Car c2 = new Car();
// c2.model = "비트비트: //private 멤버이기 때문에 접근 불가(컴파일 오류)
c2.setModel("비트비트"); //public 멤버인 셋터를 이용해서 값을 넣을 수 있다.

Field f = Car.class.getDeclaredField("model");
f.setAccessible(true); // private 멤버이지만 접근 가능하도록 설정
f.set(c2, "오호라2");
```

3. 생성자를 이용하여 의존 객체 주입
이 또한 AutowiredAnnotationBeanPostProcessor를 통해 자동으로 주입할 수 있다. 해당 클래스에 기본 생성자가 없을 때, 파라미터를 받는 다른 생성자를 찾아 호출한다. 물론 그 파라미터에 해당하는 객체가 존재해야 한다. 



## BeanPostProcessor 인터페이스
스프링 IoC 컨테이너는 객체 중에 이 인터페이스를 구현한 객체가 있다면, 설정 파일에 적혀있는 객체를 생성한 후에 이 구현체의 postProcess....() 메서드를 호출한다. 



## 필수 의존 객체와 선택 의존 객체
@Autowired의 required 값은 기본이 true이다. 의존 객체 주입이 필수사항이라는 뜻이며 해당하는 의존 객체가 없으면 예외가 발생한다.  
이를 선택사항으로 바꾸고 싶으면 false로 설정해야 한다.
```java
@Autowired(required = false)
private Engine engine;
```

## 같은 타입의 의존 객체가 여러 개 있을 때 발생하는 문제
같은 타입의 의존 객체가 여러 개 있을 때 IoC 컨테이너는 어떤 객체를 등록할 지 알 수 없기 때문에 객체를 생성할 수 없다. 따라서 `@Qualifier("객체이름")`를 사용하여 등록할 객체를 지정해주어야 한다.  
```java
@Autowired
@Qualifier("e2")
private Engine engine;
```

이때 AutowiredAnnotationBeanPostProcessor는 `@Qualifier` 애노테이션을 처리하지 못하므로 이를 처리할 BeanPostProcessor를 따로 등록해야 한다.  
따로 등록하기 귀찮다면 `<context:annotation-config/>` 태그를 사용한다. 이 태그를 선언하면 애노테이션 처리와 관련된 BeanPostProcessor 들을 자동으로 생성한다. 따라서 개발자가 일일이 암기하여 필요한 BeanPostProcessor를 등록할 필요가 없어진다.
```xml
<context:annotation-config/>
<!--  
  // <context:annotation-config/>가 아래의 BeanPostProcessor도 자동으로 생성해준다.
  <bean class="org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor"/>
-->
<bean id="c1" class="com.eomcs.spring.ioc.ex08.g.Car">
  <property name="model" value="티코"/>
  <property name="maker" value="비트자동차"/>
  <property name="cc" value="890"/>
  <property name="auto" value="true"/>
</bean>
    
<bean id="e1" class="com.eomcs.spring.ioc.ex08.g.Engine">
  <property name="maker" value="비트자동차"/>
  <property name="valve" value="16"/>
  <property name="cylinder" value="4"/>
</bean>

<bean id="e2" class="com.eomcs.spring.ioc.ex08.g.Engine">
  <property name="maker" value="캠프자동차"/>
  <property name="valve" value="16"/>
  <property name="cylinder" value="8"/>
</bean>
```


## @Resource = @Autowired + @Qualifier
이 애노테이션은 스프링 프레임워크가 아닌 자바에서 제공한다. 따로 확장 라이브러리를 다운로드하여 추가해야 한다. 


## @Component
스프링 IoC 컨테이너는 이 애노테이션이 붙은 클래스에 대해 객체를 자동 생성한다. `<context:annotation-config/>`를 통해 애노테이션을 처리할 객체를 자동생성한 후 사용한다. 또는 `component-scan 태그`로 애노테이션을 찾을 패키지를 지정해야 한다.  

component-scan 태그를 추가하면 내부적으로 annotation-config 태그가 자동으로 추가된다. 따라서 `<context:annotation-config/>`를 생략할 수 있다.

```xml
<context:annotation-config/>

<context:component-scan base-package="com.eomcs.spring.ioc.ex09"/>
```

*`<context:annotation-config/>`: @Component, @Service, @Repository, @Controller 객체를 생성하기 위해 bean 태그를 사용하지 않고도 객체를 자동 생성해주는 도우미 객체.

*`component-scan` : @Component, @Service, @Repository, @Controller 애노테이션이 붙은 클래스를 찾아서 객체를 자동 생성하도록 명령을 내리는 태그이다.

@Component를 사용하면 객체를 생성하기 위해 bean 태그를 사용하지 않고도 자동생성할 수 있다. 

클래스 선언에 @Component 애노테이션을 붙여 사용한다.
- 문법
  - @Component(value="객체이름")
  - @Component("객체이름")
  - @Component
만약 객체 이름을 생략하면 클래스 이름을 소문자로 바꾼다.

```java
@Component
public class Car {
  String model;
  String maker;
  int cc;
  boolean auto;
  Date createdDate;
  Engine engine;
  ...
}
```

## 특정 패키지의 클래스에 대해 객체 생성하지 않기
```xml
<context:component-scan base-package="com.eomcs.spring.ioc.ex09">
  <!-- 다음 패키지의 클래스 중에서 @Component,@Service,@Controller,@Repository
  애노테이션이 붙은 것은 객체를 생성한다. -->
  <context:include-filter type="regex" 
    expression="com.eomcs.spring.ioc.ex09.p2.Service2"/>
        
  <!-- 특정 패키지의 특정 클래스를 객체 생성 대상에서 제외하기  -->
  <context:exclude-filter type="regex" 
    expression="com.eomcs.spring.ioc.ex09.p2.Service1"/>
  
  <!-- 특정 애노테이션이 붙은 클래스는 객체 생성에서 제외시킨다. -->
  <context:exclude-filter type="annotation" 
    expression="org.springframework.stereotype.Controller"/>
      
  <!-- 특정 패키지만 제외하기 -->
  <context:exclude-filter type="regex" 
    expression="com.eomcs.spring.ioc.ex09.p4.*"/>
            
  <!-- 특정 패키지에서 지정된 패턴의 이름을 가진 클래스를 제외하기 -->
  <context:exclude-filter type="regex" 
    expression="com.eomcs.spring.ioc.ex09.p5.*Truck"/>
</context:component-scan>
```

# 애노테이션 설정
## AnnotationConfigApplicationContext

## @Bean 애노테이션
Java Config에서 애노테이션을 사용해 수동으로 객체를 생성할 수 있다. 
- 객체를 생성하여 리턴하는 메서드를 정의한 후
- 그 메서드에 @Bean 애노테이션을 붙인다.
- 만약 이름을 지정하지 않으면 메서드 이름을 사용하여 저장한다. 

## @Configuration
AppConfig 클래스가 스프링 설정 정보를 갖고 있는 클래스임을 선포한다. 그러면 AnnotationConfigApplicationContext 에서 이 클래스를 찾아 적절한 작업을 수행한다

예를 들어 AnnotationConfigApplicationContext 컨테이너에 Java Config 클래스를 직접 지정할 경우 @Configuration 애노테이션을 붙일 필요가 없다.  
ex) ApplicationContext iocContainer = new AnnotationConfigApplicationContext(AppConfig1.class);

그런데 다음과 같이 컨테이너에 Java Confih 클래스를 직접 알려주지 않을 경우에는 @Configuration 애노테이션을 붙여 이 클래스가 Java Config 클래스임을 알려주어야 한다.  
ex) ApplicationContext iocContainer = new AnnotationConfigApplicationContext("com.eomcs.spring.ioc.ex10");

@Configuration을 사용하는 경우는 @Component 애노테이션을 사용하여 객체를 사용하는 것에 추가 기능이 포함되어 있다. 따라서 복잡한 설정 정보를 사용할 때는 정식적으로 @Configuration 애노테이션을 사용한다.

## @PropertySource 애노테이션
.properties 파일을 데이터를 메모리에 로딩하는 일을 한다.

문법  
파일 경로가 클래스 경로를 가리킨다면, 파일 경로 앞에 `classpath:` 접두어를 붙인다. 
```java
@PropertySource({
  "classpath:com/eomcs/spring/ioc/ex10/c/jdbc.properties",
  "classpath:com/eomcs/spring/ioc/ex10/c/jdbc2.properties"
})
```

**프로퍼티 값 사용**
1. Environment 객체 주입
로딩된 프로퍼티 값을 사용하기 위해 Environment 객체를 주입하도록 Spring IoC 컨테이너에게 명령을 내리는 방법
```java
@Autowired
Environment env;

// Environment 객체를 통해 메모리에 보관된 .properties 의 값 가져오기
@Bean
public Car car1() {
  System.out.println("car1() 호출: ");
  System.out.println("  " + this.env.getProperty("jdbc.username"));
  System.out.println("  " + this.env.getProperty("jdbc2.username"));
  return new Car(null);
}
```

2. 특정 값만 필드로 주입
```java
@Value("${jdbc.url}")
String jdbcUrl;

@Value("${jdbc2.url}")
String jdbc2Url;

// @Value를 통해 필드로 주입 받은 .properties 값 꺼내기
@Bean
public Car car2() {
  System.out.println("car2() 호출: ");
  System.out.println("  " + this.jdbcUrl);
  System.out.println("  " + this.jdbc2Url);
  return new Car(null);
}
```

3. 파라미터로 주입
```java
// @Value를 사용하여 파라미터로 .properties 값 주입 받기
@Bean
public Car car3(
    @Value("${jdbc.username}") String username,
    @Value("${jdbc2.username}") String username2) {

  System.out.println("car3() 호출 :");
  System.out.println("  " + username);
  System.out.println("  " + username2);
  return new Car(null);
}
```

## @ComponentScan 애노테이션
@ComponentScan 애노테이션은 `<context:component-scan/>` 태그와 같은 역할은 한다. 
```java
// 한 개의 패키지를 지정하기
@ComponentScan(basePackages = {"com.eomcs.spring.ioc.ex11"})

// 배열 항목이 한 개일 경우 중괄호({}) 생략 가능
@ComponentScan(basePackages = "com.eomcs.spring.ioc.ex11")

// 여러 개의 패키지 지정하기
@ComponentScan(basePackages = {
   "com.eomcs.spring.ioc.ex11.p1",
   "com.eomcs.spring.ioc.ex11.p2",
   "com.eomcs.spring.ioc.ex11.p3"
})

// 특정 패키지나 클래스 제외하기
@ComponentScan(
    basePackages = "com.eomcs.spring.ioc.ex11",
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = "com.eomcs.spring.ioc.ex11.p2.*"
            ),
        @ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            value = org.springframework.stereotype.Controller.class
            )
    })
```

