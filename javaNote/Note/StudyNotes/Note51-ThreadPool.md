# 스레드 3

## 스레드 생성과 가비지
실행이 완료되어 Dead 상태가 된 스레드는 가비지가 된다. 가비지 컬렉터가 그 스레드를 해제시키기 전까지는 메모리를 계속 차지하게 된다. 

즉 스레드를 매번 새로 생성하는 방식은 과다한 가비지를 생성하기 때문에 메모리 낭비가 심하다. 

또 스레드는 실제 OS가 생성한다. 스레드를 생성하는 데에도 시간이 소요되고 자주 생성할수록 시간도 많이 소요하게 된다.

>실제 기업에서는 몇 개의 스레드, 몇 개의 가비지가 아니라 수십 수천 많으면 수백만에 이르는 클라이언트 요청이 발생하는데 이에 매번 스레드를 생성하여 처리하는 방법은 실행시간과 메모리 효율성이 떨어진다. 

## 스레드 재사용
스레드를 일회성으로 사용하는 방법이 비효율적이라면 개선방법은 간단하다. "재사용"하면 된다. 

문제는 스레드의 run() 메서드 실행이 종료되면 Dead 상태가 되어 다시 start() 할 수 없다는 것이다. 다시 시작하려고 하면 IllegalThreadStateException 예외가 발생한다. 

따라서 스레드가 종료되지 않도록 제어하여야 한다. 

**sleep()/timeout**
먼저 sleep()을 활용한 스레드 재활용 방식이다. 이 방식은 스레드를 실행하자마자 스레드를 재우고 그 시간동안 작업 내용을 설정해두면 sleep()이 종료되었을 때 설정해둔 작업들을 수행하도록 한다. 
```java
public class Exam0120 {

  public static void main(String[] args) {

    class MyThread extends Thread {
      int count;

      public void setCount(int count) {
        this.count = count;
      }

      @Override
      public void run() {
        System.out.println("스레드 시작했음!");
        try {
          while (true) {
            Thread.sleep(10000);

            System.out.println("카운트 시작!");
            for (int i = count; i > 0; i--) {
              System.out.println("==> " + i);
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    MyThread t = new MyThread();

    // 미리 스레드를 시작시켜 놓는다.
    t.start();

    Scanner keyScan = new Scanner(System.in);

    while (true) {
      System.out.print("카운트? ");
      String str = keyScan.nextLine();

      if (str.equals("quit")) {
        break;
      }

      int count = Integer.parseInt(str);
      t.setCount(count); 
    }

    System.out.println("main 스레드 종료!");
    keyScan.close();
  }
}
```
이 방식은 하나의 스레드를 재사용하여 작업을 처리할 수 있지만. 스레드가 깨어날 때까지 작업이 바로 실행되지 않고, 작업을 시키고 싶지 않아도 깨어나면 무조건 실행한다. 즉 작업을 원하는 타이밍에 실행하는 세밀한 제어가 불가능하다는 문제점이 있다. 

**sleep()/timeout 개선**
enable 변수를 추가하여 setCount할 때 true, run() 메서드 작업이 끝날 때 false로 설정하도록 한 후 true일 때만 작업하도록 한다. 
```java
...

boolean enable;

public void setCount(int count) {
  this.count = count;

  // setCount할 때 작업 활성화
  this.enable = true;
}

...

@Override
public void run() {
  System.out.println("스레드 시작했음!");
        try {
          while (true) {
            System.out.println("스레드를 10초 동안 잠들게 한다!");
            Thread.sleep(10000);

            // enable이 true일 때만 작업
            if (!enable) {
              continue;
            }
            ...

            // 작업 후 비활성화
            enable = false;

            ...
}
```
이전 버전과 비교하여 작업이 종료해도 계속 반복되는 문제는 해결했다. 하지만 sleep()의 한계로 작업 전후 10초동안 스레드가 멈추는 근본적인 문제는 해결할 수 없다.

이를 전문적으로 처리해주는 문법이 wait(), notify()이다.

**wait()/notify()**
wait() 메서드는 스레드가 대기 상태(Not Runnable)로 들어가도록 만든다.  
notify() 메서드는 wait()로 대기 상태에 있는 스레드 중 하나를 깨워 다시 동작시킨다.  

wait()과 notify()는 비동기화 영역에서 호출될 경우 IllegalMonitorStateException 예외가 발생하기 때문에 반드시 synchronized 키워드가 선언된 메서드 또는 블록에서 호출되어야 한다.

위 두 가지 메서드를 적용하면 개발자가 원하는 타이밍에 스레드를 멈추고 다시 실행하는 것이 가능해진다.

예로 위의 예제에서 sleep()과 enable 변수가 있는 부분을 wait()와 notify()로 교체한다면 작업을 끝낼 때 스레드가 not runnable 상태가 되고, setCount() 메서드가 호출되어 count 값이 변경된 후 다시 스레드가 실행되도록 할 수 있다.
```java
public class Exam0140 {

  public static void main(String[] args) {

    class MyThread extends Thread {
      int count;

      synchronized public void setCount(int count) {
        this.count = count;
        this.notify();
      }

      @Override
      public void run() {

        System.out.println("스레드 시작했음!");
        try {
          while (true) {
            System.out.println("스레드 대기중...");

            synchronized (this) {
              wait();
            }

            if (count == -1) {
              return;
            }

            System.out.println("카운트 시작!");
            for (int i = count; i > 0; i--) {
              System.out.println("==> " + i);
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    MyThread t = new MyThread();
    t.start(); // 이 스레드는 main 스레드가 실행하라고 신호를 줄 때까지 기다린다

    Scanner keyScan = new Scanner(System.in);

    while (true) {
      System.out.print("카운트? ");
      String str = keyScan.nextLine();
      if (str.equals("quit")) {
        t.setCount(-1); // 스레드를 종료하도록 count를 -1로 설정
        break;
      }
      t.setCount(Integer.parseInt(str));

    }

    System.out.println("main 스레드 종료!");
    keyScan.close();
  }
}
```

