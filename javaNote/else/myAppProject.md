## 1
- Gradle 빌드 도구를 이용하여 프로젝트 폴더를 구성하는 방법
  - `init` task 실행
- Gradle로 프로젝트를 빌드하고 실행하는 방법
  - `build`, `run` task 실행

## 2
- 문자열 출력
- 줄바꿈, 이스케이프문자, 포맷 문자열 활용
```
----------------------------------------------
[과제관리 시스템]

1. 과제
2. 게시글
3. 도움말
4. 종료
----------------------------------------------
```
## 3
- ANSI 이스케이프 코드를 사용하여 출력 문자열 꾸미기

## 4
- ANSI CODE를 변수에 저장하여 사용

## 5 
- 표준입력스트림에서 값을 입력받기(Prompt)
- 조건문, 반복문을 통해 메인메뉴 출력
```
----------------------------------------------
[과제관리 시스템]

1. 과제
2. 게시글
3. 도움말
4. 종료
> 1
과제입니다.
> 2
게시글입니다.
> 3
도움말입니다.
> 4
종료합니다. <=== 프로그램 종료!
> menu
[과제관리 시스템]

1. 과제
2. 게시글
3. 도움말
4. 종료
----------------------------------------------
```
## 6
- 배열과 반복문을 활용하여 아래 작업 실시
```
1. 메뉴를 배열에 저장한다.
2. 메뉴 목록을 출력할 때 배열에서 꺼내 출력한다.
```

## 7
- 메서드 활용하여 중복코드 제거
- static 필드, 메서드 사용
## 8
- 조건문, 메서드 사용하여 각 메뉴에 대해 아래와 같이 서브 메뉴를 출력
```
----------------------------------------------
[과제관리 시스템]

1. 과제
2. 게시글
3. 도움말
4. 종료
메인> 1
[과제]
1. 등록
2. 조회
3. 변경
4. 삭제
0. 이전
메인/과제> 1
과제 등록입니다.
메인/과제> 2
과제 조회입니다.
메인/과제> 3
과제 변경입니다.
메인/과제> 4
과제 삭제입니다.
메인/과제> menu
[과제]
1. 등록
2. 조회
3. 변경
4. 삭제
0. 이전
메인/과제> 0
메인>
----------------------------------------------
```

## 9
메서드를 역할에 따라 분류하기: 클래스


## 10
- CRUD 구현  
  xxxMenu에 메서드 구현
  - add()
  - list()
  - view()
  - modify()
  - delete()

## 11
- 사용자 정의 데이터 타입 용도의 클래스 생성
- board, assignment, member에서 사용할 클래스 생성 후 객체를 생성하여 사용

## 12
- 레퍼런스 배열 다루기
- 입력값(객체)을 저장하는 레퍼런스 배열을 생성, 확장, 조회, 출력하는 기능 구현

## 13
- 회원 메뉴 추가하고 CRUD 구현  
  xxxMenu에 메서드 구현, 10번과 동일
  - add()
  - list()
  - view()
  - modify()
  - delete()


## 14
- 스태틱 필드 한계 확인
- 변경사항 없음

## 15
- BoardMenu, AssignmentMenu, MemberMenu에 인스턴스 메서드 적용
- Prompt에 inputInt(), inputFloat(), inputBoolean() 메서드 추가
- 생성자를 통해 Prompt 객체를 XxxMenu에 주입
- ANSI Escape Sequence 값을 별도의 클래스로 분리
- 클래스를 패키지로 분류

## 16
- 인터페이스 문법으로 메뉴를 다루는 객체의 실행 규칙 정의 (Menu ...)
- 인터페이스에 정의한 대로 메뉴를 다루는 객체를 구현 (App)
- 인터페이스에 정의한 대로 메뉴를 다루는 객체를 실행 (mainMenu.execute)


## 17 
- Menu, MenuHandler 인터페이스 추가
- Menu: MenuGroup, MenuItem
- MenuHandler: 각종 핸들러들
 
**인터페이스**
Menu: execute(Prompt prompt), String getTitle()

Menuhandler: action()


MenuGroup: 생성자(title), menuSize, menus배열
execute(), printMenu(), getTitle(), add(), remove(), indexOf()

MenuItem: 생성자(title), 생성자(title, menuHandler), title, menuHandler
execute(), getTitle()

각종 핸들러: action()


## 18
- action에 parameter menu 추가 -> getTitle() 받아서 쓰기 위함

**배열이 있는 곳에 배열 다루는 코드 이관(캡슐화?)**
- BoardRepository: 배열, length private 처리  
- add(Object object),  remove(index), toArray  
- (index), get(index), set(index, Object object) 

### +핸들러 수정

## 19
- Object객체 사용해서 범용 Repository 생성  
-> ObjectRepository로 전부 변경
### 주의사항
리턴값이 object와 board 로 다른 경우에는 해당 타입으로 명시적 형변환을 해주어야 한다. 

## 20 
### 제네릭 적용
- 기존 직접 구현한 add(), remove() 메서드 기능을 arraycopy(), copyof() 메서드를 사용하여 편하게 구현하고 T[] toArray(T[]) 메서드 추가

## 21
- 목록을 다루는 기존 Repository 클래스를 자바 Collection API인 ArrayList로 교체

## 22
- 접근범위 넓힐 이유 없는 필드들 private 처리
- vo 인스턴스 getter/setter 적용

## 23
Generalization
- menuHandler 구현체의 공통 분모를 추출하여 수퍼클래스 정의
- 수퍼클래스를 추상 클래스로 정의
1. menuItem과 menuGroup에서 title을 추출하여 AbstractMenu 클래스 생성(추상클래스, title,getTitle(), setTitle())

