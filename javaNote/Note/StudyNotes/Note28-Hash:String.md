<span style="font-size:133%">

# HashSet
HashSet<제네릭> 레퍼런스 = new HashSet<제네릭>();
- 해시셋이란 집합이다. 
- 집합에는 객체를 보관한다.
- 중복 값을 저장할 수 없다.
- 값을 저장할 때 해시값을 계산하여 저장 위치를 알아낸다.
- 저장 과정:
  1) equals()와 hashCode()를 호출하여 중복 여부를 검사한다
  2) equals()의 리턴 값도 true이고 hashCode()의 리턴 값도 같을 경우, 같은 객체로 판단하여 저장하지 않는다.
  3) 저장할 때 저장 위치는 hashCode()의 리턴 값을 사용하여 계산한다.

## HashSet 중복 처리
인스턴스가 다르더라도 데이터가 같을 때 중복 저장되지 않도록 하려면 hashCode() equals() 모두 오버라이딩 해야 한다.  
- hashCode()는 같은 필드 값을 갖는 경우 같은 해시코드를 리턴하도록 변경  
- equals()는 필드 값이 같을 경우 true를 리턴하도록 변경

# HashMap
HashMap<제네릭(key),제네릭(value)> map = new HashMap<>();
- key객체의 해시코드를 사용하여 value객체를 저장하는 자료구조이다.
- 각 키에 대해 유일한 값을 가진다.

## HashMap 중복 처리
두 key 객체의 value값이 같을 경우 두 key를 같다 판단하고 동일한 value값을 꺼내려면 hashCode(),equals()를 오버라이딩하면 된다.

## Wrapper 클래스를 key객체로 사용하기
- Wrapper 클래스를 key객체로 사용하면 다른 객체여도 실제 값(데이터)가 같으면 둘을 같은 key로 간주한다.
- Wrapper 클래스는 equals()와 hashCode()를 오버라이딩하고 있기 때문이다.

## String을 key객체로 사용하기
- String도 Wrapper와 마찬가지로 equals()와 hashCode()를 오버라이딩하고 있기 때문에 true를 반환하고, 리턴값이 같다.
- 따라서 문자열이 같은 두 key는 같은 key로 간주된다.

# getClass()
Class classInfo = 레퍼런스.getClass();
classInfo.getName(); => 패키지명 + 바깥 클래스명 + 클래스명 리턴  
classInfo.getSimpleName(); => 클래스명 리턴

## Primitive Type과 String 정보
- Primitive Type은 클래스가 Object의 서브클래스가 아니기 때문에 getClass를 호출할 수 없다.
- 대신 static 변수인 class를 통해 클래스정보를 리턴받을 수 있다.
```
Class classInfo = int.class;
System.out.println(classInfo.getName());
=> int

System.out.println(String.class.getName());
java.lang.String
```

# clone()
인스턴스를 복제할 때 호출하는 메서드
- 기본적으로 Object에서 상속받은 clone()은 접근 범위가 protected여서 같은 패키지에 소속된 클래스이거나 상속 받은 서브 클래스가 아니면 호출할 수 없다.
- 따라서 clone()을 사용하여 자유롭게 객체를 복제하려면 상속받은 clone()을 오버라이딩 하여 수정해주어야 한다.
```
1. 오버라이딩
2. 접근 권한 변경 protected -> public
3. 리턴타입을 해당 클래스로 형변환
```
- 오버라이딩한 이후 clone() 시도 시 다음 예외가 발생한다.
```
java.lang.CloneNotSupportedException:
```
- 단순히 오버라이딩 했다고 끝나는 것이 아니라 해당 클래스에서 복제 기능을 활성화하는 설정을 해주어야 한다.
- 복제 기능을 활성화하려면 Cloneable 인터페이스를 구현해야 한다.
- Cloneable 인터페이스는 추상메서드가 하나도 없다.
- JVM에게 이 클래스의 인스턴스를 복제할 수 있음을 알려주는 목적의 인터페이스일 뿐이다.


## shallow copy(얕은 복제)
- 객체명.clone()한 경우
- 객체가 포함하는 의존객체는 복제하지 않는다.
- 해당 객체의 필드값을 복사하므로 필드값이 주소인 경우 결국 같은 Heap의 데이터를 가리키는 복제가 되어버린다. 
- 얕은 복제는 인스턴스 주소가 가리키고 있는 객체는 복제하지 않기 때문에 복제한 객체가 가리키는 실제 값을 변경하면 원본의 인스턴스 값도 바뀐다.

## deep copy(깊은 복제)
- 실제 인스턴스 값까지 복제하여 새 인스턴스를 생성하려면 개발자가 직접 clone() 메서드 내에 작업을 수행하는 코드를 작성해야 한다.
```
public String toString() {
      return "Car [maker=" + maker + ", name=" + name + ", engine=" + engine + "]";
    }

    public Car clone() throws CloneNotSupportedException {
      Car copy = (Car) super.clone(); // 레퍼런스로 받은 객체의 주소 정보를 가지고 Object에 정의된 메서드를 그대로 사용하여 복제 
      copy.engine = this.engine.clone(); // 레퍼런스로 받은 객체의 주소 정보에 있는 engine을 레퍼런스 주소로 주고 복제하도록 한다.
      return copy; 복제된 인스턴스 리턴
    }
  }

public static void main(String[] args) {
  Car car = new Car();
  Car car2 = (Car) car.clone()
}

```


