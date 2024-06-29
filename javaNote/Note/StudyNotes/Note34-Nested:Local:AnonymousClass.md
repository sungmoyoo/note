# inner class(non-static 중첩 클래스)
---
## 클래스 정의와 인스턴스 생성
컴파일러는 inner 클래스를 컴파일 할 때 다음과 같이
- 바깥 클래스의 인스턴스 주소를 저장할 필드를 추가하고,
- 바깥 클래스의 인스턴스의 주소를 파라미터로 받는 생성자를 만든다.
**레퍼런스선언**
"바깥클래스.중첩클래스.레퍼런스명"????????????????
- 스태틱, 인스턴스 클래스 모두 동일

**인스턴스 생성**
- 스태틱 멤버를 사용할 때는 인스턴스 생성이 없어도 가능하다.
- 인스턴스 멤버를 사용할 때는 인스턴스 생성 후 객체 주소를 통해 사용해야 한다. 

## 접근
- 바깥 클래스의 스태틱 멤버는 "클래스명.멤버" 로 접근 가능
- 바깥 클래스의 인스턴스 멤버는 바깥 객체의 주소를 저장할 빌트인 변수 this와 바깥 객체의 주소를 받는 생성자가 자동으로 생성되기 때문에 "바깥클래스명.this.멤버"로 접근할 수 있다.

## 응용
중첩 클래스가 바깥 클래스 객체를 사용해야 한다면 스태틱 중첩 클래스말고 논스태틱 중첩 클래스로 만드는 것이 좋다.  
논스태틱 중첩 클래스는 인스턴스를 저장할 필드와 바깥 클래스의 인스턴스를 받는 생성자/파라미터가 자동으로 생성되기 때문이다.

**스태틱 중첩 클래스**
```
public class Exam {

  public static void main(String[] args) {
    Musics1 m1 = new Musics1();
    m1.add("aaa.mp3");
    m1.add("bbb.mp3");
    m1.add("ccc.mp3");

    Musics1 m2 = new Musics1();
    m2.add("xxx.mp3");
    m2.add("yyy.mp3");

    // Player가 사용할 Musics 객체를 넘기기 위해 
    // 개발자가 직접 해당 생성자를 호출해 줘야 한다. 
    Musics1.Player p1 = new Musics1.Player(m1);
    Musics1.Player p2 = new Musics1.Player(m2);

    p1.play();
    p2.play();
  }
}

class Musics1 {
  List<String> songs = new ArrayList<>();

  public void add(final String song) {
    songs.add(song);
  }

  public void delete(final int index) {
    songs.remove(index);
  }

  // 중첩클래스
  static class Player {
    Musics1 musics;
    public Player(Musics1 musics) {
      this.musics = musics;
    }
    public void play() {
      for (final String song : musics.songs) {
        System.out.println(song);
      }
    }

```

**논스태틱 중첩클래스**
```
public class Exam {

  public static void main(String[] args) {
    Musics2 m1 = new Musics2();
    m1.add("aaa.mp3");
    m1.add("bbb.mp3");
    m1.add("ccc.mp3");

    Musics2 m2 = new Musics2();
    m2.add("xxx.mp3");
    m2.add("yyy.mp3");

    // Player가 사용할 바깥 클래스 Musics2의 객체를 넘길 때는 
    // 다음과 같이 파라미터가 아니라 생성자 호출 문장 앞쪽에 놓는다. 

    Musics2.Player p1 = m1.new Player();
    Musics2.Player p2 = m2.new Player();

class Musics2 {
  List<String> songs = new ArrayList<>();

  public void add(final String song) {
    songs.add(song);
  }

  public void delete(final int index) {
    songs.remove(index);
  }
  public void play() {
      // 내부에 보관된 바깥 클래스의 객체를 사용하고 싶다면,
      // 다음과 같이 '바깥클래스명.this.멤버' 형식으로 접근한다. 
      for (final String song : Musics2.this.songs) {
        System.out.println(song);
      }
      System.out.println("-----------------------------");
    }
  }
}
```

