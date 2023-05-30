package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

//요청 매핑
@RestController //반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    // => /hello-basic URL 호출이 오면 이 메서드가 실행되도록 매핑한다. : @RequestMapping("/hello-basic")
    // => 대부분의 속성을 배열[] 로 제공하므로 다중 설정이 가능하다. {"/hello-basic", "/hello-go"} : @RequestMapping({"/hello-basic", "/hello-go"})
    // => Postman으로 테스트 해보자.
    // => 스프링 부트 3.0 이전
    // => 둘다 허용, 다음 두가지 요청은 다른 URL이지만 스프링은 다음 URL 요청들을 같은 요청으로 매핑한다.
    // => 매핑: /hello-basic >> URL 요청: /hello-basic 혹은 /hello-basic/
    // => 스프링 부트 3.0 이후
    // => 스프링 부트 3.0 부터는 /hello-basic , /hello-basic/ 는 서로 다른 URL 요청을 사용해야 한다. 기존에는 마지막에 있는 / (slash)를 제거했지만, 스프링 부트 3.0 부터는 마지막의 / (slash)를 유지한다. 따라서 다음과 같이 다르게 매핑해서 사용해야 한다.
    // => 매핑: /hello-basic >> URL 요청: /hello-basic , 매핑: /hello-basic/ >> URL 요청: /hello-basic/
    /**
     * 기본 요청
     * 둘다 허용 /hello-basic, /hello-basic/
     * HTTP 메서드 모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE
     */
    // => http://localhost:8080/hello-basic
    @RequestMapping(value = "/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }

    //HTTP 메서드
    // => @RequestMapping 에 method 속성으로 HTTP 메서드를 지정하지 않으면 HTTP 메서드와 무관하게 호출된다.
    // => 모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE
    // => 만약 여기에 POST 요청을 하면 스프링 MVC는 HTTP 405 상태코드(Method Not Allowed)를 반환한다.
    /**
     * method 특정 HTTP 메서드 요청만 허용
     * GET, HEAD, POST, PUT, PATCH, DELETE
     */
    // => http://localhost:8080/mapping-get-v1
    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "ok";
    }

    //HTTP 메서드 매핑 축약
    // => HTTP 메서드를 축약한 애노테이션을 사용하는 것이 더 직관적이다.
    // => 코드를 보면 내부에서 @RequestMapping 과 method 를 지정해서 사용하는 것을 확인할 수 있다.
    /**
     * 편리한 축약 애노테이션 (코드보기)
     * @GetMapping
     * @PostMapping
     * @PutMapping
     * @DeleteMapping
     * @PatchMapping
     */
    // => http://localhost:8080/mapping-get-v2
    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }

    //PathVariable(경로 변수) 사용
    // => 최근 HTTP API는 다음과 같이 리소스 경로에 식별자를 넣는 스타일을 선호한다.
    // => /mapping/userA
    // => /users/1
    // => @RequestMapping 은 URL 경로를 템플릿화 할 수 있는데, @PathVariable 을 사용하면 매칭 되는 부분을 편리하게 조회할 수 있다.
    // => @PathVariable 의 이름과 파라미터 이름이 같으면 생략할 수 있다.
    /**
     * PathVariable 사용
     * 변수명이 같으면 생략 가능
     *
     * @PathVariable("userId") String userId -> @PathVariable userId
     * /mapping/userA
     */
    // => http://localhost:8080/mapping/jaehee
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("mappingPath userId={}", data);
        return "ok";
    }

    //PathVariable 사용 - 다중
    /**
     * PathVariable 사용 다중
     */
    // => http://localhost:8080/mapping/users/jaahee/orders/20
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

    //특정 파라미터 조건 매핑
    // => 특정 파라미터가 있거나 없는 조건을 추가할 수 있다. 잘 사용하지는 않는다.
    /**
     * 파라미터로 추가 매핑
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    // => http://localhost:8080/mapping-param?mode=debug
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    //특정 헤더 조건 매핑
    // => 파라미터 매핑과 비슷하지만, HTTP 헤더를 사용한다.
    // => Postman으로 테스트 해야 한다.
    /**
     * 특정 헤더로 추가 매핑
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    // => http://localhost:8080/mapping-header
    // => [Postman] header에 mode:debug 추가
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }

    //미디어 타입 조건 매핑
    // => HTTP 요청 Content-Type, consume
    // => Postman으로 테스트 해야 한다.
    // => HTTP 요청의 Content-Type 헤더를 기반으로 미디어 타입으로 매핑한다.
    // => 만약 맞지 않으면 HTTP 415 상태코드(Unsupported Media Type)을 반환한다.
    // => 예시)
    // => consumes = "text/plain"
    // => consumes = {"text/plain", "application/*"}
    // => consumes = MediaType.TEXT_PLAIN_VALUE
    /**
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    // => http://localhost:8080/mapping-consume
    // => 요청 헤더의 Content-Type은 무조건 APPLICATION_JSON_VALUE이야 ( = 보낼 값)
    // => [Postman] json으로 {"mode" : "debug"} 추가
    @PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    //미디어 타입 조건 매핑
    // => HTTP 요청 Accept, produce
    // => HTTP 요청의 Accept 헤더를 기반으로 미디어 타입으로 매핑한다.
    // => 만약 맞지 않으면 HTTP 406 상태코드(Not Acceptable)을 반환한다.
    // => 예시)
    // => produces = "text/plain"
    // => produces = {"text/plain", "application/*"}
    // => produces = MediaType.TEXT_PLAIN_VALUE
    // => produces = "text/plain;charset=UTF-8"
    /**
     * Accept 헤더 기반 Media Type
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     */
    // => http://localhost:8080/mapping-produce
    // => 요청 헤더의 Accept는 TEXT_HTML_VALUE이야. (= 받을 값)
    // => [Postman] header에 Accept:text/html 추가
    @PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE)
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
}
