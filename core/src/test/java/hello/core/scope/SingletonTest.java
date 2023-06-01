package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//빈 스코프란?
// => 지금까지 우리는 스프링 빈이 스프링 컨테이너의 시작과 함께 생성되어서 스프링 컨테이너가 종료될 때 까지 유지된다고 학습했다.
// => 이것은 스프링 빈이 기본적으로 싱글톤 스코프로 생성되기 때문이다.
// => 스코프는 번역 그대로 "빈이 존재할 수 있는 범위"를 뜻한다.

//스프링이 지원하는 스코프
// => 싱글톤 스코프 : 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프
// => 프로토타입 스코프 : 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프
//웹 관련 스코프
// => request 스코프 : 웹 요청이 들어오고 나갈때 까지 유지되는 스코프
// => session 스코프 : 웹 세션이 생성되고 종료될 때 까지 유지되는 스코프
// => application 스코프 : 웹의 서블릿 컨텍스트와 같은 범위로 유지되는 스코프

//빈 스코프 지정 방법
// => 1. 컴포넌트 스캔 자동 등록
// => @Scope("prototype")
// => @Component
// => public class HelloBean {}
// => 2. 수동 등록
// => @Scope("prototype")
// => @Bean
// => PrototypeBean HelloBean() {
// =>     return new HelloBean();
// => }

//프로토타입 스코프
// => 싱글톤 스코프의 빈을 조회하면 스프링 컨테이너는 항상 같은 인스턴스의 스프링 빈을 반환한다.
// => 반면에 프로토타입 스코프를 스프링 컨테이너에 조회하면 스프링 컨테이너는 항상 새로운 인스턴스를 생성해서 반환한다.


//싱글톤 빈 요청 VS 프로토타입 빈 요청
//싱글톤 빈 요청
// => 1. 싱글톤 스코프의 빈을 스프링 컨테이너에 요청한다.
// => 2. 스프링 컨테이너는 본인이 관리하는 스프링 빈을 반환한다.
// => 3. 이후에 스프링 컨테이너에 같은 요청이 와도 같은 객체 인스턴스의 스프링 빈을 반환한다.
//프로토타입 빈 요청
// => 1. 프로토타입 스코프의 빈을 스프링 컨테이너에 요청한다.
// => 2. 스프링 컨테이너는 이 시점에 프로토타입 빈을 생성하고, 필요한 의존관계를 주입한다.
// => 3. 스프링 컨테이너는 생성한 프로토타입 빈을 클라이언트에 반환한다.
// => 4. 이후에 스프링 컨테이너에 같은 요청이 오면 항상 새로운 프로토타입 빈을 생성해서 반환한다.
//프로토타입 스코프 정리
// => 여기서 핵심은 스프링 컨테이너는 프로토타입 빈을 생성하고, 의존관계 주입, 초기화까지만 처리한다는 것이다.
// => 클라이언트에 빈을 반환하고, 이후 스프링 컨테이너는 생성된 프로토타입 빈을 관리하지 않는다.
// => 프로토타입 빈을 관리할 책임은 프로토타입 빈을 받은 클라이언트에 있다.
// => 그래서 @PreDestroy 같은 종료 메서드가 호출되지 않는다.

public class SingletonTest {

    @Scope("singleton")
    static class SingletonBean {
        @PostConstruct
        public void init(){
            System.out.println("singletonBean.init");
        }

        @PreDestroy
        public void destroy(){
            System.out.println("singletonBean.destroy");
        }
    }

    /**
     * singletonBean 스코프 Find 테스트
     */
    @Test
    void singletonBeanFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);

        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);
        System.out.println("singletonBean1 = " + singletonBean1);
        System.out.println("singletonBean2 = " + singletonBean2);
        Assertions.assertThat(singletonBean1).isSameAs(singletonBean2);

        ac.close();
    }
    //실행 결과
    // singletonBean.init
    // singletonBean1 = hello.core.scope.SingletonTest$SingletonBean@492691d7
    // singletonBean2 = hello.core.scope.SingletonTest$SingletonBean@492691d7
    // 21:24:43.355 [main] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@4f2b503c, started on Wed May 31 21:24:43 KST 2023
    // singletonBean.destroy

    // => 빈 초기화 메서드를 실행하고, 같은 인스턴스의 빈을 조회하고, 종료 메서드까지 정상 호출 된 것을 확인할 수 있다.


}
