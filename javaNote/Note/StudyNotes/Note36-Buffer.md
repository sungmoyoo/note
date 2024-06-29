# Buffer
---
## Buffer의 필요성
버퍼는 바이트 단위의 임시 저장 메모리이다.  
데이터 입출력 시 데이터를 임시로 저장해두고, 호출할 때마다 혹은 용량이 가득찰 때 데이터를 입출력해주는 역할을 한다. 

기존 버퍼없이 파일 입출력이 동작하는 과정은 처리할 데이터가 많아지면 굉장히 느려진다. 반면에 버퍼를 사용하면 이를 훨씬 빠르게 처리할 수 있다.  

그 원인은 read/write의 동작과정에 있다.
```
예를 들어 write()는 호출할 때마다 해당 1바이트씩 HDD에 데이터를 출력하고, read()는 호출할 때마다 1바이트씩 HDD에서 데이터를 읽어온다. 

이때, 많은 수의 read/write를 반복하게 되면 read/write time + data seek time(데이터를 읽고 쓸 위치를 찾는데 걸리는 시간)이 많이 소요된다. 

버퍼를 사용하게 되면 
버퍼가 꽉 차면 한번에 입출력하므로 read/write 횟수가 감소하기 때문에 data seek time도 감소하며 시간이 단축된다.
```

<br></br>

# GoF Decorator 패턴

## 상속을 이용한 기능 확장 방식의 문제점
DataInputStream에서 byte 데이터를 primitive type + String으로 변환하는 작업을 수행한다.  BufferedDataInputStream에서는 DataInputStream의 기능을 상속받고 버퍼 기능을 추가하였다.  

이 과정에서 발생할 수 있는 문제점들
>1. 상속을 이용해 여러 다양한 기능을 추가시키다보면 다양한 조합의 서브클래스들이 대량으로 생성되는 문제
>2. 서브클래스끼리 기능이 중복되는 문제

## 위임과 포함을 이용한 기능 확장(Decorator)
위와 같은 상황에서 해결 방법은 데코레이터 패턴이다. 
>데코레이터 패턴은 객체의 결합을 통해 기능을 동적으로 유연하게 확장할 수 있는 구조 설계 기법을 의미한다. 쉽게 말해 기능을 상속으로 물려받는 구조가 아니라 각 기능별로 분해하여 클래스로 작성하고 객체화하여 추가 기능에 핵심 기능/추가기능을 의존 객체로 주입하는 방식이다. 
 
<img src="../img/inputStream.png"/>

**예1) 파일읽기 기본기능(핵심기능)** 
```
FileInputStream in = new FileInputStream("_");
```
FileInputStream은 Concrete Component로 입력 데이터를 파일로부터 받는 스트림이다.

**예2) 파일읽기 + 버퍼**
```
BufferedInputStream in = new BufferedInputStreamFile(new FileInputStream("_"));
``` 

**예3) 파일읽기 + 버퍼 + 데이터 변환**
```
DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream("_")));
```
DataInputStream은 바이트 스트림을 primitive 데이터 값을 읽어오는 데 사용되는 클래스이다. 여기서 BufferedInputStream을 주입받았기 때문에 버퍼 기능을 사용할 수 있고, BufferedInputStream은 FileInputStream을 의존 객체로 주입받았기 때문에 입력 핵심 기능인 read() 메서드를 오버라이딩하여 사용할 수 있다.

