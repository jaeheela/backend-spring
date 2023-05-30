package hello.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//핸들러 매핑과 핸들러 어댑터
// => 핸들러 매핑과 핸들러 어댑터가 어떤 것들이 어떻게 사용되는지 알아보자. 지금은 전혀 사용하지 않지만, 과거에 주로 사용했던 스프링이 제공하는 간단한 컨트롤러로 핸들러 매핑과 어댑터를 이해해보자.
// => Controller 인터페이스 과거 버전 스프링 컨트롤러 : org.springframework.web.servlet.mvc.Controller public interface Controller { ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception; }
// => 스프링도 처음에는 이런 딱딱한 형식의 컨트롤러를 제공했다.
// => 참고로 Controller 인터페이스는 @Controller 애노테이션과는 전혀 다르다. 간단하게 구현해보자.

//실행
// => http://localhost:8080/springmvc/old-controller
// => 콘솔에 OldController.handleRequest 이 출력되면 성공이다.
// => 이 컨트롤러는 어떻게 호출될 수 있을까?
// => 이 컨트롤러가 호출되려면 다음 2가지가 필요하다.
// => HandlerMapping(핸들러 매핑)
// => : 핸들러 매핑에서 이 컨트롤러를 찾을 수 있어야 한다.
// => : 예) 스프링 빈의 이름으로 핸들러를 찾을 수 있는 핸들러 매핑이 필요하다.
// => HandlerAdapter(핸들러 어댑터)
// => : 핸들러 매핑을 통해서 찾은 핸들러를 실행할 수 있는 핸들러 어댑터가 필요하다.
// => : 예) Controller 인터페이스를 실행할 수 있는 핸들러 어댑터를 찾고 실행해야 한다.


// 스프링 부트가 자동 등록하는 핸들러 매핑과 핸들러 어댑터
// => 스프링은 이미 필요한 핸들러 매핑과 핸들러 어댑터를 대부분 구현해두었다. 개발자가 직접 핸들러 매핑과 핸들러 어댑터를 만드는 일은 거의 없다.
// => (실제로는 더 많지만, 중요한 부분 위주로 설명하기 위해 일부 생략)
// HandlerMapping
// => 0 = RequestMappingHandlerMapping : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
// => 1 = BeanNameUrlHandlerMapping : 스프링 빈의 이름으로 핸들러를 찾는다.
// HandlerAdapter
// => 0 = RequestMappingHandlerAdapter : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
// => 1 = HttpRequestHandlerAdapter : HttpRequestHandler 처리
// => 2 = SimpleControllerHandlerAdapter : Controller 인터페이스(애노테이션X, 과거에 사용) 처리
// => 핸들러 매핑도, 핸들러 어댑터도 모두 순서대로 찾고 만약 없으면 다음 순서로 넘어간다.
// 순서
// => 1. 핸들러 매핑으로 핸들러 조회 : HandlerMapping 을 순서대로 실행해서, 핸들러를 찾는다. 이 경우 빈 이름으로 핸들러를 찾아야 하기 때문에 이름 그대로 빈 이름으로 핸들러를 찾아주는 BeanNameUrlHandlerMapping 가 실행에 성공하고 핸들러인 OldController 를 반환한다.
// => 2. 핸들러 어댑터 조회 : HandlerAdapter 의 supports() 를 순서대로 호출한다. SimpleControllerHandlerAdapter 가 Controller 인터페이스를 지원하므로 대상이 된다.
// => 3. 핸들러 어댑터 실행 : 디스패처 서블릿이 조회한 SimpleControllerHandlerAdapter 를 실행하면서 핸들러 정보도 함께 넘겨준다. SimpleControllerHandlerAdapter 는 핸들러인 OldController 를 내부에서 실행하고, 그 결과를 반환한다.
// 정리
// => OldController 핸들러매핑, 어댑터
// => OldController를 실행하면서 사용된 객체는 다음과 같다.
// => HandlerMapping = BeanNameUrlHandlerMapping
// => HandlerAdapter = SimpleControllerHandlerAdapter

