<span style="font-size:133%">

# LinkedList
Java Collection Framework에서 제공하는 리스트 클래스 중 하나로, 요소들 간 각 데이터를 노드라고 부르며 노드들은 서로 주소로 연결되어 있다. 각 노드는 데이터와 다음 노드를 가리키는 포인터로 이루어져 있다.

- ArrayList와 마찬가지로 List를 상속받아 동일한 메서드를 가지고 있다. 

## LinkedList vs ArrayList
LinkedList
- 장점: 요소를 중간에 추가하거나 삭제할 때 빠른 성능을 제공한다.
- 단점: 특정 인덱스로 접근할 수 없어 검색 성능이 느리다. 또한 각 노드마다 참조를 갖기 때문에 메모리 사용이 ArrayList에 비해 비교적 비효율적이다.

ArrayList
- 장점: 검색 속도가 빠르고, 배열 기반이므로 요소 자체에 대한 액세스도 빠르다. 
- 단점: 중간에 요소를 삭제하거나 추가하는 작업이 느리다. 해당 위치 요소가 변경되면 이후 요소들의 인덱스를 바꾸어 주어야 하기 때문이다. 또한 배열은 Immutable 객체이므로 크기 조절 시 내부 배열을 새로 복사해야 한다.

# 중첩 클래스(nested class) 
**중첩 클래스: 클래스 사용범위를 제한하는 문법**
- 코드의 가독성 up
- 이해도 up
=> 유지보수성 향상

LinkedList.java 파일
```
class LinkedList{
  void add(){-}
  Object[] toArray(){-}
  Object get(){-}
  Object set(){-}
  void add(int,Object){-}
  Object remove(){-}

  static class Node{
    ...
  }
}
```
----> 컴파일 시
```
LinkedList.class
LinkedList$Node.class

클래스 블록당 클래스 파일이 생성된다.
```

</span>