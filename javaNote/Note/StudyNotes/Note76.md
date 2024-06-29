# EL
EL(Expression Language)은 콤마(.)와 대괄호([]) 등을 사용하여 객체의 프로퍼티나,리스트, 셋, 맵 객체의 값을 쉽게 꺼내고 설정하는 문법이다. 특히 값을 꺼낼 때는 OGNL 표기법을 사용한다.

## OGNL
Java 언어에서 객체 그래프를 탐색하고 조작하기 위한 표현 언어

## EL 표기법
- 문법
${ 객체명.프로퍼티명.프로퍼티명.프로퍼티명 }
${ 객체명["프로퍼티명"]["프로퍼티명"]["프로퍼티명"] }

## 보관소 값 꺼내기
```jsp

<%
pageContext.setAttribute("name", "홍길동");
request.setAttribute("name", "임꺽정");
session.setAttribute("name", "유관순");
application.setAttribute("name", "안중근");
%>

<!-- EL로 값 꺼내기 -->
PageContext 보관소 : 
${pageScope.name} 
<%=pageContext.getAttribute("name")%>

ServletRequest 보관소 : 
${requestScope.name}
<%=request.getAttribute("name")%>

HttpSession  보관소 : 
${sessionScope.name}
<%=session.getAttribute("name")%>

ServletContext 보관소 : 
${applicationScope.name} 
<%=application.getAttribute("name")%>
```
- 보관소의 이름을 지정하지 않으면 다음 순서로 값을 찾는다.  
pageScope ==> requestScope ==> sessionScope ==> applicationScope  
- 보관소에 저장된 값을 찾지 못하면 빈 문자열을 리턴한다.

## EL 리터럴
문자열: ${"홍길동"}<br>
문자열: ${'홍길동'}<br>
정수: ${100}<br>
부동소수점: ${3.14}<br>
논리값: ${true}<br>
null: ${null}<br>

## 배열 값 꺼내기
```jsp
<%
pageContext.setAttribute("names", new String[]{"홍길동","임꺽정","유관순"});
%>

${names[0]}<br>
${names[1]}<br>
${names[2]}<br>
${names[3]}<br>
```
- EL은 로컬변수에 접근할 수 없다. 보관소에 저장된 변수만 접근가능하다.

## List 객체에서 값 꺼내기
```
<%
ArrayList<String> nameList = new ArrayList<>();
nameList.add("김구");
nameList.add("안중근");
nameList.add("윤봉길");

pageContext.setAttribute("names", nameList);
%>

${names[0]}<br>
${names[1]}<br>
${names[2]}<br>
${names[3]}<br>
```
- 배열에서 값 꺼내는 방법과 동일

## Map 객체에서 값 꺼내기
```jsp
<%
HashMap<String,Object> map = new HashMap<>();
map.put("s01", "김구");
map.put("s02", "안중근");
map.put("s03", "윤봉길");
map.put("s04 ^^", "오호라");

pageContext.setAttribute("map", map);
%>

${map["s01"]}<br>
${map['s01']}<br>
${map.s01}<br>

<!-- 
  프로퍼티 이름이 변수 이름 짓는 규칙에 맞지 않다면 다음과 같이 OGNL 표기법을 쓸 수 없다.  
  ${map.s04 ^^} 
-->

${map["s04 ^^"]}<br>
${map['s04 ^^']}<br>
```

## 일반 객체에서 값 꺼내기
```jsp
<%
Member member = new Member();
member.setNo(100);
member.setName("홍길동");
member.setEmail("hong@test.com");
member.setTel("1111-2222");

pageContext.setAttribute("member", member);
%>

${member.getNo()}<br>
${member.no}<br>
${member["no"]}<br>
${member['no']}<br>
```

## EL 연산자
- 산술연산자
100 + 200 = ${100 + 200}
100 - 200 = ${100 - 200}
100 * 200 = ${100 * 200}
100 / 200 = ${100 / 200}
100 div 200 = ${100 div 200}
100 % 200 = ${100 % 200}
100 mod 200 = ${100 mod 200}

- 논리연산자
true && false = ${true && false}
true and false = ${true and false}
true || false = ${true || false}
true or false = ${true or false}
!true = ${!true}
not true = ${not true}

- 관계 연산자
100 == 200 = ${100 == 200}
100 eq 200 = ${100 eq 200}
100 != 200 = ${100 != 200}
100 ne 200 = ${100 ne 200}
100 > 200 = ${100 > 200}
100 gt 200 = ${100 gt 200}
100 >= 200 = ${100 >= 200}
100 ge 200 = ${100 ge 200}
100 &lt; 200 = ${100 < 200}
100 lt 200 = ${100 lt 200}
100 &lt;= 200 = ${100 <= 200}
100 le 200 = ${100 le 200}

