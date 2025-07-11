package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello";
    }
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
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
