package hello.servlet.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
//3-2. HTTP 요청 데이터 - API 메시지 바디 - JSON
// => 이번에는 HTTP API에서 주로 사용하는 JSON 형식으로 데이터를 전달해보자.
// content-type: application/json
// message body: {"username": "hello", "age": 20}
// => JSON 형식의 파싱을 하기 위한 객체 추가 - hello.servlet.basic.HelloData.java

//Postman으로 테스트 : POST http://localhost:8080/request-body-json - JSON 형식으로 전송
// => content-type: application/json (Body raw, 가장 오른쪽에서 JSON 선택) , message body: {"username": "hello", "age": 20} 작성
//출력결과
// messageBody={"username": "hello", "age": 20}
// data.username=hello
// data.age=20

//참고
// 1. JSON 결과를 파싱해서 사용할 수 있는 자바 객체로 변환하려면 Jackson, Gson 같은 JSON 변환 라이브러리를 추가해서 사용해야 한다.
// => 스프링 부트로 Spring MVC를 선택하면 기본으로 Jackson 라이브러리( ObjectMapper)를 함께 제공한다.
// 2. HTML form 데이터도 메시지 바디를 통해 전송되므로 직접 읽을 수 있다. 하지만 편리한 파리미터 조회 기능( request.getParameter(...) )을 이미 제공하기 때문에 파라미터 조회 기능을 사용하면 된다.

/**
 * http://localhost:8080/request-body-json
 *
 * JSON 형식 전송
 * content-type: application/json
 * message body: {"username": "hello", "age": 20}
 */
@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {

    //Jackson 라이브러리(com.fasterxml.jackson.databind.ObjectMapper)가 제공해주는 ObjectMapper객체 이용
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        System.out.println("messageBody = " + messageBody);

        // ObjectMapper.readValue() : Convert "JSON" to "Java Object"
        // => JSON String to Object , JSON File to Object , JSON URL to Object ..
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        System.out.println("helloData.username = " + helloData.getUsername());
        System.out.println("helloData.age = " + helloData.getAge());

        response.getWriter().write("ok");
    }
}
