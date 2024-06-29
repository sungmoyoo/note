# 빠른 입출력
- 테스트 케이스가 적을 시 Scanner를 사용해도 문제가 되지 않지만, 그 수가 커질수록 Scanner로 입력받는 데에 한계(시간초과에러)가 있다.
- 대신 시간부하가 적고 속도가 빠른 BufferedReader를 사용하는 것이 권장된다.

## 왜 빠른가?
- 그냥 InputStreamReader는 문자열을 character 단위로 읽는다.
- BufferedReader는 데이터를 버퍼에 보관한 후 사용자의 요청이 있을 때 버퍼에서 문자열을 읽어온다.
- 대신 입력받은 값을 저장할 때 바이트 단위의 InputStream을 문자 단위로 변환할 필요가 있다.

## Scanner vs BufferedReader
- Scanner는 지원하는 메서드도 많고 사용하기 쉽다.
- 버퍼 사이즈가 1024로 작고 동기화처리가 안된다.

- BufferedReader는 버퍼 사이즈가 8192로 크고 동기화처리되어 Thread-Safe하다.
- 다만 문자열을 라인단위로 읽어서 데이터 가공이 필요한 경우가 많고 파싱을 지원하는 메서드도 없다.

## BufferedReader
- 객체 선언 후 readLine() 메서드를 이용하여 입력을 읽어온다.
- readLine() 메서드는 String 값을 리턴한다. 정수가 필요하면 파싱해주어야 한다.
- 토큰 단위로 데이터를 가공해야 할 경우 StringTokenizer를 사용해서 구분자를 통해 분리해주어야 한다.

## StringTokenizer
- readLine() 메서드를 통해 입력받은 값을 공백단위로 구분하여 순서대로 호출할 수 있다.
- nextToken() 메서드로 토큰단위로 입력값을 구분한다.
- 다른 구분자를 사용하고 싶다면 2번째 파라미터에 선언해주어야 한다.

## BufferedWriter
- write() 메서드에 출력하고자 하는 문자열을 파라미터로 전달하면 출력할 수 있다.
- println처럼 자동 줄바꿈을 해주지 않는다. 
- flush()나 close() 메서드를 통해 버퍼를 종료하지 않으면 제대로 출력되지 않을 수 있다.

## InputStreamReader/OutputStreamWriter
- System.in과 같은 InputStream은 입력받은 데이터를 int형으로 저장하며 UTF-16값으로 저장된다. 
- InputStream은 바이트 단위로 읽어들이기 때문에 입력한 문자 그대로 출력할 수 없다
- InputStreamReader는 이를 인코딩하여 문자단위(character)로 변환하는 클래스이다.
- OutputStreamWriter는 InputStreamReader의 반대 역할을 하는 클래스이다.


## IOException
- 입출력 스트림, 입출력 작업에서 발생되는 오류이다. 
- Scanner는 IOException을 숨긴다.
- BufferedReader는 IOException을 던진다. 따라서 throws로 예외처리해주어야 한다.

```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

// import java.io.*;
// import java.util.*;

public class Main {
  public static void main(String[] args) throws IOException{
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    
    
    int T = Integer.parseInt(br.readLine());
    
    for (long i = 0; i < T; i++) {
      StringTokenizer st = new StringTokenizer(br.readLine());
      int A = Integer.parseInt(st.nextToken());
      int B = Integer.parseInt(st.nextToken());
      bw.write(A+B + "\n");
    }
   
    bw.flush();
    
  }
}
```