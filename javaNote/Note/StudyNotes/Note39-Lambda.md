# Lambda
---
# 익명클래스와 람다
추상메서드가 한 개 있는 인터페이스를 `Fucntional Interface`라고 한다.
- 이런 경우에 람다 문법을 사용할 수 있다.
- 인터페이스는 static을 붙이지 않아도 static 멤버가 사용할 수 있다.
```
public class Exam {
  interface Player {
    void play();
  }

  public static void main(String[] args) {
      // 익명 클래스로 인터페이스 구현
      Player p1 = new Player() {
        @Override
        public void play() {
          System.out.println("익명 클래스");
        }
      };
      p1.play();

      // 람다 문법으로 인터페이스 구현하기
      Player p2 = () -> System.out.println("익명 클래스");
      p2.play();
    }
}
  
```
>람다 문법은 메서드 한 개짜리 인터페이스를 좀 더 간결하게 구현하기 위해 만든 문법이다.

## Functional Interface
>1. 추상메서드
2. exam210~exam240

### 익명클래스와 .class파일
- 자바의 nested class는 모두 별도의 .class 파일을 갖는다.

### 람다와 .class파일
- 람다는 별도의 .class 파일을 생성하지 않는다.
- 컴파일러는 람다 코드를 해당 클래스의 스태틱 메서드로 정의한다.

## 익명클래스-람다 작성 순서
인터페이스
```
public class Exam {
  interface Player {
    void play();
  }
```

1. 익명클래스 선언
```
  public static void main(String[] args) {
    Player p = new Player() {};
  }
```

2. 바디 구현
```
  public static void main(String[] args) {
    Player p = new Player() {
      public void play() {
        System.out.println("Hi");
      }
    };
  }
```

3. 람다 변환
- new 감싸는 블록 삭제
- 메서드명 삭제
- () 옆에 -> 생성
- 메서드 바디가 하나라면 중괄호랑 쉼표도 삭제
- 정리
```
  public static void main(String[] args) {
    Player p = () -> System.out.println("Hi");
  }
```

# 람다 문법
## 람다 파라미터
```
public class Exam {

  interface Player {
    void play(String name);
  }

  public static void main(String[] args) {
    // 1) 파라미터는 괄호() 안에 선언한다.
    Player p1 = (String name) -> System.out.println(name);
    p1.play("홍길동");

    // 2) 파라미터 타입을 생략할 수 있다.
    Player p2 = (name) -> System.out.println(name);
    p2.play("홍길동");

    // 3) 파라미터가 한 개일 때는 괄호도 생략할 수 있다.
    //    파라미터가 두 개 이상일 경우 괄호 생략 불가.
    Player p3 = name -> System.out.println(name);
    p3.play("홍길동");
  }
}
```

## 람다 리턴
```
public class Exam {

  interface Calculator {
    int compute(int a, int b);
  }

  public static void main(String[] args) {

    // 표현식이 하나일 때 중괄호 생략이 가능하다. 리턴 문장일 경우에는 return 키워드도 생략해야 한다. 
    Calculator c0 = (a, b) -> a * b;
    System.out.println(c0.compute(100, 200));
  }
}
```

## 아규먼트 람다 활용
- 딱 한번만 생성할 객체에 대해 클래스를 생성한다면 로컬클래스가 아닌 익명클래스를 활용한다.
- 만약 생성한 객체를 한번만 사용한다면 파라미터에 아규먼트로 바로 익명클래스를 생성한다.
- 아규먼트 자리에 익명클래스를 정의하는 코드를 두면 코드를 해석하기가 편하다.
- 람다를 활용하여 바꾸면 더 가독성이 좋아진다.

# 스태틱 메서드 레퍼런스
인터페이스의 메서드 규격(파라미터/리턴타입)과 일치하는 메서드가 있다면, 그 메서드를 람다 구현체로 대체할 수 있다.  
=> 새로 코드를 작성할 필요가 없어 편리하다.
```
public class Exam {
  static class MyCalculator {
    public static int plus(int a, int b) {return a + b;}
    public static int minus(int a, int b) {return a - b;}
    public static int multiple(int a, int b) {return a * b;}
    public static int divide(int a, int b) {return a / b;}
  }

  interface Calculator {
    int compute(int x, int y);
  }

  public static void main(String[] args) {
    Calculator c1 = MyCalculator::plus;
    // 컴파일러는 위 코드를 다음의 코드로 변환한다.
//  Calculator c1 = (x, y) -> MyCalculator.plus(x,y);

    System.out.println(c1.compute(200, 17));
  }
}
```

## 스태틱 메서드 레퍼런스 작성 순서
변경 전
```
Calculator c1 = (x, y) -> x + y;
```

1. 기존 메서드 호출
```
Calculator c1 = (x, y) -> MyCalculator.plus(x,y);
```

2. 기존 작성한 클래스의 스태틱 메서드를 람다 대신 사용
```
Calculator c1 = MyCalculator::plus;
```

## 스태틱 메서드 레퍼런스 조건/규격
인터페이스에 정의된 메서드가 호출되었을 때 
>그 파라미터 값은 메서드 레퍼런스로 지정된 스태틱 메서드에 전달될 것이므로 스태틱 메서드 파라미터는 인터페이스 메서드에 정의된 파라미터 값을 받을 수 있어야 한다.  
>스태틱 메서드의 리턴 타입도 인터페이스 리턴 타입과 일치하거나 그 타입으로 바꿀 수 있어야 한다.

