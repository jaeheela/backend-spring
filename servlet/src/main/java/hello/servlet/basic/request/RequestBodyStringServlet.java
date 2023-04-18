package hello.servlet.basic.request;


import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
//3-1. HTTP 요청 데이터 - API 메시지 바디 - 단순 텍스트
// => HTTP message body에 데이터를 직접 담아서 요청
// => HTTP API에서 주로 사용, JSON, XML, TEXT
// => 데이터 형식은 주로 JSON 사용
// => POST, PUT, PATCH
// => 먼저 가장 단순한 텍스트 메시지를 HTTP 메시지 바디에 담아서 전송하고, 읽어보자. HTTP 메시지 바디의 데이터를 InputStream을 사용해서 직접 읽을 수 있다.
// => 참고) inputStream은 byte 코드를 반환한다. byte 코드를 우리가 읽을 수 있는 문자(String)로 보려면 문자표(Charset)를 지정해주어야 한다. 여기서는 UTF_8 Charset을 지정해주었다.
// => Postman을 사용해 테스트 - 문자 전송 : POST http://localhost:8080/request-body-string
//=> content-type: text/plain
//=> message body: hello
//결과: messageBody = hello


@WebServlet(name = "requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream(); //byte코드로 받음
        //StreamUtils - 스프링이 제공하는 Utility 객체를 이용하면 편리하게 변환 가능
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); //string으로 변환
        System.out.println("messageBody = " + messageBody);
        response.getWriter().write("ok");
    }
}
