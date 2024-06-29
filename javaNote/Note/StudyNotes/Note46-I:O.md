# I/O
---
## Byte Stream - 바이트 단위 출력
**1) 데이터 출력 객체 준비**
```java
FileOutputStream out = new FileOutputStream("파일경로");
```
- 지정된 경로에 해당 파일을 자동으로 생성한다.
- 기존 같은 이름의 파일이 있으면 덮어쓴다.
- 파일 경로가 절대 경로(/, c;\)가 아니면 현재 디렉토리가 기준이 된다.

**2) 1바이트 출력**
```java
out.write(int);
```
- 파라미터 타입이 int이지만 맨 끝 1바이트만 출력한다.

**3) 출력 도구 닫기**
```java
out.close();
```
- OS에서 관리하는 자원 중에서 한정된 개수를 갖는 자원에 대해 여러 프로그램이 경우하는 경우가 있기 때문에 입출력 스트림과 연결 등은 사용 후 해제하는 습관을 들여야 한다.
- AutoCloseable 인터페이스를 구현하는 클래스를 사용할 때는 try with resource 사용해도 좋다.

## Byte Stream - 바이트 단위 읽기
**1) 데이터 입력 객체 준비**
```java
FileInputStream in = new FileInputStream("파일경로");
```
- 해당 경로에 파일이 존재하지 않으면 FileNotFoundException 예외가 발생한다.

**2) 1바이트 읽기**
```java
int b = in.read();
System.out.printf("%02x\n", b);
System.out.printf("%02x\n", in.read());
```
- read() 메서드의 리턴 타입이 int 라 하더라도 1바이트를 읽어 리턴한다.

**3) 읽기 도구를 닫기.**
```java
in.close();
```

**\*반복문 활용**
in.read() 파일의 끝에 도달하면 -1을 리턴한다.
```java
FileInputStream in = new FileInputStream("temp/test1.data")

int b;
while ((b = in.read()) != -1) {
    System.out.printf("%02x ", b);
  }

in.close();
```

## Byte Stream - 바이트 배열
- write(byte[]) : 배열의 값 전체를 출력한다.
```java
byte[] bytes = {0x7a, 0x6b, 0x5c, 0x4d, 0x3e, 0x2f, 0x30};

out.write(bytes)

out.close();
```
- write(byte[], 시작 인덱스, 출력개수) : 시작 위치부터 지정된 개수를 출력한다.
```java
out.write(bytes, 2, 3);
```

- read(byte[]) : 버퍼가 꽉 찰 때까지 읽는다.
```java
FileInputStream in = new FileInputStream("temp/test1.data");

byte[] buf = new byte[100]; // 읽은 값을 저장할 배열 준비

int count = in.read(buf);
```

- read(byte[], 저장할 위치, 저장할 개수) : 읽은 데이터를 저장할 위치에 개수만큼 저장한다. 

## Byte Stream - 텍스트 데이터 출력
- String 객체의 데이터를 출력하려면 문자열을 담은 byte[] 배열을 리턴 받아야 한다.
```java
String str = new String("AB가각")

byte[] bytes1 = str.getBytes();
byte[] bytes2 = str.getBytes("UTF-8");

FileOutputStream out = new FileOutputStream("temp/utf.txt");
out.write(bytes);
out.close();
```
- getBytes() 인코딩를 지정하지 않으면, String 클래스는 JVM 환경 변수 'file.encoding'에 설정된 문자집합으로 바이트 배열을 인코딩한다. 
- 문자집합을 지정하면 기본 문자집합과 상관없이 해당 문자집합으로 바이트 배열을 인코딩한다.

## Byte Stream - 텍스트 데이터 읽기
- 1바이트씩 읽는 경우 어떤 문자는 비트이동 연산자로 2바이트 이상을 읽어 붙여야 하는 번거로움이 생길 수 있다.
- 따라서 바이트 배열로 한번에 읽고 String 객체로 만드는 것이 편하다.
```java
FileInputStream in = new FileInputStream("sample/utf8.txt");


byte[] buf = new byte[1000];
int count = in.read(buf); // 읽은 바이트
in.close();

// 읽은 바이트를 String 객체로 생성
// new String(바이트 배열, 시작 인덱스, 읽을 개수)
String str = new String(buf, 0, count); 
Strint str2 = new String(buf, 0, count, "UTF-8");
System.out.println(str);
```
- 바이트 배열을 String 객체로 만들 때 인코딩을 지정하지 않으면 JVM 환경변수에 설정된 문자집합으로 인코딩된 것으로 간주하고 자바가 사용하는 UTF-16BE 방식의 코드 값으로 변환한다.

