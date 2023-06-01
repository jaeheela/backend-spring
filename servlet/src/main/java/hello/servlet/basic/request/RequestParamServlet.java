package hello.servlet.basic.request;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
//HTTP 요청 메시지를 통해 클라이언트에서 서버로 데이터를 전달하는 3가지 방법

//1.
// => GET - 쿼리 파라미터
// => url?username=hello&age=20
// => 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
// => 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
//2.
// => POST - HTML Form
// => content-type: application/x-www-form-urlencoded
// => 메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20
// => 예) 회원 가입, 상품 주문, HTML Form 사용
//3.
// => HTTP message body에 데이터를 직접 담아서 요청
// => HTTP API에서 주로 사용, JSON, XML, TEXT
// => 데이터 형식은 주로 JSON 사용 POST, PUT, PATCH


//1. HTTP 요청 데이터 - GET 쿼리 파라미터
// 다음 데이터를 클라이언트에서 서버로 전송해보자. - 전달 데이터 : username=hello , age=20
// => 메시지 바디 없이, URL의 쿼리 파라미터를 사용해서 데이터를 전달하자. 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
// => 쿼리파라미터는 URL에 다음과 같이 ?를 시작으로 보낼 수 있다. 추가파라미터는 &로 구분하면 된다.
// => http://localhost:8080/request-param?username=hello&age=20
// => 서버에서는 HttpServletRequest 가 제공하는 다음 메서드를 통해 쿼리 파라미터를 편리하게 조회할 수 있다.
// => 쿼리 파라미터 조회 메서드
// => String username = request.getParameter("username"); //단일 파라미터 조회
// => Enumeration<String> parameterNames = request.getParameterNames(); //파라미터 이름들 모두 조회
// => Map<String, String[]> parameterMap = request.getParameterMap(); //파라미터를 Map 으로 조회
// => String[] usernames = request.getParameterValues("username"); //복수 파라미터 조회
// => [실행1] - 파라미터 전송 : http://localhost:8080/request-param?username=hello&age=20
// => [실행2] - 동일 파라미터 전송 : http://localhost:8080/request-param?username=hello&username=kim&age=20
// => 복수 파라미터에서 단일 파라미터 조회 시 username=hello&username=kim 과 같이 파라미터 이름은 하나인데, 값이 중복이면 어떻게 될까?
// => request.getParameter() 는 하나의 파라미터 이름에 대해서 단 하나의 값만 있을 때 사용해야 한다. 지금처럼 중복일 때는 request.getParameterValues() 를 사용해야 한다.
// => 이렇게 중복일 때 request.getParameter() 를 사용하면 request.getParameterValues() 의 첫 번째 값만을 반환한다.