**리팩토링: Method Factory**
바깥 클래스의 인스턴스를 사용하는 inner 클래스라면 inner 클래스의 객체를 만드는 역할도 바깥 클래스가 하는게 유지보수에 더 낫다.
Grasp 설계 기법 중 "Information Expert"를 적용하기 위해 `Method Factory` 디자인 패턴 사용
```
public class Exam0713 {

  public static void main(String[] args) {
    Musics3 m1 = new Musics3();
    m1.add("aaa.mp3");
    m1.add("bbb.mp3");
    m1.add("ccc.mp3");

    Musics3 m2 = new Musics3();
    m2.add("xxx.mp3");
    m2.add("yyy.mp3");

    Musics3.Player p1 = m1.createPlayer();
    Musics3.Player p2 = m2.createPlayer();

    p1.play();
    p2.play();
  }
}

class Musics3 {

  List<String> songs = new ArrayList<>();

  public void add(final String song) {
    songs.add(song);
  }

  public void delete(final int index) {
    songs.remove(index);
  }

  // 객체 생성을 메서드를 통해 하도록 한다.
  public Player createPlayer() {
    return this.new Player(); 
  }

  class Player {
    public void play() {
      for (final String song : Musics3.this.songs) {
        System.out.println(song);
      }
      System.out.println("-----------------------------");
    }
  }
}
```

<br>  </br>

# local class

## 클래스 정의와 인스턴스 생성
메서드 안에 정의하는 클래스를 "local class"라 한다.
- 특정 메서드 안에서만 사용되는 경우 로컬 클래스로 정의한다.
- 쓸데없이 외부로 노출하지 않기 위함.
- 노출을 줄이면 유지보수에 좋다.
- 로컬 클래스에서 로컬 이라는 말은 '이 메서드 안에서만 사용할 수 있다'는 뜻이다.
- 그냥 사용 범위에 대한 제한을 가리키는 말이다.
- 메서드를 호출할 때 클래스가 정의된다는 뜻이 아니다.
- 컴파일이 끝나면 .class파일로 추출된다.  

## 사용 범위
다른 메서드에 정의된 클래스는 사용할 수 없다.

## .class 파일명
컴파일이 끝난 후 로컬클래스의 .class 파일명은 다음과 같다.
=> "[바깥클래스명]$[정의된순서][로컬클래스명].class" 
```
B2$1X.class
```

## 인스턴스/스태틱 메서드와 로컬클래스
인스턴스 메서드:
- 인스턴스 메서드 안에 정의된 로컬 클래스는 바깥 클래스의 인스턴스를 사용할 수 있다.
- .class 파일을 확인해보면 inner class와 마찬가지로 바깥 클래스의 인스턴스 주소를 저장하는 필드와 주소를 받는 생성자가 선언되어 있다.

스태틱 메서드:
- 스태틱 메서드는 인스턴스 주소를 저장할 this라는 변수가 없다.
- 그래서 바깥 클래스의 인스턴스를 사용할 수 없다.
- .class 파일을 확인해도 this 필드와 생성자가 선언되어 있지 않다. 

## 바깥 메서드의 로컬 변수 접근
로컬 클래스에서 메서드의 파라미터 값을 사용한다면,컴파일러는 로컬 클래스를 컴파일 할 때 파라미터 값을 저장할 수 있도록 관련 코드를 자동으로 추가한다.  
**=> 따라서 개발자가 직접 파라미터로 전달된 값을 저장할 필드와 파라미터를 받는 생성자 코드를 작성할 필요가 없다.**


```
interface Calculator {
  double compute(int money);
}

class CalculatorFactory {
  static Calculator create1(float interest) {
    class CalculatorImpl implements Calculator {
      float interest; //이율을 저장할 필드
      
      CalculatorImpl(float interest) { // 생성자가 호출될 때 이율을 받는다.
        this.interest = interest;
      }
      @Override
      public double compute(int money) {
        return money + (money * interest);
      
      }
    }
    return new CalculatorImpl(interest);
  }

  static Calculator create2(float interest) {

    class CalculatorImpl implements Calculator {
      @Override
      public double compute(int money) {
        return money + (money * interest);
        }
    }

    return new CalculatorImpl(); // 컴파일러가 new Calculator
  }
}

public class Exam {

  public static void main(String[] args) {
    Calculator c1 = CalculatorFactory.create1(0.02f);
    Calculator c2 = CalculatorFactory.create2(0.02f);

    System.out.printf("%.2f\n", c1.compute(1235_0000));
    System.out.printf("%.2f\n", c2.compute(1235_0000));
  }
}
```
바깥 메서드의 로컬 변수는 메소드 호출이 완료되는 순간 스택 메모리에서 제거되기 때문에 로컬 클래스의 객체에서 사용할 수 없을 것이다.  
그래서 컴파일러는 바깥 메서드의 로컬 변수 값을 저장할 필드와 파리미터를 받는 생성자를 클래스에 추가한다.  
따라서 CalculatorImpl 객체를 생성하여 리턴한 후 interest 변수는 컴파일러가 생성한 this 변수에 생성자를 통해 저장된다.

