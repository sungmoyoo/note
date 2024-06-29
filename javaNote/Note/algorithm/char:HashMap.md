# 백준: 단어 공부(1157번)
알파벳 대소문자로 된 단어가 주어지면, 이 단어에서 가장 많이 사용된 알파벳이 무엇인지 알아내는 프로그램을 작성하시오. 단, 대문자와 소문자를 구분하지 않는다.

## 풀이 과정
알파벳 26개의 유니코드를 인덱스로 사용하여 푸는 방법도 생각했지만 키와 값을 저장하는 HashMap도 사용 가능할 것이라고 생각했다. 

HashMap에 대한 학습이 부족했기에 API 문서와 블로그를 참고하여 HashMap이 가진 메서드, 활용법을 찾아보았다. 

## toUpperCase(), toLowerCase()
String 클래스에서 제공하는 메서드로 문자열의 대소문자를 변환하는데 사용된다. 
```
// toUpperCase() => 문자열의 모든 문자를 대문자로 변환한다.
String input = "Hello World"
String upperCase = input.toUpperCase();
System.out.println(upperCase);
=> HELLO WORLD

String input = "Hello World"
String lowerCase = input.toLowerCase();
System.out.println(lowerCase);
=> hello world
```

## HashMap : put(K key, V value)
특정 키와 값을 HashMap에 추가하는 메서드, 만약 키가 존재한다면 기존 값은 새로운 값으로 대체되고 기존 값을 리턴한다. 기존 값이 없었다면 `null`을 리턴한다. 
```
HashMap<String, Integer> map = new HashMap<>();
Integer oldValue = map.put("one", 1);
System.out.println(oldValue)
=> null
```

## HashMap : get(Object key)
주어진 키에 해당하는 값을 리턴한다. 만약 키가 존재하지 않는다면 `null`을 리턴한다. 
```
Integer value = map.get("one");
System.out.println(value)
=> 1
```

## Map: getOrDefault(Object key, V defaultValue)
getOrDefault()는 Map 인터페이스의 메서드로 기존 키에 해당하는 값이 있으면 해당 값을 리턴하고 없으면 디폴트 값으로 설정된 값이 리턴된다. 
```
Integer value = map.getOrDefault("two", 0);
System.out.println(value)
=> 0
```

## Map: keySet()
Map의 모든 키를 가지는 집합 `Set`을 리턴한다. 

## Map: entrySet()
entrySet()은 해당 Map의 키-값을 가진 Map.Entry 객체로 이루어진 집합 `Set`을 리턴한다. 
즉 키와 값을 인스턴스로 가지는 객체집합에 담아준다. 보통 Map 의 키와 값을 전체 출력할 때 사용한다. 

## Map.Entry
Map.Entry 객체는 Map에 대한 키와 값 쌍을 하나의 객체로 갖고 있어 getKey(), getValue() 메서드로 키와 값에 접근할 수 있다. 또한 setValue(value)를 통해 Map.Entry 객체의 key의 대한 value값을 쉽게 수정할 수 있다. 

## 문제 풀이
```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class Main {
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    // 알파벳을 key, 개수를 value로 저장하기 위해 HashMap 사용
    Map<Character, Integer> countMap = new HashMap<>();

    // 받은 문자를 전부 대문자로 변환
    String word = br.readLine().toUpperCase();

    // 문자열에서 알파벳 개수 세기
    for (char c : word.toCharArray()) {
      countMap.put(c, countMap.getOrDefault(c, 0) + 1);
    }

    Character maxKey = null;
    int maxCount = Integer.MIN_VALUE;

    // 최댓값 탐색, Map.Entry Set을 foreach 반복문으로 전체 순회
    for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
      if (entry.getValue() > maxCount) {
        maxKey = entry.getKey();
        maxCount = entry.getValue();

    // 2개 이상 최댓값이 존재하면 물음표 출력
      } else if (entry.getValue() == maxCount) {
        maxKey = '?';
      }
    }

    bw.write(maxKey + "\n");

    bw.close();
    br.close();

  }
}
```

## keySet() vs entrySet()
keySet() 메서드는 키 값에 대한 집합만 리턴하기 때문에 주로 키만 필요한 경우에는 keySet이 더 간결하게 사용될 수 있다. 만약 값이 필요한 경우에는 get() 메서드를 사용하여 두 번을 검색해야 하기 때문에 효율성이 떨어진다. 

그에 비해 entrySet()을 사용하면 Map.Entry 형태로 키와 값을 모두 담은 객체를 가져오기 때문에 한번에 검색할 수 있고, getValue(), getKey() 메서드를 통해 처리 및 출력의 일관성, 가독성을 높일 수 있다.

예시로 키만 출력하는 경우, 키와 값을 모두 출력하는 경우를 보면 이해가 쉽다. 
```Java
Map<String, String> map = new HashMap<>();

// 키만 출력하는 경우        
for (String key : map.keySet()) {
    System.out.println("key : " + key);
}

// 키와 값을 모두 출력하는 경우
// - keySet()
for (String key : map.keySet()) {
    System.out.println("key : " + key);
    System.out.println("value : " + map.get(key));
}

// - entrySet()
for (Map.Entry<String, String> entry : map.entrySet()) {
    System.out.println("key : " + entry.getKey());
    System.out.println("value : " + entry.getValue());
}
```









