# 1. 인터페이스 문법
여러 명의 개발자가 개발할 때 메서드명을 제각각 설정하지 않고 일관성 있게 통일하여 클래스, 객체를 호출할 수 있도록 강제해야 한다. 
즉 인터페이스는 객체 사용 규칙을 정의하는 문법

1. 인터페이스 파일 생성
2. 인터페이스 문법으로 메뉴를 다루는 객체의 실행규칙 정의
```
public interface Menu {
  public abstract void execute();
}
```
3. 인터페이스에 정의한 대로 메뉴를 다루는 객체를 구현
```
Menu boardMenu = new BoardMenu("게시판", this.prompt)
...
```
4. 인터페이스에 정의한 대로 메뉴를 다루는 객체를 실행
```
boardMenu.execute();
```

## 1.1 인터페이스 객체
```
// 인터페이스는 규칙을 정의한 것이기에 객체/인스턴스 생성 불가.
Menu m = new Menu(); (X)
```

## 1.2 인터페이스 레퍼런스
```
인터페이스 규칙에 따라 정의된 클래스의 인스턴스 주소를 담을 수 있다.
     |
Menu m = new BoardMenu(); (O)
                |
             인터페이스를 구현한 클래스
             인터페이스 규칙에 따라 정의한 클래스

Menu m = new Strong();
              |
인터페이스 규칙에 따라 만든 클래스가 아니다.
```

## 1.3 인터페이스 객체 사용
```
Menu m = new AssignmentMenu
m.exeute();  -  인터페이스 규칙에 선언된 메서드

m.add();  | - Menu 인터페이스에 선언된 메서드가 아니다.
m.view(); |   레퍼런스 타입에 선언된 메서드만 호출 가능
```

# 2. Gof(Gang of Four)의 Composite 패턴을 이용하여 메뉴를 구성하기
- 메뉴 간의 연결을 느슨하게 조정하기
  - MainMenu와 인터페이스 객체들 사이에 coupling이 강결합되어 있다.
    - 새 기능 추가 시 기존 클래스 변경해줘야 한다.
    - 새 기능이 추가되더라도 기존 코드를 변경하지 않을 방법이 필요
  - 적용 설계 원칙
    - SOLID 원칙 중에 "OCP(Open-Closed Principle)' 적용
    - GRASP 패턴의 'Low Coupling' 책임 할당 원칙 준수
  - 설계원칙을 따르는 구현 방법
    - Business Logic 분리(menuHandler..)
    - GoF 'Composite' 패턴 적용
    

=> 결론적으로 composite패턴은 계층적인 구조로 이루어진 객체를 다룰 수 있어서 재사용성이 강화된다. 

### 객체 간 관계(UML)
- Association(연관): 생성자에서 파라미터로 받아서 초기화 하는 방법
```
class MenuGroup{
  Prompt prompt;
  MenuGroup(Prompt prompt) {
    this.prompt = prompt;
  }
}
```

- Dependency(의존관계): 특정 메서드를 실행할 때 일시적으로 사용
```
class MenuGroup{
  void execute(Prompt prompt){
    ...                 |-> 의존객체를 파라미터로 받는다. 
  }
}
```
