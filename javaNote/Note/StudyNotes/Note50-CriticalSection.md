# 스레드 2

## 스레드 생명주기(Lifecycle)
```
new Thread()    start()              sleep()/wait()
  준비 -------------------> Running ---------------> Not Runnable
                              ^  |    <---------------
                              |  |    timeout/notify()
                              X  |
                              |  |  run() 메서드 종료
                              |  V
                              Dead
``` 
**Running 상태**
CPU를 받아서 실행 중이거나 CPU를 받을 수 있는 상태

**Not Runnable 상태**
CPU를 받지 않는 상태

**Dead 상태**
run() 메서드 실행종료된 상태

> run() 메서드 종료 후 다시 running 상태로 돌아갈 수 없다. 새 스레드를 실행하는 방법 밖에 없다.

### 스레드 상태 조작 메서드
*join(): 메서드를 호출한 스레드가 종료될 때까지 나머지 스레드를 기다리게 한다.  
*sleep(): 지정한 millis 동안 not runnable 상태로 만든다.

## 스레드 우선 순위 조회
getPriority()
```java
// 현재 실행 중인 스레드의 우선 순위 조회
Thread.currenThread().getPriority();
```
- 부모 스레드와 자식 스레드는 같은 값을 갖는다.
- 우선 순위가 높으면 CPU 사용 배분을 좀 더 받는다.
- Unix계열에서는 Priority 방식을 사용해서 우선순위에 따라 사용을 배분한다.
- Window OS에서는 우선 순위를 크게 고려하지 않는다(Round-Robin Scheduling)
- OS에 영향을 덛 받는 방식으로 코딩하려면 우선순위를 고려하는 방식으로 프로그래밍 하면 안된다.

## 멀티 스레딩(비동기 프로그래밍)의 문제점
**Critical Section/Critical Region**
여러 스레드가 메서드를 통해 어떤 배열의 값을 추가하려고 가정하자. 여기서 스레드는 어떤 스레드가 먼저 실행되고 어디까지 작업할지 정해져 있지 않은 상태이다. 

이때 작업이 완료되지 않은 상태에서 `Context Switching`이 발생하면서 여러 스레드가 같은 변수의 값을 변경하여 기존에 작업한 값을 덮어쓰는 문제가 발생한다.

예제
```java
public class Exam {

  static class MyList {
    int[] values = new int[100];
    int size;

    // Critical Section/Critical Region
    public void add(int value) {
      if (size >= values.length) {
        delay();
        return;
      }
      delay();
      values[size] = value;
      delay();
      size = size + 1;
      delay();
    }

    // 배열 출력
    public void print() {
      for (int i = 0; i < size; i++) {
        System.out.printf("%d:  %d\n", i, values[i]);
      }
    }

    // 컨텍스트 스위칭을 발생시키기 위해 딜레이 추가 
    public void delay() {
      int count = (int)(Math.random() * 1000);
      for (int i = 0; i < count; i++) {
        Math.atan(34.1234);
      }
    }
  }

  // 스레드 생성 클래스
  static class Worker extends Thread {
    MyList list;
    int value;

    public Worker(MyList list, int value) {
      this.list = list;
      this.value =  value;
    }

    @Override
    public void run() {
      for (int i = 0; i < 20; i++) {
        list.add(value);
      }
    }
  }
  
  public static void main(String[] args) throws Exception {
    MyList list = new MyList();

    Worker w1 = new Worker(list, 111);
    Worker w2 = new Worker(list, 222);
    Worker w3 = new Worker(list, 333);

    w1.start();
    w2.start();
    w3.start();

    Thread.sleep(10000);

    list.print();
  }
}
```
실행 결과를 확인해보면 111, 222, 0, 111 이런식으로 중간에 빈 값이 발생한다. 
size 값이 증가하기 전 `context switching`이 발생하여 다른 스레드가 메서드에 접근해 동일 인덱스의 값을 덮어씌웠기 때문에 발생한 현상이다. 
<img src="../img/critical section.png">

>이렇게 여러 스레드가 동시에 접근했을 때 문제가 발생하는 코드 영역을 `Critical Section` 또는 `Critical Region`이라 부른다.  
그리고 "Thread-Safe하지 않다" 라고 얘기한다.


*Thread Safe:
1. 여러 스레드가 동시에 작업하더라도 문제가 발생하지 않도록 조치를 했음을 의미
-> 여러 스레드가 같은 변수의 값을 동시에 변경하려 할 때 한 번에 한 스레드만이 작업하도록 제한하는 것
2. 여러 스레드가 동시에 진입해서 명령을 실행하더라도 문제가 발생하지 않는 코드 
-> 변수의 값을 조회만 하는 코드

## 멀티 스레딩(비동기 프로그래밍) 문제 해결
Critical Section에 오직 한 개의 스레드만 접근하게 하면 비동기로 인한 문제가 발생하지 않는다. 즉 동기화로 처리한다

*동기화란 여러스레드가 순차적으로 접근하는 것이다. 단 순차적으로 실행하면 동시 실행의 이점을 버리고 기존 실행방식처럼 실행한다.

