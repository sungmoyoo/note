# 패키지 접근 범위
public: 모두 공개
protected: 
(default): 같은 클래스 내 + 
private: 같은 클래스 내

# 생성자
인스턴스를 생성할 때 값을 초기화시키는 특별한 메서드
- 메서드 이름을 클래스 이름과 같게 한다.
- 리턴타입을 없앤다.
- 오직 new 명령을 실행할 때 호출할 수 있다. 나중에 따로 호출할 수 없다.


- String.format()

- substring(인스턴스)
```
String str =  new String("Hello")
String str2 = str.subString(2,4)
```
- java.lang 패키지에 있는 클래스는 임포트 안해도댐

 
