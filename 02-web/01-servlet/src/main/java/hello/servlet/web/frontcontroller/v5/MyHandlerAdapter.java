package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//유연한 컨트롤러1 - v5
// => 만약 어떤 개발자는 ControllerV3 방식으로 개발하고 싶고, 어떤 개발자는 ControllerV4 방식으로 개발하고 싶다면 어떻게 해야할까?
// => V3 : public interface ControllerV3 { ModelView process(Map<String, String> paramMap); }
// => V4 : public interface ControllerV4 { String process(Map<String, String> paramMap, Map<String, Object> model); }

//어댑터 패턴
// => 지금까지 우리가 개발한 프론트 컨트롤러는 한가지 방식의 컨트롤러 인터페이스만 사용할 수 있다.
// => ControllerV3 , ControllerV4 는 완전히 다른 인터페이스이다. 따라서 호환이 불가능하다.
// => 마치 v3는 110v이고, v4는 220v 전기 콘센트 같은 것이다.
// => 이럴 때 사용하는 것이 바로 어댑터이다.
// => 어댑터 패턴을 사용해서 프론트 컨트롤러가 다양한 방식의 컨트롤러를 처리할 수 있도록 변경해보자.

//v5구조
// => 핸들러 어댑터: 중간에 어댑터 역할을 하는 어댑터가 추가되었는데 이름이 핸들러 어댑터이다. 여기서 어댑터 역할을 해주는 덕분에 다양한 종류의 컨트롤러를 호출할 수 있다.
// => 핸들러: 컨트롤러의 이름을 더 넓은 범위인 핸들러로 변경했다. 그 이유는 이제 어댑터가 있기 때문에 꼭 컨트롤러의 개념 뿐만 아니라 어떠한 것이든 해당하는 종류의 어댑터만 있으면 다 처리할 수 있기 때문이다

// => MyHandlerAdapter : 어댑터는 이렇게 구현해야 한다는 어댑터용 인터페이스이다.
// => ControllerV3HandlerAdapter : ControllerV3를 지원하는 실제 어댑터 구현 클래스
// => ControllerV4HandlerAdapter : ControllerV4를 지원하는 실제 어댑터 구현 클래스
public interface MyHandlerAdapter {

    //boolean supports(Object handler)
    // => 어댑터가 해당 컨트롤러를 처리할 수 있는지 판단하는 메서드
    // => Object handler : 컨트롤러에 해당
    boolean supports(Object handler);

    //ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
    // => 어댑터는 실제 컨트롤러를 호출하고, 그 결과로 ModelView를 반환해야 한다.
    // => 실제 컨트롤러가 ModelView를 반환하지 못하면, 어댑터가 ModelView를 직접 생성해서라도 반환해야 한다.
    //이전에는 프론트 컨트롤러가 실제 컨트롤러를 호출했지만 이제는 이 어댑터를 통해서 실제 컨트롤러가 호출된다.
    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;
}
