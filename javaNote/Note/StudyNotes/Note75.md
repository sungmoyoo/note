# JSP
- 자바 서블릿 클래스를 만드는 재료로 사용된다.
- 그래서 서블릿 클래스를 만드는 "틀"이라 해서 "템플릿(template)"이라 부른다.
- JSP를 뷰-템플릿 기술이라 부르기도 한다.

## JSP의 구동원리
1) 웹브라우저 ==> 서블릿 컨테이너 
   - JSP 실행 요청
     예) http://localhost:8080/java-web/jsp/ex01.jsp
2) 서블릿 컨테이너가 실행
   2.1 JSP의 서블릿 객체를 찾는다
   2.2 있으면,
     2.2.1 그 서블릿 객체를 호출한다. service() ---> _jspService()
   2.3 없으면,
     2.3.1 JSP 엔진을 이용하여 JSP 파일을 가지고 서블릿 자바 소스 파일을 생성한다.
     2.3.2 자바 컴파일러를 이용하여 소스 파일을 컴파일 한다.
     2.3.3 서블릿 객체를 생성한다. - init() 호출 ---> jspInit()
     2.3.4 그 서블릿 객체를 호출한다. - service() 호출 ---> _jspService()
   2.4 JSP 파일이 변경된 상태라면,
     2.4.1 다시 "2.3"을 반복한다.
3) 서블릿 컨테이너 ==> 웹브라우저 
   - 서블릿 실행 결과를 응답

> JSP 파일은 스크립트가 인터프리팅 되는 것이 아니다. JSP 엔진이 JSP파일을 분석하여 서블릿 클래스를 생성한다. 즉 JSP 파일이 직접 실행되지 않는다.

*JSP 공부법  
: JSP를 작성할 때 사용하는 특정 태그들이 어떤 자바 코드를 생성하는지 이해(암기)하는 것이 중요하다.

### 서버 루트와 웹 애플리케이션 루트
1. 다른 경우
http://localhost:8888/myapp/board/list

2. 같은 경우
http://localhost:8888/board/list

## JSP 태그
**1. directive element**
<%@ --- %>

**2. declaration element**
<%! 필드, 메서드 %>

**3. scriptlet**
<% 자바 코드 %>
- 스크립트릿 안에 작성한 코드는 자바 클래스를 생성할 때 
  _jspService() 메서드 안에 작성한 순서 그대로 복사된다. 

**4. expression element**
<%= 표현식 %>

**5. JSP action tag**
<jsp: ---  />

**6. template data**
그냥 작성 -> 한줄당 하나의 출력문 생성


## template Data
JSP 파일에 그냥 작성하는 텍스트는 자바 출력 코드를 생성한다.

예를 들면 out.write("a")를 '그냥' 작성하면' 자바로 컴파일 될때
출력 코드 안에 템플릿 데이터가 작성되어 out.write("out.write('a')"); 로 변환된다.

## scriptlet과 JSP 주석
JSP 주석은 <%-- 주석문 --%>으로 작성한다. 이 태그로 작성된 문장은 JSP엔진이 무시되어 아예 출력되지 않는다. 

스크립트릿은 스크립트릿 안에 작성한 코드는 자바 클래스를 생성할 때 _jspService() 메서드 안에 작성한 순서 그대로 복사(삽입)된다. 

### expression element(표현식)
표현식이란 문장 중에 값을 리턴하는 문장이다. 
<%=표현식%>을 작성하면 해당 태그만 그대로 out.print(표현식)이라는 출력문으로 바뀐다. 

따라서 print문의 파라미터 자리에 올 수 있는 문장인 표현식만 작성할 수 있다. 

## declaration element
<%! %> 태그는 클래스 블록 안에 복사(삽입)한다. 즉 _jspService() 메서드 바깥에 작성된다.  

클래스에 멤버를 추가할 때 사용한다. jspInit()나 jspDestroy()와 같은 메서드를 오버라이딩 할 때도 사용할 수 있다.선언하는 위치는 상관없다. 

