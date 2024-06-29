# refactoring & loop/quit 
기존 myapp 프로젝트의 서버/클라이언트 코드를 extract method로 리팩토링하고 Request 에러와 반복/종료 프로세스를 작성하였다. 

## 기존 코드
### ServerApp
```java
public class ServerApp {

  HashMap<String, Object> daoMap = new HashMap<>();
  Gson gson;

  ServerApp() {
    daoMap.put("board", new BoardDaoImpl("board.json"));
    daoMap.put("assignment", new AssignmentDaoImpl("assignment.json"));
    daoMap.put("greeting", new BoardDaoImpl("greeting.json"));
    daoMap.put("member", new MemberDaoImpl("member.json"));

    gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  }

  public static void main(String[] args) {
    new ServerApp().run();
  }


  void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(8888);
      System.out.println("서버 실행");

      System.out.println("클라이언트 연결 대기중..");
      Socket socket = serverSocket.accept();
      System.out.println("대기목록에서 클라이언트 연결 정보를 꺼냄");

      DataInputStream in = new DataInputStream(socket.getInputStream());
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      System.out.println("입출력 준비 완료");

      while (true) {
        String dataName = in.readUTF();
        String command = in.readUTF();
        String value = in.readUTF();

        //dataName으로 DAO 찾기
        Object dao = daoMap.get(dataName);
        System.out.printf("데이터명: %s", dataName);

        //command 이름으로 메서드 찾기
        Method[] methods = dao.getClass().getDeclaredMethods();
        Method commandHandler = null;

        for (Method method : methods) {
          if (method.getName().equals(command)) {
            commandHandler = method;
            break;
          }
        }
        System.out.printf("메서드명: %s", commandHandler);

        Parameter[] params = commandHandler.getParameters();

        // 메서드를 호출할 때 파라미터에 넘겨줄 데이터를 담을 배열을 준비한다.
        Object[] args = new Object[params.length];

        // 아규먼트 값 준비하기
        // => 현재 모든 DAO의 메서드는 파라미터가 최대 1개만 있다.
        // => 1개만 있다는 가정하에서 아규먼트 값을 준비한다.
        if (params.length > 0) {
          // 파라미터 타입을 알아낸다.
          Class<?> paramType = params[0].getType();

          // 클라이언트가 보낸 JSON 문자열을 해당 파라미터 타입 객체로 변환한다.
          Object paramValue = gson.fromJson(value, paramType);

          // 아규먼트 배열에 저장한다.
          args[0] = paramValue;
        }

        // 메서드의 리턴 타입을 알아낸다.
        Class<?> returnType = commandHandler.getReturnType();
        System.out.printf("리턴: %s\n", returnType.getName());

        // 메서드를 호출한다.
        Object returnValue = commandHandler.invoke(dao, args);

        out.writeUTF("200");
        out.writeUTF(gson.toJson(returnValue));
        System.out.println("클라이언트에게 응답 완료!");
      }

    } catch (Exception e) {
      System.out.println("통신 오류!");
      e.printStackTrace();
    }
  }
}
```

---

