# DAO 프록시 객체 자동생성
---
## Java.lang.reflect.Proxy 클래스 사용법
**Proxy 객체를 만드는 방법**
java.lang.reflect.Proxy의 newProxyInstance를 사용하면 프록시 객체를 수동으로 생성하는 대신 런타임에 동적으로 인터페이스를 구현하는 프록시 객체를 자동으로 생성할 수 있다.

> 프록시 객체 자동생성을 사용하면, 객체 생성을 위해 중복되는 코드를 작성할 필요가 없어져 재사용성과 유지보수성이 향상된다. 

```java
public class Exam {
  public static void main(String[] args) {

    // Proxy.newProxyInstance(ClassLoader, ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)

    MyInterface obj = (MyInterface) Proxy.newProxyInstance(
        Exam.class.getClassLoader(), // 클래스를 메모리에 로딩하는 일을 할 객체
        new Class[] {MyInterface.class}, // 자동 생성할 클래스가 구현해야 하는 인터페이스 목록
        new MyInvocationHandler()); // 프록시 객체에서 실제 메서드 호출을 처리할지 결정한다.

    // 자동 생성된 인터페이스 구현체의 메서드 호출하기
    obj.m1();
    obj.m2();
  }
}
```

<br>

## (실습) 프록시 객체 자동생성 적용
현재 app-api 서브 프로젝트에서는 프록시 객체를 직접 만들어서 사용하는 구조이다. 따라서 중복되는 코드여도 여러 인터페이스를 만드는 비효율적인 구조를 가진다.

> 이때, 프록시 객체를 자동생성하는 라이브러리를 사용하면 하나의 클래스만 작성해도 여러 타입의 객체를 유연하게 다룰 수 있다.
```java
public class DaoProxyGenerator {

  private DataInputStream in;
  private DataOutputStream out;
  private Gson gson;

  public DaoProxyGenerator(DataInputStream in, DataOutputStream out) {
    this.in = in;
    this.out = out;
    gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  }

  public <T> T create(Class<T> clazz, String dataName) {
    return (T) Proxy.newProxyInstance(
        DaoProxyGenerator.class.getClassLoader(),
        new Class<?>[]{clazz},
        (proxy, method, args) -> {
          try {
            out.writeUTF(dataName);
            out.writeUTF(method.getName());
            if (args == null) {
              out.writeUTF("");
            } else {
              out.writeUTF(gson.toJson(args[0]));
            }
            String statusCode = in.readUTF();
            String entity = in.readUTF();

            if (!statusCode.equals("200")) {
              throw new Exception(entity);
            }

            Type returnType = method.getGenericReturnType();

            if (returnType == void.class) {
              return null;

            } else {
              return gson.fromJson(entity, returnType);
            }
          } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException(e);
          }
        }
    );
  }
}
```
- 프록시 객체를 생성하는 Factory Method 디자인 패턴으로도 볼 수 있다.
- 메서드의 리턴 타입에 제네릭이 포함될 수도 있기 때문에 return값의 리턴타입은 메서드의 getGenericReturnType()으로 결정한다.

<br>

**ClientApp**
```java
//변경 전
boardDao = new BoardDaoImpl("board", in, out);
greetingDao = new BoardDaoImpl("greeting", in, out);
assignmentDao = new AssignmentDaoImpl("assignment", in, out);
memberDao = new MemberDaoImpl("member", in, out);

//변경 후
DaoProxyGenerator daoGenerator = new DaoProxyGenerator(in, out);

boardDao = daoGenerator.create(BoardDao.class, "board");
greetingDao = daoGenerator.create(BoardDao.class, "greeting");
assignmentDao = daoGenerator.create(AssignmentDao.class, "assignment");
memberDao = daoGenerator.create(MemberDao.class, "member");
```
---

<br></br>

# Stateful vs Stateless
**1. Stateful**
- 한번 연결하면 요청/응답을 연속해서 여러번 수행할 수 있다.
- 요청/응답을 수행하지 않는 순간에도 연결 정보를 계속 유지
- 먼저 연결된 클라이언트가 연결을 끊을 때까지 다음 클라이언트는 기다려야 한다. 
- 대량의 클라이언트 연결을 지원하지 못한다.

**2. StateLess**
- 요청할 때마다 매번 연결 수행
- 연결하는데 시간이 많이 소요되어 요청 처리시간이 길어진다.
- 한번 요청/응답이 이루어지면 즉시 연결을 끊는다. 다음 클라이언트의 대기 시간은 짧아진다.
- Stateful 방식보다 더 많은 클라이언트 요청 처리할 수 있다. 

---

<br></br>

# Networking
## 상대편으로부터 연결 요청 받기 - 서버(Server)
**1) 다른 컴퓨터의 연결 요청을 기다린다.**  
- new ServerSocket(포트번호)  
  - 랜카드와 연결한 정보 생성
  - 대기열을 준비시킨다.

  >포트번호:
  - 호스트에서 실행 중인 서버 프로그램을 구분하는 번호이다.
  - (1024~49151) 사이의 값 사용한다.
  - (1~1023) 사이의 포트 번호는 특정 서버가 사용하기 위해 미리 예약된 번호다.
  - 가능한 이 범위의 포트 번호는 사용하지 않는 것이 좋다.
  - 유명 프로그램의 포트 번호도 가능한 사용하지 말라.
    - 예) Oracle DBMS(1521), MySQL DBMS(3306) 등
  - 같은 컴퓨터에서 다른 프로그램이 이미 사용중인 포트 번호는 지정할 수 없다.
  - 포트 번호는 중복으로 사용될 수 없다.
  - 서버측 포트번호는 개발자가 정한다.