## directive element
페이지의 일부를 정의하고 제어하는데 사용하는 태그

**1.page 지시문**
페이지의 속성을 정의하는 태그.  
- language : 코드를 작성할 때 사용할 언어 설정, 현재는 java 언어만 사용가능하므로 생략 가능.
- contentType : response.setContentType()을 생성한다.
- pageEncoding: JSP 파일의 인코딩을 설정한다. 
- import : 자바의 import 문을 생성한다. 여러 import문을 한번에 작성할 수 있다.
- trimDirectiveWhitespaces="true" : 지시문 끝에 줄바꿈 코드를 무시하고 싶을 때 사용한다.
- buffer : 출력 버퍼의 크기를 변경할 때 사용한다. 기본은 8kb
- autoFlush="true": 출력 버퍼가 가득 찼을 때 자동으로 출력한다. false로 설정하면 출력 버퍼가 찼을 때 예외가 발생한다.
- errorPage="URL" : JSP를 실행하는 중에 오류가 발생했을 때 포워딩할 URL을 지정한다.
- isErrorPage="true|false" : JSP 페이지가 예외를 처리하는 페이지인지 지정한다. true로 설정하면, 포워딩할 때 받은 예외 객체를 사용할 수 있도록 Throwable 타입의 exception 빌트인 객체가 추가된다.

**2.include 지시문**
- 지정한 파일을 JSP로 포함시킨 후에 자바 서블릿 클래스를 생성한다. ex) <%@ include file="./ex08_header.txt"%>  
- 일반 텍스트 파일이면 된다. JSP 파일일 필요가 없다.  - RequestDispatcher의 include()와는 다르다. RequestDispatcher의 include()는 jsp: action tag를 사용한다.  
JSP 파일이 아니더라도<#@ page pageEncoding ..>  태그를 선언하여 해당 파일의 내용을 가져올 때 인식할 문자집합을 알려주어야 한다. 또한 JSP 엔진은 <%@ page ...%>는 참고만 할 뿐 가져오지는 않는다. 

**3.tablib 지시문**
외부에 따로 정의된 JSP 확장 태그를 가져올 때 사용한다.
- JSTL(JSP Standard Tag Library)
  - JSP 명세에 추가로 정의된 태그이다.
  - Servlet/JSP API에서는 JSTL 구현체를 제공하지 않는다.
  - 따로 구현된 라이브러리를 다운로드 받아야 한다.
- 개발자가 정의한 태그
  - 개발자가 따로 태그를 정의할 수 있다.
  - 그러나 실무에서는 유지보수를 일관성을 위해 JSTL과 같은 표준 API를 사용한다.
  - 즉 개발자가 자신의 회사에서만 사용할 태그를 따로 정의하지 않는다.

*JspContext
JSP와 태그핸들러 사이에 값을 공유하기 위한 저장소

### JSP 빌트인 객체
JSP를 가지고 서블릿 클래스를 만들 때 _jspService() 메서드에서 기본으로 준비하는 객체
1) request - HttpServletRequest => _jspService() 파라미터이다.
2) response - HttpServletResponse => _jspService() 파라미터이다.
3) pageContext - PageContext => _jspService()의 로컬 변수이다.
4) session - HttpSession => _jspService()의 로컬 변수이다.
5) application - ServletContext => _jspService()의 로컬 변수이다.
6) config - ServletConfig => _jspService()의 로컬 변수이다.
7) out - JspWriter => _jspService()의 로컬 변수이다.
8) page - 서블릿 객체를 가리킨다. 즉 this 이다. => _jspService()의 로컬 변수이다.
9) exception - Throwable => _jspService()의 로컬 변수이다.

## JSP 액션 태그

**1. jsp:useBean**
객체를 생성하거나 이미 생성된 객체를 사용할 수 있는 액션태그이다. 
```jsp
<jsp:useBean id="beanName" class="packageName.BeanClass" scope="scopeType" />
```

