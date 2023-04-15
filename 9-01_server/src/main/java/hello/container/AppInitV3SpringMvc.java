package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
//스프링 MVC 서블릿 컨테이너 초기화 지원
//지금까지의 과정을 생각해보면 서블릿 컨테이너를 초기화 하기 위해 다음과 같은 복잡한 과정을 진행했다.
//ServletContainerInitializer 인터페이스를 구현해서 서블릿 컨테이너 초기화 코드를 만들었다.
//여기에 애플리케이션 초기화를 만들기 위해 @HandlesTypes 애노테이션을 적용했다.
//[/META-INF/services/jakarta.servlet.ServletContainerInitializer] 파일에 서블릿 컨테이너 초기화 클래스 경로를 등록했다.
//서블릿 컨테이너 초기화 과정은 상당히 번거롭고 반복되는 작업이다.
//스프링 MVC는 이러한 서블릿 컨테이너 초기화 작업을 이미 만들어두었다.
//덕분에 개발자는 서블릿 컨테이너 초기화 과정은 생략하고, 애플리케이션 초기화 코드만 작성하면 된다.
//스프링이 지원하는 애플리케이션 초기화를 사용하려면 다음 인터페이스를 구현하면 된다.
//WebApplicationInitializer
//  package org.springframework.web;
//  public interface WebApplicationInitializer {
//    void onStartup(ServletContext servletContext) throws ServletException;
//}
//스프링이 지원하는 애플리케이션 초기화 코드를 사용해보자.


//WebApplicationInitializer 인터페이스를 구현한 부분을 제외하고는 이전의 AppInitV2Spring 과 거의 같은 코드이다.
//WebApplicationInitializer 는 스프링이 이미 만들어둔 애플리케이션 초기화 인터페이스이다.
//여기서도 디스패처 서블릿을 새로 만들어서 등록하는데, 이전 코드에서는 dispatcherV2 라고 했고, 여기서는 dispatcherV3 라고 해주었다.
//참고로 이름이 같은 서블릿을 등록하면 오류가 발생한다.
//servlet.addMapping("/") 코드를 통해 모든 요청이 해당 서블릿을 타도록 했다.
//따라서 다음과 같이 요청하면 해당 디스패처 서블릿을 통해 /hello-spring 이 매핑된 컨트롤러 메서드가 호출된다.

//실행 : http://localhost:8080/hello-spring

//현재 등록된 서블릿 다음과 같다.
// =>/ = dispatcherV3
// => /spring/* = dispatcherV2
// => /hello-servlet = helloServlet
// => /test = TestServlet
//이런 경우 우선순위는 더 구체적인 것이 먼저 실행된다.

//참고
//여기서는 이해를 돕기 위해 디스패처 서블릿도 2개 만들고, 스프링 컨테이너도 2개 만들었다.
//일반적으로는 스프링 컨테이너를 하나 만들고, 디스패처 서블릿도 하나만 만든다.
//그리고 디스패처 서블릿의 경로 매핑도 / 로 해서 하나의 디스패처 서블릿을 통해서 모든 것을 처리하도록 한다.

/**
 * http://localhost:8080/hello-spring
 *
 * 스프링 MVC 제공 WebApplicationInitializer 활용
 * spring-web
 * META-INF/services/jakarta.servlet.ServletContainerInitializer
 * org.springframework.web.SpringServletContainerInitializer
 */
public class AppInitV3SpringMvc implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("AppInitV3SpringMvc.onStartup");

        //스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(HelloConfig.class);

        //스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
        DispatcherServlet dispatcher = new DispatcherServlet(appContext);

        //디스패처 서블릿을 서블릿 컨테이너에 등록 (이름 주의! dispatcherV3)
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherV3", dispatcher);

        // 모든  요청이 디스패처 서블릿을 통하도록 설정
        servlet.addMapping("/");
    }
}

//스프링 MVC가 제공하는 서블릿 컨테이너 초기화 분석
//스프링은 어떻게 WebApplicationInitializer 인터페이스 하나로 애플리케이션 초기화가 가능하게 할까?
//스프링도 결국 서블릿 컨테이너에서 요구하는 부분을 모두 구현해야 한다.
//spring-web 라이브러리를 열어보면 서블릿 컨테이너 초기화를 위한 등록 파일을 확인할 수 있다.
//그리고 이곳에 서블릿 컨테이너 초기화 클래스가 등록되어 있다.
// [/META-INF/services/jakarta.servlet.ServletContainerInitializer] : org.springframework.web.SpringServletContainerInitializer

//org.springframework.web.SpringServletContainerInitializer 코드를 확인해보자.
// SpringServletContainerInitializer
//   @HandlesTypes(WebApplicationInitializer.class)
//  public class SpringServletContainerInitializer implements
//  ServletContainerInitializer {}
//코드를 보면 우리가 앞서 만든 서블릿 컨테이너 초기화 코드와 비슷한 것을 확인할 수 있다.
//@HandlesTypes 의 대상이 WebApplicationInitializer 이다.
//그리고 이 인터페이스의 구현체를 생성하고 실행하는 것을 확인할 수 있다.
//우리는 앞서 이 인터페이스를 구현했다.


//정리
//스프링MVC도 우리가 지금까지 한 것 처럼 서블릿 컨테이너 초기화 파일에 초기화 클래스를 등록해두었다.
//그리고 WebApplicationInitializer 인터페이스를 애플리케이션 초기화 인터페이스로 지정해두고, 이것을 생성해서 실행한다.
//따라서 스프링 MVC를 사용한다면 WebApplicationInitializer 인터페이스만 구현하면 AppInitV3SpringMvc 에서 본 것 처럼 편리하게 애플리케이션 초기화를 사용할 수 있다.


//정리
//지금까지 서블릿 컨테이너 초기화를 사용해서 필요한 서블릿도 등록하고, 스프링 컨테이너도 생성해서 등록하고 또 스프링 MVC가 동작하도록 디스패처 서블릿도 중간에 연결해보았다.
//그리고 스프링이 제공하는 좀 더 편리한 초기화 방법도 알아보았다.
//지금까지 알아본 내용은 모두 서블릿 컨테이너 위에서 동작하는 방법이다.
//따라서 항상 톰캣 같은 서블릿 컨테이너에 배포를 해야만 동작하는 방식이다.
//과거에는 서블릿 컨테이너 위에서 모든 것이 동작했지만, 스프링 부트와 내장 톰캣을 사용하면서 이런 부분이 바뀌기 시작했다.