2. Handler의 title을 출력하는 코드을 추출하여 AbstractMenuHandler 클래스 생성(printMenuTitle()) + 상속 후 메서드 오버라이딩 할 시 코드 재정의 되기 때문에 수퍼클래스 메서드 기능을 그대로 두고 기능을 추가할 필요가 있다.
  - 수퍼클래스 메서드 콜 (super.action(menu)) => 까먹을 위험성 있음
  - 추상메서드 설정, action(Menu menu) 메서드에 추상메서드 삽입하여 구현

3. 서브클래스가 사용하는 의존객체를 수퍼클래스(AbstractMenuHandler)에서 관리(prompt)
  - prompt를 추상클래스로 옮기고 생성자로 받음
  - 콘크리트 클래스 생성자에서 추상클래스에 구현한 생성자를 실행 super(prompt)

4. 서브클래스에서 .getTitle 쓸 수 있도록 Menu menu를 protected로 설정해서 보관

## 24
java.util.Date 클래스 활용
1. java.util.Date import, 
2. Date 인스턴스로 add
3. format 형식 변경 (%3$tm..) 3$는 하나의 파라미터 지정

java.sql.Date
1. assignment 변경, 제출마감일 입력형식으로
2. Prompt inputDate 메서드 추가,
  + Prompt 리팩토링: 임시변수 매개변수로 변경? 


## 25
RunTimeException 예외 처리: 예외 발생 시 내가 만든 소스를 찾아가기
1. addHandler에서 날짜 입력 형식 예외 처리
2. 메뉴 잘못 선택 시 예외 처리
  - Main 메서드에서 예외 처리
  - modifyHandler에서 예외 처리 
    - 문제: modify에서만 적용됨
  - inputInt에서 적용
    - 문제1: -1 return 시 정상값인지 구분할 수 없음
    - 문제2: 예외 문구 출력은 호출하는 쪽에서 해야된다.
3. try catch catch catch로 여러 예외 동시에 처리
  - 각각의 Exception에 따라 처리할 예외 설정 ex) NumberFormatException, IllegalArgumentException...
4. MenuItem, MenuGroup에 예외 처리


중요!
- 프로그램을 실행하다가 어느 지점에서 예외가 발생하면 해당 위치에서 적절한 조치를 취할 것이다.
- 다만, 그에 벗어나는 조치가 되지 않은 예외가 JVM에 보고되는 경우를 대비해 마지막 보루인 main()에서는 예외를 처리해야 한다!
- main()에서 마저 처리하지 않는다면 JVM에게 보고될 것이고, 개발자나 알아 볼 메시지를 출력하고 종료해버린다.

## 26
자료구조-LinkedList
- LinkedList, Node 클래스 생성
- Node에 value, next 변수 선언

- LinkedList add() 메서드 생성
```
value는 Object value값 저장
next는 다음 노드의 주소를 저장하여 연결
```

- LinkedList toArray() 메서드 생성
```
1. arr[] 생성
2. Node는 first부터, index는 0부터
3. node가 null이 아닐때까지 반복하여 arr[index]에 value 저장
4. arr 리턴
```

- LinkedList get() 메서드 생성
```
1.index 유효범위 아닐 때 IndexOutOfBoundException throw
2.현재 위치를 int cursor = 0으로 설정
3.node는 first부터, cursor가 index와 같아질 때까지 반복, node.next로 다음 주소를 node에 저장
4. index와 cursor와 같아지면 반복 종료 후 node.value 리턴
```

- LinkedList set() 메서드 생성
```
1. 인덱스를 찾아가는 과정은 동일
2. 기존 값을 old에 저장, 새로운 값 node.value에 저장
3. old 리턴
```

- LinkedList add(index, value) 삽입하는 메서드 생성
```
1. 인덱스 유효성 검사
2. 새 노드 객체 생성, value 저장
3. 값이 없을 때: first, last 모두 node 주소 저장
4. 인덱스가 0일 때: 새 노드 객체를 first, first에 node를 저장
5. 인덱스가 size일 때: last.next를 추가된 node 주소로 바꾸고 last를 node로 바꾼다.
6. 인덱스가 중간일 때(나머지): 
  - 현 노드가 존재하는 위치를 저장하기 위해 cursor 설정
  - 새 객체 currNode를 생성하고 주소를 first로 설정
  - 인덱스 전까지 node를 반복 이동
  - node.next를 currNode.next로 교체
7. size 확장
```

- LinkedList remove(index) 메서드 생성
```
1. 인덱스 유효성 검사
2. 값이 1개일 때: first, last 모두 null
3. 맨 앞: first 주소를 first.next로 교체
4. 맨 뒤: size-1에서 currNode.next를 last값으로 교체
5. 중간: 인덱스-1에서 currNode.next를 currNode.next.next값으로 교체
6. 리팩토링: 조건문 공통코드 슬라이드
  - 중복되는 반복문 else문에 넣고 닫기
  - 중간 값 계산
  - 마지막 값 if문으로 묶고 last값 바꾸기
```

- 중첩 클래스 정의
```
node 클래스를 LinkedList 클래스 안으로 복사 후 private static 처리
```

- 제네릭 적용
```
<E> E 추가
```

- MenuGroup LinkedList 적용
```
1. LinkedList<Menu> 생성
2. 특정 인덱스 지정은 get() 메서드 호출
3. Menusize 제거 menus.size() getter 사용
4. add, remove 메서드 호출로 변경
```

- boolean remove(E value) 메서드 생성
```
1. prevNode = Null로 생성, node는 first로 생성
2. node != null 아닐때 반복, if Value값 같으면 break
3. node가 null 되면 false return
4. node가 첫번째일때 다음 노드를 first로 설정
5. 근데 first가 null이면 값이 하나이므로 last도 null처리
6. prevNode.next에 node.next로 노드 하나 삭제
```

