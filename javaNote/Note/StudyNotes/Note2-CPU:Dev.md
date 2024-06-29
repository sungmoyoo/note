# 프로그램 실행구조
 ## 1.CPU와 기계어
  ### 1.1 기계어(Instruction Set)  
  컴퓨터는 0과 1로 된 전기 신호, 즉 기계어만 이해할 수 있다.  
  application에서 명령을 입력하면 CPU에서 실행하여 결과값을 돌려준다. 
  
  ### 1.2 CPU와 기계어  
  한 CPU에서 이해할 수 있는 기계어 명령으로 작성된 프로그램은 해당 CPU에서만 실행시킬 수 있다.  
ex)Intel, Arm, Qualkomm
  
  ### 1.3 os와 실행파일
  또한 형식도 존재한다. 프로그램은 각 운영체제(os)에 맞는 형식에 따라 구성하므로 해당 운영체제에 맞는 형식과 구동이 가능한 CPU에서만 프로그램이 작동한다.  
ex)window = PE(Portable Executable)포맷
           mac = Mach-O(Mach Object)포맷
           linux = ELF 포맷

## 2. APP. 만들기
1. 기계어 = 굉장히 어렵다. ex) 0000 11xx xxxx xxxx
2. 그래서 나온 것이 Assembly언어. ex) add r1, r2
    - Assembly언어는 기계어 형식과 흡사한 문법으로서 Low-level Language이다. 
    - 사람이 어셈블리어를 작성하면 compiler가 기계어로 compile(번역)하는 형태
3. 기계어와 흡사하다는 특징때문에 os에 종속적 -> os마다 약간 다르다. -> os마다 따로 작성 -> 불편. 따라서 <b style ="color:red">os에 상관없이 같은 문법으로 명령을 작성할 필요성에 의해 등장한 언어가 바로 C언어이다.</b>
    - C언어는 데니스 리치가 창시한 언어, 브라이언 커닝핸은 책 제작을 주도, 인간에게 친숙한 문법인 High-level Language이다. 
    - C언어도 어셈블리어와 마찬가지로 compiler가 존재한다. 하지만 바로 기계어로 compile 하지 않고 어셈블리어로 compile한후 어셈블리어를 어셈블리어 컴파일러로 다시 기계어로 compile한다.  

    **한번에 기계어로 번역하지 않는 이유:**
    각 운영체제에 맞는 기계어로 컴파일 하기 위해 어셈블리어로 먼저 컴파일 한다. 즉 어떤 프로그램 제작자던 간에 기계어로 바로 컴파일 하는 것보다 어셈블리어로 컴파일하는게 쉽다.

## 3. Java App. 만들기
자바 소스 파일이 실행되는 과정
```
1. 자바 언어로 소스파일 작성 - 확장자 .java
2. Java Compiler가 컴파일하여 java.class라는 bytecode(p-code) 명령 생성
3. 여기서 바이트코드(.class)는 os에서 바로 실행이 안되기 때문에 JVM(Java Virtual Machine)에 입력하여 실행  

따라서 자바 소스 파일을 실행하기 위해서는 Javac(자바 컴파일러)와 JVM(Java)가 필요하다
```

### 3.1 Java의 단점
1. 컴파일에 대한 불편함
- 자바의 캐치프레이즈  
모든 플랫폼(os+cpu)에서 하나의 코드로 통일한 결과을 얻기  
-> "Write once, Run Anywhere!"
- 이러한 자바의 캐치프레이즈로 인해 각 os 및 cpu별로 컴파일을 해야 하는 불편함이 있다.

2. 실행속도
- 개발자는 한번만 컴파일 하면 되지만 JVM에서는 기계어를 직접 실행하는 방식이 아니기 때문에 실행속도가 느리다. 

### 3.2 Java 개발도구
**제품분류**
- Java SE(Standard Edition)
  - JDK: 컴파일러, 디버거, 프로파일러 등 개발도구 + JRE
    - JRE: JVM + 실행관련파일
- Java EE(Enterprise Edition) => 기업용 서버 APP. 제작에 필요한 도구
- Java ME(Micro Edition) => 임베디드 APP. 제작에 필요한 도구

### 3.3 JDK 설치 및 설정
- JDK 설치
1. 사이트에 접속하여 os, cpu에 맞는 JDK 다운로드 및 설치
2. 환경 변수
```
1. JDK 설치 경로를 OS에 등록.  
-> 어느 디렉토리에서도 JDK 도구를 실행할 수 있도록) ex) PATH=JDK/bin;---
2. JDK 설치 경로를 'JAVA_HOME'이라는 이름으로 등록  
-> Java App.이 쉽게 JDK를 찾을 수 있도록, 즉 자바 애플리케이션이 jdk가 설치된 위치를 알려주기 위해 설정하는 것. 
```
- os별 설명
```
윈도우의 경우 시스템 변수에 찾아보기 버튼을 눌러 설정
-> PATH편집, 찾아보기.. c/../bin까지
새로만들기로 JAVA_HOME 생성.. c/../jdk-버전까지..

맥의 경우 터미널을 통해 CLI 해야 함
-> nano .zshrc
-> export JAVA_HOME=$(/usr/libexec/java-home -v 21)
*주소 찾아 쓰는 저거는 리눅스에서 안됨
```

jshell은 자바용 repl
read -> evaluation-> print -> loop 순으로 진행되며 문법을 확인하기 위한 과정이다. 

## VSCode
codelens, whitespace, inlay 설정

## Github 저장소(Repository) 생성
공개, 리드미 파일 생성



