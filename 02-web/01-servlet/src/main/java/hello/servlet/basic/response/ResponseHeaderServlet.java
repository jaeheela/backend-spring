package hello.servlet.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//HttpServletResponse 역할
//1.
//HTTP 응답 메시지 생성
// => HTTP 응답코드 지정(2XX,4XX,5XX)
// => 헤더 생성
// => 바디 생성
//2.
// 편의 기능 제공
// => Content-Type - content(response)
// => 쿠키 - cookie(response)
// => Redirect - (response)

//HTTP 응답 데이터 - 단순 텍스트
//HttpServletResponse 제공하는 기본 기능들을 출력하는 서블릿
//http://localhost:8080/response-header
@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //[status-line]
        response.setStatus(HttpServletResponse.SC_OK);

        //[response-headers]
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("my-header", "hello");

        //[Header 편의 메서드]
        //content(response);
        //cookie(response);
        //redirect(response);

        //[message body]
        PrintWriter writer = response.getWriter();
        writer.println("ok");
    }

    private void content(HttpServletResponse response) {
        //Content-Type: text/plain;charset=utf-8
        //Content-Length: 2
        //방법1.
        //response.setHeader("Content-Type", "text/plain;charset=utf-8");
        //방법2.
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        //response.setContentLength(2); //(생략시 자동 생성)
    }

    private void cookie(HttpServletResponse response) {
        //Set-Cookie: myCookie=good; Max-Age=600;
        //방법1.
        //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");
        //방법2.
        Cookie cookie = new Cookie("myCookie", "good"); //javax.servlet.http.Cookie 이용
        cookie.setMaxAge(600); //600초
        response.addCookie(cookie);
    }

    private void redirect(HttpServletResponse response) throws IOException {
        //Status Code 302
        //Location: /basic/hello-form.html
        //방법1.
        //response.setStatus(HttpServletResponse.SC_FOUND); //302
        //response.setHeader("Location", "/basic/hello-form.html");
        //방법2.
        response.sendRedirect("/basic/hello-form.html");
    }
}