- app20 ObjectRepository 복사 후 rename
- boolean remove(E value) 메서드 생성
```
1. equals로 비교, remove(index) 호출
```

- LinkedList toArray(E[] arr) 메서드 생성
```
1. 주어진 배열이 받으려는 배열보다 작으면 배열 복사
2. 반복문을 돌면서 node.value를 받아 리턴
```

- remove 시 노드가 가비지가 되기 전에 다른 객체를 참조하던 것을 모두 Null로 바꾼다.
```
1. deleted에 삭제할 노드 보관
2. 모든 코드 끝나고 E old에 value값 저장 후 value next 모두 null로 변경
3. 요즘은 가비지 컬렉터가 이런것도 다 처리해주긴 함
```

- 인터페이스 적용
```
1. List<E> interface 규칙(메서드) 정의
2. ArrayList, LinkedList List<E> 인터페이스 구현
```

- ArrayList -> LinkedList 교체
```
1. Handler에 objectRepository 모두 List 인터페이스로 변경 and import
2. main() repository 객체 생성 부분 변경(List<>, LinkedList로 교체)
```

## 27
- Stack 구현
```
1. push()
2. pop()
3. peek()
4. empty()
```

- Queue 구현
```
1. offer()
2. poll()
3. peek()
```
- Stack 적용
```
1. MenuGroup에 Stack을 이용하여 Breadcrumb 구현
2. AbstractMenu breadcrumb 생성자에 추가
3. String을 반환하는 getMenuPath() 메서드 생성
   - toArray(), String.join 사용
4. MenuGroup, MenuHandler 생성자 breadcrumb 파라미터 추가
5. App 리팩토링
   - Stack 객체 생성 후 아규먼트 삽입
   - MenuGroup addItem, addGroup만들어서 App에 적용
6. 메뉴를 실행할 때 breadcrumb경로에서 title을 저장하고 반복문이 종료되면 제거한다.
```

## 28.
- Iterator 인터페이스 생성(hasNext(),next())
- ArrayListIterator 생성
```
1. 생성자로 전달할 ArrayList 선언, cursor 포인터 선언
2. hasNext(): cursor가 0보다 크고 list 초과하지 않으면 true
3. next(): cursor위치 get
```
- list에 Iterator를 리턴하는 규칙 정의
- ArrayList에 method implements
```
return new ArrayListIterator<>(this)
```

- LinkedListIterator 생성
```
ArrayListIterator와 동일
```
- LinkedList method implements
```
ArrayListIterator와 동일
```

- MenuGroup과 listHandler Iterator 적용
```
1. 패키지 멤버클래스로 LinkedList에 iterator() 구현
2. List 인터페이스에 iterator() 규칙 정의
3. LinkedListIterator 생성하여 리턴한다.
4. MenuGroup에서 printMenu의 menus.iterator()로 iterator 생성, while hasNext 조건을 사용하여 새로 만든 Menu menu에 담아 getTitle
5. listHandler에서도 Iterator 생성
```
- 중첩클래스 활용
```
1. 기존: 외부 클래스
2. static nested class: 그대로 복사 후 private static, LinkedListIterator 명을 IteratorImpl로 변경
3. non-static nested class: static 제거 후 자동으로 생성되는 부분 삭제
3.1 LinkedList면 cursor를 Node로 변경해주는 작업한다.
4. local class: 기존 클래스 안으로 이동 
5. 익명클래스: 정의하는 즉시 인스턴스 생성 (){-}
6. 익명클래스2:바로 return
```


## 29
- util 패키지에 ANSIEscape, Prompt 제외하고 나머지 클래스 다 삭제
- import 변경


## 30
- App void run() prepareMenu() 메서드 생성
- run()에 execute문 넣고, prepareMenu()에 add문들 넣음
- 변수들 List + mainMenu 인스턴스화
- 생성자에서 prepareMenu() 메서드 실행 
- main() 메서드에서 App 객체 생성 후 run 실행


## 31
- 생성자에서 loadAssignment 호출, run 끝나고 saveAssignment 호출
- loadAssignment, saveAssignment 메서드 생성
- saveAssignment
```
1. FileOutputStream 인스턴스 생성 파일명 = .data
2. 예외 처리
3. out 변수 블록 밖에 선언
4. finally에서 파일 닫기
5. close() 예외 무시 처리
6. autocloseable -> try with resources 문으로 변경
7. for 문 작성 for (:)문 사용, getter 사용
8. write() 사용하여 바이트 배열을 출력 스트림으로 보낸다. 
9. 바이트의 개수를 2바이트로 출력한다. (>> 8) 사용
10. 저장할 데이터의 개수를 2바이트로 출력
```

- loadAssignment
```
1. 한번에 try with resources
2. 저장된 데이터 개수를 2바이트로 읽어들이기 << 연산자와 | 활용
3. 바이트 배열 생성, 넉넉하게
4. 사이즈만큼 반복, 2바이트로 읽어들여 len에 저장 <<,| 동일
5. read(byte[], 시작, 끝)
6. 읽은 바이트 배열을 String에 저장
7. 만약 날짜 형식이 java.util.date 면 getTime() 메서드를 사용하여 Long 값인 밀리세컨드로 변환하여 int 8바이트 저장
8. 읽을 때도 8바이트 값 읽어들이기, 생성자를 활용하여 Date 객체를 생성할 땐 밀리세컨드를 long값으로 바꾸기
```

## 32.
primitive Type/String -> DataOutputStream를 통해 byte[] 출력

