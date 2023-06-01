<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%-- 회원 저장 JSP--%>
<%-- JSP는 자바 코드를 그대로 다 사용할 수 있다.--%>
<%-- <%@ page import="hello.servlet.domain.member.MemberRepository" %> : 자바의 import 문과 같다. --%>
<%-- <% ~~ %>  : 이 부분에는 자바 코드를 입력할 수 있다. --%>
<%-- <%= ~~ %> : 이 부분에는 자바 코드를 출력할 수 있다.--%>
<%-- 회원 저장 JSP를 보면, 회원 저장 서블릿 코드와 같다. 다른 점이 있다면, HTML을 중심으로 하고, 자바코드를 부분 부분 입력해주었다. --%>
<%-- <% ~ %> 를 사용해서 HTML 중간에 자바 코드를 출력하고 있다.--%>

<%
    //request, response 그냥 사용 가능
    // => 서븧릿으로 자동으로 변환되기 때문
    MemberRepository memberRepository = MemberRepository.getInstance();

    System.out.println("MemberSaveServlet.service");
    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));

    Member member = new Member(username, age);
    memberRepository.save(member);

%>
<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
    <li>id=<%=member.getId()%></li>
    <li>username=<%=member.getUsername()%></li>
    <li>age=<%=member.getAge()%></li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
