# Connectionless(UDP)
**비연결**
연결없이 데이터를 송수신한다. 

상대편이 네트워크에 연결되어 있지 않다면, 그 데이터는 버려진다. 그래서 전송 여부를 신뢰할 수 없다.

- DatagramSocket, DatagramPacket을 사용하여 처리한다.

## 데이터 송신 방식
**Client**
DatagramSocket()을 생성
```java
DatagramSocket socket = new DatagramSocket();
```

보낼 데이터를 바이트 배열로 준비
```java
byte[] bytes = "Hello".getBytes("UTF-8");
```

보낼 데이터를 패킷에 담는다.
- 패킷 = 데이터 + 데이터크기 + 받는이의 주소 + 받는이의 포트번호
```java
DatagramPacket packet = new DatagramPacket(
  bytes, 
  bytes.length, 
  InetAddress.getByName("localhost"), 
  8888 
  );
```

>*\*InetAddress.getByName("이름"):*
1. 이름을 가지고 DNS서버에 IP주소를 문의한다.
2. 이름을 검색하여 IP주소를 찾아 리턴한다.
3. getByName() 메서드가 InetAddress 객체에 IP주소를 담아 리턴한다. -> Factory Method

데이터를 전송한다.
```java
socket.send(packet);

socket.close(); // 자원 해제
```

**Server**
데이터 송수신을 담당할 소켓을 먼저 준비한다.
- Server와 Client 모두 같은 소켓 클래스를 사용한다. 서버소켓이 따로 없다.
- 받는 쪽에서는 소켓을 생성할 때 포트번호를 설정한다.
```java
DatagramSocket socket = new DatagramSocket(8888);
```

받은 데이터를 저장할 버퍼(빈 배열) 준비
```java
byte[] buf = new byte[8196]
```

빈 패킷 준비
```java
DatagramPacket emptyPacket = new DatagramPacket(buf, buf.length);
```

빈 패킷에 클라이언트가 보낸 데이터를 받는다.
- 데이터를 받을 때까지 리턴하지 않는다.
```java
socket.receive(emptyPacket);

socket.close(); 
```

패킷에 저장된 데이터를 꺼낸다. String 데이터
```java
// String(바이트 배열, 저장할 인덱스, 저장할 개수, 인코딩)
String message = new String(emptyPacket.getData(), 0, emptyPacket.getLength(), "UTF-8")
```

# Connection-Oriented(HTTP)
## Web
링크를 통해 여러 HTTP주소가 연결되어 있는 관계가 마치 거미망 같다 하여 Web이라는 이름이 붙여졌다.

**HTTP Client(Web Browswer)**
ex) GUI: Chrome, Edge, Safari, FireFox, Opera, Whale
    CLI: Wget, curl

```
HTTP 요청 프로토콜
---------------------------------
GET [자원주소] HTTP/1.1 (CRLF)
Host: [서버주소] (CRLF)
(CRLF)
---------------------------------
```

**Client 예**
```java
public class HttpClient {
  public static void main(String[] args) throws Exception {
    Socket socket = new Socket("www.rpm9.com", 80);
    PrintStream out = new PrintStream(socket.getOutputStream());
    Scanner in = new Scanner(socket.getInputStream());

    // HTTP 응답 프로토콜에 따라 클라이언트에게 데이터를 보낸다.
    // => macOS에서 JVM을 실행할 때, println()은 문자열 뒤에 0a(LF) 코드만 붙인다.
    // => 이를 해결하려면, 다음과 같이 명확하게 CRLF 코드를 붙여라.
    out.print("GET / HTTP/1.1\r\n");
    out.print("Host: www.rpm9.com\r\n");
    out.print("\r\n");
    out.flush();

    while (true) {
      try {
        System.out.println(in.nextLine());
      } catch (Exception e) {
        break;
      }
    }

    out.close();
    in.close();
    socket.close();
  }
}
```

**HTTP Server(Web Server)**
ex) Apache HTTP Server, IIS, NginX

```
HTTP 응답 프로토콜
--------------------------------
HTTP/1.1 200 OK(CRLF)
Content-Type: text/html; charset=UTF-8(CRLF)
(CRLF)
보낼 데이터
--------------------------------
```

**Server 예**
```java
public class HttpServer {
  public static void main(String[] args) throws Exception {
    ServerSocket ss = new ServerSocket(8888);
    System.out.println("서버 실행!");

    while (true) {
      Socket socket = ss.accept();
      Scanner in = new Scanner(socket.getInputStream());
      PrintStream out = new PrintStream(socket.getOutputStream());

      while (true) {
        String str = in.nextLine();
        System.out.println(str);
        if (str.equals(""))
          break;
      }

      out.print("HTTP/1.1 200 OK\r\n");
      out.print("Content-Type: text/html; charset=UTF-8\r\n");
      out.print("\r\n");
      out.print("<html><body><h1>안녕!-강사</h1></body></html>\r\n");

      out.close();
      in.close();
      socket.close();
    }
  }
}
```

