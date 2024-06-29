# EOF (End Of File)
파일의 끝을 나타내는 용어로 더 이상 읽을 수 있는 데이터가 존재하지 않을 때 EOF를 감지하여 읽기를 중지하는 맥락에서 사용된다.

## 백준 A+B - 4 (10951번), 그대로 출력하기 (11718)
1. 여러 개의 테스트 케이스를 입력받고 테스트케이스마다 A+B를 출력한다. 입력을 종료할 때는 아무 값도 입력하지 않는다.

2. 입력 받은 대로 출력하는 프로그램을 작성하시오.
- 입력이 주어진다. 입력은 최대 100줄로 이루어져 있고, 알파벳 소문자, 대문자, 공백, 숫자로만 이루어져 있다. 각 줄은 100글자를 넘지 않으며, 빈 줄은 주어지지 않는다. 또, 각 줄은 공백으로 시작하지 않고, 공백으로 끝나지 않는다.


## 사용법
**Scanner**
```java
import java.util.Scanner;

public class Main {
  public static void main(String args[]){
    Scanner sc = new Scanner(System.in);
    while(sc.hasNext()){
      System.out.println(sc.nextLine());
    }
    sc.close();
  }
}
```

**BufferedReader**
1) readLine만 사용할 경우
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main{
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String str;
    while ((str = br.readLine()) != null) {
      // 로직 전개 
    }
  }
}
```

2) StringTokenizer를 사용할 경우
```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
      
    String input;

    while ((input = br.readLine()) != null){
      StringTokenizer st = new StringTokenizer(input);
      int A = Integer.parseInt(st.nextToken());
      int B = Integer.parseInt(st.nextToken());
      
      bw.write(A + B + "\n");
    }
      
    br.close();
    bw.flush();
    bw.close();
  }
}
```

## 문제점
- 백준 사이트 제출은 입력이 파일로 들어오면 위의 코드를 실행 시 정상적으로 EOF를 찾아 처리할 수 있다. 

- 다만 IntelliJ나 이클립스와 같은 IDE에서 위의 코드를 실행 시 EOF를 찾지 못해 프로그램이 끝나지 않는다. 

- 따라서 아무것도 입력하지 않고 `Enter` 입력시 입력값이 없는데 Integer.parseInt() 메서드를 실행하려고 하니 NoSuchElement 예외가 발생하게 된다.

- while 조건문에 and 연산자로 빈 값인지 확인하는 String 메서드인 isEmpty()를 추가해주면 정상적으로 EOF로 판별하여 처리할 수 있다.


```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main{
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
    while ((str = br.readLine()) != null && !isEmpty()) {
      	// 로직 전개 
    }
  }
}
```