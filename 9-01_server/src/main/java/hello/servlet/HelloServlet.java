package hello.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
//서블릿 컨테이너 초기화2
//여기서는 HelloServlet 이라는 서블릿을 서블릿 컨테이너 초기화 시점에 프로그래밍 방식으로 직접 등록해줄 것이다.
//서블릿을 등록하는 2가지 방법 : [@WebServlet 애노테이션] [프로그래밍 방식] 중 [프로그래밍 방식] 이용

//이 서블릿을 등록하고 실행하면 다음과 같은 결과가 나온다.
//다음 내용을 통해서 서블릿을 등록해보자.
//로그: HelloServlet.service
//HTTP 응답: hello servlet!


//애플리케이션 초기화
//서블릿 컨테이너는 조금 더 유연한 초기화 기능을 지원한다. 여기서는 이것을 애플리케이션 초기화라 하겠다.
//이 부분을 이해하려면 실제 동작하는 코드를 봐야 한다.
//애플리케이션 초기화를 진행하려면 먼저 인터페이스를 만들어야 한다. 내용과 형식은 상관없고, 인터페이스는 꼭 필요하다.
//예제 진행을 위해서 여기서는 AppInit 인터페이스를 만들자. - AppInit.java
//앞서 개발한 애플리케이션 초기화( AppInit ) 인터페이스를 구현해서 실제 동작하는 코드를 만들어보자. - AppInitV1Servlet.java

public class HelloServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("HelloServlet.service");
        resp.getWriter().println("hello servlet!");
    }
}
