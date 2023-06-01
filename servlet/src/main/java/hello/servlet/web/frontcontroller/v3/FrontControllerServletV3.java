package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//Model 추가 - v3

//1. 서블릿 종속성 제거
// => 컨트롤러 입장에서 HttpServletRequest, HttpServletResponse이 꼭 필요할까?
// => 요청 파라미터 정보는 자바의 Map으로 대신 넘기도록 하면 지금 구조에서는 컨트롤러가 서블릿 기술을 몰라도 동작할 수 있다.
// => 그리고 request 객체를 Model로 사용하는 대신에 별도의 Model 객체를 만들어서 반환하면 된다.
// => 우리가 구현하는 컨트롤러가 서블릿 기술을 전혀 사용하지 않도록 변경해보자. 이렇게 하면 구현 코드도 매우 단순해지고, 테스트 코드 작성이 쉽다.

//2. 뷰 이름 중복 제거
// => 컨트롤러에서 지정하는 뷰 이름에 중복이 있는 것을 확인할 수 있다. 컨트롤러는 뷰의 논리 이름을 반환하고, 실제 물리 위치의 이름은 프론트 컨트롤러에서 처리하도록 단순화 하자.
// => 이렇게 해두면 향후 뷰의 폴더 위치가 함께 이동해도 프론트 컨트롤러만 고치면 된다. - /WEB-INF/views/new-form.jsp >> new-form , /WEB-INF/views/save-result.jsp >> save-result , /WEB-INF/views/members.jsp >> members

//V3 구조
// => FrontControllerServletV3 프론트 컨트롤러 : 프론트 컨트롤러는 인터페이스(ControllerV3)를 호출해서 구현과 관계없이 로직의 일관성 제공
// => ControllerV3 인터페이스 : 서블릿과 비슷한 모양의 컨트롤러 인터페이스 생성
// => MemberFormControllerV3, MemberSaveControllerV3 , MemberListControllerV3 클래스 : 각 컨트롤러들은 이 인터페이스를 상속받아 작성
// => 지금까지는 컨트롤러에서 서블릿에 종속적인 HttpServletRequest를 사용했었음
// => 그리고 Model도 request.setAttribute() 를 통해 데이터를 저장하고 뷰에 전달했음
// => 추가) 서블릿의 종속성을 제거하기 위해 Model을 직접 만들고, 추가로 View 이름까지 전달하는 객체를 만들것 - ModelView
// => 이번 버전에서는 컨트롤러에서 HttpServletRequest를 사용할 수 없음. 따라서 직접 request.setAttribute() 를 호출할 수도 없음
// => 별도의 Model 필요 - ModelView(뷰의 이름과 뷰를 렌더링할 때 필요한 model 객체를 가지고 있음)
// => model은 단순히 map으로 되어 있으므로 컨트롤러에서 뷰에 필요한 데이터를 key, value로 넣어주면 됨
// => ControllerV3 컨트롤러는 서블릿 기술을 전혀 사용하지 않음, 따라서 구현이 매우 단순해지고 테스트 코드 작성시 테스트 하기 쉬움
// => HttpServletRequest가 제공하는 파라미터는 프론트 컨트롤러가 paramMap에 담아서 호출해주면 됨
// => 응답 결과로 뷰 이름과 뷰에 전달할 Model 데이터를 포함하는 ModelView 객체를 반환하면 됨

//프론트 컨트롤러 V3
// => [등록 실행] : http://localhost:8080/front-controller/v3/members/new-form
// => [목록 실행] : http://localhost:8080/front-controller/v3/members
@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //기존 V2
        //MyView view = controller.process(request, response);
        // 해석) new MyView("/WEB-INF/views/save-result.jsp").render(request, response)
        //view.render(request, response);

        //변경된 V3
        //서블릿 종속성 제거
        Map<String, String> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap);

        //뷰 이름 중복 제거
        String viewName = mv.getViewName();   //논리이룸 : new-form
        MyView view = viewResolver(viewName); //물리이름 : /WEB-INF/views/new-form.jsp

        // view.render(mv.getModel(), request, response)
        // => 뷰 객체를 통해서 HTML 화면을 렌더링 한다. 뷰 객체의 render() 는 모델 정보도 함께 받는다.
        // => JSP는 request.getAttribute() 로 데이터를 조회하기 때문에, 모델의 데이터를 꺼내서 request.setAttribute() 로 담아둔다.
        // => JSP로 포워드 해서 JSP를 렌더링 한다.
        // 해석) new MyView("/WEB-INF/views/save-result.jsp").render(moodel, request, response)
        view.render(mv.getModel(), request, response);
    }

    // 뷰 리졸버 메서드
    // => 컨트롤러가 반환한 논리 뷰 이름을 실제 물리 뷰 경로로 변경한다. 그리고 실제 물리 경로가 있는 MyView 객체를 반환한다.
    // => 논리 뷰 이름 : members >> 물리 뷰 경로 : /WEB-INF/views/members.jsp
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    // 서블릿 종속성 제거
    // createParamMap() 메서드
    // => HttpServletRequest에서 파라미터 정보를 꺼내서 Map으로 변환한다.
    // => 그리고 해당 Map( paramMap )을 컨트롤러에 전달하면서 호출한다.
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}