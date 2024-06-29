# 자바 프로젝트 디렉토리

## 1. 소스 폴더와 빌드 결과 파일(바이너리) 폴더로 분리
관례적으로 소스 폴더는 src, 빌드 결과 파일(바이너리) 폴더는 bin으로 설정  
  
- 컴파일 시 원하는 폴더에 결과를 생성하는 명령어
```
javac -d bin src/Hello.java 
        |       소스파일 경로
    목적파일 경로

$javac -d build/classes/java/main app/src/main/java/com/eomcs/D.java
```
  
- 바이트코드 실행, 클래스 파일이 있는 폴더 지정
```
$java -classpath bin Hello
$java -cp bin Hello

$java -cp build/classes/java/main com.eomcs.E1
```

### git 저장소와 프로젝트 폴더
1. git 저장소 = 프로젝트 폴더  
- 통상적으로는 이 방법
```
프로젝트 폴더(repository)/src/
```

2. git 저장소 = 여러 프로젝트 폴더  
- 메인 프로젝트 1, 서브 프로젝트 여러개일때 사용
```
프로젝트 폴더/프로젝트A/src/  
프로젝트 폴더/프로젝트B/src/  
프로젝트 폴더/프로젝트C/src/  
```

## 2. Maven 표준 프로젝트 디렉토리 구조
src 폴더에 소스 파일 말고 설정파일이 섞이게 되면서 Maven 표준 프로젝트 디렉토리 구조가 등장

```
프로젝트 폴더/src/main/      -> App 소스파일을 두는 폴더
                  |-java/      -> 자바 소스파일(.java)
                  |-resources/ -> 설정파일(.xml, .properties)
                  |-kotlin/    -> 코틀린 소스파일(.kt)
프로젝트 폴더/src/test/ - 단위테스트 소스파일을 두는 폴더
                  |-java/      
                  |-resources/ 
                  |-kotlin/    
*단위테스트: 소스코드의 특정 모듈(메서드)이 의도된 대로 정확히 작동하는지 검증하는 절차
```

### 2.1 Gradle 설치
```
$ brew install gradle 
```
### 2.2 Gradle 사용, 디렉토리 구성
- Gradle 빌드도구를 사용하여 위 구조를 자동생성할 수 있다. 
```
$ gradle init
```
*DSL: 특정 영역에 코드를 작성할 때 사용할 언어 ex).java, .kotlin
                                    ex) JSP <- DSL(Java)

- gradle init 디렉토리 구성
```
java-basic/  <- root project
          |- .gradle/    <- gradle 설정파일. 로컬마다 다를 수 있다. 공유 X
          |- app/        <- main project
                |- src/
                       |- main/
                              |- java
                              |- resources
                       |- test/
                              |- java
                              |- resources
                |- build.gradle <- gradle 빌드 스크립트 파일
          |- gradle/    <- gradle을 로컬에 설치해주는 도구. 로컬에 gradle이 설치 안된 경우에 사용된다.
          |- gradlew     <-(Linux/Unix용)  |  로컬에 gradle이 설치되지 않은 경우에 사용
          |- gradlew.bat <-(Windows)      | 
          |- settings.gradle <- gradle 빌드 스크립트 보조파일
          |- .gitattributes |  git 관련 파일
          |- .gitignore     |
```
*파일이 없으면 백업이 안됨 그래서 resource 백업을 원하면 파일을 둬야함

## 3. Gradle 구동 원리
- 컴파일러 사용하려면 사용법을 자세히 알아야 한다. ex)컴파일 옵션 -d, -cp
- gradle을 사용하면 컴파일을 대신 해줌, 그러려면 gradle 빌드 스크립트 작성법을 알아야 한다.

### 3.1 Gradle tasks
- 명령어들 출력
```
gradle tasks          |  gradle로 수행할 수 있는 작업 목록 출력
gradle tasks --all    |
```
- 구조
1. 읽기
2. 빌드 스크립트에 설정된 정보를 바탕으로 실행

### 3.2 Gradle 빌드 스크립트 파일(bulid.gradle) 구조
plugin? <- gradle 실행할 때 사용할 플러그인
```
id '플러그인명'

ex) 'java' 플러그인 -> 자바 소스 빌드 작업을 수행
    'eclipse' 플러그인 -> Eclipse IDE 관련 작업 수행
    'application' 플러그인 -> java 플러그인 + 실행관련 작업 수행
    'jar' 플러그인 -> 패킹 작업을 수행
```
  
repositorys? <- 외부 라이브러리를 다운로드 받을 서버 정보
  
dependencies? <- 외부 라이브러리 정보


### 3.3 Gradle 'java' gradle plugin 사용법
1. compileJava:  애플리케이션 소스 컴파일
프로젝트폴더/app/src/main/java/ <- 이 폴더에 있는 자바 소스를 컴파일한다.  
build 폴더 생성
build/classes/java/main/ <- 이 폴더에 .class 파일을 생성  
```
gradle 없이 실행
app/src/main/java
$java -cp app/build/classes/java/main Hello

                   패키지 폴더
app/src/main/java/com/eomcs
$java -cp app/build/classes/java/main com.eomcs/App
패키지 폴더는 구분해서 명령어 쳐야함, 패키지 폴더를 가리킬때는 . 을 사용
```

2. processResources:  실행할 때 사용할 설정파일을 build폴더에 복사.
프로젝트폴더/app/src/main/resources 파일을 그대로 build폴더에 복사한다.

3. classes:  application 실행할 수 있도록 컴파일
- 1)compilejava 실행
- 2)processResources 실행

4. compileTestJava:  단위 테스트 소스 컴파일
프로젝트폴더/app/src/main/java/ 
프로젝트폴더/app/src/test/java/ <- main과 test 둘 모두 compile한다.  
build/classes/java/main/  
build/classes/java/test/  <- .class파일도 main과 test 모두 담는다.  
즉 compilejava도 같이 수행한다.

- 1)compileJava 실행
- 2)processResources 실행
- 3)classes 실행
- 4)compileTestJava
src/test/java 컴파일 해서 build/classes/java/test/ <- 에 단위테스트 .class파일을 둔다.

5. processTestResources:  단위 테스트 설정파일을 복사.
프로젝트폴더/app/src/test/resources 파일을 build폴더에 그대로 복사함

6. testClasses:  단위 테스트 관련 파일 컴파일
- 1)compileJava 실행
- 2)processResources 실행
- 3)classes 실행
- 4)compileTestJava 실행
- 5)processTestResources 실행
- 6)testClasses 실행

7. test:  ***
- 1)compileJava 실행
- 2)processResources 실행
- 3)classes 실행
- 4)compileTestJava 실행
- 5)processTestResources 실행
- 6)testClasses 실행
- 7)test 실행
    ->단위 테스트를 실행한 후에 보고서를 생성한다.
    -> build/reports/test/보고서

8. jar: 애플리케이션 패킹
- 1)compileJava 실행
- 2)processResources 실행
- 3)classes 실행
- 4)jar 패킹 
    -> build/libs/*.jar 생성

9. build:  
- 1)test <- 애플리케이션 컴파일 및 단위테스트 실행
- 2)jar  <- 배포파일 생성(패킹)

10. clean:  빌드 폴더 삭제

11. run: 'application' 플러그인 명령어
- 1)compileJava 실행
- 2)processResources 실행
- 3)classes 실행
- 4)run    <- 빌드 스크립트 파일에 지정된 메인클래스 실행 
## Java plugin task 구조
<img src="../img/javaPluginTasks.png">
  
<br> </br>



## 0. 클래스 블록과 .class파일
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