//2. HTTP 요청 데이터 - POST HTML Form
// HTML의 Form을 사용해서 클라이언트에서 서버로 데이터를 전송해보자
// => POST 요청은 메시지 바디에 쿼리 파리미터 형식으로 데이터를 전달한다. ex) username=hello&age=30
// => content-type의 기본값은 application/x-www-form-urlencoded 이다.
// => http://localhost:8080/basic/hello-form.html(웹문서)를 요청 후 입력값을 전달하자 - 전달 데이터 : username=hello , age=30
// => 예) 회원 가입, 상품 주문 등에서 사용하는 방식
// => 주의) 웹 브라우저가 결과를 캐시하고 있어서, 과거에 작성했던 html 결과가 보이는 경우도 있다.
// => 이때는 웹 브라우저의 새로 고침을 직접 선택해주면 된다. 물론 서버를 재시작 하지 않아서 그럴 수도 있다.
// => POST의 HTML Form을 전송하면 웹 브라우저는 다음 형식으로 HTTP 메시지를 만든다. - 웹 브라우저 개발자 모드 확인
// => [요청 URL: http://localhost:8080/request-param]
// => [content-type: application/x-www-form-urlencoded]
// => [message body: username=hello&age=20]
// => application/x-www-form-urlencoded 형식은 앞서 GET에서 살펴본 쿼리 파라미터 형식과 같다. 따라서 쿼리 파라미터 조회 메서드를 그대로 사용하면 된다.
// => 클라이언트(웹 브라우저) 입장에서는 두 방식에 차이가 있지만 서버 입장에서는 둘의 형식이 동일하므로, request.getParameter() 로 편리하게 구분없이 조회할 수 있다.
// => 정리하면 request.getParameter() 는 GET URL 쿼리 파라미터 형식도 지원하고, POST HTML Form 형식도 둘 다 지원한다.
// => 참고로 content-type은 HTTP 메시지 바디의 데이터 형식을 지정한다.
// => GET URL 쿼리 파라미터 형식으로 클라이언트에서 서버로 데이터를 전달할 때는 HTTP 메시지 바디를 사용하지 않기 때문에 content-type이 없다.
// => POST HTML Form 형식으로 데이터를 전달하면 HTTP 메시지 바디에 해당 데이터를 포함해서 보내기 때문에 바디에 포함된 데이터가 어떤 형식인지 content-type을 꼭 지정해야 한다.
// => 이렇게 폼으로 데이터를 전송하는 형식을 application/x-www-form-urlencoded 라 한다.
// => Postman으로 테스트 : 이런 간단한 테스트에 HTML form을 만들기는 귀찮다. 이때는 Postman을 사용하면 된다.
// => Postman 테스트 주의사항 : POST 전송시 [Body -> x-www-form-urlencoded] 선택 , Headers에서 [content-type: application/x-www-form-urlencoded] 로 지정된 부분 꼭 확인

/**
 * 1. 파라미터 전송 기능 : http://localhost:8080/request-param?username=hello&age=20
 * 2. 동일한 파라미터 전송 가능 : http://localhost:8080/request-param?username=hello&username=kim&age=20
 * */
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("[전체 파라미터 조회] - start");
        /*
          Enumeration<String> parameterNames = request.getParameterNames();
          while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            System.out.println(paramName + "=" + request.getParameter(paramName));
          }
        */
        request.getParameterNames().asIterator().forEachRemaining(paramName -> System.out.println(paramName + "=" + request.getParameter(paramName)));
        System.out.println("[전체 파라미터 조회] - end");
        //1. GET방식
        //[전체 파라미터 조회] - start
        //username=hello
        //age=20
        //[전체 파라미터 조회] - end
        //2. POST방식
        //[전체 파라미터 조회] - start
        //username=hello
        //age=30
        //[전체 파라미터 조회] - end
        System.out.println();
        System.out.println("[단일 파라미터 조회]");
        String username = request.getParameter("username");
        String age = request.getParameter("age");
        System.out.println("request.getParameter(username) = " + username);
        System.out.println("request.getParameter(age) = " + age);
        System.out.println();
        //1. GET방식
        //[단일 파라미터 조회]
        //request.getParameter(username) = hello
        //request.getParameter(age) = 20
        //2. POST방식
        //[단일 파라미터 조회]
        //request.getParameter(username) = hello
        //request.getParameter(age) = 30
        System.out.println("[이름이 같은 복수 파라미터 조회]");
        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("request.getParameterValues(username) = " + name);
        }
        response.getWriter().write("ok");
        //1. GET방식
        //=> http://localhost:8080/request-param?username=hello&age=20 요청 시
        //[이름이 같은 복수 파라미터 조회]
        //request.getParameterValues(username) = hello
        // => http://localhost:8080/request-param?username=hello&username=kim&age=20 요청 시
        //[이름이 같은 복수 파라미터 조회]
        //request.getParameterValues(username) = hello
        //request.getParameterValues(username) = kim
        //2. POST방식
        //[이름이 같은 복수 파라미터 조회]
        //request.getParameterValues(username) = hello
    }
}
