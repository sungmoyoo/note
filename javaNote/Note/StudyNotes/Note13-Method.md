# 메서드
# 1. 메서드 구조
```
return type   parameter
  |             |
int  plus(int a, int b) 
      |
    method signature
{
  int r = a + b;    -> method body
  return r;
}
```

## 1.1 method argument와 parameter
```
                아규먼트를 받는 로컬 변수 "파라미터(parameter)"
                   |          |
void hello(String name, int age){---}

hello("홍길동", 20)
        |      |
      메서드에 넘겨주는 값 "아규먼트(argument)"
```
## 1.2 가변 파라미터
- 0개 이상의 값을 받을 때 선언하는 방식
- 메서드 내부에서는 배열처럼 사용가능하다.
- 배열을 값으로 넘길 경우 그 배열을 그대로 사용한다.
[리턴타입] 메서드명(타입... 변수) {...}
메서드(값1,값2,...);
```
static void hello(String... 변수){
  for (int i = 0; i < names.length; i++){
    System.out.println(names[i]);
  }
}

public static void main(String[] args) {
  hello("홍길동","임꺾정");
}
```
주의사항)
- 가변 파라미터는 여러개 선언할 수 없다.
- 가변 파라미터는 반드시 파라미터 맨 끝에 와야 한다.


## 1.3 배열 파라미터 
[리턴타입] 메서드명(타입[] 변수) {...}
- 배열 파라미터의 경우 낱개의 값을 여러개 줄 수 없다.
- 오직 배열에 담아서 전달해야만 한다.
```
static void hello2(String[] names){...}
public static void main(String[] args) {
  String[] arr = {"김구", "안중근", "유관순"};
  hello2(arr);
}
```
# 2. 메서드 중첩 호출
매서드 안에 매서드
```
static int plus(int a, int b){
  return a + b
}

public static void main(String[] args){
  int result = plus(2,3);
  result = plus(result, 4);
  result = plus(result, 5);
}

= plus(plus(plus(2,3),4),5)
```

# 3. call by value
메서드를 호출할 때 값을 넘긴다.(byte,short,int,long,float,...)
```
main(){
  int a = 100;
  int b = 200;
  swap(a,b);
}

swap(int a, int b){
  int temp = a;
   a = b;
   b = temp;
}
```
Stack메모리
- main()이 호출되면, stack 메모리에서는 main()이 사용할 로컬변수를 생성한다. 
- main()이 종료되기 전 swap() 호출
- swap()이 사용할 로컬변수를 생성한다. 
- a 변수는 b의 값으로 b의 변수는 temp값으로 덮어진다.
- 다시 main()으로 돌아가면 a의 값은 그대로 100, b는 200이다.

### JVM Stack
메서드의 로컬변수를 만드는 메모리 영역

# 4. call by reference
```
main{
  int[] arr = new int[]{100,200};
  swap(arr);
}
swap(int[] arr){
  int temp = arr[0];
  arr[0] = arr[1];
  arr[1] = temp;
}
```
- Stack에 main()를 호출하면 arr이라는 메모리가 생성된다.
- Heap에 new로 만들어진 메모리 int[]{100,200}이 생성
- 그것이 배열 인스턴스, 레퍼런스도 생성되는데 레퍼런스는 Stack에 저장
- swap()이 호출되면 stack에 메모리 생성됨
- arr에는 레퍼런스가 들어가고 temp에는 100이 들어감
- Heap메모리에 [0]에는 200, [1]에는 100이 들어간다.
- swap()이 끝난 후 main()으로 돌아가면 arr에 바뀐 값이 존재한다.

### Heap
new 명령어를 통해 만들어진 메모리를 담는 영역

### Method Area
class bytecode와 static 변수를 담는 메모리 영역, 

