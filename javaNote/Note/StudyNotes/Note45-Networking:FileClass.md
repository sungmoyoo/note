# Networking 
---

## DNS Query Process
**1. 로컬 확인**
클라이언트가 도메인명을 통해 브라우저 검색 시 로컬 컴퓨터는 먼저 캐시된 정보가 있는지 확인한다. 
- 이전 DNS 쿼리에서 응답받은 리소스는 캐시에 추가되어 일정 기간 보존된다.

캐시에 있는 항목이 없다면, 클라이언트가 이를 DNS 서버로 전송하여 이름 확인 프로세스를 계속한다.

<br>

**2. DNS 서버 조회**
*IP 주소와 호스트명: 
```
www.naver.com
 |      |
 |    도메인명
호스트명
```
클라이언트에 기본 설정된 DNS 서버에 쿼리되면. 서버에 로컬로 구성된 이름 영역 정보와 캐시 정보에서 일치하는 항목이 있는지 찾는다.

위 두 정보에서 일치하는 응답을 찾지 못하면 이름을 완전히 찾기 위해 재귀를 사용하여 DNS 서버를 순회한다.

- 예를 들어, 루트 서버와 DNS서버에게 이름 정보에 대한 요청을 반복하여 최종적으로 일치하는 응답을 찾아 IP주소를 로컬에 반환하는 형태이다.


<br>

***랜카드 고유번호 NIC ID는 데이터 전송여부를 결정하는 중요한 식별번호이다.

## 대기열(Queue) 지정
- 클라이언트가 접속을 요청하면 대기열에 클라이언트 정보를 저장한다.
- Queue 방식으로 관리하기 때문에 FIFO에 따라 저장된다.
- 대기열의 크기가 클라이언트의 연결을 허락하는 최대 개수이다.
- 대기열을 초과하여 클라이언트 요청을 들어 왔을 때 서버는 응답하지 않는다. 클라이언트는 내부에 설정된 시간(timeout)동안 기다리다 응답을 받지 못하면 예외를 던지고 연결 요청을 취소한다.
 
 >new ServerSocket(포트번호, 대기열크기/백로그);

 -대기열 크기를 지정하지 않으면 50개가 default이다.

## 타임아웃 지정
소켓을 생성할 때 바로 타임아웃을 지정하는 방법은 없다. 소켓의 타임아웃을 지정하고 싶다면 생성 후 따로 지정하는 방법을 사용한다.
```java
// 소켓만 생성
Socket socket = new Socket();

// 연결할 서버의 주소 준비
SocketAddress socketAddress = new InetSocketAddress("localhost", 8888);

// 서버와 연결을 시도
// Socket클래스의 connect메서드는 socketAddress 객체 timeout long값을 파라미터로 받는다.
socket.connect(socketAddress, 5000); // timeout : milliseconds
```

## 연결(accept())
- 대기열에 클라이언트가 없을 때 accept()는 블로킹 상태에 놓인다.
- 대기열에 클라이언트가 존재한다면 첫번째 클라이언트를 꺼내 연결을 승인한다.
- 대기열을 초과하는 경우 timeout 시간 내에 연결을 승인하여 대기열을 비우면 초과되었었던 클라이언트가 정상적으로 대기열에 등록된다.

## Connection Oriented vs Connectionless 

**Connection Oriented**
>통신하기 전에 먼저 연결을 설정하고 통신이 완료되면 연결을 해제하는 방식. 대표적인 예시로 TCP(Transmission Control Protocol)가 있다.

- 데이터 송수신을 보장한다. 신뢰성이 있다.
- 연결과정에 시간이 소요된다.

**Connectionless**
>통신하기 위한 사전 연결 설정이 필요하지 않아 서버가 실행 중이지 않을 때도 전송이 가능한 방식. 대표적인 예시로 UDP(User Datagram Protocol)

- 데이터 송신은 보장하지 않는다. 신뢰성이 없다. 
- 연결 과정이 없기 때문에 TCP보다 빠르다.


<br>

\*HTTP1 HTTP2 HTTP3
```
HTTP1 ---> HTTP2 ---> HTTP3
{     TCP      } ----- UDP
                   |
              속도를 높이기 위해 
              연결에 소요되는 시간을 제거
```
```
통신 
  |--- Connection-Oriented(TCP) --- Stateful
  |                             --- Stateless
  |  
  |--- Connectionless(UDP)
```

## read()/write()

<img src="../img/read:write.png">

- read(),write() 호출하면 JVM이 기계어로 변환해 OS에게 전달한다. 
- OS는 NIC(랜카드)에 메모리를 올리거나 읽는다.
서버나 클라이언트 모두 NIC(랜카드)를 가지고 있는데 이 NIC에 저장된 메모리는 TCP/IP 계층 구조에 따라 OS가 데이터를 송수신한다.

