<span style="font-size:133%">

## String - 기타 메서드
배열의 모든 값을 CSV 형식의 한 문자열로 변환할 때
- format() 사용
```
String s1 = String.format("%s,%s,%s,%s,%s", arr[0], arr[1], arr[2], arr[3], arr[4]);
```

- 가변 파라미터 자리에 배열 전달
```
String s2 = String.format("%s,%s,%s,%s,%s", arr);
```

- Join() 사용
```
String s3 = String.join(",", arr[0], arr[1], arr[2], arr[3], arr[4]);

String s4 = String.join(",", arr);
```

### Arrays.copyOfRange()
배열에서 특정 범위의 항목 복사 가능
```
    String[] arr2 = Arrays.copyOfRange(arr, 2, 4);
    for (String s : arr2) {
      System.out.println(s);
    }
```

## String - 생성자 활용
- String 인스턴스 생성할 때 내부적으로 코드 값을 저장할 때 1.8버전까지는 char 배열을 생성 1.9 이후부터는 byte 배열을 생성한다.
- 생성자에서 파라미터로 전달한 값을 배열에 저장한다. 파라미터가 없으면 빈 배열을 생성한다.
- String 생성자는 파라미터로 받은 바이트 배열에 ISO-8859-1 문자 코드가 들어 있다가 간주한다.
- 따라서 생성자에 바이트 배열을 넘겨줄 때 배열에 들어 있는 코드 값이 어떤 문자표의 코드 값인지 알려줘야 한글이 깨지지 않는다.

### 인스턴스의 부가정보
- 각각의 인스턴스는 클래스 정보를 갖고 있다.
- 크기가 0인 인스턴스는 없다. 클래스 정보에 대한 링크정보(주소)를 가지고 있기 때문이다.

# Wrapper 클래스
primitive data type의 값을 캡슐화시켜 메서드를 통해 객체처럼 다루기 위해 만들어진 클래스
- 객체이기 때문에 Object에 저장할 수 있다. 
- primitive data type의 값을 객체로 주고 받아야 할 때/객체에 담아 전달할 때 사용한다.
- Java 9 부터 wrapper 클래스의 생성자가 deprecated 상태이다.
-  Wrapper 클래스의 인스턴스를 생성할 때는 생성자 대신 클래스 메서드를 사용하는 것이 좋다(valueOf)

## Wrapper 객체 값 추출
- 각 타입의 wrapper 클래스에서 오버라이딩한 toString(), toHexString() 등등의 메서드 사용 가능하다.

## Wrapper - 오토박싱/오토언박싱(=박싱/언박싱)
프로그래밍 중에 박싱과 언박싱 작업은 불편하다
- 박싱(boxing)
  - primitive type의 값을 인스턴스에 담는 일
- 언박싱(unboxing)
  - 인스턴스에 담긴 primitive 값을 다시 꺼내는 일
```
int obj = Integer.valueOf(100)
int i = obj.intValue();
```

- 오토박싱(auto-boxing)
  - 자동으로 박싱해주는 문법, 리터럴을 저장하면 컴파일 시 컴파일러가 자동으로 메서드를 호출하는 코드로 변환해준다.
- 오토언박싱(auto-unboxing)
  - 자동으로 언박싱해주는 문법, 객체를 저장하면 컴파일러가 자동으로 value를 호출하는 메서드로 변환해준다.
```
Integer obj = 100; // => Integer.valueOf(100)

Integer obj = Integer.valueOf(300);
int i = obj; // ==> obj.intValue()
```

- 객체를 받는 파라미터로 primitive type로 넣거나, primitive type을 받는 파라미터로 객체를 넣어도 오토 박싱/언박싱을 수행한다.

## Wrapper 객체 생성
- auto-boxing으로 Wrapper 객체를 생성할 경우  자주 사용되는 -128~127 범위의 정수값은 Integer 상수풀에 cache하여 메모리를 효율적으로 사용한다.

## Wrapper 객체 값 비교
- 객체이기 때문에 '=='연산자 사용불가 오버라이딩된 equals를 사용하거나 value값을 꺼내 직접 비교할 수 있다.
- -127~128범위 안이면 상수풀을 비교하기 때문에 '=='연산자 사용가능

# 예외 처리 try-catch
try{
  예외 발생가능한 코드 
} catch (Exception e){
  예외 발생시 실행하는 코드
} 

## 예외 발생과 보고
- 예외를 보고받을 때 처리하지 않으면 상위 호출자에게 예외를 보고한다.
- main() 메서드에서 예외처리하지 않으면 JVM에게 보고한다.
- 보고받은 JVM은 예외 내용 출력 후 프로그램을 종료한다.


## 예외 클래스 계층도
```
Object - Throwable | - Error - XxxError
            |               -...
            |      
            |------| - Exception - XxxException
                                 - ...
```

</span>