### ClientApp
```java
public class ClientApp {

  Prompt prompt = new Prompt(System.in);

  BoardDao boardDao;
  BoardDao greetingDao;
  AssignmentDao assignmentDao;
  MemberDao memberDao;

  MenuGroup mainMenu;

  ClientApp() {
    prepareMenu();
    prepareNetwork();
  }

  public static void main(String[] args) {
    System.out.println("[과제 관리 시스템]");
    new ClientApp().run();


  }

  void prepareNetwork() {
    try {
      System.out.println("서버 연결 중...");
      Socket socket = new Socket("localhost", 8888);
      System.out.println("서버 연결 완료");

      DataInputStream in = new DataInputStream(socket.getInputStream());
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      System.out.println("입출력 준비 완료");

      assignmentDao = new AssignmentDaoImpl("assignment.json", in, out);
      boardDao = new BoardDaoImpl("board.json", in, out);
      greetingDao = new BoardDaoImpl("greeting.json", in, out);
      memberDao = new MemberDaoImpl("member.json", in, out);


    } catch (Exception e) {
      System.out.println("클라이언트 통신 오류");
      e.printStackTrace();
    }
  }

  void prepareMenu() {
    mainMenu = MenuGroup.getInstance("메인");
    MenuGroup assignmentMenu = mainMenu.addGroup("과제");
    assignmentMenu.addItem("등록", new AssignmentAddHandler(assignmentDao, prompt));
    assignmentMenu.addItem("조회", new AssignmentViewHandler(assignmentDao, prompt));
    assignmentMenu.addItem("변경", new AssignmentModifyHandler(assignmentDao, prompt));
    assignmentMenu.addItem("삭제", new AssignmentDeleteHandler(assignmentDao, prompt));
    assignmentMenu.addItem("목록", new AssignmentListHandler(assignmentDao, prompt));

    MenuGroup boardMenu = mainMenu.addGroup("게시글");
    boardMenu.addItem("등록", new BoardAddHandler(boardDao, prompt));
    boardMenu.addItem("조회", new BoardViewHandler(boardDao, prompt));
    boardMenu.addItem("변경", new BoardModifyHandler(boardDao, prompt));
    boardMenu.addItem("삭제", new BoardDeleteHandler(boardDao, prompt));
    boardMenu.addItem("목록", new BoardListHandler(boardDao, prompt));

    MenuGroup memberMenu = mainMenu.addGroup("회원");
    memberMenu.addItem("등록", new MemberAddHandler(memberDao, prompt));
    memberMenu.addItem("조회", new MemberViewHandler(memberDao, prompt));
    memberMenu.addItem("변경", new MemberModifyHandler(memberDao, prompt));
    memberMenu.addItem("삭제", new MemberDeleteHandler(memberDao, prompt));
    memberMenu.addItem("목록", new MemberListHandler(memberDao, prompt));

    MenuGroup greetingMenu = mainMenu.addGroup("가입인사");
    greetingMenu.addItem("등록", new BoardAddHandler(greetingDao, prompt));
    greetingMenu.addItem("조회", new BoardViewHandler(greetingDao, prompt));
    greetingMenu.addItem("변경", new BoardModifyHandler(greetingDao, prompt));
    greetingMenu.addItem("삭제", new BoardDeleteHandler(greetingDao, prompt));
    greetingMenu.addItem("목록", new BoardListHandler(greetingDao, prompt));

    mainMenu.addItem("도움말", new HelpHandler(prompt));
  }

  void run() {
    while (true) {
      try {
        mainMenu.execute(prompt);
        prompt.close();
        break;
      } catch (Exception e) {
        System.out.println("예외 발생!");
      }
    }
  }
}

```
---
<br></br>

## ServerApp 변경사항
### findMethod()
요청받은 메서드 이름으로 메서드를 찾는 코드를 추출하여 메서드화
예외 처리를 좀 더 구체적으로 다루기 위해 RuntimeException을 상속받은 새 클래스를 던진다.
```java
Method findMethod(Class<?> clazz, String name) {
    Method[] methods = clazz.getDeclaredMethods();
    for (Method m : methods) {
      if (m.getName().equals(name)) {
        return m;
      }
    }
    throw new RequestException("요청 메서드가 없습니다.");
  }
```

<br></br>

### getArguments()
파라미터 객체를 배열로 리턴하는 기능 메서드로 추출
```java
Object[] getArguments(Method m, String json) {
    Parameter[] params = m.getParameters();
    System.out.printf("파라미터 개수: %d\n", params.length);

    Object[] args = new Object[params.length];

    if (params.length > 0) {
      Class<?> paramType = params[0].getType();
      Object paramValue = gson.fromJson(json, paramType);

      args[0] = paramValue;
    }
    return args;
  }
```

<br></br>

### processRequest()
요청값을 읽어 처리하고 응답 실질적인 메서드
- Reflection API를 사용하여 필요한 값 리턴해주는 findMethod(), getArguments() 사용
- 리턴 타입을 int로 변경하여 종료 입력 시 종료하도록 run()메서드와 연계된다.
- 요청 에러, 서버 에러의 경우 상태코드를 "400", "500"으로 설정하여 에러메시지와 함께 응답한다.

