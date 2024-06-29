# 웹 애플리케이션 서버 아키텍처
웹 애플리케이션 서버 아키텍처는 클라이언트의 역할과 서버의 일부 역할을 웹 브라우저와 웹 서버가 대신 해주는 서버 구조이다. 

<img src="../../img/WASArc.png">

위 그림을 예로 들면 Apache Tomcat Server를 사용하여 ClientApp를 기능을 Web Browser로 대체하고, ServerApp의 기능을 쪼개 Web Server와 Servlet Container, App으로 나누었다. 

Tomcat Server란 자바 웹 애플리케이션을 실행하기 위한 소프트웨어로 호스팅, 정적 자원 서비스, 서블릿 컨테이너 관리 등의 역할을 수행한다. 

## Apache Tomcat: Web Server
HTTP 서버 역할을 하는 Mini Web Server는 HTML, CSS, Javascript와 같은 정적 자원을 관리하고 HTTP 프로토콜에 따라 Web Browser랑 통신하며, Servlet Container에 요청을 위임한다. 

## Servlet Container
Servlet Container는 자바 클래스를 실행하고 관리하는 역할을 하며 이 자바 클래스를 Servlet이라고 한다. 이 Servlet의 생성-실행-소멸 요청을 Container가 위임받아 알아서 처리한다.

## Servlet
= Server App. + let(작은 것)  
= 작은 서버 애플리케이션  
= 서버 App의 작은 기능  

Servlet은 동적인 웹 페이지를 생성하고 처리하는 데 사용하는 웹 애플리케이션 클래스이다.  

Servlet Container가 관리하는 Servlet은 반드시 Servlet 인터페이스를 구현하는 구현체여야 한다.  

또 클라이언트가 서블릿 실행을 요청할 때 사용할 경로를 지정해주어야 한다. 경로는 애노테이션으로 지정하며 개발자가 임의로 배치정보를 설정한다. ex) @WebServlet(경로)

**Servlet 인터페이스 메서드**
```
// 서블릿 생명주기 메서드
- init(): 객체 생성후 호출됨
- service(): 요청이 들어올 때마다
- destroy(): app 종료될때

// 서블릿 정보/속성 관련 메서드
- getServletInfo()
- getServletConfig()
```

**Web Browser**
```
plain -> Web Browser -> 단순 출력
HTML -> Web Browser -> HTML 문법에 맞춰 렌더링
PDF -> Web Browser -> PDF View 실행
JPEG, GIF -> Web Browser -> 이미지 출력
ZIP -> Web Browser-> 다운로드
```

## 임베디드 톰캣 서버를 이용하여 웹 애플리케이션 서버 구축
임베디드 톰캣은 일반 외장 톰캣과 달리 서버를 자바 애플리케이션 안에 이식하여 실행되는 환경으로 스프링부트와 같은 구조를 지닌다. 따라서 실행에 필요한 jar 파일 외 ..

사실상 구조만 다르고 사용되는 기능에는 큰 차이가 없다.

```java
public static void main(String[] args) throws Exception {
  System.out.println("서버 실행!");

  // 톰캣 서버를 구동시키는 객체 준비
  Tomcat tomcat = new Tomcat();

  // 서버의 포트 번호 설정
  tomcat.setPort(8888);

  //System.out.println(new File(".").getCanonicalFile());

  // 톰캣 서버를 실행하는 동안 사용할 임시 폴더 지정
  tomcat.setBaseDir("./temp");

  // 톰캣 서버의 연결 정보를 설정
  Connector connector = tomcat.getConnector();
  connector.setURIEncoding("UTF-8");

  // 톰캣 서버에 배포할 웹 애플리케이션의 환경 정보 준비
  StandardContext ctx = (StandardContext) tomcat.addWebapp(
        "/", // 컨텍스트 경로(html,css,javascript 등 정적 자원 저장할 경로)
        new File("src/main/webapp").getAbsolutePath() // 웹 애플리케이션 파일이 있는 실제 경로
  );
  ctx.setReloadable(true); // 컨텍스트 정보를 바탕으로 리로드


  // 웹 애플리케이션 기타 정보 설정
  WebResourceRoot resources = new StandardRoot(ctx);

  // 웹 애플리케이션의 서블릿 클래스 등록
  resources.addPreResources(new DirResourceSet(
      resources, // 루트 웹 애플리케이션 정보
      "/WEB-INF/classes", // 서블릿 클래스 파일의 위치 정보
        new File("build/classes/java/main").getAbsolutePath(), // 서블릿 클래스 파일이 있는 실제 경로
        "/" // 웹 애플리케이션 내부 경로
  ));

  // 웹 애플리케이션 설정 정보를 웹 애플리케이션 환경 정보에 등록
  ctx.setResources(resources);

  // 톰캣 서버 구동
  tomcat.start();

  // 톰캣 서버를 구동한 후 종료될 때까지 JVM을 끝내지 말고 기다린다.
  tomcat.getServer().await();

  System.out.println("서버 종료!");
  }
```

