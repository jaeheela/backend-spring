package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
//HTTP 요청 - 기본, 헤더 조회
// => 애노테이션 기반의 스프링 컨트롤러는 다양한 파라미터를 지원한다.
// => 이번 시간에는 HTTP 헤더 정보를 조회하는 방법을 알아보자.

// => HttpServletRequest
// => HttpServletResponse
// => HttpMethod : HTTP 메서드를 조회한다. - org.springframework.http.HttpMethod
// => Locale : Locale 정보를 조회한다. - LocaleResolver로 확장 가능..!
// => @RequestHeader MultiValueMap<String, String> headerMap : 모든 HTTP 헤더를 MultiValueMap 형식으로 조회한다.
// => @RequestHeader("host") String host : 특정 HTTP 헤더를 조회한다. - 속성 (필수 값 여부:required), (기본 값:defaultValue)
// => @CookieValue(value = "myCookie", required = false) String cookie : 특정 쿠키를 조회한다. - 속성 (필수 값 여부:required), (기본 값:defaultValue)
// => MultiValueMap : MAP과 유사한데, 하나의 키에 여러 값을 받을 수 있다. HTTP header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다. - keyA=value1&keyA=value2

//참고
// => @Conroller 의 사용 가능한 파라미터 목록
// => : https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments
// => @Conroller 의 사용 가능한 응답 값 목록
// => : https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types
@Slf4j
@RestController
public class RequestHeaderController {

    // => @Slf4j을 사용하면 다음 코드를 자동으로 생성해서 로그를 선언해준다. 개발자는 편리하게 log 라고 사용하면 된다.
    // private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RequestHeaderController.class);

    // => http://localhost:8080/headers
    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale,
                          @RequestHeader MultiValueMap<String, String> headerMap,
                          @RequestHeader("host") String host,
                          @CookieValue(value = "myCookie", required = false) String cookie
                          ) {

        //MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        //map.add("keyA", "value1");
        //map.add("keyA", "value2");
        //[value1,value2]
        //List<String> values = map.get("keyA");
        //log.info("values={}", values);

        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);
        return "ok";
    }
    //2023-05-23 01:46:54.688  INFO 45301 --- [nio-8080-exec-1] h.s.b.request.RequestHeaderController    : values=[value1, value2]

    //2023-05-23 00:38:34.201  INFO 43268 --- [nio-8080-exec-1] h.s.b.request.RequestHeaderController    : request=org.apache.catalina.connector.RequestFacade@7f5bebbb
    //2023-05-23 00:38:34.202  INFO 43268 --- [nio-8080-exec-1] h.s.b.request.RequestHeaderController    : response=org.apache.catalina.connector.RequestFacade@7f5bebbb
    //2023-05-23 00:38:34.203  INFO 43268 --- [nio-8080-exec-1] h.s.b.request.RequestHeaderController    : httpMethod=GET
    //2023-05-23 00:38:34.203  INFO 43268 --- [nio-8080-exec-1] h.s.b.request.RequestHeaderController    : locale=ko
    //2023-05-23 00:38:34.203  INFO 43268 --- [nio-8080-exec-1] h.s.b.request.RequestHeaderController    : headerMap={host=[localhost:8080], connection=[keep-alive], sec-ch-ua=["Google Chrome";v="113", "Chromium";v="113", "Not-A.Brand";v="24"], sec-ch-ua-mobile=[?0], sec-ch-ua-platform=["macOS"], upgrade-insecure-requests=[1], user-agent=[Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36], accept=[text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7], sec-fetch-site=[none], sec-fetch-mode=[navigate], sec-fetch-user=[?1], sec-fetch-dest=[document], accept-encoding=[gzip, deflate, br], accept-language=[ko,en;q=0.9,ko-KR;q=0.8,en-US;q=0.7], cookie=[Idea-3a450657=3afb1f52-af87-447c-ad3d-33520aa3acbf; JSESSIONID=A524DE8C890847A6C3FF2B055D9F09E1]}
    //2023-05-23 00:38:34.204  INFO 43268 --- [nio-8080-exec-1] h.s.b.request.RequestHeaderController    : header host=localhost:8080
    //2023-05-23 00:38:34.205  INFO 43268 --- [nio-8080-exec-1] h.s.b.request.RequestHeaderController    : myCookie=null
}
