# I18N(국제화)와 L10N(지역화)
---
1. **이전 방식**
```java
System.out.printf("제목: %s\n", title);
// 출력 내용이 특정 언어로 고정 ex(제목)
```

다른 언어를 지원하기 위해서는 그 언어에 맞춰서 코드를 다시 짜야 한다.

2. **다국어 지원**
```java
System.out.printf("%s: %s\n", prop.get("title"), title);
// (%s:) = 사용자가 선호하는 언어로 라벨을 출력
//(prop.get("title")) = *.properties, *.xml 등 파일에서 특정 언어로 작성된 라벨을 읽어온다.
```
- 다른 언어로 라벨을 교체할 수 있도록 프로그래밍을 하는 것을 `I18N 지원` 이라 부른다.  

- 특정 국가의 특정 언어로 라벨을 작성하는 것을 `L10N 지원` 이라 부른다. (지역화는 프로그래밍이 아니다. 파일 작성이다.)

---

<br><br>

# 제네릭(Generic)
제네릭은 프로그래밍에서 특정한 타입에 종속되지 않고 유연한 코드를 작성할 수 있게 해주는 문법이다. 
**특징**
- 여러 타입을 다루기 위해 중복 코드를 작성할 필요가 없다. 즉 재사용성을 높인다.
- 타입 정보가 자동으로 바뀌어 형변환하는 번거로움을 없앤다.
- 다양한 타입을 지정하고 해당 타입으로 제한하여 잘못된 타입을 입력하지 않도록 하여 타입 안전성을 보장해준다.

## 메서드에 사용
제네릭을 이용하면 여러 타입을 모두 수용할 수 있는 메서드를 만들 수 있다.  
`Object`의 다형적 변수를 이용한 방법과 타입별로 메서드를 만드는 방법의 장점만 모두 취할 수 있다.(다형성, 형변환x)

- <다루는_타입_별명> 다루는_타입_별명 메서드명(다루는_타입_별명 파라미터, ...) {...}  
- 예) <What> What test(What obj) {...}
> `What`을 가리키는 용어로 "타입 파라미터"라고 부른다.

### 제네릭의 타입 파라미터로 많이 사용하는 이름
보통 긴 이름을 사용하지 않고 다음과 같이 한 개의 대문자로 된 이름을 많이 사용한다.

>T - Type이라는 의미를 표현할 수 있어 많이 사용하는 이름이다.
E - Element라는 의미로 목록의 항목을 가리킬 때 사용한다.
K - Key 객체를 가리킬 때 사용한다.
N - Number의 의미로 숫자 타입을 가리킬 때 주로 사용한다.
V - Value의 의미로 값의 타입을 가리킬 때 사용한다.
S,U,V 등 - 한 번에 여러 타입을 가리킬 때 두 번째, 세 번째, 네 번째 이름으로 주로 사용한다.

## 클래스에 사용
`Object`를 이용해 다형적 변수를 사용하면 다양한 객체를 보관할 수 있다. 하지만 특정 타입의 객체로 제한할 수 없고 값을 꺼낼 때마다 형변환을 해주어야 하는 불편함이 있다.  
>범용 클래스 전문 제작 문법인 제네릭을 클래스에 사용하면 한 개의 클래스가 다양한 타입의 객체를 제한적으로 다룰 수 있다.

- 클래스 전체에서 사용할 "타입 파라미터(타입 이름을 저장하는 변수)"를 선언
- class 클래스명<타입파라미터명, 타입파라미터명, ...> {...}
- 타입 파라미터로 T를 설정했을 때, T가 어떤 타입인지는 Box 객체를 생성할 때 결정된다.
- 레퍼런스 변수를 선언할 때 어떤 타입을 다룰지 지정하면 new를 실행할 때는 타입 생략 가능
```java
Box<String> b = new Box<>(); 
```

**컴파일러**
>제네릭 문법은 컴파일 할때 코드를 검사하는 용도로 사용된다. JVM은 제네릭 문법을 모르고, 컴파일러가 담당한다. 

**클래스 생성**
>제네릭을 사용한 클래스가 생성될 때 여러 타입의 클래스가 생성되는 것이 아니라 하나만 생성된다. 클래스가 생성될 때 내부적으로는 타입파라미터가 Object로 변환되어 생성된다.

