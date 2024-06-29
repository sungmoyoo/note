# Connection-Oriented(TCP)
**연결지향**
연결 후에 데이터를 송수신 하기 때문에 데이터 송수신에 대한 신뢰를 보장한다.

- TCP 통신 방법이 전형적인 예이다.
- TCP는 상태 유무에 따라 Stateful 또는 Stateless의 특성을 가질 수 있다. 

---

<br>

# Stateful
**통신 방식**
서버와 클라이언트를 연결한 후, 연결을 끊을 때까지 요청 응답을 계속 수행한다.

**단점**
- 클라이언트가 요청하지 않아도 계속 연결된 상태를 유지하기 때문에 서버 메모리를 많이 차지한다.
- 동시에 많은 클라이언트의 요청을 처리하기 힘들다.
- 이전 클라이언트가 연결을 끊기 전까지 다음 클라이언트의 연결이 제한된다.

**장점**
- 한번 서버에 연결되면 계속 연결을 유지되므로 요청할 때마다 연결 작업을 수행할 필요가 없다. 따라서 속도가 비교적 빠르다.
- 연결된 상태에서 수행한 작업 정보를 서버에 유지할 수 있어 영속성이 필요한 작업을 처리하는데 유리하다.

<br>

# Stateless
서버에 작업을 요청할 때 연결하고, 서버로부터 응답을 받으면 연결을 끊는다. 즉 요청/응답은 한 번만 수행한다. 

**단점**
- 요청할 때마다 매번 서버에 연결해야 하기 때문에 시간이 많이 소요된다.
- 연결과정 사용자 인증, 권한 확인 등 과정이 복잡하면 더 느려진다.
- 클라이언트와 작업한 내용을 서버에 유지하지 못한다.

**장점**
- 서버 메모리를 많이 사용하지 않는다.
- Stateful 방식보다 더 많은 클라이언트의 요청을 처리할 수 있다. 

<br></br>

# Stateful, Stateless의 여러 문제점
---
## 서버 종료 문제
### 개요
Stateful과 Stateless 두 방식 모두 클라이언트에서는 조건에 따라 반복문을 종료함으로써 연결을 끊고 종료할 수 있다. 하지만 서버에서는 순차적으로 다음 클라이언트의 연결을 계속 받는 역할을 하고 있기에 원하는 시점에 종료하려면 강제로 종료하는 방법 밖에 없다. 

### 개선
**1. 클라이언트가 서버를 종료하는 명령 송신.**   
`quit`를 입력하면 클라이언트를 종료하고 `stop`을 입력하면 서버와 클라이언트를 종료한다.
```java
// Client
...

do {
  System.out.print("이름? ");
  name = keyScan.nextLine();

  out.println(name);
  out.flush();

  String str = in.readLine();
  System.out.println(str);

  } while (!name.equalsIgnoreCase("quit") && !name.equalsIgnoreCase("stop"));

...
```
<br>

**2. 클라이언트로부터 명령을 받으면 서버를 종료.**
클라이언트가 보낸 명령을 대소문자 구분없이 읽어 처리한다.  
만약 해당 명령이 서버를 종료하는 명령이라면 서버 연결(accept()) 반복문을 종료한다. 
```java
//Server
...

loop: while (true) {
  try (Socket socket = serverSocket.accept();
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream())) {

    System.out.println("클라이언트가 연결되었음!");

    while (true) {
      String name = in.readLine();
      if (name.equalsIgnoreCase("quit")) { // 클라이언트와 연결 끊기
        out.println("Goodbye!");
        out.flush();
        break;
      } else if (name.equalsIgnoreCase("stop")) { // 서버 종료하기
        out.println("Goodbye!");
        out.flush();
        break loop;
      }

      out.printf("%s 님 반갑습니다!\n", name);
      out.flush();
    }

...
```

**3. 로컬에서만 가능하도록 처리**
위의 코드에서는 클라이언트로 연결한 누구나 서버를 종료시킬 수 있다. 따라서 서버를 운영하는 로컬에서만 서버를 종료할 수 있도록 접속한 클라이언트의 IP주소를 받아 로컬에서만 서버를 종료할 수 있도록 설정한다.
```java
//Server
...

// 접속한 클라이언트의 IP 주소 알아내기
InetAddress inetAddr = socket.getInetAddress();
System.out.printf("접속자: %s\n", inetAddr.getHostAddress());

while (true) {
  String name = in.readLine();
  if (name.equalsIgnoreCase("quit")) { //클라이언트와 연결 끊기
    out.println("Goodbye!");
    out.flush();
    break;

  } else if (name.equalsIgnoreCase("stop")) {
    out.println("Goodbye!");
    out.flush();
           
    // localhost 에서만 서버를 멈출 수 있다.
    if (inetAddr.getHostAddress().equals("localhost")) { // 서버 종료하기
      break loop;
    } else {
      break; // 클라이언트 연결만 끊는다.
    }
  }
  out.printf("%s 님 반갑습니다!\n", name);
  out.flush();
}

...
```