- empty
보관소에 해당 객체가 없는지 검사한다. 없으면 true, 있으면 false.
  ${empty name}

- 조건 연산자
name == "홍길동" : ${name == "홍길동" ? "맞다!" : "아니다!"}

# JSTL
JSTL(JSP Standard Tag Library)은 JSP 확장 태그로 외부 라이브러리를 가져와 사용해야 한다. 

## JSTL 라이브러리 모듈
- Core(c) : http://java.sun.com/jsp/jstl/core
- XML(x) : http://java.sun.com/jsp/jstl/xml
- I18N(fmt) : http://java.sun.com/jsp/jstl/fmt
- Database(sql) : http://java.sun.com/jsp/jstl/sql
- Functions(fn) : http://java.sun.com/jsp/jstl/functions

## 모듈 사용
JSTL 모듈의 네임스페이스를 가져와 사용한다.
```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
```

- JSTL 태그 사용
<접두어명:태그명 속성="값" 속성="값"/> 

*I18N(Internationalization 의 약자)  
=> 프로그램을 짤 때 여러 언어를 고려해서 코딩하는 것을 말한다.  
=> 특히 화면에서 버튼에 제목이나 라벨을 출력할 때 특정 언어로 고정된 값을 출력하지 않고, 외부 파일(예: label_ko_KR.properties)에서 읽어 온 값을 출력하도록 프로그래밍 하는 것.
   
*L10N(Localization 의 약자)  
=> 특정 언어에 대해 라벨 텍스트를 담은 프로퍼티 파일(예: label_ko_KR.properties)을 작성하는 것을 말한다.

## Core의 out (c:out)
출력문을 만드는 태그.
```jsp
<%
pageContext.setAttribute("name", "유관순");
%>

<c:out value="임꺽정"/><br>
<c:out value="${null}" default="홍길동"/><br>
<c:out value="${null}">홍길동</c:out><br>
<c:out value="${'임꺽정'}" default="홍길동"/><br>
<c:out value="${name}" default="홍길동"/><br>
<c:out value="${name2}" default="홍길동"/><br>
```

## Core의 set (c:set)
보관소에 값을 저장하는 태그
```jsp
<c:set scope="request" var="name1" value="홍길동2"/>
1: ${requestScope.name1}<br>
2: ${pageScope.name1}<br>
3: ${name1}<br> 

<c:set target="${pageScope.m1}" property="email" value="hong@test.com"/>
```
- scope를 생략하면 기본이 PageContext이다. 

## Core의 remove(c:remove)
보관소에 저장된 값을 제거하는 태그
```jsp
<c:remove var="name" scope="page"/>
<c:remove var="name" scope="request"/>
<c:remove var="name" scope="session"/>
<c:remove var="name" scope="application"/>
```

## Core의 if(c:if)
조건문을 만드는 태그
```jsp
<c:set var="name" value="홍길동"/>
<c:set var="age" value="16"/>
<c:set var="gender" value="man"/>

<!-- 조건문은 EL로 만드는 것이 정확하다 -->
<c:if test="${not empty name}">
    <p>${name}님 환영합니다!
</c:if>
<c:if test="${age < 19}">
    <p>미성년입니다.</p>
</c:if>
<c:if test="${age >= 19}">
    <p>성년입니다.</p>
</c:if>
```

조건문의 결과를 보관소에 저장
```jsp
<!-- 시작태그와 끝 태그 사이에 값이 없다면 끝태그 대신 `/`로 처리가능 -->
<c:if test="${gender == 'woman'}" var="r1"/>
${r1}<br>
${pageScope.r1 ? "여성" : "남성"}<br>
```
- 기본 저장소는 

