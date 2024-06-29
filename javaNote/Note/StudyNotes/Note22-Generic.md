# 제네릭

- 레퍼런스를 선언하는 시점에 지정된 타입이 아닌 값을 넣으려고 하면
- 컴파일 오류가 발생한다.
- 즉 입력받고 리턴하는 데이터 타입을 특정 타입만 사용하도록 제한할 수 있는 문법이 제네릭(generic)

```
리턴타입을 <datatype>을 통해 변수 선언 시 리턴타입 오른쪽에 지정 
타입 파라미터라고도 한다 => 타입 이름을 받는 변수
```

## Java17 Collection API
앞의 실습에서 구현한 이 메소드들은 java.lang 패키지에 있는 Arraylist 클래스의 메서드들이다.
- ArrayList  
  - .get()  
  - .add()  
  - .remove()
  - .set()
  - .toArray()
- System
  - arraycopy
- Arrays
  - copyOf





