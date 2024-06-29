HashSet

numberFormatException?
```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    int N = Integer.parseInt(br.readLine());
    int count = 0;

    for (int n = 0; n < N; n++) {
      Set<Character> charSet = new HashSet<>();
      String word = br.readLine();
      boolean isGroup = true;


      for (int i = 0; i < word.length(); i++) {
        char pointer = word.charAt(i);

        if (charSet.contains(pointer)) {
          isGroup = false;
        }

        charSet.add(pointer);

        if (i < word.length() - 1 && pointer != word.charAt(i+1)) {
          charSet.clear();
        }
      }

      if (isGroup) {
        count++;
      }
    }

    bw.write(count + "\n");

    bw.close();
    br.close();

  }
}
```