## 여러개의 타입 파라미터
```java
// 여러 개의 타입 파라미터를 지정하기
class A<T,S,U> {
  T v1;
  S v2;
  U v3;
}
public class Exam {

  public static void main(String[] args) {
    A<String,Integer,Member> obj = new A<>();

    obj.v1 = new String("Hello");
    obj.v2 = Integer.valueOf(100);
    obj.v3 = new Member("홍길동", 20);
  }
}
```

## 제네릭으로 배열 만들기
1) 제네릭의 타입 파라미터로 레퍼런스 배열을 생성할 수 없다.
```java
static <T> T[] create1() {
    T[] arr;
    //    arr = new T[10]; // 컴파일 오류!
    return null;
  }
```

2) 견본 배열을 받아서 복제하는 방법
내부에서 생성할 배열 크기보다 더 큰 배열을 넘기면 copyOf()로 새 크기에 맞춰 새 배열을 생성한다. 
```java
static <T> T[] create2(T[] arr) {
    return Arrays.copyOf(arr, 10);
  }
```

3) 배열의 타입 정보를 받아 생성
```java
@SuppressWarnings("unchecked")
  // => 생성할 배열의 타입 정보를 파라미터로 넘기면, 
  // ex) String[] arr3 = create3(String.class);
  static <T> T[] create3(Class<?> type) {
    return (T[]) Array.newInstance(type, 10);
  }   // => newInstance()로 새 배열 생성하여 리턴, 리턴값이 Object이므로 타입 파라미터로 형변환
```

4) 견본 배열에서 타입 정보를 추출하여 배열을 생성하기
```java
  // 배열을 넘기면 배열의 항목 타입을 알아내어 새 배열을 만드는 방법.
@SuppressWarnings("unchecked")
  static <T> T[] create4(T[] arr) {
    Class<?> arrayTypeInfo = arr.getClass(); // 결과값 -> String[]
    // System.out.println(arrayTypeInfo); // 결과출력

    Class<?> arrayItemTypeInfo = arrayTypeInfo.getComponentType(); // 결과값 -> String
    // System.out.println(arrayItemTypeInfo); // 결과출력

    return (T[]) Array.newInstance(arrayItemTypeInfo, 10);
    // getComponentType()으로 알아낸 타입 정보를 newInstance의 파라미터로 넘겨 새 배열 생성
  }
```

## 파라미터 타입 레퍼런스와 인스턴스
제네릭에서는 수퍼클래스가 서브클래스의 객체주소를 담을 수 없다.
```java
public class Exam {

  static class A {}
  static class B1 extends A {}
  static class B2 extends A {}
  static class C extends B1 {}

  static class Box<T> {
    void set(T obj) {}
  }

  public static void main(String[] args) {
    Box<B1> box1;

    //  box1 = new Box<Object>(); // 컴파일 오류!
    //  box1 = new Box<A>(); // 컴파일 오류!
    box1 = new Box<B1>();
    //  box1 = new Box<C>(); // 컴파일 오류!
  }
}
```

**extends**  
서브클래스의 객체 주소를 다루고 싶다면 extends를 사용해야 한다.
```java
public class Exam {

  static class A {}
  static class B1 extends A {}
  static class B2 extends A {}
  static class C extends B1 {}

  static class Box<T> {
    void set(T obj) {}
  }

  public static void main(String[] args) {
    Box<? extends B1> box1; 
    //  어떤 타입인지 모르겠지만 B1이거나 B1의 자식이라는 뜻

    //  box1 = new Box<Object>(); // 컴파일 오류!
    //  box1 = new Box<A>(); // 컴파일 오류!
    box1 = new Box<B1>();
    box1 = new Box<C>(); 
  }
}
```

**super**
수퍼클래스의 타입을 다룰 수 있다.
```java
public class Exam {

  static class A {}
  static class B1 extends A {}
  static class B2 extends A {}
  static class C extends B1 {}

  static class Box<T> {
    void set(T obj) {}
  }

  public static void main(String[] args) {
    Box<? super B1> box1; 
    //  어떤 타입인지 모르겠지만 B1이거나 B1의 부모라는 뜻

    box1 = new Box<Object>();
    box1 = new Box<A>(); 
    box1 = new Box<B1>();
    // box1 = new Box<C>(); // 컴파일 오류!
  }
}
```

## 제네릭 레퍼런스/인스턴스
레퍼런스와 객체 생성 시 제네릭을 사용한 경우

