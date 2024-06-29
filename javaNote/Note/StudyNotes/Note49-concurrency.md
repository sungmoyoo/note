# 병행 처리(Concurrency)
여러 프로세스/작업을 빠르게 전환하여 마치 동시에 실행되는 것처럼 보이도록 하는 기술.

*동시에 여러 작업을 처리하는 기술은 병렬 처리라고 하며, 보통 다중 프로세서 또는 다중 코어 시스템의 개념이며, 이에 대한 알고리즘은 병행 처리에 비해 매우 난해하다.

## 병행 처리를 고려하는 경우
**1. 작업 순서에 상관없는 경우**  
서로 간섭하지 않는 작업이라면 병행처리할 수 있다. 반대로 순서를 지켜 수행해야 하는 경우 병행처리할 수 없다. 

예를 들어 여러 개의 독립적인 파일을 읽거나, 여러 사용자로부터 동시에 요청이 들어오는 기능을 구현할 때 병행 처리를 고려할 수 있다.

**2. 같은 작업을 여러번 수행해야 하는 경우**  
데이터를 여러 번 처리해야 하는 경우, 각각의 작업을 병행 처리하여 전체 작업 시간을 단축할 수 있다. 

예를 들어 데이터베이스 쿼리, 이미지 처리, 계산 작업 등에서 유용하다. 

>결론적으로 병행 처리는 작업 시간 효율성 측면에서 사용을 고려한다.  
병행 실행 시간의 총합과 순차 실행 시간의 총합을 비교하여 병행 처리 적용 여부를 판단한다. 

## 병행처리 방법
<img src="../img/Concurrency.png">

<br>

**1. 프로세스 복제(Concurrent)**  
프로세스 복제는 프로세스 자체를 복제하여 운영체제에 의해 별도의 메모리 공간을 할당받고 코드와 Heap 메모리 모두 복제한다.

C언어에서 fork() 명령어를 통해 프로세스 복제를 구현할 수 있다. 
```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
  int i = 0;
  pid_t processId = 0;
  
  processId = fork(); // 현재 실행 실행 위치에서 프로세스 복제
  
  for (i = 0; i < 10000; i++) {
    printf("[%d] ==> %d\n", processId, i);
    int temp = rand() * rand();
  }
  
  return 0;
}
```
- 복제된 프로세스가 원본 프로세스와 다른 동작을 할 수 없다
- 코드와 메모리가 같이 복제되기 때문에 메모리 소비가 크다.

<br>

**2. 스레드**  
하나의 프로세스에서 실행되는 실행 흐름(단위)로 프로세스의 힙 메모리 등의 자원은 공유하고 코드 실행에 필요한 스택 메모리와 실행 위치정보는 스레드 별로 관리하는 방법이다. 

- 코드와 인스턴스 데이터는 공유하기 때문에 프로세스 복제 방법에 비해 메모리 사용이 적다.

# 스레드 1
## 실행 과정
<img src="../../img/ThreadStart.png">

<br>

- JVM이 클래스 파일을 실행하여 생성된 main이라는 Thread 객체는 실행 위치 정보를 담고 있다. 코드가 아니다.
- main Thread 객체가 가진 실행 위치 정보는 main() 메서드를 찾아 실행하는데 사용된다. 
- main() 메서드에서 새 스레드를 생성하는 코드가 실행되면 새 스레드 객체로 분리되어 독립적인 흐름을 가지며 오버라이딩한 run() 메서드를 기존 실행흐름과 병행하여 실행한다. 
- 이때 새 스레드를 만든 실행흐름을 Parent Thread라 하고, 분리된 스레드를 Child Thread라고 한다. 
- Parent Thread가 먼저 종료된다 하더라도 프로세스는 종료하지 않고 모든 스레드의 실행이 끝나야 종료된다.

## 현재의 실행 라인 알아내기
```java
Thread t = Thread.currentThread();
System.out.println("실행 흐름명 = " + t.getName());
```

