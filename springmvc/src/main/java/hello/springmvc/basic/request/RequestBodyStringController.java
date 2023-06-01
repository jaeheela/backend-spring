package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
//HTTP 요청 메시지 - 단순 텍스트
// => 서블릿에서 학습한 내용을 떠올려보자.
// => HTTP message body에 데이터를 직접 담아서 요청
// => HTTP API에서 주로 사용, JSON, XML, TEXT
// => 데이터 형식은 주로 JSON 사용
// => POST, PUT, PATCH
// => 요청 파라미터와 다르게, HTTP 메시지 바디를 통해 데이터가 직접 넘어오는 경우는 @RequestParam , @ModelAttribute 를 사용할 수 없다.
// => (물론 HTML Form 형식으로 전달되는 경우는 요청 파라미터로 인정된다.)
// => 먼저 가장 단순한 텍스트 메시지를 HTTP 메시지 바디에 담아서 전송하고, 읽어보자.
// => HTTP 메시지 바디의 데이터를 InputStream 을 사용해서 직접 읽을 수 있다.
// => Postman을 사용해서 테스트 해보자.

@Slf4j
@Controller
public class RequestBodyStringController {

    //Postman >> http://localhost:8080/request-body-string-v1 >> Body row, Text 선택 >> 안녕하세요 전송
    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody); //messageBody=안녕하세요

        response.getWriter().write("ok");
    }


    /**
     * 스프링 MVC는 다음 파라미터를 지원한다.
     * InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
     * OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
     */
    //Postman >> http://localhost:8080/request-body-string-v2 >> Body row, Text 선택 >> 안녕하세요 전송
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody); //messageBody=안녕하세요
        responseWriter.write("ok");
    }

    /**
     * 스프링 MVC는 다음 파라미터를 지원한다.
     * HttpEntity
     * - HTTP header, body 정보를 편리하게 조회
     * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * 응답에서도 HttpEntity 사용 가능
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    // "HttpEntity"
    // => 1. HTTP header, body 정보를 편리하게 조회
    // => 2. 메시지 바디 정보를 직접 조회
    // => 3. 요청 파라미터를 조회하는 기능과 관계 없음 - @RequestParam X, @ModelAttribute X
    // => 4. HttpEntity는 응답에도 사용 가능 : 메시지 바디 정보 직접 반환, 헤더 정보 포함 가능, view 조회X
    // => 5. HttpEntity 를 상속받은 다음 객체들도 같은 기능을 제공한다
    // => : "RequestEntity" - HttpMethod, url 정보가 추가, 요청에서 사용
    // => : "ResponseEntity" - HTTP 상태 코드 설정 가능, 응답에서 사용 ex. return new ResponseEntity<String>("Hello World", responseHeaders,HttpStatus.CREATED)
    // => 6. 참고로 스프링MVC 내부에서 HTTP 메시지 바디를 읽어서 문자나 객체로 변환해서 전달해주는데, 이때 HTTP 메시지 컨버터( HttpMessageConverter )라는 기능을 사용한다. 이것은 조금 뒤에 HTTP 메시지 컨버터에서 자세히 설명한다.
    //Postman >> http://localhost:8080/request-body-string-v3 >> Body row, Text 선택 >> 안녕하세요 전송
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {

        String messageBody = httpEntity.getBody(); //변환된 바디 주세요
        log.info("messageBody={}", messageBody);//messageBody=안녕하세요

        return new HttpEntity<>("ok");
    }

    /**
     * @RequestBody
     * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * @ResponseBody
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     **/
    //@RequestBody
    // => @RequestBody 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다.
    // => 참고로 "헤더 정보"가 필요하다면 HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.
    // => 이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam , @ModelAttribute 와는 전혀 관계가 없다.

    //요청 파라미터 vs HTTP 메시지 바디
    // => 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
    // => HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody

    //@ResponseBody
    // => @ResponseBody 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다.
    // => 물론 이 경우에도 view를 사용하지 않는다.
    //Postman >> http://localhost:8080/request-body-string-v4 >> Body row, Text 선택 >> 안녕하세요 전송
    @ResponseBody
    @PostMapping("/request-body-string-v4") //HTTP 요청 바디의 값 읽을 수 있음
    public String requestBodyStringV4(@RequestBody String messageBody) {
        log.info("messageBody={}", messageBody);
        return "ok"; //HTTP 응답 바디에 넣어서 반환
    }
}