## Character Stream - 문자 단위 출력
JVM의 문자열을 파일로 입출력할 때, FileOutputStream과 같은 바이트 스트림 클래스를 사용하면 문자집합을 지정해야 하는 번거로움이 있었다.
> 이런 불편함을 해결하기 위해 만든 것이 Reader/Writer 계열의 문자 스트림 클래스이다.

**1) 출력 도구 준비**
```java
FileWriter out = new FileWriter("temp/test2.txt");
```
- FileWriter는 문자 데이터를 출력할 때 UCS2 코드를 JVM 환경변수 file.encoding 에 설정된 character set 코드로 인코딩하여 출력한다.
- JVM을 실행할 때 -Dfile.encoding=문자집합 옵션으로 기본 문자 집합을 설정한다. 만약 file.encoding 옵션을 설정하지 않으면 OS의 기본 문자집합으로 자동 설정된다.
**2) 문자 출력**
```java
out.write(0x7a6bac00);
out.write('A');
```
**3) 출력 도구 닫기;
```java
out.close()
```

### 출력할 문자 집합 설정
```java
FileWriter out = new FileWriter("파일경로",Charset.forName("인코딩"));
```
- 출력 스트림 객체를 생성할 때  문자 집합을 지정하면 UCS2 문자열을 해당 문자집합으로 인코딩 한다.

## Character Stream - 문자 단위 읽기
**1) 입력 도구 준비**
```java
FileReader in = new FileReader("temp/utf8.txt");
```

**2) 문자 읽기**
```java
int ch1 = in.read(); 41 => 0041('A');
int ch2 = in.read(); ea b0 80 => ac00('가');
```
- JVM 환경 변수 'file.encoding'에 설정된 문자코드표에 따라 바이트를 읽어서 UCS2로 변환한 후에 리턴한다.

**3) 읽기 도구 닫기**
```java
in.close();
```

### 입력된 값의 문자집합 설정
```java
FileReader in = new FileReader("temp/utf8.txt", Charset.forName("UTF-8"));
```

## Character Stream - 문자 배열 출력
- FileWriter 는 char[] 을 출력한다.
```java
FileWriter out = new FileWriter("temp/test2.txt");

char[] chars = new char[] {'A', 'B', 'C', '0', '1', '2', '가', '각', '간', '똘', '똥'};

// 전체 출력
out.write(chars);

// 특정 부분만 출력
out.write(chars, 2, 3);

out.close();
```

## Character Stream - 문자 배열 읽기
```java
FileReader in = new FileReader("temp/test2.txt");
char[] buf = new char[100];

// 전체 읽기
int count1 = in.read(buf);

// 특정 위치에 저장
int count2 = in.read(buf, 10, 40);

String str = new String(buf, 0, count);

in.close();
```

## Character Stream - String 출력하기
```java
FileWriter out = new FileWriter("temp/test2.txt");

String str = new String("AB가각");
out.write(str); 

out.close();
```

## Character Stream - 텍스트 읽기
```java
FileReader in = new FileReader("temp/test2.txt");

// FileReader 객체가 읽을 데이터를 저장할 메모리를 준비한다.(Factory Method)
CharBuffer charBuf = CharBuffer.allocate(100);

int count = in.read(charBuf);

// 버퍼의 데이터를 꺼내기 전에 읽은 위치를 0으로 초기화시킨다.
// - read() 메서드가 파일에서 데이터를 읽어서 버퍼에 채울 때 마다 커서의 위치는 다음으로 이동한다.
// - 버퍼의 데이터를 읽으려면 커서의 위치를 처음으로 되돌려야 한다.(flip)
// - flip() 메서드를 호출하여 커서를 처음으로 옮긴다. 그런 후에 버퍼의 텍스트를 읽어야 한다.
charBuf.flip();

// 데코레이터를 붙인다.
// => 버퍼 기능 + 한 줄 읽기 기능
BufferedReader in2 = new BufferedReader(in);

System.out.println(in2.readLine());

// 마지막으로 연결한 것부터 해제한다.
in2.close();
in.close();
```





