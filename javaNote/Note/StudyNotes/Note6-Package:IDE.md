
# 패키지와 클래스
## 0 .클래스 블록과 .class파일
### 0.1 소스파일과 클래스 블록, .class파일
- <b style="color:red">자바의 최소 컴파일 기본 단위는 class블록이다. 소스파일이 아니다.</b>
```
-----A.java-----
class A{}    <-기본단위 --> 컴파일 시 A.class

-----B.java-----
class b1{}    --> 컴파일 시 B1.class가 생성
                    즉 클래스 블록당 .class 파일을 생성

-----C.java-----
class c1{} --> C1.class 생성됨
class c2{} --> C2.class  ``
class c3{} --> C3.class  ``
```
## 1. 패키지

### 1.1 package와 클래스 블록, .class파일
```
src/main/java/  <- 소스 디렉토리는 java
          |-A.java
          |-B.java
          |-C.java
          |-com/
             |-eomcs/
                 |-D.java
                      class d1{}
                      class d2{}
```
=> com/eomcs 폴더에 있는 자바소스를 컴파일 했는데 결과폴더를 보면 com/eomcs폴더에 .class파일이 놓여지지 않았다.
=> 특정 폴더에 소속된다고 지정하지 않았기 때문이다. 루트 디렉토리의 클래스로 간주된다.

```
src/main/java/  <- 소스 디렉토리는 java
          |-A.java
          |-B.java
          |-C.java
          |-com/
             |-eomcs/
                 |-E.java
                      package com.eomcs; <- 패키지 지정
                      class E1 {}
                      class E2 {}
```
*패키지
-> 클래스가 소속된 그룹이름
-> 디렉토리를 다룬다.
-> 클래스를 역할에 따라 관리하기 위해서 => 정리정돈
=> 소스를 관리하기 쉽게 하기 위해 단순히 디렉토리로 분류하는 것은 소용없다.  
=> 따라서 문법으로 명시해야 하는데, 그것이 바로 패키지 문법

```
소스폴더                        목적폴더
src/main/java             build/classes/java/main
        |-com     컴파일                        |-com         <-없으면 자동생성
          |-eomcs  =>                            |-eomcs
            E.java                                  E1.class
com/eomcs는 패키지 디렉토리라 한다.                       E2.class
JVM에서는 디렉토리가 아닌 패키지로 인식한다.

따라서 실행할 때
$java -cp build/classes/java/main com.eomcs.E1
           목적 폴더까지만 지정         패키지명.파일명
```

### 1.2 패키지 이름과 도메인명
패키지 이름: 
- 패키지 이름은 보통 도메인명을 사용: 개발자간, 회사간에 이름 충돌 방지
- 도메인명을 거꾸로 사용: 상위에서 하위로 분류하기 쉽게
```
src/main/java
          |-com/
              |-microsoft
              |-facebook
              |-oracle
          |org/
              |-apache
```

패키지명 작성 관례
```
com.microsoft.msword.checker
------------  -----  ------
    |           |      |
  도메인명       제품명   역할명
              서비스명
```

### 1.3 패키지와 공개 여부, 파일명
```
src/main/java
        |-com
          |-eomcs
              |-F.java
                  class F1 {}
                  class F2 {}
              |-G.java
                  class Test {}
        |-Test2.java
          |-class Test2{}
```
- 패키지 멤버 클래스(패키지 클래스): 패키지에 소속된 클래스
  - 공개여부와 상관없이 같은 패키지에 소속된 경우 접근 가능