## Servlet 사용(GenericServlet Abstract Class 사용 이유)
Servlet 인터페이스를 구현하기 위해서는 서블릿 생명 주기 메서드를 포함한 여러 메서드를 구현해야 한다. 개발자가 직접 Servlet 인터페이스를 구현해도 되지만 이러한 작업을 대신 구현해주는 GenericServlet 추상클래스를 사용하면 service() 를 제외한 나머지 메서드를 구현한 기본적인 구조를 제공해준다. 

```java
// 배치 파일 경로 지정, URL 기본주소+경로로 검색 시 클라이언트에서 요청을 보낸다. 
@WebServlet("/board/list")
public class BoardListServlet extends GenericServlet {

  private BoardDao boardDao;

  public BoardListServlet() {
    // 데이터를 받아오기 위한 DB커넥션풀
    DBConnectionPool connectionPool = new DBConnectionPool(
        "jdbc:mysql://localhost/studydb", "study", "Bitcamp!@#123");
    this.boardDao = new BoardDaoImpl(connectionPool, 1);
  }

  @Override
  public void service(ServletRequest servletRequest, ServletResponse servletResponse)
      throws ServletException, IOException {
    
    // 응답할 컨텐츠 타입을 정한다.
    servletResponse.setContentType("text/html;charset=UTF-8");
    // 응답 내용을 출력할 때 사용할 PrintWriter 객체를 생성한다. 
    PrintWriter out = servletResponse.getWriter();

    // 응답내용 html
    out.println("<!DOCTYPE html>");
    out.println("<html lang='en'>");
    out.println("<head>");
    out.println(" <meta charset='UTF-8'>");
    out.println(" <title>비트캠프 데브옵스 5기</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>게시글</h1>");

    try {
      out.printf("%s\t%s\t%10s\t%s\t%s\n", "No", "Title", "Writer", "Date", "Files");
      out.printf("<table border='1'>");
      out.printf("    <thead>");
      out.printf("    <tr> <th>번호</th> <th>제목</th> <th>작성자</th> <th>등록일</th> <th>첨부파일</th> </tr>");
      out.printf("    </thead>");
      out.printf("    <tbody>");

      List<Board> list = boardDao.findAll();

      for (Board board : list) {
        out.printf("<tr> <td>%d</td> <td>%s</td> <td>%s</td> <td>%4$tY-%4$tm-%4$td</td> <td>%5$d</td> </tr>\n",
            board.getNo(),
            board.getTitle(),
            board.getWriter().getName(),
            board.getCreatedDate(),
            board.getFileCount());
      }
      out.println("</tbody>");
    } catch (Exception e) {
      out.println("<p>게시글 목록 오류</p>");
      out.println("<pre>");
      e.printStackTrace(out);
      out.println("</pre>");
    }

    out.println("</body>");
    out.println("</html>");
  }
}
```


## 웹브라우저를 이용하여 클라이언트 구축
## 웹 기술을 도입하여 애플리케이션 작성
## 캐시와 쿠키