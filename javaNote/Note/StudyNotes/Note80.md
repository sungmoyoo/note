# IOC Contatiner
IOC (Inversion Of Control) 컨테이너, 인스턴스의 생성과 관리를 담당하는 객체. bean container라고도 부른다.  
IOC Container는 각 객체가 의존하는 객체(dependency)를 자동으로 주입해 준다. 이를 "의존 객체 주입(dependency injection)"이라 한다. 

## 제어 역전(Inversion of Control):
객체나 프로그램 일부에 대한 제어를 컨테이너나 프레임워크로 이전하는 소프트웨어 엔지니어링 원칙


## ApplicationContext 인터페이스


## ApplicationContext 구현체


##



<!-- # 객체 생성 - <bean> 태그 사용법 -->



<!-- ## 의존성 주입방법(Dependency Injection)
- 생성자 주입  
의존 객체는 생성자에서 주입하는 것이 좋다. 의존 객체의 의미 자체가 그 객체가 없이는 작업을 수행할 수 없다는 말인데 이러한 필수 객체는 생성자에서 받는 것이 유지 보수에 좋다. 즉 의존 객체 없이 해당 객체를 생성하는 일을 방지할 수 있기 때문이다.

- 세터 주입

- 필드 주입
@Autowired, @inject 와 같은 주석을 사용하여 필드에 종속성을 주입하는 방법이다. Java에서 가장 권장되지 않는 DI 유형이지만 실무에서 많이 사용한다. 필드 주입은 클래스의 종속성이 무엇인지  -->