# 종단 간 암호화(end-to-end encryption)
기존의 HTTP 방식은 전송중인 데이터를 가로채면 데이터 그대로 확인할 수 있었다. 이러한 HTTP의 보안적 문제를 해결한 프로토콜이 HTTPS이며 이때 적용된 기술이 종단 간 암호화 기술이다.

## 정의
통신하는 두 당사자 간에 메시지나 데이터를 보호하기 위한 암호화 기술이다. 이는 중간에 있는 서버나 제 3자가 메시지를 해독할 수 없도록 공개키와 개인키를 사용한다.

=> `HTTPS`의 S가 `Secure`의 약자로,종단 간 암호화 기술이 적용되어 데이터 통신이 보안적으로 안전하다는 것을 나타낸다.

**1. 공개키 (Public Key):** 
공개 키는 모두에게 공개되어 있고, 데이터를 암호화하는데 사용된다. 쉽게 이해하면 자물통이다. 공개되어 있기 때문에 누구나 공개키를 사용해 데이터를 암호화할 수 있지만, 해독은 키의 소유자가 그 개인키를 사용해야만 할 수 있다.

**2. 개인키 (Private Key):** 
개인 키는 오직 키의 소유자만 알고 있으며, 이 개인키를 사용해 공개키를 해독할 수 있다.

**3. 대칭키 (Symmetric key)**
실질적인 데이터 통신에 사용되는 키로 주로 클라이언트에서 생성되어 공개키/개인키를 통해 서버와 공유하여 클라이언트와 서버 모두 가지게 되는 키이다. 공개키/개인키보다 암호화/복호화 속도가 더 빠르다.

## HTTPS 구동 원리
**1.클라이언트와 서버 연결 시작**
클라이언트가 서버에 HTTPS 연결을 요청하면 서버는 공개키와 함께 인증서를 제공한다.

**2. 인증서 확인**
클라이언트는 서버로부터 받은 인증서를 인증 기관(CA)를 통해 인증서를 검증한다.

**3. 대칭키 생성 및 암호화**
클라이언트는 대칭키를 생성한 후 서버의 공개 키를 사용해 암호화한다. 이 암호문은 서버로 전송된다.

**4. 데이터 통신**
서버에서 개인키로 대칭키를 해독하면 서로 안전한 대칭키를 공유하는 상태가 된다. 이후 대칭키를 사용해 암호화하여 데이터를 송수신한다. 

이와 같이 클라이언트와 서버 간에 안전한 통신을 설정하기 위해 암호화된 키를 공유하는 과정을 SSL/TLS 핸드셰이크 과정이라고 한다.

# URI/URL/URN
**URI(Uniform Resouce Indetifier)**
통합 자원 식별자, URI는 특정 리소스를 식별하는 형식을 의미한다. URL과 URN을 하위 개념으로 포함한다.

**URL(Uniform Resource Locator)**
URL은 리소스의 위치를 나타내는 특별한 형식의 URI이다. URL은 특정 프로토콜을 사용하여 리소스에 접근하는데 필요한 정보를 가지고 있다. 즉 네트워크 상의 자원이 어디 있는지 위치를 알려주기 위한 규약이라고 할 수 있다.

**URN(Uniform Resource Name)**
URL이 리소스가 있는 위치를 지정한다면, URN은 리소스에 고유한 이름을 부여한 것이다. 현재까지는 URL이 주로 웹에서 사용되며, URL은 적용되지 않았다.

# URL 클래스
## 웹 상에서 자원의 위치를 표현하는 방법
[프로토콜]://서버주소:포트번호/자원의경로?파라미터명=값&파라미터명=값
- 프로토콜: http(80), https(443), ftp(21/20) 등
- 서버주소: IP 주소(192.168.0.19), 도메인명(www.bitcamp.co.kr)
- 포트번호: 80 또는 443(생략할 수 있다), 8080(프록시 서버) 등
- 자원의경로: /index.html, /board/list.jsp 등
- 서버에 보내는 파라미터(Query String): 파라미터명=값&파라미터명=값

## 자원
정적 자원(static)
- 요청할 때 마다 결과 콘텐트가 변경되지 않는 자원. 즉 파일을 가리킨다.
- 예) HTML, GIF, JPEG, PNG, CSS, JavaScript, TXT 등의 파일

동적 자원(dynamic)
- 요청할 때 마다 결과 콘텐트가 변할 수 있는 자원. 
- 메일 조회, 게시물 변경, 주문 등의 웹 프로그램을 가리킨다.
- 예) index.php, index.jsp, /board/list 등

# URL 구성
>https://naver.com:443/search.naver?where=nexearch&query=bitcamp
http = `Protocol`, `Scheme`
naver.com = `Host Address`, `Domain`
443 = `Port`
search.naver = `Path`
where=nexearch&query=bitcamp = `Query String(qs)`

## URL 클래스 생성
java 19 버전까지는 URL 인스턴스 생성하는 방식을 사용
```java
URL url = new URL("https://search.naver.com:443/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=bitcamp");
```

java 20 부터는 URI의 toURL()메서드를 호출하여 생성
```java
URL url = new URI("https://search.naver.com:443/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=bitcamp").toURL();
```