## Pooling 기법(=GoF의 flyweight 패턴)
그냥 wait()와 notify()만 사용하는 것으로는 아직 문제점이 남아있다.

리소스 효율성이 떨어진다. wait(), notify()로 스레드를 제어하기 전에 스레드의 생성, 소멸하는 과정을 유연하게 설계할 수 없으며 오버헤드가 발생할 수 있다.

*오버헤드: 어떤 작업을 수행하는데 필요한 추가적인 비용, 부하(시간, 메모리 등)

예를 들면 위처럼 코드 작성 시 정해진 스레드만 사용해야 하는 경우 유연성이 떨어지고, 무한 루프에 객체를 생성하는 코드를 넣는 경우에는 wait(), notify()를 하는 의미가 사라진다.

Pooling 기법은 이러한 리소스를 효율적으로 관리하기 위한 기법으로, 미리 생성된 리소스(객체)를 풀(Pool)에 저장해두고 필요할 때마다 풀에서 리소스를 가져다 사용하는 방식이다. 

**스레드 사용**
<img src ="../../img/Pooling1.png">

**스레드 반납**
<img src ="../../img/Pooling2.png">



### Thread Pool 구현
기존 myapp에 Pooling 기법을 적용

<img src="../../img/myAppThreadPool.png">

**- ThreadPool**  
ThreadPool은 스레드 객체인 WorkerThread를 저장하는 객체이다.  
List 자료구조를 통해 초기 사이즈를 입력받아 객체를 생성(create), 리턴(return), 반납(revert) 받는 역할을 한다. 
```java
import java.util.ArrayList;
import java.util.List;

public class ThreadPool implements Pooling<WorkerThread> {

  List<WorkerThread> list = new ArrayList<>();

  public ThreadPool() {
  }

  // 처음에 스레드를 미리 일정 개수 생성해 두기
  public ThreadPool(int initSize) {
    if (initSize <= 0 || initSize > 100) {
      return;
    }
    for (int i = 0; i < initSize; i++) {
      list.add(create());
    }
  }

  @Override
  public WorkerThread get() {
    if (list.size() > 0) {
      return list.remove(0);
    }
    WorkerThread thread = new WorkerThread(this);
    thread.start();
    return thread;
  }

  @Override
  public void revert(WorkerThread workerThread) {
    list.add(workerThread);
  }

  private WorkerThread create() {
    WorkerThread thread = new WorkerThread(this);
    thread.start();
    try {
      Thread.sleep(500); // 스레드가 wait 할 시간을 확보한다.
    } catch (Exception e) {
    }
    System.out.printf("새 스레드 생성!(%s)\n", thread.getName());
    return thread;
  }
}
```
**- WorkerThread**  
WorkerThread는 Java.lang.Thread를 상속받아 만든 스레드 객체이다.  
작업을 완료한 스레드를 pool에 저장하고 unidirectional(단방향 참조 관계)로 만들기 위해 생성자로 Pooling 객체를 받았으며, play() 추상메서드를 가진 Worker를 setWorker로 받아 인터페이스 구현체를 사용한다. 

스레드 객체를 생성하고 난 직후 wait() 메서드를 호출하여 대기시킨 다음, setWorker 호출 시 notify()로 깨워 아래 Worker의 Play() 작업을 실행시키는 구조이다. 

```java
package bitcamp.util;

public class WorkerThread extends Thread {

  Pooling<WorkerThread> pool;
  Worker worker;

  public WorkerThread(Pooling<WorkerThread> pool) {
    this.pool = pool;
  }

  synchronized public void setWorker(Worker worker) {
    this.worker = worker;

    this.notify();
  }

  @Override
  public void run() {
    try {
      while (true) {
        synchronized (this) {
          this.wait();
        }
        try {
          worker.play();
        } catch (Exception e) {
          System.out.println("클라이언트 요청 처리 중 오류 발생");
          e.printStackTrace();
        }
        // 작업을 완료했으면 다시 pool로 돌아간다.
        pool.revert(this);
      }
    } catch (Exception e) {
      System.out.println("스레드 예외 발생");
      e.printStackTrace();
    }
  }
}
```


**- ServerApp Pooling 기법 적용**
```java
public static void main(String[] args) {
    new ServerApp().run();
  }

  void run() {
    System.out.println("[과제관리 서버시스템]");

    try (ServerSocket serverSocket = new ServerSocket(8888)) {

      System.out.println("서버 실행!");

      while (true) {
        Socket socket = serverSocket.accept();
        // 1. 익명클래스 Worker 인터페이스 구현체 사용
//        WorkerThread t = threadPool.get();
//        t.setWorker(new Worker() {
//          @Override
//          public void play() {
//            service(socket);
//          }
//        });

        // 2. 람다 문법 리팩토링
//        WorkerThread t = threadPool.get();
//        t.setWorker(() -> service(socket));

        // 3. 최종
        threadPool.get().setWorker(() -> service(socket));

      }
    } catch (Exception e) {
      System.out.println("통신 오류!");
      e.printStackTrace();
    }
  }
```


# Java에서 제공하는 스레드풀 프레임워크
## ExcutorService