- IO 패키지 생성
- DataOutputStream 클래스 생성
```
1. FileOutputStream 상속
2. writeShort, writeInt, writeLong, writeUTF, writeBoolean 메서드 작성
3. save메서드들 수정
```
- DataInputStream 클래스 생성, 
```
1. FileInputStream 상속
2. readShort, readInt, readLong, readUTF(readNBytes도 가능), readBoolean 메서드 작성
3. load메서드들 수정
```


## 33.
- 기존 방식과 buffer방식 성능 비교
1. 기존 방식
```
write() -> HDD -> read()
```
read/write =  data seek time + read/write time
데이터 읽고 쓸 위치를 찾는데 걸리는 시간이 많이 소요된다. 

2. 개선 방식
```
write() -> Buffer -> HDD -> Buffer -> read()
```
버퍼가 꽉차면 출력, 버퍼가 비었으면 읽기 = read/write 횟수 감소 = data seek time 감소  
=> read/write 시간 감소

- 버퍼 기능 추가 BufferedDataOutputStream
```
1. DataOutputStream을 상속받는 BufferedDataOutputStream 클래스 생성
2. buffer 바이트배열 생성, size 초기화
3. write(int b) 메서드에서 buffer[size++]에 b 저장,  
만약 버퍼가 모두 차면 즉시 버퍼에 저장된 데이터를 파일로 출력한다. 
4. write(byte[] b) 메서드에서도 반복문 돌면서 buffer[size++]에 b 저장, 버퍼 꽉차면 파일로 출력 동일
5. 중복 코드 flush()
6. close() 오버라이딩, flush() 추가
```

- 버퍼 기능 추가 BufferedDataInputStream
```
1.DataInputStream을 상속받는 BufferedDataInputStream 클래스 생성
2. buffer 바이트배열 생성, size 초기화, cursor 초기화
3. read() 메서드 오버라이딩: size가 0일 때 buffer를 read(), 만약 size가 음수면 -1 리턴, cursor를 0으로 설정하고 buffer[cusrsor++]  
>주의
byte 배열로 읽어온 것을 int로 바꿀 때(1바이트에서 4바이트로 바꾸는 과정), 바이트 값으로 봤을 때 음수일 경우 int로 리턴할 때도 음수가 된다. 이를 방지하기 위해 앞 3바이트를 0으로 처리하여 양수화시킨다.
따라서 And 비트논리 연산자로 0xff를 필터링 해주어야 한다.

4. read(byte[] b, int off, int len) 메서드 생성
5. 반복문 int i = off, count = 0 변수 2개로 설정,  
즉 시작은 off부터 저장, 복사 길이는 count로 len만큼 복사
int b = read()로 읽어와 b가 -1 일 때 count가 0보다 크면 count 리턴 작으면 -1 리턴
arr[i]에 (byte) b 저장, len 리턴
```

## 34.
- 기존 IO 패키지 삭제
- DataOutputStream으로 대체 -> FileOutputStream 덧붙인다.
- DataInputStream도 동일


## 35. 
save
- DataOutput -> ObjectOutput
- out.writeUTF -> out.writeObject
load
- 따로 안하고 한번에 in.readObject() + 형변환하여 객체에 담음

- write 빼고 리스트 통째로 출력해보기
```
List<> list레퍼런스에 , deserialize하여 담고 addAll로 list를 Repository에 담는다.
or 
prepareMenu 위치 변경
List들 인스턴스 생성 안하도록 변경
예외처리에서 List 객체 생성하도록 변경
```

## 36.
**중복코드 제거**
load, save 하나로 만들기
- loadData1
```
1. 메서드에 대해서만 제네릭 선언
2. 파라미터로 filepath, List<E> dataList
3. list<E> list 생성하여 데이터 읽은 다음 dataList.addAll
```
- saveData
```
1. 파라미터로 filepath, List<?> dataList
2. DataList write
```
- 예외 지우고, 인스턴스를 초기화문장에서 생성

- loadData1 주석 처리

- loadData2 
```
1. 리턴값을 List<E>로 바꿈
2. 파라미터로 List가 아닌 Class<E> clazz 주어진다.
3. 바로 리턴
4. 예외 시 ArrayList<>() 생성
이 방법은 예외 처리할 때마다 ArrayList<>() 생성한다는 게 단점
5. class 빼보기 - 제네릭 이해
```

**serialVersionUID**
1. vo에 static final long serialVersionUID 설정


## 37.
- FileWriter, FileReader로 수정

- CsvString 인터페이스 생성: String toCsvString 메서드 정의

- saveData 수정 
```
1. 파라미터로 List< ? extends CsvString>, 
2. dataList CsvString csvObject foreach 
```

- toCsvString 구현: String.format 사용

- loadData 수정
```
1. Scanner(FileReader) 데코레이터 역할 적용, 한줄씩 읽음
2. 파라미터로 clazz 추가
(reflect api?)
3. 팩토리 메서드 준비: 
-public static 타입 creatFromCsv(String csv){-}
-스트링 배열에 split으로 구분해 담는다.
-리턴할 객체 생성
-객체에 set(배열[인덱스])
4. 클래스 정보를 가지고 팩토리메서드를 알아낸다.
5. 팩토리메서드에 csv문자열을 전달하고 객체를 리턴받는다.invoke
```

## 38.
- JSON 입출력하기
- build.gradle에 의존 라이브러리 추가
- 파라미터 CsvString 제거 + 인터페이스 구현 다 제거
- saveData
```
1. 성능을 위해 BufferedWriter 사용
2. 날짜 형식 변경: GsonBuilder 클래스의 setDateFormat 설정 후 create로 gson 객체 생성. 객체에 dataList 리스트를 넣어 Json 형식으로 변환한 후 write
```

