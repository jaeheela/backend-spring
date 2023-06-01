package hello.servlet.web.frontcontroller.v2;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
// v1의 단점
// => 모든 컨트롤러에서 뷰로 이동하는 부분에 중복이 있고, 깔끔하지 않다.
// => String viewPath = "/WEB-INF/views/new-form.jsp";
// => RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
// => dispatcher.forward(request, response);
// => 이 부분을 깔끔하게 분리하기 위해 별도로 뷰를 처리하는 객체를 만들자.

//View 분리 - v2
// => FrontControllerServletV2 프론트 컨트롤러 : 프론트 컨트롤러는 인터페이스(ControllerV2)를 호출해서 구현과 관계없이 로직의 일관성 제공
// => ControllerV2 인터페이스 : 서블릿과 비슷한 모양의 컨트롤러 인터페이스 생성
// => MemberFormControllerV2, MemberSaveControllerV2 , MemberListControllerV2 클래스 : 각 컨트롤러들은 이 인터페이스를 상속받아 작성
// => 추가) 각 컨트롤러들은 복잡한 dispatcher.forward()를 직접 생성해서 호출하지 않고 단순히 MyView 객체를 생성하고 거기에 "뷰 이름만 넣고 반환"해 중복 제거
// => ControllerV2의 반환 타입이 MyView 이므로 프론트 컨트롤러는 컨트롤러의 호출 결과로 MyView를 반환 받음
// => 그리고 view.render() 를 호출하면 forward 로직을 수행해서 JSP가 실행됨

//프론트 컨트롤러 V2
// => [등록 실행] : http://localhost:8080/front-controller/v2/members/new-form
// => [목록 실행] : http://localhost:8080/front-controller/v2/members
@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {

    private Map<String, ControllerV2> controllerMap = new HashMap<>();

    public FrontControllerServletV2() {
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        ControllerV2 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyView view = controller.process(request, response);
        view.render(request, response);
    }
}