### synchronized
크리티컬 섹션 구간에 `synchronized` 키워드를 붙이면 오직 한 번에 한 개의 스레드만이 접근할 수 있게 된다. 

구체적으로는 특정 객체의 `synchronized` 블록에 진입하면 해당 객체에 대한 락이 획득되어 다른 스레드들은 메서드나 블록에 접근하지 못하게 된다.

```java
synchronized public void add(int value) {...}
```

이렇게 크리티컬 섹션에 동시에 접근하지 못하게 하는 기법을 `뮤텍스()` 또는 `세마포어(1)(semaphore)`라 부른다. 

**세마포어(semaphore)**
critical section에 접근하는 스레드의 수를 제어하는 기법이다. 보통 semaphore(동기화 스레드 수)의 형식으로 표시한다.
예를 들어 semaphore(3)은 동시에 3개의 스레드가 접근할 수 있다는 의미이다. 사실상 Thread-safe하지 않은 코드에서는 semaphore(1), 즉 mutex만 중요하다. 

**mutex**
criticcal section 오직 한 개의 스레드만이 접근할 수 있도록 제어하는 기법이다. semaphore(1)과 같으며, 자바에서 synchronized가 critical section을 뮤텍스로 선언해준다. 

## synchronized 특징
synchronized의 진정한 역할은 같은 값에 대한 변수를 변경하지 못하도록 막는 것이다. 

스태틱 메서드, 인스턴스 메서드 상관없이 synchronized를 적용할 수 있다.

만약 같은 값이 아니라 다른 값에 대해 변경을 한다면, 이에 대한 접근은 synchronized가 막지 않는다. 다른 값이면 덮어씌워질 위험이 없기 때문에 Thread-safe하기 때문이다.

아래는 다른 인스턴스 객체에 대해 두 스레드가 접근하는 예제이다.
```java
public class Exam {
  public static void main(String[] args) {
    Job job1 = new Job();
    Job job2 = new Job();

    Worker w1 = new Worker("홍길동", job1);
    Worker w2 = new Worker("임꺽정", job2);

    w1.start();
    w2.start();
  }

  static class Job {
    void play(String threadName) throws Exception {
      System.out.println(threadName);
      Thread.sleep(10000);
    }
  }

  static class Worker extends Thread {
    Job job;
    public Worker(String name, Job job) {
      super(name);
      this.job = job;
    }

    @Override
    public void run() {
      try {
        job.play(getName());

      }catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
```
W1과 W2 스레드는 서로 다른 객체인 `job1`과 `job2` 객체를 참조하여 run()이라는 메서드를 사용한다.

<img src="../img/synchronized2.png">

실행 결과, 같은 메서드처럼 보여도 다른 객체이기 때문에 동시에 이름이 출력되는 것을 알 수 있다. 

반대로 서로 다른 여러 메서드가 같은 변수에 접근하는 경우에는 `Critical Section`이 발생한다. 

이에 대한 예제는 다음과 같다. 
```java
public class Exam610 {
  public static void main(String[] args) {
    Job job = new Job();

    Worker1 w1 = new Worker1("홍길동.play1() 호출", job);
    Worker2 w2 = new Worker2("임꺽정.play2() 호출", job);
    Worker3 w3 = new Worker3("분홍알.play3() 호출", job);

    w1.start();
    w2.start();
    w3.start();

  }

  static class Job {
    void play1(String threadName) throws Exception {
      System.out.println(threadName);
      Thread.sleep(10000);
    }

    void play2(String threadName) throws Exception {
      System.out.println(threadName);
      Thread.sleep(10000);
    }

    void play3(String threadName) throws Exception {
      System.out.println(threadName);
      Thread.sleep(10000);
    }
  }

  static class Worker1 extends Thread {
    Job job;
    public Worker1(String name, Job job) {
      super(name);
      this.job = job;
    }

    @Override
    public void run() {
      try {
        job.play1(getName());

      }catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  static class Worker2 extends Thread {
    Job job;
    public Worker2(String name, Job job) {
      super(name);
      this.job = job;
    }

    @Override
    public void run() {
      try {
        job.play2(getName());

      }catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  static class Worker3 extends Thread {
    Job job;
    public Worker3(String name, Job job) {
      super(name);
      this.job = job;
    }

    @Override
    public void run() {
      try {
        job.play3(getName());

      }catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
```
위 예제에서 w1, w2, w3은 각각 play1(), play2(), play3() 메서드를 호출한다. 하지만 세 메서드 모두 같은 객체의 threadName을 참조하고 있기 때문에 문제가 발생할 수 있다. 

<img src="../img/synchronized2.png">
따라서 이를 동기화하려면 세 메서드 모두 synchronized 키워드를 선언해야 한다. 

>결국 synchronized 사용 시 가장 핵심이 되는 점은 Critical Section이 발생할 때 메서드가 아니라 객체 또는 스태틱 변수 수준에서 선언을 고려해야 한다는 것이다.