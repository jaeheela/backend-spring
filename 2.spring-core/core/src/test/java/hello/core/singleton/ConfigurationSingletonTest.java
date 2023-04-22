package hello.core.singleton;
import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.assertj.core.api.Assertions.*;

//@Configuration과 싱글톤의 관계를 알기 위한 테스트
// => 확인해보면 memberRepository 인스턴스는 모두 같은 인스턴스가 공유되어 사용된다.
// => AppConfig의 자바 코드를 보면 분명히 각각 2번 new MemoryMemberRepository 호출해서 다른 인스턴스가 생성되어야 하는데?
// => 어떻게 된 일일까? 혹시 두 번 호출이 안되는 것일까? 실험을 통해 알아보자. - AppConfig
public class ConfigurationSingletonTest {
    @Test
    void configurationTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        //모두 같은 인스턴스를 참고하고 있다.
        System.out.println("memberService -> memberRepository = " + memberService.getMemberRepository());
        System.out.println("orderService -> memberRepository  = " + orderService.getMemberRepository());
        System.out.println("memberRepository = " + memberRepository);
        //memberService -> memberRepository = hello.core.member.MemoryMemberRepository@c074c0c
        //orderService -> memberRepository  = hello.core.member.MemoryMemberRepository@c074c0c
        //memberRepository = hello.core.member.MemoryMemberRepository@c074c0c

        //모두 같은 인스턴스를 참고하고 있다.
        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }
    // => //@Configuration 삭제후 인스턴스가 같은지 테스트 결과
    // => memberService -> memberRepository = hello.core.member.MemoryMemberRepository@6239aba6
    // => orderService -> memberRepository  = hello.core.member.MemoryMemberRepository@3e6104fc
    // => memberRepository = hello.core.member.MemoryMemberRepository@12359a82
    // => 당연히 인스턴스가 같은지 테스트 하는 코드도 실패하고, 각각 다 다른 MemoryMemberRepository 인스턴스를 가지고 있다.
    // => 확인이 끝났으면 @Configuration이 동작하도록 다시 돌려놓자.

    @Test
    void configurationDeep() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        //AppConfig도 스프링 빈으로 등록된다.
        AppConfig bean = ac.getBean(AppConfig.class);
        System.out.println("bean = " + bean.getClass());
        // => 사실 AnnotationConfigApplicationContext 에 파라미터로 넘긴 값은 스프링 빈으로 등록된다.
        // => 그래서 AppConfig 도 스프링 빈이 된다.
        // => AppConfig 스프링 빈을 조회해서 클래스 정보를 출력해보자.
        //bean = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$bd479d70
        // => 순수한 클래스라면 다음과 같이 출력되어야 한다.
        // bean = class hello.core.AppConfig
        // => 그런데 예상과는 다르게 클래스 명에 xxxCGLIB가 붙으면서 상당히 복잡해진 것을 볼 수 있다.
        // => 이것은 내가 만든 클래스가 아니라 스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고,
        // => 그 다른 클래스를 스프링 빈으로 등록한 것이다! 그 임의의 다른 클래스가 바로 싱글톤이 보장되도록 해준다.
        // => 아마도 다음과 같이 바이트 코드를 조작해서 작성되어 있을 것이다.(실제로는 CGLIB의 내부 기술을 사용하는데 매우 복잡하다.) - AppConfig

        // => @Configuration 을 적용하지 않고, @Bean 만 적용하면 어떻게 될까?
        // => @Configuration 을 붙이면 바이트코드를 조작하는 CGLIB 기술을 사용해서 싱글톤을 보장하지만, 만약 @Bean만 적용하면 어떻게 될까? - AppConfig
        // => //@Configuration 삭제
        // => public class AppConfig {
        // => }
        // => 이제 똑같이 실행해보자.
        // => bean = class hello.core.AppConfig
        // => 이 출력 결과를 통해서 AppConfig가 CGLIB 기술 없이 순수한 AppConfig로 스프링 빈에 등록된 것을 확인할 수 있다.
    }
}