- loadData
```
1. BufferedReader 사용
2. 로컬에서 사용할 것이기 때문에 가변 객체 StringBuilder 생성
3. readLine이 null이 아니면 입력값 StringBuilder에 append
4. GsonBuilder 클래스의 setDateFormat 설정 후 객체 생성, fromJson으로 객체를 생성하는데 StringBuilder 문자열과, TypeToken.getParameterized(ArrayList.class, clazz)를 넘긴다.
5. NullPointerException은 삭제, While문에서 처리해주고 있어서
6. 로딩 중 오류 발생하면 빈 ArrayList 리턴
```

## 39.
- myapp.dao 패키지 생성
- AbstractDao 추상클래스 생성
```
1. 제네릭 설정
2. saveData/loadData 그대로 복사
ArrayList<Board> list 변수 초기화
```

- saveData 변경
```
1. DataList 삭제
2. catch문 삭제
3. DAOException 클래스 생성
4. RunTimeException 생성자 상속
5. catch문 재생성, 예외 발생하면 DaoException throw하도록
```

- loadData 변경
```
1. void로 변경
2. Class 정보 받는 파라미터 삭제
3. return 하지 않고 list에 저장한다. 형변환도 ArrayList
4. catch 문에서 DaoException 출력하도록 설정
5. fromJson에 알아낸 타입 파라미터 정보를 넘긴다.
```

- 테스트클래스 생성
```
1. DaoTest 생성
2. AbstractDao 상속
3. 메인메서드 생성
4. 객체 생성 후 테스트 메서드 호출
```

- 타입 파라미터 정보를 알아내는 방법
```
// 메서드를 호출한 클래스(DaoTest) 정보를 알아낸다.
Class<?> clazz = this.getClass(); 

// AbstractDao(수퍼클래스) 클래스의 정보를 알아낸다.
ParameterizedType classInfoWithTypeParameters = (ParameterizedType) this.getClass().getGenericSuperclass(); 

// AbstractDao에서 제네릭 타입을 알아내어 클래스로 변환, 출력
Class<?> genericType = (Class) classInfoWithTypeParameters.getActualTypeArguments()[0];

이후 쓸모없는 변수 삭제하고 체인 호출 방식으로 리팩토링
```

UI코드와 데이터 처리 코드를 분리하면 DAO의 재사용성이 높아진다.
데이터 처리 코드를 분리하면 DAO를 바꿀때마다 Handler를 수정할 필요가 없어진다. 

- Handler 생성자 수정(Objectrepository->xxxDao)
- BoardDao에 add 메서드 추가
- BoardDao에 delete 메서드 추가
```
1.Index 받아 삭제
2.예외는 if로 처리
```
- BoardDao findAll 추가
```
1. iterater 삭제
2. list 받고 반복문으로 출력
```

- BoardDao findby 추가
- BoardDao update 추가
```
1. modify/view Handler findBy로 유효성 검사
2. update로 set
3. view는 findBy로 꺼낸 객체 출력
```

- app 수정
```
1. filepath 데이터 넘길 수 있도록 BoardDao, AbstractDao 생성자에 추가
2. 생성자로 인스턴스 데이터 받았으니 loadData(), savaData() 파라미터 삭제
3. Handler 파라미터로 Dao 객체 전달
```

- 고유 식별 값 추가
```
1. vo no 변수 추가
2. toString 삭제
3. setter/getter 추가
4. no 포함한 toString 재생성
5. json 파일에 no 설정
6. BoardDao에 lastKey 추가, lastKey는 getLast에서 getter 사용
7. 메서드 수정
  - add: setNo 1 증가된 lastKey
  - delete: 리스트만큼 반복문 돌면서 i의 key값이 no랑 같을 때 remove(i)
  - findBy: 똑같은 조건의 반복문에서 list.get(i)
  - update: 똑같은 조건의 반복문에서 set(i, board)
8. 리팩토링
  - indexOf 메서드 추가
  - 중복 코드 수정
```

- 인터페이스로 DAO 객체 사용법을 정의, DAO 교체가 용이
```
1. BoardDao를 json 패키지에 복사 (BoardDaoImpl)
2. 기존 BoardDao를 인터페이스로 변경
3. 인터페이스 구현 후 @Override 애노테이션 표시
```


## 40.
- App 분리
```
1. App 폴더 복사해서 App-client, App-server 생성
2. settings.gradle include 메인클래스 변경
3. build.gradle application mainclass 변경
4. ClientApp 
  - 메인 메서드에 과제관리 시스템 출력
  - new ClientApp().run 명령어 작성
```

- 서버/클라이언트 연결
```
ServerApp
1. Dao 객체 생성, Run 메서드 제외 모두 삭제
2. Menu 패키지, Handler 패키지 삭제

run() 메서드
1단계: 네트워크 장비와 연결하기 위한 정보를 준비한다.
- ServerSocket 객체 생성, 포트번호를 아규먼트로 입력(8888)
- try ~ catch로 예외 처리

2단계: 클라이언트의 연결을 기다림
- Socket 레퍼런스 생성, accept()하여 클라이언트 연결 정보를 꺼낸다.

3단계: 클라이언트와 통신
- getInputStream/getOutputStream + Decorator 패턴 사용하여 입출력 스트림 객체 생성
- 요청 규칙에 따라 write, 요청 규칙에 따라 read
- 응답 결과 write, 응답 결과 read
- 받은 명령 수행
- Json 형식으로 변환하여 응답 결과 write, read => GsonBuilder 사용
```

