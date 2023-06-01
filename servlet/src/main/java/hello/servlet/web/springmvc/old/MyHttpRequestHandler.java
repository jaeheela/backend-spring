package hello.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
// HttpRequestHandler
// => 핸들러 매핑과, 어댑터를 더 잘 이해하기 위해 Controller 인터페이스가 아닌 다른 핸들러를 알아보자.
// => HttpRequestHandler 핸들러(컨트롤러)는 서블릿과 가장 유사한 형태의 핸들러이다.
// => HttpRequestHandler 인테페이스 : public interface HttpRequestHandler { void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException; }
// => 간단하게 구현해보자.

// => 실행 : http://localhost:8080/springmvc/request-handler
// => 웹 브라우저에 빈 화면이 나오고, 콘솔에 MyHttpRequestHandler.handleRequest 가 출력되면 성공이다.

//순서
// => 1. 핸들러 매핑으로 핸들러 조회 : HandlerMapping 을 순서대로 실행해서, 핸들러를 찾는다. 이 경우 빈 이름으로 핸들러를 찾아야 하기 때문에 이름 그대로 빈 이름으로 핸들러를 찾아주는 BeanNameUrlHandlerMapping 가 실행에 성공하고 핸들러인 MyHttpRequestHandler 를 반환한다.
// => 2. 핸들러 어댑터 조회 : HandlerAdapter 의 supports() 를 순서대로 호출한다. HttpRequestHandlerAdapter 가 HttpRequestHandler 인터페이스를 지원하므로 대상이 된다.
// => 3. 핸들러 어댑터 실행 : 디스패처 서블릿이 조회한 HttpRequestHandlerAdapter 를 실행하면서 핸들러 정보도 함께 넘겨준다. HttpRequestHandlerAdapter 는 핸들러인 MyHttpRequestHandler 를 내부에서 실행하고, 그 결과를 반환한다.
//정리
// => MyHttpRequestHandler 핸들러매핑, 어댑터 MyHttpRequestHandler 를 실행하면서 사용된 객체는 다음과 같다.
// => HandlerMapping = BeanNameUrlHandlerMapping
// => HandlerAdapter = HttpRequestHandlerAdapter

//@RequestMapping
// => 가장 우선순위가 높은 핸들러 매핑과 핸들러 어댑터는 RequestMappingHandlerMapping , RequestMappingHandlerAdapter 이다.
// => @RequestMapping 의 앞글자를 따서 만든 이름인데, 이것이 바로 지금 스프링에서 주로 사용하는 애노테이션 기반의 컨트롤러를 지원하는 매핑과 어댑터이다.
// => 실무에서는 99.9% 이 방식의 컨트롤러를 사용한다.

@Component("/springmvc/request-handler")
public class MyHttpRequestHandler implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MyHttpRequestHandler.handleRequest");
    }
}
