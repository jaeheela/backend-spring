package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//컨트롤러(Controller) -> 핸들러(Handler)
// => 이전에는 컨트롤러를 직접 매핑해서 사용했다.
// => 그런데 이제는 어댑터를 사용하기 때문에, 컨트롤러 뿐만 아니라 어댑터가 지원하기만 하면, 어떤 것이라도 URL에 매핑해서 사용할 수 있다.
// => 그래서 이름을 컨트롤러에서 더 넒은 범위의 핸들러로 변경했다.

//V3 실행
// => 등록: http://localhost:8080/front-controller/v5/v3/members/new-form
// => 목록: http://localhost:8080/front-controller/v5/v3/members
//V4 실행
// => 등록: http://localhost:8080/front-controller/v5/v4/members/new-form
// => 목록: http://localhost:8080/front-controller/v5/v4/members

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    //매핑 정보
    // => 매핑 정보의 값이 ControllerV3 , ControllerV4 같은 인터페이스에서 아무 값이나 받을 수 있는 Object 로 변경되었다.
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    //handlerMappingMap : "/front-controller/v5/v3/members/new-form": new MemberFormControllerV3()
    //handlerMappingMap : "/front-controller/v5/v4/members/new-form" : new MemberFormControllerV4()

    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();
    //handlerAdapters : [new ControllerV3HandlerAdapter, ControllerV4HandlerAdapter()]

    //생성자
    // => 핸들러 매핑과 핸들러 어댑터를 초기화(등록)한다.
    public FrontControllerServletV5() {
        initHandlerMappingMap(); //핸들러 매핑 초기화
        initHandlerAdapters(); //어댑터 초기화
    }


    //1.
    //핸들러 매핑 초기화
    private void initHandlerMappingMap() {
        //V3
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());
        //V4
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }


    //2.
    //핸들러 어댑터 초기화 : 해당 컨트롤러를 처리할 수 있는 핸들러 어댑터 생성해 추가
    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter()); //V3
        handlerAdapters.add(new ControllerV4HandlerAdapter()); //V4
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Object handler = getHandler(request); //ex. "/front-controller/v5/v3/members/new-form": new MemberFormControllerV3()
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler); //ex. new ControllerV3HandlerAdapter()

        //어댑터 호출
        // => 어댑터의 handle(request, response, handler) 메서드를 통해 실제 어댑터가 호출된다.
        // => 어댑터는 handler(컨트롤러)를 호출하고 그 결과를 어댑터에 맞추어 반환한다.
        // => ControllerV3HandlerAdapter 의 경우 어댑터의 모양과 컨트롤러의 모양이 유사해서 변환 로직이 단순하다.
        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);

    }


    //1-1.
    //핸들러 매핑
    // => 핸들러 매핑 정보인 handlerMappingMap 에서 URL에 매핑된 핸들러(컨트롤러) 객체를 찾아서 반환한다.
    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }


    //2-2.
    //핸들러를 처리할 수 있는 어댑터 조회
    // => handler 를 처리할 수 있는 어댑터를 adapter.supports(handler) 를 통해서 찾는다.
    // => handler가 ControllerV3 인터페이스를 구현했다면, ControllerV3HandlerAdapter 객체가 반환된다.
    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        //MemberFormControllerV3, MemberFormControllerV4
        for (MyHandlerAdapter adapter : handlerAdapters) { //handlerAdapters : [new ControllerV3HandlerAdapter, ControllerV4HandlerAdapter()]
            if (adapter.supports(handler)) { //true
                return adapter;
            }
        }

        //false
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }


    //뷰 리졸버
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}