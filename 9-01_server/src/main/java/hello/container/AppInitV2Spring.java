package hello.container;

import hello.spring.HelloConfig;
import hello.spring.HelloController;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
//애플리케이션 초기화를 사용해 서블릿 컨테이너에 스프링 컨테이너를 생성하고 등록

//AppInitV2Spring 는 AppInit 을 구현했다.
//AppInit 을 구현하면 애플리케이션 초기화 코드가 자동으로 실행된다.
// 앞서 MyContainerInitV2 에 관련 작업을 이미 해두었다.

//스프링 컨테이너 생성
//AnnotationConfigWebApplicationContext 가 바로 스프링 컨테이너이다.
//AnnotationConfigWebApplicationContext 부모를 따라가 보면 ApplicationContext 인터페이스를 확인할 수 있다.
//이 구현체는 이름 그대로 애노테이션 기반 설정과 웹 기능을 지원하는 스프링 컨테이너로 이해하면 된다.
//appContext.register(HelloConfig.class) 컨테이너에 스프링 설정을 추가한다.

//스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
//new DispatcherServlet(appContext) 코드를 보면 스프링 MVC가 제공하는 디스패처 서블릿을 생성하고,
//생성자에 앞서 만든 스프링 컨테이너를 전달하는 것을 확인할 수 있다.
//이렇게 하면 디스패처 서블릿에 스프링 컨테이너가 연결된다.
//이 디스패처 서블릿에 HTTP 요청이 오면 디스패처 서블릿은 해당 스프링 컨테이너에 들어있는 컨트롤러 빈들을 호출한다.

//디스패처 서블릿을 서블릿 컨테이너에 등록
//servletContext.addServlet("dispatcherV2", dispatcher) 디스패처 서블릿을 서블릿 컨테이너에 등록한다.
// [/spring/*] 요청이 디스패처 서블릿을 통하도록 설정
// [/spring/*] 이렇게 경로를 지정하면 [/spring] 과 그 하위 요청은 모두 해당 서블릿을 통하게 된다.
// [/spring/hello-spring] & [/spring/hello/go]

//주의!
//서블릿을 등록할 때 이름은 원하는 이름을 등록하면 되지만 같은 이름으로 중복 등록하면 오류가 발생한다.
//여기서는 dispatcherV2 이름을 사용했는데, 이후에 하나 더 등록할 예정이기 때문에 이름에 유의하자

//실행 : http://localhost:8080/spring/hello-spring
//결과 : hello spring!

//실행 과정 정리
//[/spring/hello-spring] 실행을 [/spring/*] 패턴으로 호출했기 때문에 다음과 같이 동작한다.
//1. dispatcherV2 디스패처 서블릿이 실행된다. ( /spring )
//2. dispatcherV2 디스패처 서블릿은 스프링 컨트롤러를 찾아서 실행한다. ( /hello-spring )
//=> 이때 서블릿을 찾아서 호출하는데 사용된 [/spring] 을 제외한 [/hello-spring] 가 매핑된 컨트롤러 (HelloController )의 메서드를 찾아서 실행한다.
// (쉽게 이야기해서 뒤에 * 부분으로 스프링 컨트롤러를 찾는다.)

/**
 * http://localhost:8080/spring/hello-spring
 */
public class AppInitV2Spring implements AppInit {
    @Override
    public void onStartup(ServletContext servletContext) {
        System.out.println("AppInitV2Spring.onStartup");

        //스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(HelloConfig.class);

        //스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
        DispatcherServlet dispatcher = new DispatcherServlet(appContext);

        //디스패처 서블릿을 서블릿 컨테이너에 등록 (이름 주의! dispatcherV2)
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherV2", dispatcher);

        // /spring/* 요청이 디스패처 서블릿을 통하도록 설정
        servlet.addMapping("/spring/*");
    }
}
