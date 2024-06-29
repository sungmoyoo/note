# 클래스
1. 메서드 분류 
: 메서드의 기능에 따라 서로 관련된 매서드를 별도로 분류해두면 유지보수하기 좋다.

2. 데이터 타입 정의
변수 -> field
연산자 -> method

# 1. 메서드 분류목적 클래스
1. 클래스 생성
2. 클래스명.메서드명()으로 참조하여 연결

### refactor
- refacor/rename:  
메서드명 일괄 변경 기능

- refactor/move class:  
메서드 다른 클래스로 이동 기능

## 1.1 쌍방 참조 해소
쌍방 참조될 경우 클래스가 상호 참조할 경우 강하게 연결되기 때문에 재사용하기가 불편하다.(의존), 유지보수도 어려워진다.
```
app08의 경우 app과 BoardMenu가 쌍방참조하고 있다. App(execute()),BoardMenu(prompt())
```


- 해결방법
메인 메서드에 존재하는 메서드를 새 클래스로 만든다. 
```
BoardMenu가 참조하는 App의 prompt() 메서드를 분리하여 클래스로 만든다.
prompt() 메서드는 다른 프로젝트에서도 쓰일 수 있는 일반적인 기능이기 떄문에 별도의 클래스로 분리하는 것이 재사용성을 높이는 방법이다.
```

## 1.2 GRASP Patterns
```
General  Responsible Assignment  Software Pattern
-------  ----------------------  ----------------
(일반적인)        (책임할당)             (설계기법)
```
클래스를 작성할 때 메서드와 필드(변수)를 두는 원칙

- Information Expert
정보를 갖고 있는 클래스에서 그 정보를 다룬다.

- Creator
- Controller
- High Cohesion
- Low-Coupling
느슨한 연결로 객체 간에 의존성을 낮추는 것.
