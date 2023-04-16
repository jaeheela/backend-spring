package hello.servlet.web.frontcontroller.v2;

import hello.servlet.web.frontcontroller.MyView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//View 분리 - v2

// v1의 단점
// => 모든 컨트롤러에서 뷰로 이동하는 부분에 중복이 있고, 깔끔하지 않다.
// => String viewPath = "/WEB-INF/views/new-form.jsp";
// => RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
// => dispatcher.forward(request, response);
// => 이 부분을 깔끔하게 분리하기 위해 별도로 뷰를 처리하는 객체를 만들자.

//V2 구조
// => 서블릿과 비슷한 모양의 컨트롤러 인터페이스 생성 - ControllerV2
// => 각 컨트롤러들은 이 인터페이스를 상속받아 작성 - MemberFormControllerV2, MemberSaveControllerV2 , MemberListControllerV2
// => 프론트 컨트롤러는 인터페이스(ControllerV1)를 호출해서 구현과 관계없이 로직의 일관성 제공 - FrontControllerServletV2
// => 추가) 각 컨트롤러들은 복잡한 dispatcher.forward()를 직접 생성해서 호출하지 않고 단순히 MyView 객체를 생성하고 거기에 뷰 이름만 넣고 반환함
// => 중복이 확실하게 제거됨

//    @Override
//    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String viewPath = "/WEB-INF/views/new-form.jsp";
//        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
//        dispatcher.forward(request, response);
//    }
// => 변경 : 컨트롤러가 뷰를 반환함
//    @Override
//    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        return new MyView("/WEB-INF/views/new-form.jsp");
//    }

public interface ControllerV2 {

    MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