### 문자열 결합
```
"aaa" + obj(레퍼런스) 
= "aaa" + obj.toString()의 리턴값
= "aaa" + "리턴값"
= "aaa리턴값"
```

# String
## 문자열 객체
- String 레퍼런스
  - String은 자바 기본 타입이 아니다. 클래스이다.
- String 인스턴스
  - Heap에 Hello 문자 코드를 저장할 메모리를 만들고 그 주소를 리턴한다.
  - 내용물의 동일 여부를 검사하지 않고 무조건 인스턴스를 생성한다.
  - 따라서 같은 문자열이어도 다른 인스턴스를 가진다.

## 문자열 리터럴 
- string constant pool(상수풀) 메모리 영역에 String 인스턴스를 생성한다. 
- 상수풀에 이미 같은 문자열의 인스턴스가 있다면 그 주소를 리턴한다. 
```
String x1 = "Hello";
String x2 = "Hello";

x1 == x2 => true
```

## intern()
- String 객체에 들어있는 문자열과 동일한 문자열을 갖고 있는 String 객체를 상수풀에서 찾는다.
- 있으면 그 String 객체의 주소를 리턴하고, 없으면 상수풀에 String 객체를 생성한 후 그 주소를 리턴한다. 
- 긴 문자열의 경우 긴 문자열을 그대로 다 적는것보다 객체를 지정하는 것이 더 편할 수 있다. 

## equals()
- 레퍼런스의 값과 파라미터로 주어진 값이 같은지 확인

## equalsIgnoreCase()
- 대소문자 구분 없이 값을 비교한다. 

## String.equals() vs Object.equals()
- String 클래스로 만든 객체는 equals로 실제 문자열을 비교할 수 있다.
- Object를 상속받는 사용자 정의 클래스는 문자열 비교가 아닌 인스턴스를 비교한다, equals를 오버라이딩 하지 않았기 때문이다.
- 오버라이딩하면 String 클래스처럼 equals로 문자열을 비교할 수 있다.

## Stringbuffer
- Object에서 상속받은 equals()를 오버라이딩 하지 않는다. 
- StringBuffer 에 들어 있는 문자열을 비교하려면 String 값을 toString()으로 꺼내 String에 저장 후 비교하면 된다.

## String - hashCode()
- String의 hashCode()는 문자열이 같으면 같은 해시코드를 리턴하도록 오버라이딩했다. 
- HashSet이나 HashMap에서 이 리턴값을 사용한다.

## String - toString()
- String은 상속받은 toString()을 오버라이딩 했다.
- this 주소를 그대로 리턴한다. 

## String - 다형적 변수와 형변환
- 상위클래스 레퍼런스에 하위 클래스 객체를 가리켰다고 가정할 때
- toString()을 호출하면 실제 레퍼런스가 가리키는 객체의 클래스부터 메서드를 찾아 올라간다. 

## String - toLowerCase()
  - toLowerCase()는 문자열을 소문자로 변환해주는 메서드이다.
  - Object 레퍼런스가 String 객체를 가리키더라도 원래 타입이 Object인 경우 원래 클래스의 메서드를 호출하고 싶다면 원래 타입으로 형변환해야 한다.
  - 메서드 호출 시 원래 객체 클래스부터 찾아 올라가지만, 컴파일 단계에서 형식이 안맞아 에러가 발생하기 때문이다.

## immutable vs mutable
  - String은 immutable(불변) 객체이다.
    - 불변객체는 한번 저장된 값을 절대 변경할 수 없다.
    - 값을 바꾸려고 하면 새 인스턴스를 생성하여 저장한다. 
  - StringBuffer는 mutable(가변) 객체이다.
    - replace(), concat() 메서드 등으로 변경할 수 있다.

## StringBuffer(HashTable) vs StringBuilder(HashMap)
- StringBufffer는 가변 문자열을 나타내는 클래스로, 스레드 안전성이라는 특징을 가진다. 

- StringBuilder는 가변 문자열을 나타내는 클래스로, 스레드 안전하지 않은 버전의 문자열 연산을 한다. 

### StringBuilder와 멀티스레드
- StringBuilder는 여러 스레드가 동시에 버퍼를 사용하는 것을 막지 않는다. 즉 작업중인 스레드에 대해 다른 스레드가 진입하는 것을 통제하지 않는다. 
- 다른 스레드가 한 작업을 덮어 쓸 수 있다.
- single 스레드만 작업하는 경우에 적합하다.
- Thread-Safe 하지 않다.

### StringBuffer와 멀티스레드
- Stringbuffer는 os가 한 번에 한 스레드만이 버퍼를 다루도록 제한한다. 즉 먼저 진입해서 작업하던 객체가 나올 때까지 진입할 수 없다.
- 이렇게 통제하는 것을 synchronized(=동기화 처리; =lock(잠금))라고 한다.
- lock/unlock하는데 시간이 소요된다.
- StringBuilder보다 느리다.
- 여러 스레드가 접근하는 작업도 Thread-Safe 보장

```
Thread Safe
1. 여러 스레드가 동시에 작업하더라도 문제가 발생하지 않도록 조치를 했음을 의미
-> 여러 스레드가 같은 변수의 값을 동시에 변경하려 할 때 한 번에 한 스레드만이 작업하도록 제한하는 것

2. 여러 스레드가 동시에 진입해서 명령을 실행하더라도 문제가 발생하지 않는 코드 
-> 변수의 값을 조회만 하는 코드
```

</span>