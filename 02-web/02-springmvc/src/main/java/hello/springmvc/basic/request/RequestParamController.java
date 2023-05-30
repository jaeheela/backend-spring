package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
//HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form

// HTTP 요청 데이터 조회 - 개요
// => 서블릿에서 학습했던 HTTP 요청 데이터를 조회 하는 방법을 다시 떠올려보자.
// -> 그리고 서블릿으로 학습했던 내용을 스프링이 얼마나 깔끔하고 효율적으로 바꾸어주는지 알아보자.
// => HTTP 요청 메시지를 통해 클라이언트에서 서버로 데이터를 전달하는 방법을 알아보자.

// 클라이언트에서 서버로 요청 데이터를 전달할 때 사용법 3가지
// 1. GET - 쿼리 파라미터
// => /url?username=hello&age=20
// => 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
// => 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
// 2. POST - HTML Form
// => content-type: application/x-www-form-urlencoded
// => 메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20
// => 예) 회원 가입, 상품 주문, HTML Form 사용
// 3. HTTP message body에 데이터를 직접 담아서 요청
// => HTTP API에서 주로 사용, JSON, XML, TEXT
// => 데이터 형식은 주로 JSON 사용
// => POST, PUT, PATCH

//HTTP요청 파라미터
// 1. 쿼리 파라미터, HTML Form
// => HttpServletRequest 의 request.getParameter() 를 사용하면 다음 두가지 요청 파라미터를 조회할 수 있다.
// => GET, 쿼리 파라미터 전송 예시 : http://localhost:8080/request-param?username=hello&age=20
// => POST, HTML Form 전송 예시 : POST /request-param ... content-type: application/x-www-form-urlencoded username=hello&age=20
// => GET 쿼리 파리미터 전송 방식이든, POST HTML Form 전송 방식이든 둘다 형식이 같으므로 구분없이 조회할 수 있다.
// => 이것을 간단히 요청 파라미터(request parameter) 조회라 한다.
// => 지금부터 스프링으로 요청 파라미터를 조회하는 방법을 단계적으로 알아보자.
// 2. @RequestParam
// => 스프링이 제공하는 @RequestParam 을 사용하면 요청 파라미터를 매우 편리하게 사용할 수 있다.
// 3. @ModelAttribute
@Slf4j
@Controller
public class RequestParamController {

    // Post Form 페이지 생성 : 먼저 테스트용 HTML Form을 만들어야 한다.
    // => 리소스는 /resources/static 아래에 두면 스프링 부트가 자동으로 인식한다.
    // => 참고로 Jar 를 사용하면 webapp 경로를 사용할 수 없다. 이제부터 정적 리소스도 클래스 경로에 함께 포함해야한다.
    // => http://localhost:8080/basic/hello-form.html