- 클래스명<타입명> = new 클래스명<>();
```java
ArrayList<Member> list = new ArrayList<Member>();
```
- 레퍼런스 선언에 제네릭 정보가 있다면 new에서는 생략 가능
```java
ArrayList<Member> list = new ArrayList<>();
```
- 레퍼런스 변수를 선언할 때는 생략 불가
```java
// ArrayList<> list = new ArrayList<Member>(); // 컴파일 오류!
```

- 레퍼런스를 선언할 때 제네릭 타입을 지정하지 않으면 객체 생성 시 어떤 제네릭 타입을 지정하더라도 상관없다.
```java
ArrayList list1; 

list1 = new ArrayList<Member>();
list1 = new ArrayList<String>();
list1 = new ArrayList<>();

list1.add(new String());
list1.add(new Member("홍길동", 20))
// 레퍼런스 선언할 때 지정하지 않으면 객체 생성 시 지정한 타입은 무시된다.
```

- 레퍼런스를 선언할 때 제네릭 타입을 <?>로 선언하면 객체 생성 시 어떤 제네릭 타입을 지정하더라도 상관없다. 
```java
ArrayList<?> list2; 

list2 = new ArrayList<Member>();
list2 = new ArrayList<String>();
list2 = new ArrayList<>();

// list1.add(new String()); // 컴파일 오류!
// list1.add(new Member("홍길동", 20)) // 컴파일 오류!
```
>레퍼런스 선언할 때 제네릭 타입을 ? 로 설정했기 때문에 add() 메서드의 파라미터 타입은 ? 가 된다. 
>즉 파라미터 타입이 뭔지 정확하게 설정되지 않았기 때문에 컴파일러는 문법의 유효여부를 검사할 수 없다. 


## 제네릭 파라미터
메서드의 파라미터로 제네릭을 적용한 경우

**메서드명(ArrayList) - 제네릭 타입 지정 X**
- 레퍼런스를 선언할 때 제네릭 타입을 지정하지 않으면 제네릭 타입에 상관없이 객체를 넘길 수 있다.
- 다만 이렇게 선언할 시 타입 오류를 잡기 위한 제네릭의 의미가 퇴색된다.
```java
public class Exam {
  static class A {}  static class B1 extends A {}
  static class B2 extends A {}
  static class C extends B1 {}

  public static void main(String[] args) {
    m1(new ArrayList<A>);
    m1(new ArrayList<C>);
  }

  static void m1(ArrayList list) {
    list.add(new A());
    list.add(new C());
  }
}
```

**메서드명(ArrayList<?>) - 미확정 와일드카드**
- 모든 타입에 대해 ArrayList 객체를 파라미터로 넘길 수 있다.
- 메서드 내부에서는 타입 검사를 할 수 없기 때문에 add() 메서드처럼 타입 검사가 필요한 코드에서는 컴파일 오류가 발생한다.
```java
public class Exam {
  static class A {}  static class B1 extends A {}
  static class B2 extends A {}
  static class C extends B1 {}

  public static void main(String[] args) {
    m1(new ArrayList<A>);
    m1(new ArrayList<C>);
  }

  static void m1(ArrayList<?> list) {
    // list.add(new A()); // 컴파일 오류!
    // list.add(new C()); // 컴파일 오류!
    Object obj1 = list.get(0); // OK!, 받아주는 객체가 Object이기 때문에 가능
  }
}
```