## Stateless : 작업 결과 초기화 문제
### 개요 
Stateless에서는 한 번 요청/응답이 이루어지면 연결이 끊기고 기존 작업결과가 모두 초기화된다. 따라서 Stateless에서 기존 정보를 유지하기 위해 클라이언트 ID 식별자를 사용하는 방식을 채택할 수 있다.

### 개선
예시로 연산자와 값을 보내 요청하면 연산에 대한 결과를 응답해주는 계산기 클래스에 Long값의 현재시간 밀리세컨드를 가지는 clientID를 추가하여 기존 결과값 유지 기능 구현
```java
// Client
...

// 서버에서 이 클라이언트를 구분할 때 사용하는 번호.
// => 0 번으로 서버에 요청하면, 서버가 신규 번호를 발급해 줄 것이다.
long clientId = 0;

while (true) {
  System.out.print("연산자? ");
  String op = keyScan.nextLine();

  System.out.print("값? ");
  int value = Integer.parseInt(keyScan.nextLine());

  try (Socket socket = new Socket("localhost", 8888);
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      DataInputStream in = new DataInputStream(socket.getInputStream())) {

    // => 서버에 클라이언트 아이디를 보낸다.
    out.writeLong(clientId);

    // => 서버에 연산자와 값을 보낸다.
    out.writeUTF(op);
    out.writeInt(value);
    out.flush();

    // => 서버에서 보낸 클라이언트 아이디를 읽는다.
        
    long id = in.readLong();
    // 기존에 저장된 ID와 서버에서 받은 아이디가 다를 경우 서버에서 받은 ID를 새 아이디로 저장한다.
    if(clientId != id) {
      clientId = id; 
      }
    // => 서버에서 보낸 결과를 읽는다.
    System.out.println(in.readUTF());

    ...
```

## 클라이언트 요청 문제
Stateful나 Stateless는 요청/응답이 종료될 때까지 다른 클라이언트들은 기다려야 하는 문제가 있다. 해당 문제는 Single Thread이기 때문에 발생한다. 

예를 들어 카페의 키오스크를 빗대어 표현하면, 키오스크가 한 대만 존재한다고 볼 수 있다. 주문을 여러번 반복하던 한번만 하던 주문을 완료하지 못하면 뒤의 손님이 이용 못하는 것과 같은 개념이라고 볼 수 있다.

이를 해결하기 위해서는 키오스크를 여러 대 두는 Multi Thread를 적용하면 된다.

Multi Thread를 적용해보기 전에 Thread의 개념을 먼저 알아보자.

# Thread
## Thread의 개념
기본 실행흐름이다. 각 프로세스는 최소한 하나의 Thread를 가지고 있는데, 이를 Main Thread라고 한다. Main Thread는 프로그램이 시작될 때 자동으로 생성되며, 프로그램이 종료될 때까지 실행을 담당한다. 

<img src="../img/Thread.png">

Main Thread는 프로그램의 진입점 역할을 하며 실행이 시작되면 해당 코드를 순차적으로 실행한다. 이 때 진입하는 곳이 main() 메서드이며 다른 메서드에 진입하더라도 프로그램을 종료할 때는 리턴되어 main()메서드에서만 종료된다. 

## Multi Thread
Thread는 각각 독립적으로 실행되며 병렬적으로 동작한다. 즉 여러 작업을 동시에 처리할 수 있다. 이러한 방식을 Multi Thread라고 한다.

<img src="../img/Multi Thread.png">

## 스레드 만들기 
1. Thread를 상속받는 클래스를 생성한다.
2. run() 메서드를 오버라이딩한다.
3. 메서드 바디에 요청 처리 코드를 작성
```java
class MyThread extends Thread {
  @Override void run() {
    // main 실행흐름과 분리해서 독립적으로 실행하고 싶은 코드
  }
}
```

## 스레드 실행
**방법1**
부모스레드로부터 실행을 분리하여 run()을 호출한다.
```java
public static void main(String[] args) {
  MyThread t = new MyThread();
  t.start();
}
```

**방법2**
부모스레드에 실행을 분리하고 t.run()을 호출한 다음 r.run() 호출하는 방법
Runnable 인터페이스를 구현체를 사용한다.
```java
MyRunnable r = new MyRunnable();
Thread t = new Thread(r);
t.start();
```

## 리팩토링
방법2에서 인터페이스 구현체를 한번만 사용하고자 할 때 익명클래스를 사용하여 코드의 가독성을 높이고 불필요한 클래스 정의를 피할 수 있다.

```java
public static void main(String[] args) {
  new Thread(new Runnable() {
    @Override
    public void run() {
      // main 실행흐름과 분리해서 독립적으로 실행하고 싶은 코드
    }
  }).start();
}
```

하나의 추상 메서드만 가지는 Functional Interface이면 람다 표현식으로 작성할 수 있다.
```java
public static void main(String[] args) {
  new Thread(() -> {
      // main 실행흐름과 분리해서 독립적으로 실행하고 싶은 코드
    }
  }).start();
}
```
