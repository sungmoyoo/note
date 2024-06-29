# Software
## 1.소프트웨어의 구분
- 소프트웨어는 크게 System S/W와 Application S/W로 나뉜다.
  ### 1.1 System Software  
  : os, driver, embedded, IoT 등 H/W를 제어하는 소프트웨어
  ### 1.2 Application Software  
  : excel, photoshop, lol, gmail 등 응용프로그램

<br></br>

## 2. 응용 소프트웨어(Application S/W)
- 응용소프트웨어는 또 Standalone과 client/server 프로그램으로 나뉜다.
  ### 2.1 Standalone  
  : MS-Docs, Excel, 지뢰찾기 등 인터넷에 연결되지 않고 설치해서 단독적으로 실행할 수 있는 프로그램
  ### 2.2 Client/Server  
  : client와 server 간 통신을 통해 각각 요청과 응답을 해주는 프로그램 client가 PC에서 특정 파일(클라이언트)를 설치하면 client/server 구조이고 웹브라우저가 client 역할을 하면 Web(server) Application Software(program)이라고 한다. 

<br></br>

## 3. Web Application Software 동작 과정
```
1. 웹브라우저(클라이언트)에서 웹서버로 요청
2. url 접속시 HTML, CSS, JS, Images는 해당 파일 자체를 다운로드하여 화면 출력
3. Java의 경우 서버에서 실행한 뒤 나온 결과값을 리턴하는 구조
4. 직접적으로 웹서버에서 java를 실행하고 관리하는 것이 힘들어 Servlet Container에 위임
5. servlet container는 spring framework를 실행시켜 결과를 받음
6. spring framework는 java객체를 직접적으로 관리  
  
spring boot는 servlet container와 springframework를 합친 것
```

<br></br>

## 4. Application Architecture 진화
- 웹 어플리케이션은 기본적으로
```
1. 클라이언트가 서버에 요청을 보낸다.
2. 서버가 클라이언트에 요청을 응답한다.
``` 
이 두 가지 과정만을 통해 동작하기에 충분했다.  
시간이 흐르며 과도한 데이터량을 통제하기 위한 DBMS가 도입되었고 더 시간이 흘러 동적인 사이트가 많아지고 로직이 복잡해짐에 따라 웹서버만으로 커버가 불가능했고 이에 웹 애플리케이션 서버(WAS)가 등장했다.  