**메서드명(ArrayList<B1>) - 타입 지정**
- `<B1>` 타입에 대해서만 ArrayList 객체를 넘길 수 있다.
- 서브클래스를 객체로 넘길 수는 없지만 만들어진 객체를 다룰 때는 B1의 하위 타입도 저장하고 다룰 수 있다.
- 단 다른 타입의 값을 다시 꺼낼 때는 `<B1>` 타입으로 형변환해주어야 한다. 
```java
public class Exam {
  static class A {}  static class B1 extends A {}
  static class B2 extends A {}
  static class C extends B1 {}

  public static void main(String[] args) {
    // m1(new ArrayList<A>); // 컴파일 오류!
    m1(new ArrayList<B1>);
    // m1(new ArrayList<C>); // 컴파일 오류!
  }

  static void m1(ArrayList<B1> list) {
    // list.add(new A()); // 컴파일 오류!
    list.add(new B1());
    list.add(new C());

    list.get(1)

    // 값을 꺼낼 때 메서드에 지정된 타입으로 꺼내야 한다.
    for (int i = 0; i < List.size(); i++) {
      B1 temp = list.get(i)
      System.out.println(temp)
    }
  }
}
```
**메서드명(ArrayList<? extends B1> list) - 상한 와일드카드**
- `<B1>`이거나 `<B1>`을 상속받는 타입 중 하나라는 뜻을 가진다.
- `<B1>` 타입 및 그 하위 타입에 대해서 객체를 넘길 수 있다.
```java
public class Exam {
  static class A {}  static class B1 extends A {}
  static class B2 extends A {}
  static class C extends B1 {}

  public static void main(String[] args) {
    // m1(new ArrayList<Object>) // 컴파일 오류!
    m1(new ArrayList<B1>);
    m1(new ArrayList<C>);
  }

  static void m1(ArrayList<? extends B1> list) {
    // list.add(new B1()); // 컴파일 오류!
    // list.add(new C()); // 컴파일 오류!
    Object obj1 = list.get(0);
    B1 obj1 = list.get(0);
    // B1을 상속받는 타입이니 B1 객체로 받을 수 있다.
  }
}
```
**메서드명(ArrayList<? super B1> list) - 하한 와일드카드**
- `<B1>` 이거나 `<B1>` 상위 타입 중 하나라는 의미이다.
- `<B1>` 타입 및 그 상위 타입에 대해서 ArrayList 객체를 파라미터로 넘길 수 있다.
```java
public class Exam {
  static class A {}  static class B1 extends A {}
  static class B2 extends A {}
  static class C extends B1 {}

  public static void main(String[] args) {
    m1(new ArrayList<Object>) 
    m1(new ArrayList<B1>);
    // m1(new ArrayList<C>); // 컴파일 오류!
  }

  static void m1(ArrayList<? super B1> list) {
    // list.add(new A()); // 컴파일 오류!
    list.add(new B1()); // OK
    list.add(new C()); // OK
    // 넘어오는 항목 타입이 최소 B1 이상이기 때문에 타입 검사에서 B1 서브클래스는 오류가 발생하지 않는다. 
  }
}
```

---

# 애노테이션(Annotation)
---
**애노테이션이란?**
- 클래스, 필드, 메서드, 로컬 변수 선언에 붙이는 특별한 주석이다.
- 다른 주석과 달리 컴파일이나 실행할 때 추출할 수 있다.
- 클래스 파일에(.class)에 남길 수 있는 형식을 갖춘 주석.

## 애노테이션 용도
<img src="../img/Annotation.png">

1. 소스코드 생성(Source Code Generation):  
애노테이션을 통해 자동으로 소스파일이나 설정파일을 생성할 수 있다.

2. 컴파일 타임 처리(Compile-Time Processing):
컴파일 타임에 애노테이션 정보를 읽어 특정한 작업을 수행한다.

3. 런타임 처리(Runtime Processing):
런타임에 동작을 변경하거나 부가적인 정보를 미리 제공할 수 있다.
예를 들어 JsonFormat 애노테이션을 활용하여 Json을 객체로 변환할 때 날짜 형식, 시간대 등을 지정할 수 있다. 

## 애노테이션 선언
```java
@MyAnnotation // 클래스 선언에 붙일 수 있다.
public class Exam {

  @MyAnnotation // 필드에 붙일 수 있다.
  static int a;

  @MyAnnotation int b; // 필드 선언 바로 앞에 둘 수 있다.

  @MyAnnotation // 메서드 선언에 붙일 수 있다.
  void m1(
      @MyAnnotation 
      int p1, // 파라미터(로컬변수)에 붙일 수 있다. 

      @MyAnnotation String p2
      ) {

    @MyAnnotation int local; // 로컬변수 선언에 붙일 수 있다.

    //@MyAnnotation System.out.println("okok"); // 그러나 다른 일반 문장에는 붙일 수 없다.

    //@MyAnnotation // 다른 일반 문장에는 붙일 수 없다.
    for (int i = 0; i < 100; i++) {
      @MyAnnotation int a; // 로컬 변수 선언에 붙일 수 있다.
    }
  }

  @MyAnnotation  // static, non-static 상관없이 메서드 선언에 붙일 수 있다.
  static void m2() {
  }
}
```

## 애노테이션 프로퍼티

