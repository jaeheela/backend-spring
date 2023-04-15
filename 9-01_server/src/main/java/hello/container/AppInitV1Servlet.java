package hello.container;

import hello.servlet.HelloServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
//서블릿을 등록하는 2가지 방법 : [@WebServlet 애노테이션] [프로그래밍 방식] 중 [프로그래밍 방식] 이용

//여기서는 프로그래밍 방식으로 HelloServlet 서블릿을 서블릿 컨테이너에 직접 등록한다.
// HTTP로 [/hello-servlet] 를 호출하면 HelloServlet 서블릿이 실행된다.

//참고 - [프로그래밍 방식]을 굳이 사용하는 이유
//@WebServlet 을 사용하면 애노테이션 하나로 서블릿을 편리하게 등록할 수 있다.
// 하지만 애노테이션 방식을 사용하면 유연하게 변경하는 것이 어렵다. 마치 하드코딩 된 것 처럼 동작한다.
// 아래 참고 예시를 보면 [/test] 경로를 변경하고 싶으면 코드를 직접 변경해야 바꿀 수 있다.
//@WebServlet(urlPatterns = "/test")
//public class TestServlet extends HttpServlet { }
//반면에 프로그래밍 방식은 코딩을 더 많이 해야하고 불편하지만 무한한 유연성을 제공한다.
//예를 들어서 hello-servlet 경로를 상황에 따라서 바꾸어 외부 설정을 읽어서 등록할 수 있다.
//서블릿 자체도 특정 조건에 따라서 if 문으로 분기해서 등록하거나 뺄 수 있다.
//서블릿을 내가 직접 생성하기 때문에 생성자에 필요한 정보를 넘길 수 있다.
//예제에서는 단순화를 위해 이런 부분들을 사용하지는 않았지만 프로그래밍 방식이 왜 필요하지? 라고 궁금하신 분들을 위해서 적어보았다.

/**
 * http://localhost:8080/hello-servlet
 */
public class AppInitV1Servlet implements AppInit {
    @Override
    public void onStartup(ServletContext servletContext) {
        System.out.println("AppInitV1Servlet.onStartup");
        //순수 서블릿 코드 등록
        ServletRegistration.Dynamic helloServlet = servletContext.addServlet("helloServlet", new HelloServlet());
        helloServlet.addMapping("/hello-servlet");
    }
}

//서블릿 컨테이너 초기화( ServletContainerInitializer )는 앞서 알아보았다.
// 그런데 애플리케이션 초기화( AppInit )는 어떻게 실행되는 것일까? 다음 코드를 만들어 보자. - MyContainerInitV2.java