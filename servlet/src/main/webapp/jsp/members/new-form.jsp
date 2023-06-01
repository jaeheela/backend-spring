<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- 회원 등록 폼 JSP--%>
<%-- 첫 줄은 JSP문서라는 뜻이다. JSP 문서는 이렇게 시작해야 한다. 회원 등록 폼 JSP를 보면 첫 줄을 제외하고는 완전히 HTML와 똑같다.--%>
<%-- JSP는 서버 내부에서 서블릿으로 변환되는데, 우리가 만들었던 MemberFormServlet과 거의 비슷한 모습으로 변환된다.--%>
<%-- 실행 : http://localhost:8080/jsp/members/new-form.jsp - 실행시 .jsp 까지 함께 적어주어야 한다.--%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/jsp/members/save.jsp" method="post">
    username: <input type="text" name="username" />
    age:      <input type="text" name="age" />
    <button type="submit">전송</button>
</form>
</body>
</html>
