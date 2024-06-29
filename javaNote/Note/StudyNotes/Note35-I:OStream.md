# File I/O Stream 입출력
---
일반적으로 간단한 CRUD 프로그램을 만들어서 데이터를 입력받고 배열, 자료구조 등에 저장할 경우 프로그램 종료 시 메모리에 저장된 데이터가 모두 해제되어 사라진다.  
>File 입출력을 활용하면 데이터에 영속성(Persistent)을 추가할 수 있다.

# FileOutputStream
FileOutputStream은 자바에서 파일에 데이터를 쓰기 위한 클래스이다. OutputStream 클래스를 상속받아 `byte` 단위로 데이터를 쓴다.

## 객체 생성
FileOutputStream을 통해 데이터를 작성하기 위해서는 해당 클래스 안에 있는 write 메서드를 사용해야 한다. 객체 생성 시 필요한 정보인 저장할 경로/파일명을 생성자에 주입해주어야 한다.

## 데이터 작성
**write(int b)**
정수 값의 마지막 1바이트만을 파일에 작성한다.  
비트 이동 연산자를 통해 8비트씩 이동시키면 다른 바이트값도 저장할 수 있다.

**write(byte[] b)**
바이트 배열의 모든 내용을 파일에 작성한다.

**write(byte[] b, int off, int len)**
파라미터로 주어진 바이트 배열의 시작지점(off)에 길이(len)만큼의 데이터를 파일에 작성한다.

- write(byte[] b)로 파일을 출력하면 바이트 간 구분이 없는 상태가 된다. 
- 경우에 따라서 write(int b)로 해당 데이터 앞부분에 길이 정보를 저장하여 파일 읽기를 효과적으로 처리할 수 있다.

## 스트림 닫기
- FileInputStream을 사용한 후 스트림을 닫지 않고 프로그램을 종료할 경우 데이터가 정상적으로 저장되지 않을 수 있고 자원 해제 측면에서도 도움이 되지 않는다. 
- FileOutputStream은 `AutoCloseable` 인터페이스를 구현하고 있기 때문에 try-with-resources 문을 사용할 수 있다.


# FileInputStream
FileInputStream은 파일에서 데이터를 읽기 위한 클래스이다. InputStream을 상속받아 데이터를 `byte` 단위로 읽어온다.

## 객체 생성
FileInputStream을 통해 데이터를 읽어오려면 읽어올 경로/파일명을 주입하여 객체를 생성하여야 한다.

## 데이터 읽기
**read()**
파일에서 1바이트를 읽어온다. 

**read(byte[] b)**
파일에서 파라미터로 넘겨준 바이트 배열에 데이터를 저장하여 리턴한다.

**read(byte[] b, int off, int len)**
파라미터로 주어진 배열의 시작지점(off)에서 길이(len)만큼 저장하여 리턴한다.

## 스트림 닫기
- FileInputStream, FileOutputStream 외에도 입출력 스트림은 사용 종료 후 close() 해주어야 한다.
- FileInputStream도 try-with-resources 문을 사용할 수 있다.

# <b style="color:red"> 결론 </b>
>-입출력 단위는 일반적으로 바이트 단위로 이루어져 있다.  
>-read(), write() 사용 시 비트이동연산자(>>, <<) 8비트씩 이동하여 원하는 바이트 값을 저장, 읽어올 수 있다. 