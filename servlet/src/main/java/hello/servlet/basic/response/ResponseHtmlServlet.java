package hello.servlet.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


//HTTP 응답 데이터의 종류 3가지
//1. 단순 텍스트 응답 - ResponseHeaderServlet.java
// => response.setStatus(HttpServletResponse.SC_OK);
// => response.setHeader("Content-Type", "text/plain;charset=utf-8");
// => response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
// => response.setHeader("Pragma", "no-cache");
// => response.setHeader("my-header", "hello");
// => PrintWriter writer = response.getWriter();
// => writer.println("ok");

//2. HTML 응답 - ResponseHtmlServlet.java
// => HTTP 응답으로 HTML을 반환할 때는 content-type을 text/html 로 지정해야 한다.
// => response.setContentType("text/html");
// => response.setCharacterEncoding("utf-8");
// => PrintWriter writer = response.getWriter();
// => writer.println("<html>");
// => writer.println("<body>");
// => writer.println("  <div>안녕?</div>");
// => writer.println("</body>");
// => writer.println("</html>");
// => [실행] : http://localhost:8080/response-html - 페이지 소스보기 사용하면 결과 HTML 소스코드 확인 가능

//3. API JSON 응답 - ResponseJsonServlet.java
// => response.setContentType("application/json");
// => response.setCharacterEncoding("utf-8");
// => HelloData helloData = new HelloData();
// => helloData.setUsername("kim");
// => helloData.setAge(20);
// => String result = objectMapper.writeValueAsString(helloData);
// => response.getWriter().write(result);
//=> [실행] : http://localhost:8080/response-json

//HTTP 응답 데이터 - HTML
@WebServlet(name = "responseHtmlServlet", urlPatterns = "/response-html")
public class ResponseHtmlServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Content-Type: text/html;charset=utf-8
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<body>");
        writer.println("  <div>안녕?</div>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
