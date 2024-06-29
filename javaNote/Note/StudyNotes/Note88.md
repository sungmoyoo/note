# myapp 76~77
## @PropertySource 사용법
1) jdbc 프로퍼티를 등록하는 파일을 resource/config 아래에 저장한다.
```properties
jdbc.url=jdbc:mysql://localhost/studydb
jdbc.username=study
jdbc.password=Bitcamp!@#123
```

2) @PropertySource 애노테이션에 jdbc.properties 파일 경로를 설정한다.
```java
@ComponentScan(value = {
    "bitcamp.myapp.dao",
    "bitcamp.util"
})

@PropertySource({
    "classpath:config/jdbc.properties"
})
public class RootConfig {

  public RootConfig() {
    System.out.println("RootConfig() 호출됨");
  }

}
```

3) 프로퍼티를 받을 변수에 @Value 애노테이션과 넣을 프로퍼티 이름을 지정한다.
```java
@Component
public class DBConnectionPool implements ConnectionPool {

  // 개별 스레드용 DB 커넥션 저장소
  private static final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

  // DB 커넥션 목록
  ArrayList<Connection> connections = new ArrayList<>();

  @Value("${jdbc.url}")
  private String jdbcUrl;

  @Value("${jdbc.username}")
  private String username;

  @Value("${jdbc.password}")
  private String password;

  public DBConnectionPool() {
    System.out.println("DBConnectionPool() 호출됨!");
  }

  public Connection getConnection() throws Exception {
    // 현재 스레드에 보관중인 Connection 객체를 꺼낸다.
    Connection con = connectionThreadLocal.get();

    if (con == null) {
      // 스레드에 보관된 Connection 이 없다면,

      if (connections.size() > 0) {
        // 스레드풀에 놀고 있는 Connection이 있다면,
        con = connections.remove(0); // 목록에서 맨 처음 객체를 꺼낸다.
        System.out.printf("%s: DB 커넥션풀에서 꺼냄\n", Thread.currentThread().getName());

      } else {
        // 스레드풀에도 놀고 있는 Connection 이 없다면,
        // 새로 Connection을 만든다.
        con = new ConnectionProxy(DriverManager.getConnection(jdbcUrl, username, password), this);
        System.out.printf("%s: DB 커넥션 생성\n", Thread.currentThread().getName());
      }

      // 현재 스레드에 Connection 을 보관한다.
      connectionThreadLocal.set(con);

    } else {
      System.out.printf("%s: 스레드에 보관된 DB 커넥션 리턴\n", Thread.currentThread().getName());
    }

    return con;
  }

  public void returnConnection(Connection con) {
    // 스레드에 보관중인 Connection 객체를 제거한다.
    connectionThreadLocal.remove();

    // Connection을 커넥션풀에 반환
    connections.add(con);

    System.out.printf("%s: DB 커넥션을 커넥션풀에 반환\n", Thread.currentThread().getName());
  }

  public void closeAll() {
    for (Connection con : connections) {
      ((ConnectionProxy) con).realClose();
    }
  }
}

```
## CharacterEncodingFilter Java Config로 변경
XML로 설정한 필터 정보를 직접 만들어서 Java Config로 변경하는 방법이다.
```java
CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("UTF-8");
javax.servlet.FilterRegistration.Dynamic filterRegistration =
    servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
filterRegistration.addMappingForServletNames(
    EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE),
    false,
    new String[]{"app"}
);
```

만약 AbstractAnnotationConfigDispatcherServletInitializer 클래스를 상속받아 만들 경우 getServletFilters() 메서드를 오버라이딩 하여 CharacterEncodingFilter("인코딩할 문자집합") 객체를 넣어주면 된다.
```java
@Override
protected Filter[] getServletFilters() {
  return new Filter[]{new CharacterEncodingFilter("UTF-8")};
}
```


## Model 객체를 추가하고 redirect 했을 때 발생하는 문제
게시글과 가입인사는 카테고리 번호를 통해 구분되고 같은 BoardController를 사용한다.

근데 게시글을 add한 후 리턴한 주소에 추가적으로 boardName과 category 번호가 한번 더 붙어 잘못된 URL로 redirect 되는 문제가 발생한다. 
>http://localhost:8888/app/board/list?category=1&boardName=%EA%B2%8C%EC%8B%9C%EA%B8%80&category=1
<img src = >

### 원인?
원래는 DispatcherServlet이 페이지 컨트롤러에게 값을 담을 Map 객체를 건네주고 페이지 컨트롤러가 그걸 받아서 작업을 수행하고 jsp view URL을 리턴을 하면, DispatcherServlet은 Map 객체에 담겨있는 값이 있다면 Map 객체에 들어있는 값을 ServletRequest로 카피한다.

문제는 view URL을 리턴하는 게 아니라 redirect를 하면 페이지 컨트롤러는 Map 객체에 있는 값이 필요한 값이라고 판단하고 redirect 경로 뒤에 파라미터 값으로 붙여서 리턴하기 때문에 위와 같은 문제가 발생한 것이다. 

```java
@PostMapping("/add")
public String add(
    Board board,
    MultipartFile[] attachedFiles,
    HttpSession session,
    Map<String, Object> map) throws Exception {

  int category = board.getCategory();
  map.put("boardName", category == 1 ? "게시글" : "가입인사");
  map.put("category", category);
  ...

  return "redirect:list?category=" + category;
  ...
}
```

## 해결
JSP에 보내지 않고 redirect를 하기 때문에 BoardName을 Map객체에 담을 필요가 없다. 

그렇다면 redirect문을 수정하여 파라미터가 붙은 동작을 이용하여 올바른 URL 주소를 리턴하도록 할 수 있다. 
```java
@PostMapping("/add")
public String add(
    Board board,
    MultipartFile[] attachedFiles,
    HttpSession session,
    Model model) throws Exception {

  int category = board.getCategory();
  model.addAttribute("category", category);
  ...

  return "redirect:list";
  ...
}
```


