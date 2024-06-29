# World Wide Web 등장배경과 활용
웹은 1989년 CERN(유럽 입자 물리학 연구소)에서 팀 버너스 리에 의해 개발됨. 

기존의 방식은 FTP C/S를 사용하였고 이는 논문에 언급된 다른 논문을 보기 위해서는 그 논문이 업로드되어 있는 서버에 접속해서 다운로드 받아야 한다. 이 방식은 서버 주소와 파일경로를 알아야 해서 공유가 어렵고 텍스트 위주이다. 

팀 버너스 리는 이러한 기존 방식을 개선하기 위해 인터넷과 Hyper-Text 를 결합한 기술을 고안함.

이후 Markup Language를 정의하여 Hyper-Text Markup Language(HTML)가 탄생함

* Hyper-Text?  
Hyper-Text는 문서 안에 다른 문서의 연결정보를 삽입 가능하고 그림 등 다른 매체를 삽입할 수 있는 장점을 가진다.)
* Markup?  
데이터를 제어하는 명령 = meta data = tag

# HTTP
기존 FTP C/S방식에서 HTML으로 넘어감에 따라 기존의 FTP 방식에서 사용하던 프로토콜로는 다른 문서와 연결된 HTML을 주고 받기에는 불편하였다. 따라서 HTML을 전문적으로 주고받는 프로토콜을 정의한 것이 HTTP(Hyper-Text Tranfer Protocol)이다. 이때 클라이언트쪽으로 HTTP client, 서버쪽을 HTTP server라고 한다. 

```
HTML은 문서안에 연결정보를 삽입함으로써 여러 문서 간 연결이 가능해졌고 이의 형태가 거미망과 유사하여 `Web`이라 부르게 되었고 문서를 탐색하는 클라이언트는 `Browser`라고 부르게 되었다.
> HTTP Server = Web, HTTP Client = Browser
```
# 정적 웹페이지
초기 웹페이지는 페이지 열람만 할 수 있어 홍보 목적으로 사용되었다.

# 동적 웹페이지와 CGI
웹의 기능을 향상시키기 위해 CGI(Common Gateway Interface)를 활용한 동적 웹페이지가 등장하였다.  동적 웹페이지는 프로그램-스크립트-웹 애플리케이션의 형태로 발전해왔다. 

## 1. 프로그램 활용
CGI는 웹서버와 App 사이에 데이터를 주고 받는 규칙으로 이 규칙에 따라 C/C++로 만든 CGI프로그램이 웹 애플리케이션의 시초이다. 방명록, 게시판을 예로 들 수 있다.

## 2. 스크립트 활용
Perl, PHP 등 컴파일 방식(인터프리터)의 스크립트를 활용한 스크립트 엔진이 등장하였다. 텍스트 처리가 쉬워 주문서비스와 같은 곳에 활용되었다.

## 3. 웹 애플리케이션
C/S 아키텍처에서 Web App. 아키텍처로 바뀌면서 프로그램 변경 및 배포가 용이해지고 보안이 쉬워졌다.  
예를 들면 기존 C/S 아키텍처에서는 여러 클라이언트에서 DBMS에 직접 접속하기 때문에 아이디 암호 노출 시 DB 보안에 치명적이고 기능을 변경할 때마다 App을 재배포해야 하는 불편함이 있다.  
Web App. 아키텍처는 클라이언트의 역할을 브라우저가 대신하고 서버쪽의 App만 DB에 접근하기 때문에 보안이 강화되고, 기능 변경때도 서버쪽에만 배포하므로 유지보수가 쉽다. 


**Web Application 아키텍처**
1. Monolithic 아키텍처
- 전통의 아키텍처를 의미, 하나의 서비스 또는 애플리케이션이 모든 기능를 담는 구조
- 장점
  - 기능 간의 연동이 쉽다.(직접 메서드 호출 가능)
- 단점
  - 기능 변경 시 전체 서비스를 재시작
  - 일부 기능만 동작시키거나 멈출 수 없다.
  - 특정 기능에 더 많은 자원을 할당할 수 없다. 
  - 강결합, 상호 기능이 매우 의존적이다. 

2. MicroService 아키텍처
- 기능별로 서비스를 분리한 구조, 기능 별로 DBMS가 다르다.
- 장점
  - 기능별 제어가 쉽다. 일부 기능만 사용 가능
  - 기능 변경 시 배포가 쉽다. 
  - 특정 기능에 자원을 더 할당할 수 있다.