    /**
     * 반환 타입이 없으면서 이렇게 응답에 값을 직접 집어넣으면, view 조회X
     */
    // => request.getParameter() : 여기서는 단순히 HttpServletRequest가 제공하는 방식으로 요청 파라미터를 조회했다.
    // http://localhost:8080/request-param-v1?username=hello&age=20
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username = {}, age={}",username,age);
        response.getWriter().write("ok");
    }

    /**
     * @RequestParam 사용
     * - 파라미터 이름으로 바인딩
     * @ResponseBody 추가
     * - View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
     */
    // => @RequestParam : 파라미터 이름으로 바인딩
    // => @ResponseBody : View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
    // => @RequestParam의 name(value) 속성이 파라미터 이름으로 사용
    // => @RequestParam("username") String memberName >> request.getParameter("username")
    // http://localhost:8080/request-param-v2?username=hello&age=20
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {

        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

    /**
     * @RequestParam 사용
     * HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
     */
    // => HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
    // http://localhost:8080/request-param-v3?username=hello&age=20
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * @RequestParam 사용
     * String, int 등의 단순 타입이면 @RequestParam 도 생략 가능
     */
    // => String , int , Integer 등의 단순 타입이면 @RequestParam 도 생략 가능
    // => 주의 @RequestParam 애노테이션을 생략하면 스프링 MVC는 내부에서 required=false 를 적용한다.
    // => required 옵션은 바로 다음에 설명한다.
    // => 참고로 이렇게 애노테이션을 완전히 생략해도 되는데, 너무 없는 것도 약간 과하다는 주관적 생각이 있다.
    // => @RequestParam 이 있으면 명확하게 요청 파리미터에서 데이터를 읽는 다는 것을 알 수 있다.
    // http://localhost:8080/request-param-v4?username=hello&age=20
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 파라미터 필수 여부 - requestParamRequired
     * @RequestParam.required
     * /request-param-required -> username이 없으므로 예외
     *
     * 주의!
     * /request-param-required?username= -> 빈문자로 통과
     * 주의!
     * /request-param-required
     * int age -> null을 int에 입력하는 것은 불가능, 따라서 Integer 변경해야 함(또는 다음에 나오는 defaultValue 사용)
     */
    // => @RequestParam.required : 파라미터 필수 여부, 기본값이 파라미터 필수(true)이다.
    // => /request-param 요청 : username 이 없으므로 400 예외가 발생한다.
    // => 주의!
    // 1. 파라미터 이름만 사용 : /request-param?username=
    // => 파라미터 이름만 있고 값이 없는 경우 빈문자로 통과
    // 2. 기본형(primitive)에 null 입력 : /request-param 요청 @RequestParam(required = false) int age
    // => null 을 int 에 입력하는 것은 불가능(500 예외 발생)
    // => 따라서 null 을 받을 수 있는 Integer 로 변경하거나, 또는 다음에 나오는 defaultValue 사용
    // http://localhost:8080/request-param-required?username=hello&age=20 ok
    // http://localhost:8080/request-param-required?username=hello&age= ok
    // http://localhost:8080/request-param-required?username=hello& ok
    // http://localhost:8080/request-param-required?username= ok
    // http://localhost:8080/request-param-required? error
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 기본 값 적용 - requestParamDefault
     * @RequestParam
     * - defaultValue 사용
     * 참고: defaultValue는 빈 문자의 경우에도 적용
     * /request-param-default?username=
     */
    // => 파라미터에 값이 없는 경우 defaultValue 를 사용하면 기본 값을 적용할 수 있다. 이미 기본 값이 있기 때문에 required 는 의미가 없다.
    // => defaultValue 는 빈 문자의 경우에도 설정한 기본 값이 적용된다.
    // http://localhost:8080/request-param-default?username= : 로그출력 - username=guest, age=-1
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }

    /**
     * 파라미터를 Map으로 조회하기 - requestParamMap
     * @RequestParam Map, MultiValueMap
     * Map(key=value)
     * MultiValueMap(key=[value1, value2, ...]) ex) (key=userIds, value=[id1, id2])
     */
    // => 파라미터를 Map, MultiValueMap으로 조회할 수 있다.
    // => @RequestParam Map , Map(key=value)
    // => @RequestParam MultiValueMap , MultiValueMap(key=[value1, value2, ...] ex) (key=userIds, value=[id1, id2])
    // => 파라미터의 값이 1개가 확실하다면 Map 을 사용해도 되지만, 그렇지 않다면 MultiValueMap 을 사용하자.
    // http://localhost:8080/request-param-map?username=hello&age=20
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    /**
     * @ModelAttribute 사용 - modelAttributeV1
     * 참고: model.addAttribute(helloData) 코드도 함께 자동 적용됨, 뒤에 model을 설명할 때 자세히 설명
     */
    // => 실제 개발을 하면 요청 파라미터를 받아서 필요한 객체를 만들고 그 객체에 값을 넣어주어야 한다.
    // => 스프링은 이 과정을 완전히 자동화해주는 @ModelAttribute 기능을 제공한다. 먼저 요청 파라미터를 바인딩 받을 객체를 만들자. - HelloData.java
    // => 실행해보면 마치 마법처럼 HelloData 객체가 생성되고, 요청 파라미터의 값도 모두 들어가 있다.
    // => 스프링MVC는 @ModelAttribute 가 있으면 다음을 실행한다.
    // => HelloData 객체 생성 >> 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾음 >> 그해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력(바인딩)
    // => 예) 파라미터 이름이 username 이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력한다.
    // => 프로퍼티 : 객체에 getUsername() , setUsername() 메서드가 있으면, 이 객체는 username 이라는 프로퍼티를 가지고 있다.
    // => username 프로퍼티의 값을 변경하면 setUsername() 이 호출되고, 조회하면 getUsername() 이 호출된다.
    // => 바인딩 오류 : age=abc 처럼 숫자가 들어가야 할 곳에 문자를 넣으면 BindException 이 발생한다. 이런 바인딩 오류를 처리하는 방법은 검증 부분에서 다룬다.
    // http://localhost:8080/model-attribute-v1?username=hello&age=20
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

    /**
     * @ModelAttribute 생략 - modelAttributeV2
     * * String, int 같은 단순 타입 = @RequestParam
     * * argument resolver 로 지정해둔 타입 외 = @ModelAttribute
     */
    // => @ModelAttribute 는 생략할 수 있다. 그런데 @RequestParam 도 생략할 수 있으니 혼란이 발생할 수 있다.
    // => 스프링은 해당 생략시 다음과 같은 규칙을 적용한다.
    // => 규칙1. String , int , Integer 같은 단순 타입 = @RequestParam
    // => 규칙2. 나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)
    // => 참고로 argument resolver는 뒤에서 학습한다.
    // http://localhost:8080/model-attribute-v2?username=hello&age=20
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }
}
