# 객체 직렬화(serialize)
---
자바에서 데이터를 다른 컴퓨터 환경은 바이트 스트림으로 연결되어 있다. 따라서 개발자가 직접 입출력하려고 하면 데이터를 바이트로 변환하고, 바이트를 데이터로 바꿔주는 작업을 해야한다.
이와 같은 작업을 해주는 기술이 serialize이다.

>serialize란 객체를 바이트 형태로 변환하는 포맷 변환 기술을 말한다. 반대 개념인 deserialize는 바이트로 변환한 데이터를 다시 객체로 변환하여 JVM에 상주시키도록 한다.

단, 외부의 자바 시스템에서는 사용할 수 있지만, 다른 프로그래밍 언어에서는 사용할 수 없는 것이 단점이다.

serialize를 하면 파일에 해당 클래스의 정보, SerialVersionUID, 데이터 내용이 출력된다.

## serialize 조건
- serialize 인터페이스를 구현하여 기능을 활성화한 상태여야 한다.
- 필요한 경우 개발자가 SerialVersionUID를 직접 조정해야 한다. 


### SerialVersionUID
java.io.Serializable 구현 클래스에 자동 추가되는 스태틱 필드, 유효성 검사가 목적이다.  

- 개발자가 추가하지 않으면 컴파일러가 자동으로 추가한다.
- 개발자가 값을 지정하지 않으면 컴파일러가 필드와 메서드 정보를 바탕으로 자동생성한다.

>인스턴스를 만들 때 클래스 정보와 SerialVerisonUID 가 일치해야 한다.

**=> 즉, 저장할 때 생성한 인스턴스의 버전과 읽을 때 사용할 클래스의 버전ID가 동일하면 정상적으로 읽고, 다르면 예외를 발생시킨다. 따라서 필요한 경우 개발자가 직접 serialVersionUID 스태틱 변수를 명시적으로 추가해야 한다.**

<br></br>

# Java I/O Stream API
---
<img src="../img/StreamClass.png">

# byte stream vs character stream
## byte stream:
데이터를 바이트 단위로 처리하는 입출력 스트림으로 InputStream과 OutputStream 클래스들이 바이트 스트림이다.
```
FileOutputStream.write() => 1byte씩 출력 => 파일
파일 => FileInputStream.read()=> 1byte씩 입력 
```

**character stream:**  
입출력을 문자 단위로 처리하는 스트림, 입출력 시 특정 문자를 인코딩하여 문자를 읽고 쓰며, 따라서 한 문자가 몇 바이트로 표현되는지 바이트가 가변 길이인지 고려해야 한다.

- FileWriter.write() => UTF-16BE => UTF-8 => 파일
>JVM 문자집합인 UTF-16BE에서 file.encoding으로 설정한 문자집합으로 변환하여 출력한다.

- 파일 => FileReader.read() => UTF-8 => UTF-16BE 리턴
>JVM 환경변수인 file.encoding에 설정된 문자집합으로 간주하고 읽고 JVM 문자집합인 UTF-16BE으로 변환한다.
 
```
주의! FileReader/FileWriter는 문자를 변환하기 때문에 바이너리 파일은 사용하면 안되고 텍스트 파일만 사용한다.
```




