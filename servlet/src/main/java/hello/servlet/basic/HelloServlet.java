package hello.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//step2.
// 스프링 부트 환경에서 서블릿 등록

//HTTP 요청 메시지 로그로 확인하는 법
// => application.properties 파일 : [logging.level.org.apache.coyote.http11=debug] 내용 추가 >> 서버 재시작 후 요청 >> 서버가 받은 HTTP 요청 메시지도 출력됨
// => 주의) 운영서버에 이렇게 모든 요청 정보를 다 남기면 성능저하 발생 가능, 개발 단계에서만 적용하기
// => 서버가 받은 HTTP 요청 메시지 확인
//Host: localhost:8080
//Connection: keep-alive
//sec-ch-ua: "Google Chrome";v="111", "Not(A:Brand";v="8", "Chromium";v="111"
//sec-ch-ua-mobile: ?0
//sec-ch-ua-platform: "macOS"
//Upgrade-Insecure-Requests: 1
//User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36
//Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
//Sec-Fetch-Site: none
//Sec-Fetch-Mode: navigate
//Sec-Fetch-User: ?1
//Sec-Fetch-Dest: document
//Accept-Encoding: gzip, deflate, br
//Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
//Cookie: Idea-4bbb167b=a36200a3-ec12-4380-838b-e9f6e828ed89; Idea-3a450657=3afb1f52-af87-447c-ad3d-33520aa3acbf; JSESSIONID=2BF9181E9170D78740A4AA9D74BF8C2E
//
//]

// 웹 브라우저 실행
// => 실행 : http://localhost:8080/hello?username=world
// => 결과 : hello world
// => 콘솔실행 결과 :
// HelloServlet.service
// request = org.apache.catalina.connector.RequestFacade@5e4e72
// response = org.apache.catalina.connector.ResponseFacade@37d112b6
// username = world

//서블릿 컨테이너 동작 방식
// => 1. 스프링 부트가 실행되면서 스프링 부트가 내장 톰캣 서버를 생성해줌
// => 2. 서블릿 컨테이너 기능을 가진 톰캣 서버는 helloServlet 서블릿을 생성함
// => 3. 웹브라우저에서 http://localhost:8080/hello?username=world 라고 요청함
// => 4. HTTP 요청 메세지를 기반으로 HttpServletRequest 객체를 생성해 내용 담아 서블릿에게 전달함
// => GET/hello?username=world HTTP/1.1
// => Host:localHost:8080
// => 5. 매개변수로 HttpServletRequest를 받아서 로직을 실행함
// => 6. 실행결과를 HTTP 응답 메세지인 HttpServletResponse 객체에 담아둠
// => 7. 메소드가 종료되면 WAS 서버가 HttpServletResponse 객체 정보로 HTTP 응답을 생성해 웹브라우저에게 응답함
// => HTTP/1.1 200 OK
// => Content-Type : text/plain;charse=utf-8
// => Content-Length: 11 (참고로!!! Content-Length는 웹 애플리케이션 서버가 자동으로 생성해줌)
// => hello world
// => 8. 웹브라우저에는 hello world가 출력됨

//@WebServlet : 서블릿 애노테이션 (name속성: 서블릿 이름 , urlPatterns속성: URL 매핑)
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    //HTTP 요청을 통해 매핑된 URL이 호출되면
    // => 서블릿 컨테이너는 protected void service(HttpServletRequest request, HttpServletResponse response) 메서드 실행함
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("HelloServlet.service");
        System.out.println("request = " + request);
        System.out.println("response = " + response);
        String username = request.getParameter("username");
        System.out.println("username = " + username);

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("hello " + username); //웹브라우저에 출력

    }
}

//주의
//IntelliJ 무료 버전을 사용하는데, 서버가 정상 실행되지 않는다면 프로젝트 생성 대신에 자바 직접 실행에 있는 주의 사항을 읽어보자.