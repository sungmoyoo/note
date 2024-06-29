# Networking을 이용하여 데이터 공유하기
**Networking을 이용하는 목적**
로컬에서 파일을 다룰 경우 아래와 같은 상황이 발생한다.
1. 개인용 App
```
사용자 --> App --> 파일..
```

2. 업무용 App
```
인사팀1 --> App --> 파일1..

인사팀2 --> App --> 파일2..

인사팀3 --> App --> 파일3..
```
=> 로컬에 파일을 저장하는 방식으로는 인사팀 부서원 전체가 같은 파일을 공유할 수가 없다.

<br></br>

## 데이터 공유 방식
**1. 공유 폴더/공유 디스크(networking)**  
- 공유 폴더/공유 디스크에서 파일 또는 폴더를 여러 사용자나 장치 간에 공유하기 위한 용도로 사용되는 기술이다. 

>예) 
>Window - 네트워크 드라이브  
>Linux - NFS

업무용 App으로 사용할 경우 여러 명이 같은 파일에 대해 작업할 수 있다. 하지만 여러 App이 같은 파일을 동시에 변경할 때 다른 App이 변경한 내용을 덮어 써버리는 문제가 발생한다.

**2. 데이터 공유 관리 시스템**
- 동시 요청 처리를 위해 파일 관리 App 프로그램이 파일 입출력을 통제하는 방식이다. 

파일 입출력을 파일 관리 App이 요청순서에 따라 관리해주기 때문에 App끼리 데이터를 덮어쓰는 문제가 발생하지 않는다.

서비스를 이용하는 요청 프로그램을 `Client App`이라고 하고  서비스를 제공하는 응답 프로그램을 `Server App`이라고 한다.
> 이런 식의 App 구조를 `Client/Server` 줄여서 `C/S 프로그램` 이라 부른다.

<br></br>

## myapp 디렉토리 구조 변경
<img src="../img/myappDirectory.png">

기존 standalone 방식으로 만든 app을 app-client와 app-server로 분리하여 Json형식으로 통신(요청/응답)하도록 변경한다.

<br></br>

### 요청 규칙(protocol)
>- 데이터명                  
>- 명령                     
>- 데이터(entity) - Json형식  

### 상태코드
>- 200 : 성공
>- 400 : 요청 오류
>- 500 : 서버 실행 오류

<br></br>

# 서버/클라이언트 연결
### ServerApp
**1단계: 랜카드 연결 정보를 준비한다.**
- 랜카드와 연결하는 것은 실제 OS가 수행한다.
- JVM은 OS가 작업한 결과를 가져온다.
```java
ServerSocket serverSocket = new ServerSocket(8888) // 포트 번호
```
*\*포트번호(Port): 랜카드로 수신 받은 데이터를 받을 대상을 구별할 때 사용하는 식별 번호. 중복 불가.*

**2단계: 클라이언트의 연결을 기다림**
- 클라이언트 대기 목록에서 먼저 연결된 순서대로 클라이언트 연결정보를 꺼낸다.
- 아무것도 없으면 연결될 때까지 리턴하지 않고 기다린다.
```java
Socket socket = serverSocket.accept();
// ServerApp에 있는 Socket 객체는 클라이언트 연결 정보를 가진다.
```
---
### ClientApp
**3단계: 서버와 연결**
- Socket 객체를 만들어 연결한다. 
- 클라이언트 연결 정보를 대기열 목록에 올린다.
```java

Socket socket = new Socket("127.0.0.1", 8888);
// ClientApp에 있는 Socket객체는 서버 연결 정보를 가진다.
```

>서버 주소: IP주소, 도메인명  
>포트 번호: 서버 포트 번호  
>=> 로컬 컴퓨터를 가리키는 주소
>   - IP 주소: 127.0.0.1
>   - 도메인명: localhost
---

<br></br>

# Socket 파일 입출력
일반적으로 Socket 객체에 getI.OStream 메서드를 사용하여 입출력을 한다.

**getInputStream**
- 네트워크 소켓, 파일, 데이터 소스 등과 같은 입력 스트림을 얻을 때 사용하는 메서드
- InputStream 객체를 리턴한다.

**getOutputStream**
- 네트워크 소켓, 파일, 데이터 소스 등과 같은 출력 스트림을 얻을 때 사용하는 메서드
- OutputStream 객체를 리턴한다. 

> InputStream, OutputStream을 통해 read/write를 사용하는 것보다 Decorator 패턴을 사용하여 DataI.OStream을 연결하여 편리한 메서드를 사용한다.

### ClientApp
```java
// 데코레이터 패턴 적용
DataInputStream in = new DataInputStream(socket.getInputStream());
DataOutputStream out = new DataOutputStream(socket.getOutputStream());

// 요청 규칙
out.writeUTF("board");
out.writeUTF("findAll");
out.writeUTF("");

// 응답 결과 출력
String response = in.readUTF();
System.out.println(response);
```

### ServerApp
```java
DataInputStream in = new DataInputStream(socket.getInputStream());
DataOutputStream out = new DataOutputStream(socket.getOutputStream());

// 10초 후에 읽을 수 있도록 스레드 대기(밀리세컨드)
Thread.sleep(10000);

// 요청 읽기
String dataName = in.readUTF();
String command = in.readUTF();
String entity = in.readUTF();

// 응답 
Thread.sleep(10000);
out.writeUTF("Command Result");
```

<br></br>

**write/read 입출력 특징**
- write: 랜카드의 메모리로 출력된다. 상대편이 읽을 때까지 기다리지 않고 즉시 리턴한다.

- read: 상대편에서 보낸 데이터는 램에 보관된다. 램에 보관된 데이터를 읽으면 리턴한다. 데이터가 없으면 상대편이 보낸 데이터가 들어올 때까지 리턴하지 않는다(기다린다).

<br></br>