```java
int processRequest(DataInputStream in, DataOutputStream out) throws IOException {
    System.out.println("[클라이언트 요청]");
    String dataName = in.readUTF();
    if (dataName.equals("quit")) {
      out.writeUTF("Good Bye!");
      return -1;
    }
    String command = in.readUTF();
    String value = in.readUTF();

    try {
      Object dao = daoMap.get(dataName);
      if (dao == null) {
        throw new RequestException("요청 데이터가 없습니다.");
      }
      System.out.printf("데이터: %s\n", dataName);

      Method commandHandler = findMethod(dao.getClass(), command);

      System.out.printf("메서드: %s\n", commandHandler.getName());

      Object[] args = getArguments(commandHandler, value);

//      Class<?> returnType = commandHandler.getReturnType();
//      System.out.printf("리턴: %s\n", returnType.getName());

      // 메서드를 호출한다.
      Object returnValue = commandHandler.invoke(dao, args);

      out.writeUTF("200");
      out.writeUTF(gson.toJson(returnValue));
      System.out.println("클라이언트에게 응답 완료!");

    } catch (RequestException e) {
      out.writeUTF("400");
      out.writeUTF(gson.toJson(e.getMessage()));

    } catch (Exception e) {
      out.writeUTF("500");
      out.writeUTF(gson.toJson(e.getMessage()));
    }
    return 0;
  }
```

<br></br>

### service()
- try with resources 문법으로 스트림과 소켓을 자동으로 close하도록 변경
- client에서 close() 메서드 실행될 때 processRequest에서 -1을 리턴하여 반복문 종료
```java
void service(Socket socket) throws Exception {
  try (Socket s = socket;
      DataInputStream in = new DataInputStream(socket.getInputStream());
      DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

    System.out.println("클라이언트 연결");

    while (processRequest(in, out) != -1) {
      System.out.println("----------------------------");
    }

    System.out.println("클라이언트 연결 종료");

  } catch (Exception e) {
    System.out.println("클라이언트 연결 오류");
  }
}
```

<br></br>

### Run()
- try with resources 문법 사용
- 클라이언트가 연결되면 service가 호출된다. Socket 연결이 되지 않으면 service 메서드 호출이 안됨.
```java
void run() {
  System.out.println("[과제관리 서버시스템]");

  try (ServerSocket serverSocket = new ServerSocket(8888)) {
    System.out.println("서버 실행");

    while (true) {
      service(serverSocket.accept());
    }

  } catch (Exception e) {
    System.out.println("통신 오류!");
    e.printStackTrace();
  }
}
```

<br></br>

## ClientApp 변경사항
### close()
- client를 종료하는 메서드 추가
```java
void close() {
  try (Socket socket = this.socket;
      DataInputStream in = this.in;
      DataOutputStream out = this.out) {
    
    out.writeUTF("quit");
    System.out.println(in.readUTF());

  } catch (Exception e) {
    // 종료하다가 발생한 예외는 무시한다.
  }
}
```
### run()
- close() 메서드 추가
```java
void run() {
  while (true) {
    try {
      mainMenu.execute(prompt);
      prompt.close();
      close();
      break;
    } catch (Exception e) {
      System.out.println("예외 발생!");
    }
  }
}
```
---

<br></br>

# GoF의 Proxy 패턴
<img src="../img/Proxy Pattern.png">

프록시 패턴이란 실제 작업을 수행하는 객체인 `서비스 객체`에 접근할 때 직접 참조하는 것이 아니라 대리하는 객체인 `프록시 객체`를 통해 접근하는 방식이다.

> 정확히는 서비스 객체를 호출하는 것과 같이 객체 사용방법이 복잡한 경우 그 과정을 캡슐화하고 프로그래밍 일관성을 위해 서비스 객체와 동일한 사용규칙을 갖도록 하는 패턴

<br>

예로, 앞에서 다룬 app-server와 app-client의 통신 방법에서 proxy 패턴을 확인할 수 있다.

<img src="../img/Proxy Example.png">

- 사용하는 쪽 입장에서는 stub이 실제 일을 하는 객체처럼 보이지만 사실은 실제 일을 하는 객체와 사용하는 쪽 사이에서 요청/응답해주는 대행자 역할을 한다.
- 여기서 프록시 객체는 ClientApp에 존재하는 stub만 해당한다. 
- 프록시 객체는 원격 객체와 같은 인터페이스를 구현해야 하기 때문이다.
- 서버 쪽의 skeleton은 기능 상 원격 객체를 call하는 역할을 할 뿐이다. 
- 해당 유형은 프록시 패턴 중 원격 프록시에 해당하며 이외에도 가상 프록시, 보호 프록시 등이 존재한다.

