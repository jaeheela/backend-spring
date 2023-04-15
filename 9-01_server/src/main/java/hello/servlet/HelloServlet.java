package hello.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
//서블릿 컨테이너 초기화2
//서블릿 컨테이너 초기화를 조금 더 자세히 알아보자.
//여기서는 HelloServlet 이라는 서블릿을 서블릿 컨테이너 초기화 시점에 프로그래밍 방식으로 직접 등록해줄 것이다.
//서블릿을 등록하는 2가지 방법 : [@WebServlet 애노테이션] [프로그래밍 방식]

//이 서블릿을 등록하고 실행하면 다음과 같은 결과가 나온다.다음 내용을 통해서 서블릿을 등록해보자.
//로그: HelloServlet.service
//HTTP 응답: hello servlet!
public class HelloServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("HelloServlet.service");
        resp.getWriter().println("hello servlet!");
    }
}
