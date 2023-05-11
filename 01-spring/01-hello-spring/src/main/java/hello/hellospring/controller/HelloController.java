package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//1. thymeleaf 템플릿 엔진 사용 + MVC 패턴
//2. thymeleaf 템플릿 엔진 사용 + MVC 패턴 + 쿼리스트링으로 값 넘기기
//3. API 사용
// => @ResponseBody 사용 원리
// => HTTP의 BODY에 문자 내용을 직접 반환
// => viewResolver 대신에 HttpMessageConverter가 동작 - 스프링 부트에서 다 등록을 해놨음 - byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음
// => [기본 문자처리: StringHttpMessageConverter] , [기본 객체처리: MappingJackson2HttpMessageConverter]
// => 사실 객체를 JSON 말고도 XML로도 받을 수 있지만 실무에서는 거의 JSON으로만 사용함 - 실무에서는 Converter 거의 손대지 않고 있는 그대로 사용
// => 참고로 클라이언트의 HTTP Accept 해더와 서버의 컨트롤러 반환 타입 정보 둘을 조합해서 HttpMessageConverter가 선택되는것임. 더 자세한 내용은 스프링 MVC 강의에서 설명
@Controller
public class HelloController {

    // [실행]: http://localhost:8080/hello
    // => thymeleaf 템플릿 엔진 사용 - 컨트롤러에서 리턴 값으로 문자를 반환하면 뷰 리졸버(viewResolver)가 화면을 찾아서 처리함
    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello";
    }

    // [실행]: http://localhost:8080/hello-mvc?name=spring
    // => MVC와 템플릿 엔진 - [Controller - HelloController의 hello(Model model) 요청처리메소드], [View - hello-template.html] , [Model]
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) { //@RequestParam 어노테이션은 required가 기본적으로 true 이므로, 값을 반드시 넘겨야함
        model.addAttribute("name", name);
        return "hello-template";
    }

    // [실행]: http://localhost:8080/hello-string?name=spring
    // => API - @ResponseBody 문자 반환 - viewResolver 대신에 StringHttpMessageConverter가 동작
    // => @ResponseBody를 사용하면 뷰 리졸버( viewResolver )를 사용하지 않음, 대신에 HTTP의 BODY에 문자 내용을 직접 반환 (HTML BODY TAG를 말하는 것이 아님)
    // => 즉, HTTP 헤더 응답 바디부에 직접 ["hello" + name]을 넣으므로 요청한 클라이언트에게 문자가 그대로 넘어감
    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name){
        return "hello  " + name;
    }

    // [실행]: http://localhost:8080/hello-api?name=spring
    // => API - @ResponseBody 객체 반환 - viewResolver 대신에 MappingJackson2HttpMessageConverter가 동작
    // => @ResponseBody를 사용하고 객체를 반환(Hello객체)하면 객체가 JSON으로 변환됨
    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name")String name){
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }
    public class Hello{
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

}