```java
ServerSocket serverSocket = new ServerSocket(8888);
System.out.println("ServerSocket 생성!");
```

**2) 연결을 승인한다.(순차적으로)**
- 소켓.accept()
  - 연결을 기다리고 있는 클라이언트가 있다면 맨 먼저 접속한 클라이언트의 연결을 승인한다.
  - 클라이언트가 서버에 접속을 요청하면 그 정보를 대기열 목록에 올려놓고 관리한다.
  - accept()를 호출하면 대기열에서 순서대로 꺼내 해당 클라이언트와 연결된 소켓을 만든다.
```java
Socket socket = serverSocket.accept();
System.out.println("클라이언트와 연결된 Socket 생성!");
```

**3) 입출력 과정 + 닫기**
```java
// 데이터 송수신을 위한 입출력 스트림 준비
PrintStream out = new PrintStream(socket.getOutputStream());
Scanner in = new Scanner(socket.getInputStream());

// 상대편이 보낸 문자열을 한줄 읽는다.
// 상대편이 한 줄의 데이터를 보낼 때까지 리턴하지 않는다.
// 이런 메서드를 블로킹 메서드라 부른다.
String str = in.nextLine();
System.out.printf("상대편> %s\n", str);

// 상대편으로 문자열을 한줄 보낸다.
// 클라이언트가 데이터를 받을 때까지 기다리지 않는다.
// NIC의 메모리에 데이터를 전달한 후 즉시 리턴한다.
out.println("반갑습니다");

// 사용 후 입출력 도구 닫기
in.close();
out.close();
keyscan.close();

// 사용 후 네트워크 연결 닫기
socket.close(); // 클라이언트와 연결을 끊는다.
serverSocket.close(); // 클라이언트의 연결 요청을 받지
```

## 상대편에 연결을 요청하기 - 클라이언트(Client)
**1) 다른 컴퓨터와 네트워크로 연결한다.**
- new Socket(원격 호스트의 IP 주소/도메인이름, 원격 호스트 프로그램의 포트번호)
  - 서버와 연결되면 Socket 객체가 생성된다.
  - 서버와 연결될 때까지 리턴하지 않는다.
  - 서버에 연결할 수 없으면 예외가 발생한다.
    - 기본으로 설정된 타임아웃 시간까지 연결되지 않으면 예외가 발생한다.
  - 클라이언트 포트번호는 소켓을 생성하는 순간 OS가 자동으로 발급한다.(49152~65535)
```java
Socket socket = new Socket("192.168.0.12", 8888); // 서버의 대기열에 등록된다면 리턴한다.
System.out.println("서버와 연결된 Socket 생성!");
```

**2) 입출력 과정 + 닫기**
```java
// 데이터 송수신을 위한 입출력 스트림 준비
PrintStream out = new PrintStream(socket.getOutputStream());
Scanner in = new Scanner(socket.getInputStream());

// 상대편으로 문자열을 한줄 보낸다. 
// 서버가 데이터를 받을 때 까지 기다리지 않고 리턴한다.
out.println("안녕하세요");

// 상대편에서 보낸 문자열을 한 줄 읽는다.
// 상대편이 한 줄의 데이터를 보낼 때까지 리턴하지 않는다.
String str = in.nextLine();
System.out.printf("상대편> %s\n", str);

// 입출력 스트림 + 연결 닫기
in.close();
out.close();
socket.close();
keyscan.close();
```

> 소켓 객체를 생성하여 연결하는 것까지가 네트워킹이다. 이후는 일반적인 입출력 과정이다. 다만, 네트워킹 시에는 입출력 규칙이 존재한다. 이를 `protocol`이라고 한다. 

## 파일 보내기/받기
파일을 네트워킹으로 주고 받는 방법은 단순히 File I/O Stream를 사용해 protocol에 맞추어 통신하면 된다.

**1) 파일 보내기**
```java
// 보낼 파일 경로 지정
File file = new File("temp/kk.jpeg");

// 파일 입력 스트림 준비
FileInputStream fileIn = new FileInputStream(file);

// 네트워크 연결 과정은 동일
Socket socket = new Socket("192.168.0.12", 8888);

// 데이터 송수신 입출력 스트림 준비
DataOutputStream out = new DataOutputStream(socket.getOutputStream());
Scanner in = new Scanner(socket.getInputStream());

// 파일 크기 보내기
out.writeLong(file.length());

// 파일 이름 보내기
out.writeUTF(file.getName());

// 파일 데이터 보내기
int b;
while ((b = fileIn.read()) != -1) {
  out.write(b);
}

// 서버 응답받고 종료
String response = in.nextLine();
System.out.println(response);

in.close();
out.close();
socket.close();
fileIn.close();
```

**2) 파일 받기**
위의 파일보내기와 같이 네트워크 연결과정은 동일하다.
```java
// 파일 크기 읽기
long filesize = in.readLong();

// 파일 이름 읽기
String filename = in.readUTF();

// 파일 데이터 읽기
File file = new File("temp/ok_" + filename);
FileOutputStream fileOut = new FileOutputStream(file);

for (long i = 0; i < filesize; i++) {
  fileOut.write(in.read());
}

// 클라이언트에게 응답하기
out.println("OK!");
```