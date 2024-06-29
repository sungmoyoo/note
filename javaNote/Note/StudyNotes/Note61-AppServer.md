# Application Server Architecture

기존 Applicatipn Architecture는 Server Application이 존재하지 않고 Client Application이 바로 DBMS 연결하는 형태이다.

1형태 pc - Unix - DBMS
이 때 당시에는 서버 비용이 비쌌다

2형태 pc - DBMS (JDBC 사용)
서버 비용이 비싸니 바로 연결하는 형태로 바뀌었다.
이 방식은 기능이 추가/변경/삭제 되면 Client Application을 매번 다시 재설치해야 한다.  


3형태 pc - Server App - DBMS
2형태의 불편함과 기술의 발전으로 다시 서버를 추가하는 방식으로 바뀐 것. 서버 비용도 1형태 시대보다 매우 저렴해졌고 Server Application을 구성하는 기술도 발전되어서 가능했다.



## StringWriter
ServerApp의 printMenu는 여러 문자열을 출력하는데, Server와 Client 간에는 문자열을 하나씩 주고 받고 있다.

이를 위해 버퍼를 사용하여 한번에 출력할 수 있도록 한다.

이때 사용되는 것이 StringWriter이다.
StringWriter는 출력하는 문자열을 내부 버퍼에 유지한다. 


## Prompt를 Handler를 실행시키는 action() 메서드의 파라미터로 옮긴 이유

DAO랑 Handler는 클라이언트가 접속해도 같은 객체를 사용한다.
만약 Prompt를 생성자로 받아버리면 여러 클라이언트가 접속했을 때 동일한 Prompt 객체를 사용하기 때문에 여러 클라이언트의 입력이 충돌될 수 있기 때문이다. 

위와 동일한 이유로 Stack의 breadcrumb도 공유할 수 없도록 Prompt로 옮겨야 한다. 