- 네트워크 DAO 구현
```
1. DaoImpl 그대로 복사해서 network 패키지에 생성
2. Client에 필요없는 파일 삭제 json패키지
3. 생성자 4개(dataName, in, out)- 파라미터 + gson 객체 생성
4. try catch 모두 적용
5. add~update 구현 , IndexOf 삭제
- add: write 요청형식에 따라 요청, 상태코드 정상 아닐 경우에 리턴받는 entity throw Exception
- delete: 
```

- ClientApp/ServerApp 네트워크DAO 적용
```
ClientApp
1. prepareNetwork() 메서드에 기존 작성해놓은 Socket 연결 명령 옮김
2. 생성자에서 객체 생성 X 변수초기화만 하고, 입출력 스트림 Socket문 아래에 네트워크 Dao 구현 

ServerApp
1. 변수초기화 블록에 HashMap 객체 생성
2. 생성자에 각 타입 별 daoMap put + gsonBuilder create()
3. 클라이언트 통신 이후 while (true) 반복문을 통해 클라이언트 요청 무한 받기
4. readUTF로 요청 형식 받기
5. dataName으로 DAO를 찾는다
- mapdao에서 클라이언트로부터 받은 dataName을 get하여 Object dao 객체에 담는다.
6. command 이름으로 메서드를 찾는다.
- dao객체의 클래스 정보를 찾고 거기서 해당 클래스에만 선언된 모든 메서드를 Method 배열에 저장.
- command와 같은 이름의 메서드를 저장할 commandHandler 변수 선언
- Method 배열을 반복하여 메서드명이 같으면 commandHandler에 저장
6. 메서드의 파라미터 정보를 알아낸다.
- Parameter 배열에 commandHandler 모든 파라미터 저장
7. 메서드를 호출할 때 파라미터에 넘겨줄 데이터를 담을 배열을 준비한다.
- 파라미터 배열의 길이만큼 아규먼트(Object) 배열 생성
8. 아규먼트 값 준비하기(파라미터가 최대 1개인 상황 가정)
- 파라미터 길이가 0 이상인 경우, 파라미터 배열 인덱스 0번째의 타입 정보를 Class<?> 타입으로 저장
9. 클라이언트가 보낸 Json 문자열을 gson.fromJson을 통해 해당 파라미터 타입 객체로 변환.
10. 변환한 객체를 아규먼트 배열에 저장
11. 메서드의 리턴 타입을 알아낸다.
- 아직은 필요없지만 만듦
12. 메서드를 호출한다.
- invoke 메서드를 통해 commandHandler에 저장된 메서드를 호출
- 파라미터로는 해당 메서드를 가진 객체, 파라미터(아규먼트) 배열를 넣어주면 된다.
13. 리턴값을 Json형식으로 변환하여 write
```

- 마지막으로 ServerApp, ClientApp에 new 인스턴스 생성 후 run() 메서드 호출 확인

- 리팩토링, ServerApp 반복/종료 처리

```
ServerApp
1. findMethod 메서드 생성, 파라미터(clazz, name)
  - command 이름으로 메서드 찾는 명령 잘붙, Return 수정
  - 본문에 findMethod 사용, + 요청값 틀릴경우 상태코드 400 리턴
2. DAO찾는 경우에도 상태코드 에러 설정
3. getArguments(method, json) 메서드 생성
- 파라미터 찾고 배열 만드는 부분 잘붙
- Object 배열 리턴
4. findMethod RuntimeException 예외 throw
5. 본문에서 예외 try ~ catch로 받아 응답코드 400, e.getMessage() 리턴 => if 삭제
6. bitcamp에 RequestException 클래스 생성, RuntimeException 상속받아 세밀하게 예외 처리
7. dao 찾을 때는 따로 if로 Request throw, 서버 쪽 문제가 발생할 경우 catch로 응답코드 500 리턴
8. processRequest(DataInputStream, DataOutputStream) 메서드 생성
- while문 안 내용 전부 잘붙
9. service(Socket) 메서드 생성
- Data I/O 입출력 객체 생성, processRequest 반복문 넣기
- serverSocket.accept() service 파라미터로 넣고 무한 반복
10. service에 try~catch 처리(클라이언트 연결 오류)

ClientApp
1. Socket. Data I/O 인스턴스 필드화
2. close() 메서드 생성
- try with resources 처리 - (this.socket, this.in, this.out)
- writeUTF("quit") 하고 in.readUTF 출력
- 예외 발생한 경우는 무시

ServerApp
1. processRequest에서 dataName 요청값이 quit인 경우 return -1 아니면 0
2. service도 리턴값이 -1일 경우 반복문 종료, service의 try문도 try with resources로 변경
- 쓰지도 않는 Socket s를 왜 넣었나? = 너무 길어지니까
```


41. 공통 기능을 서브 프로젝트로 분리하기 
- temp 에 gradle init (참고용)
- app-common 서브 프로젝트 폴더 생성
- setting.gradle에 app-common include 추가
- app-server build.gradle app-common에 복붙
- app-client src app-common에 복붙
- build.gradle 수정
```
1. id 'java-library'로 수정
2. application, eclipse 설정, Run 설정 삭제
3. server, client도 eclipse 설정 삭제
4. app-client/server의 dependencies에 implementation project(':app-common') 추가
```

- app-common 수정
```
1.client와 관련된 클래스, 패키지 모두 삭제
```

- app-client/app-server 수정
```
1. vo 패키지 삭제
2. dao 인터페이스 삭제
```
- gradle clean/build 

- app-api 서브 프로젝트 생성: app-server에서 제공하는 DAO 객체의 stub을 분리
```
1. build.gradle id 'java-library'
2. app-client가 포함
3. app-client DAO stub 삭제
4. app-api 필요 없는 파일 삭제
```
- gradle clean/build 