# 인스턴스 메서드 레퍼런스
스태틱 메서드 레퍼런스처럼 메서드 한 개 짜리 인터페이스 구현체를 만들 때 규격이 일치하면 기존 인스턴스 메서드를 람다 구현체로 사용할 수 있다.
```
public class Exam {
  static class Calculator {
    double rate;

    public Calculator(double rate) {
      this.rate = rate;
    }

    public double year(int money) {
      return money * rate / 100;
    }

  static interface Interest {
    double compute(int money);
  }

  public static void main(String[] args) {
    Calculator 보통예금 = new Calculator(0.5);
    Interest i1 = 보통예금::year;

    System.out.printf("년 이자: %.1f\n", i1.compute(10_0000_0000));
  }
}
```

## 메서드 레퍼런스 활용 예
Predicate 인터페이스는 입력값 하나를 받아서 `true` 또는  `false`를 반환하는 test(T t)를 가지고 있다.
1) 로컬 클래스로 인터페이스 구현체 만들기
```
public class Exam {

  public static void main(String[] args) {
    
    class MyPredicate<T> implements Predicate<T> {
      @Override
      public boolean test(T value) {
        return ((String)value).isEmpty();
      }
    }
    Predicate<String> p1 = new MyPredicate<>();
    System.out.println(p1.test("")); // true
    System.out.println(p1.test("Hello!")); // false
  }
}
```

2) 익명 클래스로 인터페이스 구현체 만들기
```
public class Exam {

  public static void main(String[] args) {
    
    Predicate<String> p2 = new Predicate {
      @Override
      public boolean test(String value) {
        return value.isEmpty();
      }
    };

    System.out.println(p1.test("")); // true
    System.out.println(p1.test("Hello!")); // false
  }
}
```

3) 람다로 인터페이스 구현체 만들기
```
public class Exam {

  public static void main(String[] args) {
    
    Predicate<String> p2 = (String value) -> value.isEmpty();

    System.out.println(p1.test("")); // true
    System.out.println(p1.test("Hello!")); // false
  }
}
```

4) 메서드 레퍼런스를 사용하여 기존 클래스의 메서드를 인터페이스 구현체로 사용하기
```
public class Exam {

  public static void main(String[] args) {
    
    Predicate<String> p4 = String::isEmpty;

    System.out.println(p1.test("")); // true
    System.out.println(p1.test("Hello!")); // false
  }
}
```
- 메서드 레퍼런스 사용 조건/규격에 따르면
- isEmpty()는 스태틱 메서드가 아니라 스태틱 메서드처럼 호출할 수 없다.
- isEmpty()는 파라미터가 없기 때문에 String값을 받을 수 없다.
> 되는 이유?
> 타입 파라미터의 클래스가 인스턴스 메서드의 클래스랑 같으면 아래 코드와 같이 변경되기 때문에 단축 문법으로 사용할 수 있다.
```
Predicate<String> p4 = (String value) -> { return value.isEmpty(); };
```


# 생성자 레퍼런스
인터페이스에 정의된 메서드가 생성자의 형식과 일치하다면 메서드 레퍼런스로 생성자를 지정할 수 있다. 
```
public class Exam {

  interface ListFactory {
    List create();
  }

  public static void main(String[] args) {
    // 인터페이스 구현
    ListFactory f1 = ArrayList::new;

    // 인터페이스의 메서드를 호출하면
    // 지정된 클래스의 인스턴스를 만든 후 생성자를 호출한다.
    List list = f1.create(); 

    System.out.println(list instanceof ArrayList);
    System.out.println(list.getClass().getName());
  }
}
```

생성자 레퍼런스를 지정할 때 인터페이스 파라미터에 따라 호출할 생성자가 결정된다. 
```
public class Exam {

  static class Message {
    String name;

    public Message() {
      this.name = "이름없음";
    }

    public Message(String name) {
      this.name = name;
    }

    public void print() {
      System.out.printf("%s님 반갑습니다!\n", name);
    }
  }

  static interface Factory1 {
    Message get();
  }

  static interface Factory2 {
    Message get(String name);
  }

  public static void main(String[] args) {
    Factory1 f1 = Message::new; // Message() 생성자를 가리킨다.

    Factory2 f2 = Message::new; // Message(String) 생성자를 가리킨다.

    Message msg = f1.get(); 
    msg.print();

    msg = f2.get("홍길동");
    msg.print();
  }
}
```

# forEach()
Iterable 인터페이스의 `Default Method` 중 하나로, 컬렉션을 순회하며 각 요소에 대해 주어진 명령을 실행한다. 따라서 반복문을 사용하지 않고도 컬렉션의 각 요소를 간결하게 처리할 수 있다.
```
public class Exam {

  public static void main(String[] args) {
    ArrayList<String> names = new ArrayList<>();
    names.add("홍길동");
    names.add("임꺽정");

    /* 로컬클래스 사용
    class MyConsumer<T> implements Consumer<T> {
      @Override
      public void accept(T item) {
        System.out.println(item);
      }
    }
    names.forEach(new MyConsumer<String>());
    */

    /* 파라미터로 익명클래스 사용
    names.forEach(new Consumer<String>() {
      @Override
      public void accept(String item) {
        System.out.println(item);
      }
    });
    */

    /* 람다 문법 사용
    names.forEach(item -> System.out.println(item));
    */

    /* 메서드 레퍼런스 사용
    names.forEach(System.out::println);
    */
  }
}

```