- 단점
  - 기능 간에 연동 시 오버헤드 발생, 즉 반복적인 부가작업이 늘어남
  - 세션 유지 및 관리가 번거롭다.
  - DB 무결성 관리가 어렵다. 


# Java Web Application
# Java EE
JavaEE(Enterprise Edition) : 기업에서 사용할 App을 만드는데 필요한 기술과 도구를 제공. 다중 동시사용자용 App 개발에 필요한 기술 및 도구 모음
- 동시 접속 제어(통신, 멀티스레딩)
- 사용자인증/권한 제어
- 자원관리
- App 분산 처리

**Java EE의 주요 기술**
|                            |                                     | JavaEE 8                        | JavaEE 7                        | JavaEE 6                        | JavaEE 5                      |
|----------------------------|-------------------------------------|---------------------------------|---------------------------------|---------------------------------|-------------------------------|
| Web<br>기술                | Servlet<br>JSP<br>JSF<br>EL<br>JSTL | 4.0<br>2.3<br>2.3<br>3.0<br>1.2 | 3.1<br>2.3<br>2.2<br>3.0<br>1.2 | 3.0<br>2.2<br>2.0<br>2.2<br>1.2 | 2.5<br>2.1<br>1.2<br>X<br>1.0 |
| 분산 <br>컴포넌트 <br>기술 | EJB<br>JPA<br>JTA                   | 3.2<br>2.2<br>1.2               | 3.2<br>2.1<br>1.2               | 3.1<br>2.0<br>1.1               | 3.0<br>1.0<br>1.0             |
| 분산 <br>서비스 <br>기술   | WebService<br>JAX-RPC               | 1.3<br>1.1                      | 1.3<br>1.1                      | 1.3<br>1.1                      | 1.0<br>1.1                    |

*JSF: JSF 태그로 HTML, Javascript, css를 자동생성해주는 기술, 하지만 JS 라이브러리가 대거 등장하며 발전하면서 실패함

## Servlet과 EJB, RESTful

<img src="../../img/JavaEE.png">

EJB 방식은 Stub 객체가 자바 객체이기 때문에 Java App.에서만 실행할 수 있다. RESTful 방식은 HTTP 프로토콜에 따라 요청과 응답을 하기 때문에 프로그래밍 언어와 상관없이 플랫폼, 기술 독립적이다. 따라서 현재는 거의 RESTful 방식을 사용한다.

## JavaEE와 서버
**JavaEE Implementation Server**
JavaEE 기술 명세에 따라 동작하도록 구현한 서버  
웹기술, 분산컴포넌트 기술, 분산서비스 기술, 자원관리 등 JavaEE 전체 기술을 구현한다.  
ex) JBoss, WebLogic, WebSphere, JEUS, Geronimo, GlassFish 등

**Servlet Container**
JavaEE 기술 중 웹 기술만 구현 Servlet, JSP, EL, JSTL 등..
따라서 분산 컴포턴트 기술이나 분산 서비스 기술을 실행할 수 없다. 
ex) Tomcat, Resin, Jetty, Undertow


## JavaEE 기술 버전과 서버 버전
|           |            | Servlet | JSP |
|-----------|------------|---------|-----|
| JavaEE    | Tomcat6    | 2.5     | 2.1 |
| JavaEE    | Tomcat7    | 3.0     | 2.2 |
| JavaEE    | Tomcat8    | 3.1     | 2.3 |
| JavaEE    | Tomcat9    | 4.0     | 2.3 |
| JakartaEE | Tomcat10   | 5.0     | 3.0 |
| JakartaEE | Tomcat10.1 | 6.0     | 3.1 |

Tomcat버전 그 상위버전의 클래스/인터페이스/메서드를 사용할 수 없다. 그에 따라 Servlet JSP도 마찬가지이다.  
하위 버전은 사용 가능하지만 기술문서를 볼 때 버전을 잘 확인해야 한다. 

## JavaEE와 JakartaEE
- ~JavaEE 8버전까지는 Oracle이 유지보수  
- JakartaEE 8버전부터는 Eclipse에서 유지보수  

JavaEE와 JakartaEE는 같은버전, 패키지명만 바뀜


# Web 프로젝트 표준 디렉토리 구조
```
app/
  /-src/
     /-main/
       /-java/  <-- 자바 소스폴더: .java
       /-resources/  <-- 자바 소스폴더: .xml, .properties 등
       /-webapp/
         /-WEB-INF/
           /-web.xml  <-- deployment descriptor file(DD file) (선택사항)
           /-기타 웹 애플리케이션 설정파일

* DD file: component의 배치 정보를 기술한 파일
```

