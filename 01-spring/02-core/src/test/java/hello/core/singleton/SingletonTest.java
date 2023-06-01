package hello.core.singleton;
import hello.core.AppConfig;
import hello.core.member.MemberService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;
//웹 애플리케이션과 싱글톤

public class SingletonTest {

    /**
     * 1.
     * 스프링 없는 순수한 DI 컨테이너 테스트
     * => 우리가 만들었던 스프링 없는 순수한 DI 컨테이너인 AppConfig는 요청을 할 때 마다 객체를 새로 생성한다.
     * => 고객 트래픽이 초당 100이 나오면 초당 100개 객체가 생성되고 소멸된다! 메모리 낭비가 심하다.
     * => 해결방안은 해당 객체가 딱 1개만 생성되고, 공유하도록 설계하면 된다. - 싱글톤 패턴
     */
    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너 - 싱글톤 아님")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();
        //1. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();
        //2. 조회: 호출할 때 마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();
        //참조값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1); //memberService1 = hello.core.member.MemberServiceImpl@63f259c3
        System.out.println("memberService2 = " + memberService2); //memberService2 = hello.core.member.MemberServiceImpl@67545b57
        assertThat(memberService1).isNotSameAs(memberService2); //isNotSameAs
    }

    /**
     * 2.
     * 싱글톤 패턴을 사용하는 테스트 코드 - 호출할 때 마다 같은 객체 인스턴스를 반환
     *
     *  싱글톤 패턴의 문제점
     *  => 1. 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
     *  => 2. 의존관계상 클라이언트가 구체 클래스에 의존한다. DIP를 위반한다. // SingletonService.getInstance();
     *  => 3. 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성이 높다. 테스트하기 어렵다.
     *  => 4. 내부 속성을 변경하거나 초기화 하기 어렵다.
     *  => 5. private 생성자로 자식 클래스를 만들기 어렵다.
     *  => 6. 결론적으로 유연성이 떨어진다. 안티패턴으로 불리기도 한다.
     */
    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    public void singletonServiceTest() {

        //private으로 생성자를 막아두었다. 컴파일 오류가 발생한다.
        //new SingletonService();

        //1. 조회: 호출할 때 마다 같은 객체를 반환
        SingletonService singletonService1 = SingletonService.getInstance();
        //2. 조회: 호출할 때 마다 같은 객체를 반환
        SingletonService singletonService2 = SingletonService.getInstance();

        //참조값이 같은 것을 확인
        System.out.println("singletonService1 = " + singletonService1); //singletonService1 = hello.core.singleton.SingletonService@67545b57
        System.out.println("singletonService2 = " + singletonService2); //singletonService2 = hello.core.singleton.SingletonService@67545b57
        assertThat(singletonService1).isSameAs(singletonService2); //isSameAs
        singletonService1.logic();
    }

    /**
     * 3.
     * 싱글톤 컨테이너를 사용하는 테스트 코드
     *
     * 싱글톤 컨테이너
     *  => 스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤(1개만 생성)으로 관리한다.
     *  => 지금까지 우리가 학습한 스프링 빈이 바로 싱글톤으로 관리되는 빈이다.
     *
     *  => 스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리한다. 이전에 설명한 컨테이너 생성 과정을 자세히 보자.
     *  => 스프링 컨테이너는 객체를 하나만 생성해서 관리한다. 스프링 컨테이너는 싱글톤 컨테이너 역할을 한다.
     *  => 이렇게 싱글톤 객체를 생성하고 관리하는 기능을 "싱글톤 레지스트리"라 한다.
     *  => 스프링 컨테이너의 이런 기능 덕분에 싱글턴 .패턴의 모든 단점을 해결하면서 객체를 싱글톤으로 유지할 수 있다.
     *
     *  => 싱글톤 패턴을 위한 지저분한 코드가 들어가지 않아도 된다.
     *  => DIP, OCP, 테스트, private 생성자로 부터 자유롭게 싱글톤을 사용할 수 있다.
     *  => 싱글톤 컨테이너 적용 후 스프링 컨테이너 덕분에 고객의 요청이 올 때 마다 객체를 생성하는 것이 아니라, 이미 만들어진 객체를 공유해서 효율적으로 재사용할 수 있다.
     *  => 참고로 스프링의 기본 빈 등록 방식은 싱글톤이지만, 싱글톤 방식만 지원하는 것은 아니다.
     *  => 요청할 때 마다 새로운 객체를 생성해서 반환하는 기능도 제공한다. 자세한 내용은 뒤에 빈 스코프에서 설명하겠다.
     */
    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        //1. 조회: 호출할 때 마다 같은 객체를 반환
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        //2. 조회: 호출할 때 마다 같은 객체를 반환
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);
        //참조값이 같은 것을 확인
        System.out.println("memberService1 = " + memberService1); //memberService1 = hello.core.member.MemberServiceImpl@6b53bcc2
        System.out.println("memberService2 = " + memberService2); //memberService2 = hello.core.member.MemberServiceImpl@6b53bcc2
        //memberService1 == memberService2
        assertThat(memberService1).isSameAs(memberService2);
    }



}