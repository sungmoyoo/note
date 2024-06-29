# StringTokenizer

## 백준 개수 세기 (10807번)
총 N개의 정수가 주어졌을 때 정수 v가 몇 개인지 구하는 프로그램 작성

## 피드백
배열에 저장하여 숫자를 탐색하는 방법으로 답을 구할 수도 있지만 
코드 간결성을 위해 값을 저장하지 않고 StringTokenizer를 통해 답을 구하는 방법이 좋다.

## 풀이
**Scanner/배열**
```java
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
      
    int N = sc.nextInt();
      
    int[] arr = new int[N];
      
    for (int i = 0; i < N; i++) {
      arr[i] = sc.nextInt();
    }
      
    int v = sc.nextInt();
      
    int cnt = 0;
    for (int value : arr) {
      if(value == v) {
        cnt++;
      }
    }
    System.out.println(cnt);
    sc.close();
  }
}
```

**BufferedReader/StringTokenizer**
```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException{
      
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
      
      int N = Integer.parseInt(br.readLine());
      StringTokenizer st = new StringTokenizer(br.readLine());
      int v = Integer.parseInt(br.readLine());
      
      int cnt = 0;
      while(st.hasMoreTokens()) {
        if(Integer.parseInt(st.nextToken()) == v) {
          cnt++;
        }
      }
      System.out.println(cnt);
    }
    bw.close()
    br.close()
}
```

## hasMoreTokens()
StringTokenizer 클래스 객체에서 다음에 읽어 들일 token이 있으면 true, 없으면 false를 return한다. while문에서 사용할 경우 반복하는 동안 실행될 nextToken()에 읽어들일 값이 없을 때까지 반복할 수 있다.

**코드 동작 원리**
1. new StringTokenizer: 값을 구분자를 기준으로 입력받는다. 
2. hasMoreTokens: StringTokenizer에 더 이상 처리되지 않은 토큰이 있는지를 확인한다.
3. nextToken: 다음 토큰을 추출하고 문자열에서 제거한다.
4. Integer.parseInt: 추출한 토큰을 정수로 변환한다.
5. 값을 비교한 후 cnt 변수를 증가시킨다.