```
public 클래스
src/main/java
        |-com
          |-eomcs
              |-G.java  <-소스파일명과 클래스명이 일치해야 한다.
                  public class G1 {}
                  class G2 {}   <- G1과 G2는 패키지 멤버 클래스이지만 G2는 패키지에서만 사용되는 패키지 private 클래스이다.
              |-Test.java
                  class Test {}
        |-Test2.java
          |-class Test2{}
```
- 공개클래스는 유지보수할 때 소스파일을 찾기 쉽도록 하기 위해 반드시 소스파일명과 일치해야 한다.
- 한 소스파일에 public클래스가 2개 이상 존재할 수 없다.
#### 1.3.1 다른 클래스를 사용하는 소스파일을 컴파일하기
```
-소스코드-
package com.eomcs;

class Test {
  F1 v1;
  F2 v2;
}
```

```
-컴파일-
$javac 
        -d 목적폴더 
        -classpath 소스파일에서 사용하는 다른 클래스경로(.class파일이 있는 목적폴더 경로)
        -소스파일 경로

ex) $javac -d build/classes/java/main -cp build/classes/java/main src/main/java/com/eomcs/Test.java
```
#### 1.3.2 공개되지 않은 클래스는 다른 패키지에서 접근 불가!
- 다른 패키지에 소속된 경우에는 반드시 패키지명까지 적어야 한다.
- 즉 패키지소속 클래스를 사용할 때는 패키지명.클래스까지 적어서 호출 ex)com.eomcs.F1
```
-소스코드-
class Test2 {
  com.eomcs.F1 v1;
  com.eomcs.F2 v2;
}
```
<font size="6.2">
<b style="color:red">결론!</b>  
</font>  

1개 소스파일 - 공개여부에 상관없이 1개의 클래스 블록  
소스파일명 = 클래스 블록 이름 일치 시키기  
=> 클래스가 정의된 소스파일을 찾기 쉽다.  
=> 유지보수가 쉽다.  

## main() 메서드
- 프로그램 entry-point(진입점=입구)
```
$java Hello
```
1. classpath에 지정된 경로에서 Hello.class 파일을 찾는다.
  - 만약 설정안하면
  - 1. JDK 설치폴더
  - 2. -classpath 지정 옵션
  - 3. os의 classpath
  - 4. 현재 폴더 순으로 찾는다.
2. .class 파일의 bytecode를 검증한다.
3. main() 메서드 호출

## IDE(integated Development Environment; 통합개발환경)
```
+컴파일,실행,디버깅,테스팅,패킹,버전관리 등 개발의 전반적인 작업 도와준다.
단순 에디터 --------------------------> IDE  -> JDK,git, 개발 도구 사용
ex)메모장          vscode            -편집기     
vi            플러그인을 사용한         -위저드기능
nano          반자동 IDE 도구          자동화도구
```

# eclipse 설치 및 초기 설정
eomcs-java/app/src/main/java/com/eomcs/

- build.gradle 설정 (gradle이랑 섞임)
```
appication 플러그인 삭제
java 플러그인 추가
repository 설정
dependencies 설정
java 버전 설정
task.named(단위테스트 수행) 설정
task.withTypet(컴파일 옵션 설정;인코딩)
eclipse 프로젝트 이름 설정
```

- 이클립스 프로젝트 임포트할 수 있도록 필요한 파일 준비하라는 명령어
```
gradle eclipse
```

- Gradle 자바 프로젝트를 이클립스 IDE로 임포트
import 할때는 root project가 아닌 main project import
+
Eclipse IDE가 사용하는 설정파일이 필요, 없으면 import 자체가 불가
```
.settings : 프로젝트에 대한 더 상세한 설정
.classpath : 소스폴더, 목적폴더, 외부 라이브러리
.project : 어떤 프로젝트인지 알아야 에디터랑 화면을 준비하니까
```

- 설정파일은 Gradle을 통해 자동생성 가능
```
1. build.gradle에 eclipse plugin을 추가해주어야 함 
   project설정: sourceCompatibility = 문법 버전, targerCompatibility = JVM 버전
2. eclipse 설정을 해야된다.
$ gradle eclipse
3. sourceFileEncoding 설정?
```





# Java 문법
## 주석
lang/ex02/Exam0100-Exam0300