## Core의 choose(c:choose)
다중 조건문을 만드는 태그, switch와 같다.
```jsp
<c:set var="name" value="홍길동"/>
<c:set var="age" value="16"/>

<c:choose>
    <c:when test="${age < 19}">
        <p>미성년입니다.</p>
    </c:when>
    <c:when test="${age >= 19 and age < 65}">
        <p>성년입니다.</p>
    </c:when>
    <c:otherwise>
        <p>노인입니다.</p>
    </c:otherwise>
</c:choose>
```
# Core의 forEach(c:forEach)
반복문을 만든다. 
```jsp
<!-- 배열 -->
<%
pageContext.setAttribute("names", new String[]{"홍길동", "임꺽정", "유관순"});
%>

<ul>
<c:forEach items="${names}" var="n">
    <li>${n}</li>
</c:forEach>
</ul>

<!-- Collection 객체-->
<%
List<String> names2 = new ArrayList<>();
names2.add("홍길동");
names2.add("임꺽정");
names2.add("유관순");
pageContext.setAttribute("names2", names2);
%>

<ul>
<c:forEach items="${names2}" var="n">
    <li>${n}</li>
</c:forEach>
</ul>

<!-- Map 객체-->
<%
Map<String,Object> names3 = new HashMap<>();
names3.put("s01", "홍길동");
names3.put("s02", "임꺽정");
names3.put("s03", "유관순");
pageContext.setAttribute("names3", names3);
%>
<!-- Map 객체에 대해 반복문을 돌리면 var로 저장되는 것은 key와 value를 갖고 있는 Entry 객체이다. -->
<ul>
<c:forEach items="${names3}" var="n">
    <li>${n.getKey()} : ${n.getValue()} => ${n.key} : ${n.value}</li>   
</c:forEach>
</ul>

<!--CVS(comma separated value) 문자열 -->
<%
pageContext.setAttribute("names4", "홍길동,임꺽정,유관순,김구");
%>

<ul>
<c:forEach items="${names4}" var="n">
    <li>${n}</li>
</c:forEach>
```


## Core의 forTokens(c:forTokens)
반복문을 만든다.
```jsp
<%
pageContext.setAttribute("names1", "홍길동,임꺽정,유관순,김구");
%>

<c:forTokens items="${names1}" var="n" delims=",">
  <li>${n}</li>
</c:forTokens>
```

## Core의 url(c:url)
복잡한 형식의 url을 만드는 태그
```jsp
<c:url value="https://search.naver.com/search.naver" var="naverUrl">
    <c:param name="where" value="nexearch"/>
    <c:param name="sm" value="top_hty"/>
    <c:param name="fbm" value="1"/>
    <c:param name="ie" value="utf8"/>
    <c:param name="query" value="홍길동"/>
</c:url>

<pre>${naverUrl}</pre>
```

## Core의 import (c:import)
HTTP 요청을 수행하는 코드를 만든다.
```jsp
<!--HTTP 요청-->
<c:url value="ex10_sub.jsp" var="url1">
    <c:param name="name" value="홍길동"/>
    <c:param name="age" value="20"/>
    <c:param name="gender" value="woman"/>
</c:url>

<pre>${url1}</pre>

<!-- 지정된 URL을 요청하고 서버로부터 받은 콘텐트를 contents라는 이름으로 PageContext 보관소에 저장한다. -->
<c:import url="${url1}" var="contents"/>

<textarea cols="120" rows="20">${pageScope.contents}</textarea>

<c:import url="https://www.naver.com" var="contents2"/>
<textarea cols="120" rows="20">${pageScope.contents2}</textarea>
```

## Core의 redirect(c:redirect)
redirect 응답하는 태그
```Jsp
<!--조건이 참일 때 redirect-->
<c:if test="${param.search == 'naver'}">
    <c:redirect url="http://www.naver.com"/>
```

## I18N의 parseDate(fmt:parseDate)
문자열로 지정된 날짜 값을 java.util.Date 객체로 만드는 태그
```jsp
<fmt:parseDate value="2020-04-14" pattern="yyyy-MM-dd" var="d1"/>
<fmt:parseDate value="04/14/2020" pattern="MM/dd/yyyy" var="d2"/>

<%
Date date1 = (Date)pageContext.getAttribute("d1");
Date date2 = (Date)pageContext.getAttribute("d2");

out.println(date1.toString() + "<br>");
out.println(date2.toString() + "<br>");
%>
```

## I18N의 formatDate(fmt:formatDate)
java.util.Date 객체의 값을 문자열로 형식 포맷해주는 태그
```jsp
<%
pageContext.setAttribute("today", new Date());
%>

<fmt:formatDate value="${pageScope.today}" 
    pattern="yyyy-MM-dd"/><br>
<fmt:formatDate value="${pageScope.today}" 
    pattern="MM/dd/yyyy"/><br>
<fmt:formatDate value="${pageScope.today}" 
    pattern="yyyy-MM-dd hh:mm:ss"/><br>
<hr>

<fmt:formatDate value="${pageScope.today}" 
    pattern="yyyy-MM-dd"
    var="str1"/>
    
<p>오늘 날짜는 '${pageScope.str1}'입니다.</p>   
```