URL 분석
```java
System.out.printf("프로토콜: %s\n", url.getProtocol());
System.out.printf("서버주소: %s\n", url.getHost());
// 기본 포트번호는 생략가능하다.
// 지정하지 않으면 -1 리턴. 실제 접속할 때는 기본 포트번호 사용.
System.out.printf("포트번호: %d\n", url.getPort()); 
System.out.printf("자원경로: %s\n", url.getPath());
System.out.printf("서버에 보내는 파라미터: %s\n", url.getQuery());
```

### URL 문서 내의 위치 지정
자원 경로 다음에 문서의 내부 위치를 지정하면 웹브라우저는 해당 위치로 자동 스크롤한다.
자원의 내부 위치를 표현하는 방법은 다음과 같다.
```java
// http://서버주소:포트/자원의경로/../xxx#문서의 내부 위치(문서 레퍼런스)

URL url = new URI("https://tools.ietf.org/html/rfc2616#section-5.1").toURL();
```

### URL 부가 데이터 지정
URL에 추가 정보를 첨부하려면 query string을 지정하면 된다.
```java
// http://서버주소:포트/자원경로?파라미터명=값&파라미터명=값&파라미터명=값 

URL url = new URI("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=bitcamp").toURL();
```
다음과 같이 전달된 부가 데이터는 서버에서 해당 값을 읽어서 처리하거나, 필터링하는 등 서버 로직에 활용될 수 있다.

### URL 로컬 파일 경로 지정
웹 애플리케이션에서 로컬 파일에 대한 접근이 필요한 경우에 로컬 파일 경로를 지정한다. 예를 들어 파일이나 사진 업로드 등의 기능을 구현할 때 사용한다.

일반적으로 Java에서는 보안상의 이유로 로컬 파일에 직접적으로 접근하는 것이 허용되지 않는다.
```java
URL url = new URI("file:///Users/eomjinyoung/git/bitcamp-study/Hello.java").toURL();
```

### URL 클래스를 이용하여 HTTP 요청 수행
URL 클래스를 이용하면 HTTPS 프로토콜을 신경쓰지 않고 HTTP, HTTPS 요청을 수행할 수 있다.

**openStream 사용**
```java
// 주소 검증 및 준비
URL url = new URI("https://sports.news.naver.com/index").toURL();

// 서버와 연결하고 HTTP 요청을 수행하고 데이터를 읽을 도구 준비
BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

// 응답 데이터를 읽고 출력 + 닫기
while (true) {
  String str = in.readLine();
  if (str == null)
    break;

  System.out.println(str);
}
in.close();
```

**URLConnection 사용**
URLConnection 객체 사용하면 응답 헤더의 값을 메서드, 헤더 이름을 통해 다양한 값을 추출할 수 있다.
```java
URL url = new URI("https://sports.news.naver.com/index").toURL();

// URL 정보를 가지고 HTTP 요청을 수행할 객체를 얻는다.
URLConnection con = url.openConnection();

// 웹서버와 연결한 후 HTTP 요청한다.
con.connect();

// 응답 헤더 getter
System.out.printf("Content-Type: %s\n", con.getContentType());
System.out.printf("Content-Length: %d\n", con.getContentLength());
System.out.printf("Content-Encoding: %s\n", con.getContentEncoding());

// 헤더 이름 지정
System.out.printf("Content-Type: %s\n", con.getHeaderField("Content-Type"));
System.out.printf("Server: %s\n", con.getHeaderField("Server"));

// 웹서버의 응답 데이터를 읽어들일 도구를 리턴한다.
InputStream in = con.getInputStream();

BufferedReader in2 = new BufferedReader(new InputStreamReader(in));

// 출력 + 닫기
while (true) {
  String str = in2.readLine();
  if (str == null)
    break;

  System.out.println(str);
}

in2.close();
in.close();
```

# Base64
데이터를 문자열로 인코딩하는 방식이다. 즉 바이너리 데이터를 문자 데이터로 변환하는데 사용된다. 

Base64 방식은 데이터를 문자화하므로 6bit 크기를 갖는 데이터로서 어떤 네트워크 환경에서도 송수신할 수 있다.

>일반적으로 바이너리 데이터를 텍스트에 포함해서 전송, 수신, 저장하고 싶을 때 사용한다. 

예를 들면 사진 자체를 URL로 전송하고자 할 때 URL에는 바이너리 데이터를 포함할 수 없기 때문에 Base64 방식으로 텍스트로 변환하여 URL에 포함 시킬 수 있다. 

## java.util.Base64
자바에서는 Base64 인코딩 및 디코딩을 지원해주는 크랠스가 존재한다. 
```java
// 인코딩
Encoder encoder = Base64.getEncoder();
byte[] base64Bytes = encoder.encode(bytes);
System.out.println(new String(base64Bytes));

// 디코딩
Decoder decoder = Base64.getDecoder();
byte[] bytes2 = decoder.decode(base64Bytes);
System.out.println(new String(bytes2, "UTF-8"));
```


