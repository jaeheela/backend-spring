package hello.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//컨트롤러를 스프링 빈으로 직접 등록한다.
//참고로 여기서는 컴포넌트 스캔을 사용하지 않고 빈을 직접 등록했다.
@Configuration
public class HelloConfig {

    @Bean
    public HelloController helloController() {
        return new HelloController();
    }
}
//이제 애플리케이션 초기화를 사용해서 서블릿 컨테이너에 스프링 컨테이너를 생성하고 등록하자. - hello.container.AppInitV2Spring.java