- 인터페이스에서 메서드를 정의하는 것과 유사하다.
- 메서드 이름은 프로퍼티(변수)명처럼 작성한다.
- 즉 일반적인 메서드는 보통 동사로 이름을 시작하지만, 애노테이션은 명사(명사구)로 이름을 짓는다.
- 값을 꺼낼 때, 메서드 호출로 꺼낸다.
- 애노테이션에서 값을 설정할 때는 다음과 같이 변수 형태를 사용한다.
```java
value="hello"
```
- 그래서 프로퍼티의 이름을 변수 형태로 짓는다.
- getValue 가 아니라 value라고 한다.
```java
public @interface MyAnnotation2 {
  String value(); // 애노테이션의 기본 프로퍼티이다.
}
```

```java
@MyAnnotation2(value="okok")
public class Exam0120 {

  @MyAnnotation2(value="okok")
  int a;

  @MyAnnotation2(value="okok")
  void m() {}
  // 변수 값은 필수이다.
}
```

## 애노테이션 유지 정책
**애노테이션 유지 범위**
1. CLASS
  - .class 파일까지는 유지된다.
  - runtime때 메모리에 로딩되지 않는다.
  - 애노테이션을 정의할 때 유지 범위를 지정하지 않으면 기본이 CLASS이다.

2. SOURCE
  - 컴파일할 때 제거된다.
  - .class파일에 포함되지 않는다.
  - 보통 소스 파일에서 애노테이션 값을 꺼내 다른 파일을 자동 생성하는 도구를 만들 때 많이 사용한다.

3. RUNTIME
  - .class 파일까지 유지되고, runtime에 메모리에 로딩된다.
  - 실행 중에 애노테이션을 참조/추출해야 할 경우에 많이 사용한다.

**애노테이션 유지 정책 지정**
애노테이션 정의 클래스에 @Retention을 선언하여 범위를 설정할 수 있다.
- CLASS,SOURCE,RUNTIME 설정
```java
@Retention(value=RetentionPolicy.CLASS)
public @interface MyAnnotation {
  String value();
}
```
```java
@Retention(value=RetentionPolicy.SOURCE)
public @interface MyAnnotation2 {
  String value();
}
```
```java
@Retention(value=RetentionPolicy.RUNTIME)
public @interface MyAnnotation3 {
  String value();
```

- 애노테이션 사용
```java
@MyAnnotation(value="값1") // 유지정책 => CLASS
@MyAnnotation2(value="값2") // 유지정책 => SOURCE
@MyAnnotation3(value="값3") // 유지정책 => RUNTIME
public class MyClass {
}
```

## 애노테이션 값 추출
```java
public class Exam {
  public static void main(String[] args) {
    Class<?> clazz = MyClass.class;
    // Class 관련 정보를 다루는 객체 clazz에 MyClass 정보 저장

    // => 유지정책 : RUNTIME
    MyAnnotation3 obj = clazz.getAnnotation(MyAnnotation3.class);
    // Class 클래스의 getAnnotation()으로 애노테이션 정보를 추출하여 obj에 저장

    if (obj == null) {
      System.out.println("MyAnnotation3을 추출할 수 없습니다!");
    } else {
      // 값을 꺼낼 때는 메서드 호출하듯이 꺼내면 된다.
      System.out.println("MyAnnotation3.value=" + obj.value());
    }
  }
}
```

## 필수/선택 프로퍼티
**필수 프로퍼티**
- default 값을 지정하지 않으면 필수 프로퍼티가 된다.
- 노테이션을 사용할 때 반드시 값을 지정해야 한다.
```java
public @interface MyAnnotation {
  String value(); 
}
```

**선택 프로퍼티**
- default 값이 있으면,애노테이션을 사용할 때 값을 지정하지 않아도 된다.
```java
public @interface MyAnnotation2 {
  String value() default "홍길동";
}
```

**값 지정**
```java
//@MyAnnotation // 필수 프로퍼티 값을 지정하지 않으면 컴파일 오류!
@MyAnnotation(value="값") // OK!

@MyAnnotation2 // 애노테이션의 프로퍼티 값을 지정하지 않으면 default 값이 사용된다.
//@MyAnnotation2(value="물론 이렇게 프로퍼티 값을 지정해도 된다.")

public class MyClass {
}
```

## 프로퍼티 이름 생략
- 기본값이 설정되어 있지 않은 경우 반드시 프로퍼티 값을 지정해야 한다.
- `value` 라는 이름을 가진 프로퍼티는 "value=" 생략 가능!
- `value` 라는 이름이 아니면 생략 불가!
- 여러 프로퍼티 값도 지정할 경우, value 이름 생략 불가!

