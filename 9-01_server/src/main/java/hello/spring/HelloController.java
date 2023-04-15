package hello.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//스프링 컨테이너 등록
//이번에는 WAS와 스프링을 통합해보자.
//앞서 배운 서블릿 컨테이너 초기화와 애플리케이션 초기화를 활용하면 된다.

//다음과 같은 과정이 필요할 것이다.
//1. 스프링 컨테이너 만들기
//2. 스프링MVC 컨트롤러를 스프링 컨테이너에 빈으로 등록하기
//3. 스프링MVC를 사용하는데 필요한 디스패처 서블릿을 서블릿 컨테이너 등록하기

//현재 라이브러리에는 스프링 관련 라이브러리가 전혀 없다.
//스프링 관련 라이브러리를 추가하자. build.gradle - spring-webmvc 추가
//spring-webmvc 라이브러리를 추가하면 스프링 MVC 뿐만 아니라 spring-core 를 포함한 스프링 핵심 라이브러리들도 함께 포함된다.
@RestController
public class HelloController {
    //간단한 스프링 컨트롤러다.
    //실행하면 HTTP 응답으로 hello spring! 이라는 메시지를 반환한다.
    @GetMapping("/hello-spring")
    public String hello() {
        System.out.println("HelloController.hello");
        return "hello spring!";
    }
}

//hello.spring.HelloConfig.java로 가서 컨트롤러를 스프링 빈으로 직접 등록하자
//참고로 여기서는 컴포넌트 스캔을 사용하지 않고 빈을 직접 등록했다.