//뷰 리졸버
// => 이번에는 뷰 리졸버에 대해서 자세히 알아보자. OldController - View 조회할 수 있도록 변경
// => View를 사용할 수 있도록 다음 코드를 추가했다.
// => 실행 : http://localhost:8080/springmvc/old-controller
// => 웹 브라우저에 Whitelabel Error Page 가 나오고, 콘솔에 OldController.handleRequest 이 출력될 것이다.
// => 실행해보면 컨트롤러를 정상 호출되지만, Whitelabel Error Page 오류가 발생한다. application.properties 에 다음 코드를 추가하자
// => spring.mvc.view.prefix=/WEB-INF/views/
// => spring.mvc.view.suffix=.jsp
// InternalResourceViewResolver(뷰 리졸버)
// => 스프링 부트는 InternalResourceViewResolver 라는 뷰 리졸버를 자동으로 등록하는데,
// => 이때 application.properties 에 등록한 spring.mvc.view.prefix , spring.mvc.view.suffix 설정 정보를 사용해서 등록한다.
// => 참고로 권장하지는 않지만 설정 없이 다음과 같이 전체 경로를 주어도 동작하기는 한다. return new ModelAndView("/WEB-INF/views/new-form.jsp");
// => 실행 : http://localhost:8080/springmvc/old-controller
// => 등록 폼이 정상 출력되는 것을 확인할 수 있다. 물론 저장 기능을 개발하지 않았으므로 폼만 출력되고, 더 진행하면 오류가 발생한다.

//스프링 부트가 자동 등록하는 뷰 리졸버
// => (실제로는 더 많지만, 중요한 부분 위주로 설명하기 위해 일부 생략)
// => 1 = BeanNameViewResolver : 빈 이름으로 뷰를 찾아서 반환한다. (예: 엑셀 파일 생성 기능에 사용)
// => 2 = InternalResourceViewResolver : JSP를 처리할 수 있는 뷰를 반환한다.
// 순서
// => 1. 핸들러 어댑터 호출 : 핸들러 어댑터를 통해 new-form 이라는 논리 뷰 이름을 획득한다.
// => 2. ViewResolver 호출 : new-form 이라는 뷰 이름으로 viewResolver를 순서대로 호출한다. BeanNameViewResolver 는 new-form 이라는 이름의 스프링 빈으로 등록된 뷰를 찾아야 하는데 없다. InternalResourceViewResolver 가 호출된다.
// => 3. InternalResourceViewResolver : 이 뷰 리졸버는 InternalResourceView 를 반환한다.
// => 4. 뷰 - InternalResourceView : InternalResourceView 는 JSP처럼 포워드 forward() 를 호출해서 처리할 수 있는 경우에 사용한다.
// => 5. view.render() : view.render() 가 호출되고 InternalResourceView 는 forward() 를 사용해서 JSP를 실행한다.
//참고
// => InternalResourceViewResolver 는 만약 JSTL 라이브러리가 있으면 InternalResourceView를 상속받은 JstlView 를 반환한다.
// => JstlView는 JSTL 태그 사용시 약간의 부가 기능이 추가된다.
// => 다른 뷰는 실제 뷰를 렌더링하지만, JSP의 경우 forward() 통해서 해당 JSP로 이동(실행)해야 렌더링이 된다.
// => JSP를 제외한 나머지 뷰 템플릿들은 forward() 과정 없이 바로 렌더링 된다.
// => Thymeleaf 뷰 템플릿을 사용하면 ThymeleafViewResolver 를 등록해야 한다.
// => 최근에는 라이브러리만 추가하면 스프링 부트가 이런 작업도 모두 자동화해준다.


//@Component : 이 컨트롤러는 /springmvc/old-controller 라는 이름의 스프링 빈으로 등록되었다. 빈의 이름으로 URL을 매핑할 것이다.
@Component("/springmvc/old-controller")
public class OldController implements Controller {

    // http://localhost:8080/springmvc/old-controller
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return new ModelAndView("new-form");
    }
}