```java
public @interface MyAnnotation2 {
  String value(); // 필수 프로퍼티
}
//---------------------------------
//@MyAnnotation(value="홍길동") // OK!
@MyAnnotation("홍길동") // OK! 
public class MyClass {
}
```

```java
public @interface MyAnnotation2 {
  String tel(); // 필수 프로퍼티
}
//---------------------------------
@MyAnnotation(tel = "홍길동") // OK! 
public class MyClass {
}
```

```java
public @interface MyAnnotation2 {
  String tel(); // 필수 프로퍼티
  String value(); // 필수 프로퍼티
}
//---------------------------------
@MyAnnotation(tel = "홍길동", value = "홍길동") 
// OK! 값을 설정할 때 순서는 상관없다.
public class MyClass {
}
```

## 애노테이션 프로퍼티 타입 - 배열
```java
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation2 {
  // 배열 프로퍼티의 기본 값을 지정할 때 중괄호를 사용한다.
  // 배열 값이 한 개일 경우 중괄호 생략 가능!
  String[] v1() default {"가나다","라마바"};
  int[] v2() default {100, 200, 300};
  float[] v3() default 3.14f;
}
//---------------------------------
@MyAnnotation2 // 지정하지 않으면 default 값 사용
public class MyClass2 {
}
//---------------------------------
//배열 값 추출
public class Exam {
  public static void main(String[] args) {
    Class<?> clazz = MyClass2.class;
    MyAnnotation2 obj = clazz.getAnnotation(MyAnnotation2.class);

    printValues(obj.v1());
    System.out.println("----------------------");

    printValues(obj.v2());
    System.out.println("----------------------");

    printValues(obj.v3());
  }
```

## 애노테이션 적용 범위
@Target을 사용하여 애노테이션을 붙일 수 있는 범위를 제어할 수 있다. 
- Type : 클래스나 인터페이스 선언부에만 붙일 수 있다
```java
@Target(ElementType.TYPE)
public @interface MyAnnotation {
}
//---------------------------------
@MyAnnotation // OK!
public class MyClass {

  //  @MyAnnotation
  int i; // 컴파일 오류!

  //  @MyAnnotation
  public void m(/*@MyAnnotation*/ int p) {
    /*@MyAnnotation*/ int a;
  }
}
```

- FIELD : 필드에만 붙일 수 있다.
```java
@Target(ElementType.FIELD)
public @interface MyAnnotation2 {
}
//---------------------------------
public class MyClass2 {
  @MyAnnotation2 int i;
  @MyAnnotation2 static int i2;

  public void m(int p) {
    int a;
  }
}
```

- METHOD : 필드에만 붙일 수 있다.
```java
@Target(ElementType.METHOD)
public @interface MyAnnotation2 {
}
//---------------------------------
public class MyClass3 {
  int i;
  static int i2;

  @MyAnnotation3
  public void m(int p) {
    int a;
  }
}
```

- LOCAL_VARIABLE : 로컬 변수에만 붙일 수 있다.
```java
@Target(ElementType.LOCAL_VARIABLE)
public @interface MyAnnotation4 {
}
//---------------------------------
public class MyClass4 {
  int i;
  static int i2;

  public void m(int p) {
    @MyAnnotation4 int a;
  }
}
```

- 여러 범위도 지정할 수 있다.
```java
@Target({ElementType.LOCAL_VARIABLE, ElementType.PARAMETER, ElementType.FIELD})
public @interface MyAnnotation5 {
}
```

## 애노테이션 중복
- 기본은 한 번만 가능
- 애노테이션을 중복해서 사용할 수 있게 하려면 @Repeatable를 표시하고 중복해서 사용할 애노테이션을 지정해주어야 한다.

```java
// @Repeatable 선언(반복 선언)
@Repeatable(value=Employees.class)
public @interface Employee {
  String value();
}
//---------------------------------
// @Employee 반복 지정
public @interface Employees {
  Employee[] value();
}
//---------------------------------
// @Employee 중복 사용
@Employee("홍길동")
@Employee("임꺽정")
public class MyClass2 {

  @Employee("홍길동")
  @Employee("임꺽정")
  public void m1(int p) {}

  @Employee("홍길동")
  @Employee("임꺽정")
  public void m2(int p) {}
}
```
