# HashSet 
- 여러개의 LinkedList 생성
- add(객체) 메서드가 호출되면 전달된 객체를 Hash 값으로 변환 후 생성된 LinkedList 개수로 나눈 나머지 인덱스에 추가된다.
- 따라서 값을 꺼낼 때 순서가 일치하지 않는다.
**따라서

# Iterator

## Iterator 구현
1. 패키지 멤버 클래스
```
ArrayList -> ArrayIterator - -> Iterator
```

2. static nested class
```
| ArrayList  |
|   static   | - -> Iterator
|IteratorImpl|
```

**중첩클래스 사용 목적 => ArrayList 클래스 안에서만 쓰일 클래스라면 ArrayList 클래스 안에 두는 것이 더 직관적이고 유지보수에 더 낫다.**

3. non - static nested class
```
| ArrayList  |
| non-static | - -> Iterator
|IteratorImpl|
바깥 클래스의 인스턴스 주소를 받는 코드(this)가 자동으로 추가된다. 
```
**=> 따라서 바깥클래스의 객체 정보를 사용하기 위해 굳이 생성자와 변수를 만들 필요없이 규칙에 따라 바깥클래스.this로 편하게 가져다 쓸 수 있다.**

4. local class
```
| ArrayList  |
| iterator{  | - -> Iterator
|IteratorImpl|
| }          |
1. 특정 메서드 안에서만 사용되는 클래스라면 그 메서드 안에 두는 것이 더 직관적이다. 
2. 로컬 클래스도 논스태틱 클래스처럼 인스턴스 주소를 저장하는 코드가 자동으로 생성된다.
```

5. 익명클래스
```
 ArrayList         |
| Iterator<E> obj  | - -> Iterator
|= new Iterator(){}|
| }                |
1. 익명클래스는 이름이 없기 때문에 정의하는 즉시 인스턴스 생성
2. 객체 생성 후 (){-} 나오면 익명클래스
```

# Nested Class

## top level class: 접근범위
top level class = 패키지에 소속된 클래스

public : 다른 패키지에서도 접근 가능
package-private(modifier X): 다른 패키지 접근 불가
**=> 같은 패키지의 클래스를 사용할 때는 패키지명을 생략할 수 있다.**

## 중첩 클래스(nested class)
- 특정 클래스 안에서만 사용되는 클래스가 있다면 중첩클래스 선언
- 즉, 노출범위를 좁히는 것이 유지보수에 좋다.

1) static nested class
- 바깥 클래스의 인스턴스에 종속되지 않는 클래스
- top level class와 동일하게 사용한다.

2) non-static nested class(=inner class)
- 바깥 클래스의 인스턴스에 종속되는 클래스
- 중첩 클래스에서 바깥 클래스의 인스턴스 멤버를 사용한다는 뜻
- 바깥 클래스 객체 정보를 자동으로 this에 저장(컴파일 시 생성자 생성)

3) local class
- 특정 메서드 안에서만 사용되는 클래스
- 패키지에 직접 소속된 멤버클래스가 아니라서 modifier가 없다. 메서드 종료 시 사라진다.

4) anonymous class
- 클래스 이름이 없다.
- 이름이 없으니, new 명령으로 따로 인스턴스를 생성할 수 없다. 
- 즉, 클래스를 정의하는 동시에 인스턴스를 생성해야 한다.
- 클래스 이름이 없기에 생성자도 정의할 수 없다.
- 인스턴스 값을 초기화시키기 위해 복잡한 코드를 생성해야 한다면, `인스턴스 블록`을 사용하여 초기화 코드를 작성해야 한다.
```
문법
new 수퍼클래스() {수퍼 클래스를 상속 받은 익명 클래스 정의}
new 인터페이스() {인터페이스를 구현한 익명 클래스 정의}

주의!
new extends 수퍼클래스 implements 인터페이스 {익명 클래스 정의} <== 이런 문법은 없다.

new 수퍼클래스 implements 인터페이스() {인터페이스를 구현한 익명 클래스 정의} <== 이런 문법은 없다.

new 인터페이스1, 인터페이스2, 인터페이스3() {인터페이스를 구현한 익명 클래스 정의} <== 이런 문법은 없다.

수퍼 클래스를 지정하거나 인터페이스를 지정하거나 둘 중 하나만 해야 한다.
```

## non-static nested class
- inner class의 객체를 만들 땐 바깥 클래스의 객체 주소를 가지고 만든다.
