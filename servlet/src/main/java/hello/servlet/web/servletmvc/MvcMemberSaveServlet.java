package hello.servlet.web.servletmvc;


import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//MVC 패턴 적용 - 회원 저장 서블릿 - 컨트롤러

// => [컨트롤러] : 서블릿 이용
// => [뷰] : JSP 이용
// => [모델] : HttpServletRequest 객체 이용

//HttpServletRequest를 Model로 사용한다.
// => request가 제공하는 setAttribute() 를 사용하면 request 객체에 데이터를 보관해서 뷰에 전달할 수 있다.
// => 뷰는 request.getAttribute() 를 사용해서 데이터를 꺼내면 된다.

// => [main/webapp/WEB-INF/views/save-result.jsp]에서 모델에 저장한 member 객체를 꺼낼 수 있지만, 너무 복잡해진다.
// => JSP는 ${} 문법을 제공하는데, 이 문법을 사용하면 request의 attribute에 담긴 데이터를 편리하게 조회할 수 있다.
// => HTML Form에 데이터를 입력하고 전송을 누르면 저장 결과를 확인할 수 있다.
// => MVC 덕분에 컨트롤러 로직과 뷰 로직을 확실하게 분리한 것을 확인할 수 있다. 향후 화면에 수정이 발생하면 뷰 로직만 변경하면 된다.

// => [실행] : http://localhost:8080/servlet-mvc/members/new-form
@WebServlet(name = "mvcMemberSaveServlet", urlPatterns = "/servlet-mvc/members/save")
public class MvcMemberSaveServlet extends HttpServlet {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        //Model에 데이터를 보관한다.
        request.setAttribute("member", member);

        String viewPath = "/WEB-INF/views/save-result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