42. DAO프록시 객체 자동생성
- app-api dao패키지에 daoProxyGenerator 클래스 생성
- T를 리턴하는 create(Class<T> clazz, String dataName) 메서드 생성
```
0. Data I/O Stream private 변수 생성자로 받기, gson빌더 생성자에 생성
1. T타입의 Proxy.newProxyInstance를 리턴하는 익명클래스 작성
2. functional Interface 람다 문법 사용
3. DAO 코드 복붙 후 수정
- out.writeUTF 메서드명은 method.getName()으로 변경, 만약 args가 0인 경우 빈문자열 아니면 0번째 인덱스를 toJson하여 writeUTF
4. return은 getReturnType을 해서 알아내 해당 타입으로 리턴
- 주의! 리턴타입이 어떤 건지에 따라 다르게 리턴해주어야 함
- 리스트, 클래스, 등
```

ClientApp 
- Factory 메서드 방식으로 객체 생성


43. Stateless 통신 방식
ServerApp
- processRequest 반복 삭제, return 타입 삭제
- quit 받는것 삭제

DaoProxyGenerator
- 생성자 수정, (String host, int port)
- try with resource를 통해 서버에 연결할 때마다 Socket을 연결하고 입출력 객체 생성하도록 변경

ClientApp
- 호스트랑 포트번호를 파라미터로 넘기고 소켓 연결 부분을 삭제
- close() 메서드 필요 없어져서 삭제


4. 여러 클라이언트 요청 동시 처리: Thread
run() 메서드의 service() 호출을 별도로 하기 위해
ServerApp 수정

- Thread를 상속받아 non-static nested class RequestProcessor 생성
```
1. socket 생성자로 받는다.
2. run()을 오버라이딩
3. main 실행흐름과 분리해서 독립적으로 실행하고 싶은 코드를 둔다
```
**실행방법1**
부모 스레드로부터 실행을 분리하여 run()을 호출한다.

**실행방법2**
Runnable 인터페이스를 구현체를 Thread의 파라미터로 넘겨 생성한 객체를 실행하는 방법

**리팩토링**
- 인터페이스 구현체 익명클래스로 변경
- fucntional interface 람다 변환


45. Pooling 기법 적용(스레드 재사용)
**Pooling 기법을 활용하여 스레드 관리**
- 서버에 util 패키지 생성
- ThreadPool, WorkerThread 클래스 생성
- Worker 인터페이스 생성
Worker
```
1. void play() 메서드 
```

WorkerThread
```
1. Thread 상속
2. setWorker에 Worker를 받는다.
3. run() 메서드 오버라이딩 한 후 try catch
4. try문 안에 반복문 넣고 this.wait()
5. setWorker에 this.notify()
6. wait()에 synchronized (this) 
```

ThreadPool
```
0. WorkerThread를 담는 ArrayList 생성
1. WorkerThread를 리턴하는 get() 메서드 생성
- 사이즈가 0보다 클 경우 list.remove(0)
- 없으면 새 WorkerThread(this) 생성하여 리턴
2. 반납 받는 revert(WorkerThread) 생성
- list에 add
```

WorkerThread
```
1. ThreadPool을 생성자로 받는다.
2. 작업을 완료했으면 pool.revert(this)
```

// 쌍방 참조 개선
- Pooling<E> 인터페이스 생성
```
1. E get();
2. void revert(E e);
```

ThreadPool
```
1. Pooling<WorkerThread> 구현
2. 메서드 오버라이드
```

WorkerThread
```
1. ThreadPool을 Pooling으로 교체
```

// ServerApp 적용
- 변수초기화 문장 ThreadPool 객체 생성
- run에 적용
```
1. Threadpool 객체 생성
2. run()메서드 threadpool.get()
3. setWorker(Worker 구현)
4. 리팩토링
5. 클라이언트 연결/종료 포맷팅- 스레드명 출력하도록
```

// 스레드 미리 만들어두기
ThreadPool
- create() 메서드 생성
```
1. WorkerThread 객체 생성하여 start()
2. try catch문에서 Thread.sleep(), 스레드가 wait할 시간 확보
3. 스레드 리턴
```

@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
AssignmentList 
- thread.sleep();
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

- 생성자로 초기사이즈(initSize) 받기
```
1. 기본 생성자 생성, 매개변수 없는 ThreadPool 만들 수도 있어서
2. 초기사이즈를 파라미터로 받는 생성자 생성
  - 만약 초기사이즈가 0보다 작거나, 100이 넘어가면 그냥 return
  - 초기사이즈만큼 반복해서 WorkerThread 객체 생성하여 list에 추가
3. ServerApp에서 초기사이즈 주면 됨
```

46. ExecutorService, Executor 적용
- ExecutorService newCashedThreadPool 정적 메서드 팩토리 사용
- execute() 메서드 안에 Runnable 인터페이스 구현 후 람다 문법으로 리팩토링



47. DBMS 도입

- MySql 설치
```
1. brew update
2. brew install mysql
3. brew services start mysql or mysql.server start
4. mysql_secure_installation
5. mysql -u root -p
```

- MySql 빌드스크립트 수정
```
1. 기존 app-api, api-server 프로젝트에서 제외
2. app-common의 dao, vo myapp으로 복사 
3. app-client build.gradle implementation 수정
  - app-api, app-common 주석처리
  - MySql JDBC Driver 의존 라이브러리에 추가 
```

- mysql 구현체 생성
```
1. BoardDao,AssignmentDao,MemberDao를 구현하는 Impl생성
2. 메서드 구현s
```


