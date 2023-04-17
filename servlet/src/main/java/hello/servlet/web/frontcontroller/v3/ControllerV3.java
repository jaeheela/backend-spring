package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;

import java.util.Map;
//Model 추가 - v3

//1. 서블릿 종속성 제거
// => 컨트롤러 입장에서 HttpServletRequest, HttpServletResponse이 꼭 필요할까?
// => 요청 파라미터 정보는 자바의 Map으로 대신 넘기도록 하면 지금 구조에서는 컨트롤러가 서블릿 기술을 몰라도 동작할 수 있다.
// => 그리고 request 객체를 Model로 사용하는 대신에 별도의 Model 객체를 만들어서 반환하면 된다.
// => 우리가 구현하는 컨트롤러가 서블릿 기술을 전혀 사용하지 않도록 변경해보자. 이렇게 하면 구현 코드도 매우 단순해지고, 테스트 코드 작성이 쉽다.
//2. 뷰 이름 중복 제거
// => 컨트롤러에서 지정하는 뷰 이름에 중복이 있는 것을 확인할 수 있다.
// => 컨트롤러는 뷰의 논리 이름을 반환하고, 실제 물리 위치의 이름은 프론트 컨트롤러에서 처리하도록 단순화 하자.
//  => 이렇게 해두면 향후 뷰의 폴더 위치가 함께 이동해도 프론트 컨트롤러만 고치면 된다.
/// => WEB-INF/views/new-form.jsp >> new-form
// => /WEB-INF/views/save-result.jsp >> save-result
// => /WEB-INF/views/members.jsp >> members

//V3 구조
// => 서블릿과 비슷한 모양의 컨트롤러 인터페이스 생성 - ControllerV3
// => 각 컨트롤러들은 이 인터페이스를 상속받아 작성 - MemberFormControllerV3, MemberSaveControllerV3 , MemberListControllerV3
// => 프론트 컨트롤러는 인터페이스(ControllerV1)를 호출해서 구현과 관계없이 로직의 일관성 제공 - FrontControllerServletV3
// => 지금까지는 컨트롤러에서 서블릿에 종속적인 HttpServletRequest를 사용했었음 그리고 Model도 request.setAttribute() 를 통해 데이터를 저장하고 뷰에 전달했음
// => 추가) 서블릿의 종속성을 제거하기 위해 Model을 직접 만들고, 추가로 View 이름까지 전달하는 객체를 만들것 - ModelView
// => 추가) 이번 버전에서는 컨트롤러에서 HttpServletRequest를 사용할 수 없음
// => 추가) 따라서 직접 request.setAttribute() 를 호출할 수 도 없음 - 별도의 Model 필요 - ModelView
// => 추가) ModelView : 뷰의 이름과 뷰를 렌더링할 때 필요한 model 객체를 가지고 있음
// => 추가) model은 단순히 map으로 되어 있으므로 컨트롤러에서 뷰에 필요한 데이터를 key, value로 넣어주면 됨

// => ControllerV3 컨트롤러는 서블릿 기술을 전혀 사용하지 않음
// => 따라서 구현이 매우 단순해지고, 테스트 코드 작성시 테스트 하기 쉬움
// => HttpServletRequest가 제공하는 파라미터는 프론트 컨트롤러가 paramMap에 담아서 호출해주면 됨
// => 응답 결과로 뷰 이름과 뷰에 전달할 Model 데이터를 포함하는 ModelView 객체를 반환하면 됨
public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);
}