## TCP/IP
- 인터넷에서 데이터를 전송하기 위한 표준 프로토콜 스위트(Protocol Suite)

>기본적으로 "분해" -> "전송" -> "조립"의 과정을 거친다.

여기서 분해(Encapsulation)는 송신하는 과정에서 일어나고 조립(Decapsulation)은 수신하는 과정에서 일어난다.

데이터 송수신은 `응용 계층` - `전송 계층` - `인터넷 계층` - `네트워크 접근 계층`의 4단계로 데이터가 전달되면서 각 전송 단위인 Data, Segment, Packet, Frame에 맞추어 분해되고 조립된다.

이 과정에서 데이터(패킷, 프레임..)가 유실된다면 해당 데이터만 다시 전송할 것을 서버에 요청한다.

# java.io.File 활용

## 1. 지정한 폴더 및 그 하위 폴더까지 모두 검색하여 파일 및 디렉토리 이름을 출력.
```
결과 예)
/Users/bitcamp/git/test
src/
  main/
    java/
      com/
        \-- Hello.java
        \-- Hello2.java
\-- build.gradle
\-- settings.gradle
\-- Hello.java
...
```

```java
import java.io.File;

Public class Main {
  public static void main(String[] args) throws Exception {
    // 디렉토리 지정(현재 디렉토리)
    File dir = new File("."); 

    // 현재 디렉토리의 계산된 절대경로 출력
    System.out.println(dir.getCanonicalPath());

    // 메서드 호출
    printList(dir);
  }

  // 지정한 디렉토리의 하위 파일 및 디렉토리 목록을 출력하는 메서드
  static void printList(File dir, int level) {

    // listFile: 디렉토리에 포함된 파일 및 하위 디렉토리에 대한 File객체 배열을 리턴한다.
    File[] files = dir.listFiles();

    // 디렉토리 안의 모든 파일/디렉토리 목록을 출력
    for (File file : files) {

      // 들여쓰기 추가
      System.out.print(getIndent(level));

      // 디렉토리인지 파일인지 구분하여 출력, 숨김폴더는 출력하지 않는다.
      if (file.isDirectory() && !file.isHidden()) {
        System.out.printf("%s/\n", file.getName());
        
        // 하위 디렉토리 안에 파일/디렉토리를 출력한다(재귀호출)
        printList(file, level + 1);
        
        // 파일 이름 출력
      } else if (file.isFile()) {
        System.out.print("\\--")
        System.out.printf("%s\n", file.getName());
      }
    }
  }

  // 파일 구조를 나타내는 들여쓰기를 디렉토리 입장 횟수(level)에 따라 추가하여 String으로 반환해주는 메서드 
  static String getIndent(int level) {
    // 가변 객체인 strBuilder 생성
    StringBuilder strBuilder = new StringBuilder();
    
    // level만큼 들여쓰기 추가
    for (int i = 0; i < level; i++) {
      strBuilder.append("  ");
    }

    // String으로 변환하여 리턴
    return strBuilder.toString();
  }
}
```

<br>

## 2. 지정한 폴더를 삭제
>java.io.File 클래스의 delete() 메서드는 비어있는 디렉토리만 삭제할 수 있다.

```java
import java.io.File;

public class Main {
  public static void main(String[] args) {
    // 삭제할 디렉토리 지정
    File dir =  new File("temp");

    // 메서드 호출
    deleteFile(dir);
  }

  static void deleteFile(File dir) {
    // 파라미터로 주어진 dir이 디렉토리라면 하위 파일이나 디렉토리를 재귀호출하여 지운다.
    if (dir.isDirectory()) {
      File[] files = dir.listFiles();
      for (File file : files) {
        deleteFile(file);
      }
    }
    
    // 최종적으로 지정된 파일을 지운다.
    dir.delete();
  }

}
```

## 3. 지정한 폴더에서 .class 파일만 찾아 출력
> 특정 조건에 부합하는 파일만 출력하도록 하려면 java.io.FileFiler를 사용한다. 

```java
import java.io.File;
import java.io.FileFilter;

public class Main {
  public static void main(String[] args) {
    // 폴더 지정
    File dir = new File("bin/main");
    
    // 메서드 호출
    printClasses(dir);
  }

  static void printClasses(File dir) {
    // 익명클래스로 FileFilter 객체를 파라미터로 넣어줌으로써 디렉토리이거나 .class로 끝나는 파일만 true를 리턴
    File[] files = dir.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.isDirectory() || (pathname.isFile() && pathname.getName().endsWith(".class"));
      }
    });

    /* 람다
    File[] files = dir.listFiles( 
      p -> p.isDirectory() || (p.isFile() && p.getName().endsWith(".class"))
    );
    */

    // 반환된 배열에서 재귀호출 또는 출력
    for (File file : files) {
      if (file.isDirectory()) {
        printClasses(file);
      } else {
        System.out.printf("%s\n", file.getName());
      }
    }
  }
}
```