# 5. 레퍼런스 리턴
```
class MyObject{
  int a;
  int b;
}
MyObject swap(int a, int b){
  MyObject ref = new MyObject();
  ref.a = b;
  ref.b = a;
}
main(){
  int a = 100;
  int b = 200;
  MyObject ref = swap(a,b)
}
```
1. main() 호출과 동시에 Stack에 args, a, b, ref 로컬변수 생성
2. swap() 호출 시 다른 frame에 a,b,ref 로컬변수 생성
3. new 명령어에 의해 Heap에 a,b 생성, 해당 레퍼런스를 swap() ref에 전달
4. swap에 파라미터로 전달된 int a와b가 Heap에 ref.a, ref.b에 각각 b와 a 저장
5. swap()의 ref를 return하면 swap() frame 제거됨
6. 리턴값을 MyObject의 ref에 할당
7. 출력 후 main() frame의 모든 로컬변수 삭제
8. heap 영역에 남아있는 메모리는 가비지컬렉터에 의해서 관리


# 5. JVM 메모리 실행순서와 메모리
실행 순서와 메모리
1) java -classpath bin com.eomcs.lang.ex07.Exam0410
   => JVM은 클래스 정보를 Method Area 영역에 로드(보관)한다.
   그대로 보관하는 것이 아니라 실행할 수 있는 구조로 바꿔서 바이트코드를 배치한다.  

2) main() 호출
   => Method Area에 (보관)로딩된 바이트코드에서 main() 메서드를 찾아 실행(호출)한다. 
   => JVM Stack 영역에 main() 메서드가 사용할 로컬 변수를 만든다.
   => new 명령문이 있다면 해당 변수를 Heap에 만든다. 

3) swap() 호출
   => JVM Stack 영역에 swap() 메서드가 사용할 로컬 변수를 준비한다.

4) swap() 실행 완료
   => JVM Stack 영역에 있던 swap()이 사용한 메모리를 제거한다.

5) main() 실행 완료
   => JVM Stack 영역에 있던 main()이 사용한 메모리를 제거한다.

6) JVM 실행 종료
   => OS가 JVM에게 사용하라고 빌려줬던 모든 메모리를 회수한다.  

<font size="6.2"><b style="color:red">결론!</b></font>

- 소스코드에 변수의 위치와 상관없이 메서드를 호출하는 시점에 로컬변수 메모리 모두 생성됨
- 메서드가 종료되면 JVM이 해당 메서드의 모든 메모리를 회수한다. 즉 로컬변수가 모두 제거된다.
- new를 통해 heap 메모리에 저장된 인스턴스는 사라지지 않는다. 
이를 관리하는 것이 가비지 컬렉터(Garbage Collector)
- 클래스를 통해 정의된 변수를 만들면 이를 객체 또는 인스턴스라고도 한다. 
- 객체는 클래스에서 정의한 것을 토대로 메모리에 인스턴스를 할당한 것, 인스턴스 주소를 가리킬 때도 객체라는 표현을 사용한다.

# 6. 재귀호출
```
sum(n) = n + sum(n-1), n > 0

sum(4) = 4 + sum(3)
               3 + sum(2)
                      2 + sum(1)
                            1 + X
sum(4) = 4 + 3 + 2 + 1 = 10 
```
### 스택 오버플로우
JVM 스택 메모리가 꽉 차서 더이상 메서드 실행에 필요한 로컬 변수를 만들 수 없는 상태일 때 발생하는 에러, 재귀호출과 같이 Frame과 로컬변수 많이 만드는 상황에서 많이 발생한다.

```
static int sum(int value) {
  System.out.println(value);
  if (value == 1)
    return 1;

  return value + sum(value - 1);
}

public static void main(String[] args){
  System.out.println(sum(40444));
}
```

# 7. main() method() - 프로그램 아규먼트
```
$ java -classpath bin/main com.eomcs.lang.ex07.Exam0520 aaa bbb ccc
                                                        -----------
                                                        프로그램 아규먼트
```
aaa bbb ccc -> "aaa" "bbb" "ccc"
- 공백 제거 후 문자열로 String[] 배열 생성 후 main()에 전달
- 아무것도 주지 않으면 빈 배열의 주소만 넘어옴 null이 아니다.
- 변수명은 자유 나머지는 문법상 고정
```
public static void main(String[] 변수명)
```

# 8. JVM 아규먼트
```
                                cp 순서 상관 없음
$ java -D(이름=값) -D(이름=값) ... -cp bin/main com.eomcs...
        ----------------
    (이름)->
getProperty->properties 객체
            <-값
```