## 바깥 메서드의 로컬 변수 접근
- final로 선언된 경우 접근 가능
- final은 아니지만 단 한번만 선언된 경우 접근 가능
- 값을 두 번 이상 할당한 경우 접근 불가
**=> 변경되기 전/후 값 중 어떤 값을 사용해야 할지 혼란스러운 상황이 발생할 수 있기 때문에 enclosing method를 사용할 때 해당 변수를 변경하지 않는 경우에만 허락하도록 규칙이 정해져 있다.**

<br>  </br>

# anonymous class
## 인터페이스를 구현한 익명클래스 정의
- 인터페이스의 경우 static으로 선언하지 않아도 스태틱 멤버에서 사용할 수 있다. 
- 규칙을 정의한 것이기 때문에 인스턴스 멤버라는 개념이 존재하지 않는다.
- 바깥 클래스(outer class)의 인스턴스 주소를 저장할 필드를 추가할 수 없다.

```
interface A {
    void print();
  }

  class X {}

  static class Y {}

  public static void main(final String[] args) {

    // 1) 로컬 클래스로 인터페이스 구현하기
    class My implements A {
      String name = "홍길동";

      @Override
      public void print() {
        System.out.printf("Hello, %s!\n", this.name);
      }
    }

    A obj = new My();
    obj.print();
    
    // 2) 익명 클래스로 인터페이스 구현하기

```

## 익명 클래스를 정의하는 과정
1단계: 로컬 클래스로 정의  
2단계: 클래스의 이름을 지운다.
3단계: 클래스 이름이 없기 때문에 'class', 'implements' 키워드는 없앤다.
4단계: 클래스 이름이 없기 때문에 바로 new 키워드를 이름 앞에 둔다.(인스턴스 정의와 동시에 생성)
5단계: 익명클래스의 생성자가 없기 때문에 수퍼 클래스의 생성자를 호출, => 생성자를 호출하도록 ()를 붙인다.

## 클래스를 상속받은 익명 클래스 정의 - 생성자
익명 클래스의 객체를 만들 때 호출하는 생성자는 수퍼클래스에 존재하는 생성자여야 한다.  
쉽게 말해 컴파일러는 호출하는 수퍼클래스의 생성자와 동일한 파라미터를 갖는 생성자를 익명 클래스에 추가한다.
```
```
static class A {
    String message;
    int value;

    A(String message) {
      this.message = message;
    }

    A(String message, int value) {
      this.message = message;
      this.value = value;
    }

    void print() {
      System.out.printf("A의 print(): %s, %d\n", this.message, this.value);
    }
  }
}
public static void main(final String[] args) {
    A obj2 = new A("오호라!", 100) {
    };
    obj2.print();
  }
}

### 수퍼클래스와 인터페이스를 동시에 지정할 수 있을까?'
둘 중 하나만 상속 받거나 구현해야지, 동시에 다 할 수 없다.

### 여러 개의 인터페이스를 구현할 수 있을까?
안된다. 익명 클래스에는 그런 문법은 없다.

## 익명클래스 생성자
1) 익명 클래스는 생성자를 직접 정의할 수 없다.
- 그러나 컴파일러가 컴파일할 때 익명 클래스의 생성자를 만든다.
      
2) 대신 인스턴스 블록으로 생성자를 대신한다. 수퍼클래스의 생성자를 지정한다는 의미이다.
- 인스턴스 블록에 작성한 코드는 결국 컴파일러가 자동 생성한 생성자에 들어간다.
- 그래서 인스턴스 블록에 작성한 코드가 실행될 것이다.

컴파일러는 익명 클래스의 생성자를 다음과 같이 추가할 것이다.

## 익명클래스가 놓이는 장소

## 익명클래스가 놓이는 장소 - 리턴
리턴값으로 인터페이스 익명클래스가 놓이는 경우 람다 문법, 스태틱 레퍼런스 등으로 리팩토링할 수 있다.
```
class My {
  static void m1() {
    System.out.println("오호라!!!!");
  }

  void m2() {
    System.out.println("와우~~~~!");
  }
}

public class Exam0450 {
  // 인터페이스의 경우 static으로 선언하지 않아도 스태틱 멤버에서 사용할 수 있다.
  interface A {
    void print();
  }

// 1) 일반 문장

static A create2() {
    return new A() {
      @Override
      public void print() {
        System.out.println("Hello2!");
      }
    };
  }

2) 

static A create3() {
    return () -> System.out.println("Hello3!");
  }

3) 

static A create4() {
    return My::m1;
  }
// 컴파일러는 위의 문장을 다음과 같이 바꾼다.
// return () -> My.m1();


4) 

static A create5() {
    return new My()::m2;
  }
// 컴파일러는 위의 문장을 다음과 같이 바꾼다.
// return () -> new My.m2();
```