package hello.servlet.web.servletmvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//MVC 패턴 적용 - 회원 등록 폼 서블릿 - 컨트롤러

// => [컨트롤러] : 서블릿 이용
// => [뷰] : JSP 이용
// => [모델] : HttpServletRequest 객체 이용
// => HttpServletRequestㄴ는 내부에 데이터 저장소를 가지고 있는데,
// => request.setAttribute() , request.getAttribute() 를 사용하면 데이터를 보관하고 조회할 수 있음

// [/WEB-INF]
// => 이 경로안에 JSP가 있으면 외부에서 직접 JSP를 호출할 수 없다.
// => 우리가 기대하는 것은 항상 컨트롤러를 통해서 JSP를 호출하는 것이다.

//redirect vs forward
// => 리다이렉트는 실제 클라이언트(웹 브라우저)에 응답이 나갔다가, 클라이언트가 redirect 경로로 다시요청한다. 따라서 클라이언트가 인지할 수 있고, URL 경로도 실제로 변경된다.
// => 반면에 포워드는 서버 내부에서 일어나는 호출이기 때문에 클라이언트가 전혀 인지하지 못한다.

// => [main/webapp/WEB-INF/views/new-form.jsp]에서  form의 action을 보면 절대 경로( / 로 시작)가 아니라 상대경로( / 로 시작안함)인 것을 확인할 수 있다.
// => 이렇게 상대경로를 사용하면 폼 전송시 현재 URL이 속한 계층 경로 + save가 호출된다. 즉, [/servlet-mvc/members/save]
// => 주의) 이후 코드에서 해당 jsp를 계속 사용하기 때문에 상대경로를 사용한 부분을 그대로 유지해야 한다.

// => [실행] : http://localhost:8080/servlet-mvc/members/new-form
@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response); //dispatcher.forward() : 다른 서블릿이나 JSP로 이동할 수 있는 기능, 서버 내부에서 다시 호출이 발생함
    }
}


