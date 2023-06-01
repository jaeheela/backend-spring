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
import java.util.List;
//MVC 패턴 적용 - 회원 목록 조회 서블릿 - 컨트롤러

// => [컨트롤러] : 서블릿 이용
// => [뷰] : JSP 이용
// => [모델] : HttpServletRequest 객체 이용

// => request 객체를 사용해서 List<Member> members 를 모델에 보관했다.

// => [main/webapp/WEB-INF/views/members.jsp]에서 모델에 담아둔 members를 JSP가 제공하는 taglib기능을 사용해서 반복하면서 출력했다.
// => members 리스트에서 member 를 순서대로 꺼내서 item 변수에 담고, 출력하는 과정을 반복한다.
// => <c:forEach> 이 기능을 사용하려면 다음과 같이 선언해야 한다.
// <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 해당 기능을 사용하지 않고, 다음과 같이 출력해도 되지만, 매우 지저분하다.
//<%
//    for (Member member : members) {
//        out.write("    <tr>");
//        out.write("     <td>" + member.getId() + "</td>");
//        out.write("     <td>" + member.getUsername() + "</td>");
//        out.write("     <td>" + member.getAge() + "</td>");
//        out.write("    </tr>");
//    }
//%>
// => JSP와 같은 뷰 템플릿은 이렇게 화면을 렌더링 하는데 특화된 다양한 기능을 제공한다.
// => 참고) 앞서 설명했듯이 JSP를 학습하는 것이 이 강의의 주 목적이 아니다. JSP가 더 궁금한 분들은 이미 수 많은 자료들이 있으므로 JSP로 검색하거나 관련된 책을 참고하길 바란다. (반나절이면 대부분의 기능을 학습할 수 있다.)

// => [실행] : http://localhost:8080/servlet-mvc/members
@WebServlet(name = "mvcMemberListServlet", urlPatterns = "/servlet-mvc/members")
public class MvcMemberListServlet extends HttpServlet {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Member> members = memberRepository.findAll();

        request.setAttribute("members", members);

        String viewPath = "/WEB-INF/views/members.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}


//MVC 패턴 - 한계
// => MVC 패턴을 적용한 덕분에 컨트롤러의 역할과 뷰를 렌더링 하는 역할을 명확하게 구분할 수 있다.
// => 특히 뷰는 화면을 그리는 역할에 충실한 덕분에, 코드가 깔끔하고 직관적이다.
// => 단순하게 모델에서 필요한 데이터를 꺼내고, 화면을 만들면 된다.
// => 그런데 컨트롤러는 딱 봐도 중복이 많고, 필요하지 않는 코드들도 많이 보인다.


//MVC 컨트롤러의 단점
//1. 포워드 중복
// => View로 이동하는 코드가 항상 중복 호출되어야 한다.
// => 물론 이 부분을 메서드로 공통화해도 되지만, 해당 메서드도 항상 직접 호출해야 한다.
// => RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
// => dispatcher.forward(request, response);

// 2. ViewPath에 중복
// => String viewPath = "/WEB-INF/views/new-form.jsp";
// => prefix: /WEB-INF/views/
// => suffix: .jsp
// => 그리고 만약 jsp가 아닌 thymeleaf 같은 다른 뷰로 변경한다면 전체 코드를 다 변경해야 한다.

//3. 사용하지 않는 코드
// => 다음 코드를 사용할 때도 있고, 사용하지 않을 때도 있다.
// => 특히 response는 현재 코드에서 사용되지 않는다.
// => HttpServletRequest request, HttpServletResponse response
// => 그리고 이런 HttpServletRequest , HttpServletResponse 를 사용하는 코드는 테스트 케이스를 작성하기도 어렵다.

//4. 공통 처리의 어려움
// => 기능이 복잡해질수록 컨트롤러에서 공통으로 처리해야 하는 부분이 점점 더 많이 증가할 것이다.
// => 단순히 공통 기능을 메서드로 뽑으면 될 것 같지만, 결과적으로 해당 메서드를 항상 호출해야 하고, 실수로 호출하지 않으면 문제가 될 것이다.
// => 그리고 호출하는 것 자체도 중복이다.
// => 정리하면 공통 처리가 어렵다는 문제가 있다.
// => 이 문제를 해결하려면 컨트롤러 호출 전에 먼저 공통 기능을 처리해야 한다.
// => 소위 수문장 역할을 하는 기능이 필요하다. 점
// => 프론트 컨트롤러(Front Controller) 패턴을 도입하면 이런 문제를 깔끔하게 해결할 수 있다. (입구를 하나로!)
// => 스프링 MVC의 핵심도 바로 이 프론트 컨트롤러에 있다.