# Package와 namespace
1. package 
- 자바 클래스가 소속된 그룹
- 소스 디렉토리에 속해 있는 최상위 패키지를 루트 패키지라고 한다. 
```java
// java.lang 패키지 소속 클래스는 패키지명을 작성하지 않아도 된다.
System.out.println("Hello");

// java.lang 외의 패키지는 이름을 명시하거나 import를 해서 생략할 수 있다.
java.util.ArrayList arr = null;
```

2. namespace
- 자바 외 다른 프로그래밍 언어에서는 package가 아닌 namespace라 부른다. ex) C++, C#, xml
```xml
<!-- 
  기본 namespace : xmlns=..
  그 외 namespace는 별명을 정의한다. : xmlns=context...
-->
<beans
  xmlns="http://---" 
  xmlns:context="http://---">

<!-- 기본 namespace에 소속된 태그는 바로 사용-->
<beans---/>

<!-- 그외는 namespace 별명을 붙여 사용-->
<context:component-scan ---/>
```

# 코드를 읽는 법
기본적으로 코드를 읽을 때 정확한 방법은 메서드가 클래스의 정보를 가지고 연산을 수행한다는 관점으로 읽는 것이다. 하지만 이 방법은 코드 수가 많아지면 자연스럽게 읽는 것이 힘들다. 따라서 관점을 바꾸어 클래스가 메서드를 실행한다는 관점으로 읽으면 자연스럽고 빠르게 읽을 수 있다.

예를 들어 아래와 같은 코드가 있을 때

```java
Constructor[] csts = clazz.getConstructors();
```
사실상 연산자(Operator)는 getConstructors()이고 피연산자(Operand)는 clazz이지만 clazz가 메서드를 호출한다고 읽으면 편하다는 의미이다. 
