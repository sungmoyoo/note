### C++ vs Java
```
C++: OOP에 따라 소스를 클래스 단위로 쪼개서 관리, 컴파일 시 하나의 파일이 된다. 
소스 -> 클래스  -> 컴파일 -> 실행파일 -> RAM -> CPU
...       클래스
...       클래스
```

```
Java: 클래스 단위로 쪼갠 후 바이트코드로 각각 컴파일한다. 각각 별도로 관리되며 로딩할때 필요한 클래스파일을 찾아서 실행한다.
소스 -> 클래스 -> 컴파일 -> 바이트코드 -> RAM -> CPU
...    클래스 -> 컴파일 -> 바이트코드 -> RAM -> CPU
...    클래스 -> 컴파일 -> 바이트코드 -> RAM -> CPU
```

### C++ + DLL(Dynamic Linking Library)
```
C++과 DLL: 클래스파일을 컴파일할 때 .exe과 .dll을 만든다. .exe파일은 실행파일이고 .dll 정보파일로서 합쳐지지 않고 관리된다.
소스 
...  클래스 -> compile -> | .exe ------> RAM -> CPU
...  클래스 -> compile -> |
...  클래스 -> compile -> .dll, .so -> RAM -> CPU
...  c    -> compile -> .dll, .so
...  c    -> compile -> .dll, .so
...  c    -> compile -> .dll, .so
```

# 1. 레퍼런스 배열
primitive type 배열
```
int a, b, c;
a = 100;
b = 200;
c = 300;

int[] arr = new int[3];
arr[0] = 100;
arr[1] = 200;
arr[2] = 300;
```

reference type 배열
```
Object a, b, c;
a = new Object();
b = new Object();
c = new Object();


Object[] arr = new Object[3];  // Object의 주소를 담을 레퍼런스를 3개 만들라는 명령
arr[0] = new Object();
arr[1] = new Object();
arr[2] = new Object();

- arr[0],arr[1],arr[2]는 각각 인스턴스의 주소값을 담는다.
- 즉 배열이다.
```

### 자바에서 인스턴스 배열을 만드는 방법은?
```
없다.
```

## 1.1 레퍼런스 배열과 인스턴스
```
Assignment[] assignments = new Assignment[3];
int length = 0;

assignments[length] = new Assignment();
length++;
assignments[length] = new Assignment();
length++;
assignments[length] = new Assignment();
```

## 1.2 레퍼런스 배열과 메모리 영역
- 레퍼런스 배열은 heap 메모리에 레퍼런스 배열을 생성하는 것이다. 
- 일반적인 배열 선언방식과 유사, new 명령을 통해 heap 메모리에 생성한다.
```
Object[] arr = new Object[3]

arr[0] = 10;
arr[1] = 20;
arr[2] = 30;
```
- 다수 객체 관리, 효율적인 자료 구조를 위해 사용된다. 

## 1.3 배열의 항목 삭제
- 배열은 인덱스에 의해 순차적으로 저장되는 자료구조이다. 만약 한 인덱스의 값을 삭제하고자 한다면 삭제 후 인덱스를 정렬하는 작업을 거쳐야 한다. 
```
일반적으로 사용하는 방법은 인덱스의 값을 한칸씩 당겨서 초기화하는 방법이다.
Object[] arr = new Object[3]

arr[0] = 10;
arr[1] = 20;
arr[2] = 30;

index = 1

for (i=index, i < arr.length-1; i++)
arr[i] = arr[i + 1];
arr[arr.length] = null; //마지막 인덱스 값 삭제
```


## 1.4 레퍼런스 배열 크기 늘리기
- 한번 생성된 배열은 크기를 늘릴 수 없다. 배열 크기를 늘리고 싶으면 새로운 배열을 만들어야 되고 가비지가 생긴다.