## JVM의 전체 스레드 계층도
JVM의 최상위 스레드 그룹인 system의 계층도 재귀 호출 방식으로 출력하기
- currentThread() : 현재 실행 라인 리턴하는 메서드
- mainThread.getThreadGroup(): 현재 실행 라인이 소속된 스레드 그룹을 리턴하는 메서드
- mainGroup.getParent(): 스레드 그룹의 부모 그룹을 리턴하는 메서드
- enumerate(스레드 배열, false/true): 스레드 그룹에 소속된 하위 스레드를 배열에 저장하는 메서드. false로 설정하면 하위 그룹에 소속된 스레드는 제외한다. 리턴값은 length이다.
- enumerate(스레드그룹 배열, false/true): 스레드 그룹에 소속된 하위 그룹을 배열에 저장하는 메서드. false 조건, 리턴값은 동일하다.
```java
public class Exam0180 {
  public static void main(String[] args) {
    
    Thread mainThread = Thread.currentThread(); 
    ThreadGroup mainGroup = mainThread.getThreadGroup(); 
    ThreadGroup systemGroup = mainGroup.getParent(); // 

    printThreads(systemGroup, "");
  }

  static void printThreads(ThreadGroup tg, String indent) {
    // 현재 실행 라인 이름을 출력
    System.out.println(indent + tg.getName() + "(TG)"); 

    // 현재 스레드 그룹에 소속된 스레드들 출력하기
    Thread[] threads = new Thread[10];
    int size = tg.enumerate(threads, false); // 
    for (int i = 0; i < size; i++) {
      System.out.println(indent + "  ==> " + threads[i].getName() + "(T)");
    }

    // 현재 스레드 그룹에 소속된 하위 스레드 그룹들 출력하기
    ThreadGroup[] groups = new ThreadGroup[10];
    size = tg.enumerate(groups, false);
    for (int i = 0; i < size; i++) {
      printThreads(groups[i], indent + "  ");
    }
  }
}
```

**JVM의 스레드 계층도: (Oracle JDK 21 기준)**
맥OS 기준
```
system(TG)  
  ==> Reference Handler(T)  
  ==> Finalizer(T)  
  ==> Signal Dispatcher(T)  
  ==> Attach Listener(T)  
  ==> Notification Thread(T)  
  ==> main(TG)  
        ==> main(T)  
  ==> InnocuousThreadGroup(TG)  
        ==> Common-Cleaner(T)  
```

## 멀티 태스킹
동시에 여러 작업을 실행하는 것(=병행 처리:concurrency)

>별도의 CPU로 실행 = 완전한 동시 실행
하나의 CPU로 돌아가면서 실행 = 불완전 동시 실행

프로세스 복제, 스레드 모두 동시에 작업하는 것처럼 보이지만 실제로 동시 실행하는 방식이 아니다. 하나의 CPU로 돌아가면서 실행한다.

여러 프로세스를 번갈아가며 실행해도 동시에 실행하는 것처럼 보이는 이유는 CPU의 작업 처리 속도가 매우 빠르기 때문이다.

이처럼 프로세스를 번갈아가며 실행하는 규칙을 CPU Scheduling이라 하며 OS에서 관리된다.

**대표적인 CPU Scheduling 방식**  
1. Round-Robin(Window): 시분할 실행
- 일정 시간을 돌아가면서 실행
- 각 프로세스마다 균등한 시간이 배분된다.

2. Priority + Aging(Unix/Linux): 
- 우선순위가 높은 프로세스를 더 자주 실행

## Context Switching
CPU의 실행시간을 쪼개 코드를 번갈아가며 실행할 때, 실행 위치 및 정보(context)를 저장하고 로딩하는 과정을 컨텍스트 스위칭이라 한다.

여기서 주의할 점은 너무 짧은 간격으로 스위칭하면 명령을 실행하는 시간보다, 컨텍스트 스위칭하는데 시간을 다 낭비하게 된다.  
=> 컨텍스트 스위칭 시간을 최적화하는 것이 중요(OS가 고민할 문제)