- ClientApp에서 DaoProxyGenerator 삭제, 그냥 구현체 객체 생성
- JDBC 드라이버 준비
```
0. 드라이버 생성은 
1. DriverManager.getConnection(-) 하여 DBMS와 드라이버매니저 연결
```
- Connection 객체 Dao에서 생성자로 받기

- MySql 사용자 추가해서 studydb 데이터베이스 생성
```
1. 로컬호스트에서만 접속할 수 있는 사용자
  CREATE USER 'study'@'localhost' IDENTIFIED BY '암호';
 
2. 데이터베이스 생성
  CREATE DATABASE 데이터베이스명
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

3. 사용자 권한 부여
  GRANT ALL ON studydb.* TO 'study'@'localhost'
```
이후 getConnection 잘됨

- 테이블 생성
```
create table boards(
  board_no int primary key auto_increment,
  title varchar(255) not null,
  content text not null,
  writer varchar(30) not null,
  created_date datetime null default now()
);

어떤 데이터베이스 쓸지 지정을 안하면 에러 발생 table 뒤에 데이터베이스 이름을 지정해도 되지만 매번 지정하기 귀찮기 때문에 "use studydb"해서 지정
```

- 테이블에 값 집어넣기

- mysql 구현체 
findAll()
```
1. Statement 객체 준비 con.createState()
2. 실행문을 전달하고 ResultSet 객체에 필요 정보 저장
3. list 생성
3. ResultSet을 사용해 가져온 column 값을 객체에 저장
4. next()와 반복문을 사용해 객체를 list에 추가하여 리턴
```

findBy()
```
1. findAll과 동일하게 ResultSet을 리턴받는 executeQuery 메서드로 SQL 실행문 전달 where로 no 조건 걸어야함
2. 만약 값이 존재할 경우 객체에 값을 담아 리턴
3. 없으면 null
```

add()
```
1. return받을 필요없음, 따라서 executeUpdate를 사용
2. insert into + String.format으로 SQL문 전달
```

delete()
```
1. executeUpdate로 실행문 전달.
2. delete from + where 사용
3. int return
```

update()
```
1. executeUpdate로 실행문 전달.
2. update xxx set + where로 변경 후 int 리턴
```


48. DB 교체
네이버 클라우드 DB 생성
- cloud db for mysql에서 DB생성
- public domain 신청
- 방화벽(ACG) 설정
```
1. inbound 내 IP만
2. Outbount 제한 x
```

49. PreparedStatement 적용
- 기존 Statement -> PreparedStatement로 변경
```
1. SQL 실행문을 안으로 집어넣고 수정
2. try with resouces로 변경
3. ? in-parameter 삽입
```


50. Application Server Architecture
app-client 분리
- app-client 복사 app-server로 이름 바꾸기
- setting.gradle에서 include
- build.gradle mainClass ServerApp 설정
- main() 있는 파일 ServerApp으로 변경

**ClientApp 수정**
- main, run 메서드 제외 모두 삭제
- run 메서드 바디 삭제
- ClientApp server(String serverAddress) 메서드 생성
```
1. serverAddress 변수 받기 this로
2. return this
```
- void port(int port) 메서드 생성
```
1. port 변수 받기
2. return this
```
- main()에서 바로 실행
```
1. new ClientApp.server().port().run()로 바로 실행
```

void run() 수정
```
1. try with resouces로 socket, Data I/O Stream 생성
2. while true로 String response readUTF
3. prompt 자원 추가, autocloseable 구현해서
4. String input을 Server로 보냄
5. 서버 연결 종료 조건 응답 추가
```
- dao, menu, vo, handler 패키지 모두 삭제

**ServerApp 수정**  
void run() 수정
- SeverSocket 생성
```
1. Socket 생성
2. void processRequest(Socket)에서 통신 처리
```

void processRequest() 생성
```
1. Socket, Data I/O Stream try with resources
2. 먼저 응답
3. client 입력 읽고
4. 종료 조건문 추가
5. 다시 출력
```

**Prompt 변경**  
기존 Scanner로 입력받는 것 삭제, Data I/O Stream로 클라이언트와 입출력
- Data I/O Stream 생성자로 받기
- StringWriter(), PrintWriter(StringWriter) 객체 생성 => PrintWriter에 StringWriter를 붙이면 다양한 print를 사용해 버퍼에 저장할 수 있다.
- Scanner 관련 코드 삭제

print(), println(), printf() 생성
- 입력은 String을 받는다.
- PrintWriter의 메서드를 사용할 것이기 때문에 동일한 이름의 메서드로 생성
- 앞에서 생성한 PrintWriter 객체의 메서드 호출

String input()
```
1. printf(String, args) 먼저 출력
2. end() 메서드 호출하여 클라이언트로 보냄
3. 클라이언트가 입력하면 바로 리턴 (in.readUTF)
4. end()가 예외를 던져야 하니 try catch로 RuntimeException throw
```

close()
```
1. PrintWriter, StringWriter 자원 해제
2. Data I/O Stream은 ServerApp에서 try with resouces로 자동 해제됨
```

형 변환 메서드들
- 변경없음

processRequest() 수정
- Prompt를 try with resources에 추가
- excute 호출

MenuGroup excute() 수정
- Prompt를 action()에서 사용할 것이기 때문에 excute의 파라미터로 전달해야 한다. 
```
1. 따라서 MenuHandler, Menu 인터페이스 규칙에 프롬프트 받도록 추가
2. AbstractMenuHandler의 action에도 프롬프트 받도록 추가
```

**Stack 공유 문제**
현재 어디 메뉴에 위치해 있는지 출력하는 부분에서 Stack을 공유하고 있다.  

이를 수정하기 위해 Prompt로 Stack을 옮긴다. 