주요 속성  
- `id`: 객체를 꺼내거나 저장할 때 사용할 이름
- `class`: 보관소에서 객체를 찾을 수 없을 때 생성할 객체의 클래스명, 반드시 패키지 이름을 포함해 클래스명(fully-qualified name; FQName)을 지정해야 한다.
- `scope`: 객체를 꺼내거나 생성된 객체를 저장할 보관소 이름, 다음 4개의 값 중 한 개를 지정할 수 있다.('page','request','session','application') 
값을 지정하지 않으면 기본이 "page" 이다.
- `type`: 보관소에서 꺼낸 객체의 타입을 지정할 때 사용한다.

사용 형태
```jsp
<jsp:useBean id="객체명" class="클래스명" scope="보관소명"/>

<jsp:useBean id="레퍼런스명" type="레퍼런스타입" scope="보관소"/>
```
type 속성 대신에 class 속성을 사용하면 id로 지정한 객체를 찾지 못했을 때 해당 객체를 만들고, 그 id로 보관소에 저장한다.  
단 class 속성에는 generic 문법을 사용할 수 없다. 또한 보관소에 객체가 없을 때 생성해야 하기 때문에 class 속성에는 인터페이스를 설정할 수 없다.

만약 class 속성을 사용하면서 제네릭 문법을 같이 사용하고 싶다면 class와 type을 함께 사용하면 된다. id를 찾지 못할 경우 class 속성이 새 객체를 생성하여 보관하고 값을 꺼낼 때 type에 지정된 객체 타입으로 제네릭을 사용할 수 있다. 
```jsp
<jsp:useBean id="list" type="java.util.List<String>" class="java.util.ArrayList" scope="page"/>
```

**2. jsp:setProperty**
객체의 setter 메서드를 호출하는 액션태그이다. 자바 객체의 프로퍼티 타입이 자바 원시 타입과 문자열인 경우에만 가능하다. 다른 타입이라면 따로 처리해야 한다.
```jsp
<jsp:setProperty name="b3" property="no" value="100"/>
<jsp:setProperty name="b3" property="contents" value="내용입니다."/>
<jsp:setProperty name="b3" property="viewCount" value="88"/>
```

**3. jsp:include/jsp:forward**
jsp:include  
다른 페이지로 실행을 위임한 후 그 실행 결과를 포함시킬 때 사용한다. 실행이 끝난 후 제어권이 되돌아 온다. 즉 RequestDispatcher.include() 코드를 생성한다. page 속성에 지정하는 URL은 서블릿/JSP 이어야 한다.
```jsp
<jsp:include page="ex19_header.jsp"/>
```

jsp:forward  
다른 페이지로 실행을 위임할 때 사용한다. 제어권이 넘어가면 되돌아 오지 않는다. 즉 RequestDispatcher.forward() 코드를 생성한다. page 속성에 지정하는 URL은 서블릿/JSP 이어야 한다.
```jsp
<jsp:forward page="error.jsp"/>
```

## errorPage 속성과 isErrorPage 속성
errorPage  
page 지시문에 들어가는 속성, JSP를 실행하는 중에 오류가 발생했을 때 실행할 JSP를 지정할 수 있다. 이 속성에 URL을 지정하지 않으면 오류가 발생했을 때 서블릿 컨테이너의 기본 오류 출력 페이지가 실행된다.
```jsp
<%@ page 
  language="java" 
  contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"
  errorPage="ex20_error.jsp"%>
```

isErrorPage
오류가 발생했을 때 실행되는 JSP 쪽에서 그 오류 내용을 받을 때 사용한다. 실행되는 JSP 쪽에 이 속성을 `true`로 지정하지 않으면 실행되지 않는다. 
```jsp
<%@ page 
  language="java" 
  contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"
  isErrorPage="true"%>
```