> 결론적으로 (원격) 프록시 패턴은 다른 프로세스의 메서드를 호출하는 과정에서 작성하는 복잡한 절차의 코드를 캡슐화함으로써 보다 간결한 코드를 만들어준다. 

<br></br>

## 원격에 존재하는 코드를 호출하는 이유
**1인용 App**
- 순차적 처리
- 적은 성능 요구
> 1인용 애플리케이션의 경우 1대의 컴퓨터(프로세스)로 충분하다.

**다인용 App**
- 기업용
- 동시 처리 필요
- 많은 성능 요구
> App의 기능을 여러 컴퓨터에 분할해서 실행할 필요성이 대두 => `분산컴퓨팅 기술(Distributed computing)`이 등장  
> 또한 각 기능별로 참조관계가 존재할 수 있기에 다른 컴퓨터(프로세스)의 메서드를 호출하는 기술도 필요해졌다. 

## 원격 메서드 호출 제약
분산 컴퓨팅 기술을 도입하면 기능별 프로세스가 서로 참조하는 관계가 형성되기도 한다. 이 때 한 프로세스가 다른 프로세스의 메서드를 사용하려고 한다면 원격메서드 호출 기술이 필요하다.  
>일반적으로는 다른 컴퓨터/프로세스의 메모리를 직접 접근할 수 없기 때문이다. 즉 local에서 remote의 변수나 메서드를 사용할 수 없다. 따라서 원격메서드를 호출하는 기술을 사용해야 한다.

```
*Remote와 local의 개념:
같은 컴퓨터에 있어도 다른 프로세스인 경우 local/remote로 표현한다. 다른 컴퓨터에 있는 다른 프로세스인 경우도 local/remote로 표현한다.
```

## 원격 메서드 호출 기술 발전사
**1. RPC(Remote Procedure Call)**
- 절차적 프로그래밍 언어 ex) C, Pascal 등

**2. RMI(Remote Method Invocation)**
- 객체지향 프로그래밍 언어 ex)C++, Java
- 현재 myapp의 app-client에서 app-server의 서비스 객체를 호출한 것이 이 방식이다. 

**3. CORBA(Common Object Request Broker Architecture)**
- 이기종 플랫폼(다른 Language) 간에 호출이 가능하게 만든 기술 ex) C -> Java, Java -> Pascal
- 서비스 객체를 만드는 측에서 제공한다. (stub을 서버 개발자가 만들어서 제공)

**4. WebService**
- 서버에서 IIOP *WSDL을 받아와서 Local의 IDE가 stub을 자동 생성  

\*WSDL(XML): 서비스 객체가 제공하는 메서드 정보

**5. RESTful**
- 클라이언트 측에 stub 역할을 하는 객체가 필요 없다.
- 언어에 상관없이 HTTP 통신에 맞춰 서비스 객체 이용, 즉 이기종 플랫폼 간에 호출 가능.
- 전송 데이터 포맷: XML, JSON을 주로 사용
- Web Server는 서비스객체에 대해 위임하고 리턴받는다.
---

<br></br>

# 서브 프로젝트 다루기
<img src="../img/Sub Project.png"></img>

- 두 개의 메인 프로젝트에서 공통된 코드가 있다면 서브클래스로 분리하여 사용할 수 있다. 
- 또한 특정 기능을 담당하는 객체를 라이브러리로 분리하면 재사용성이 높아진다. 

## 빌드 스크립트 설정
> 서브 프로젝트를 라이브러리로 사용하려면 다음과 같은 명령어를 추가해주면 된다.

**settings.gradle**
```groovy
include('프로젝트명') //다중 프로젝트(멀티 프로젝트/서브프로젝트) 선언
```

**build.gradle**
```groovy
plugins {
  id 'java-library' // java 라이브러리 프로젝트라고 정의하는 빌드 스크립트
}

dependencies {
  implementation project('프로젝트명') //프로젝트의 종속성 설정(서브 프로젝트를 포함한다는 의미)
}
```

