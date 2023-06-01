package hello.core.scan;

import hello.core.AutoAppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AutoConfigTest {

    /**
     * 컴포넌트스캔 대상으로 컴포넌트로 등록한 스프링 빈 조회 테스트
     *
     * => AnnotationConfigApplicationContext 를 사용하는 것은 기존과 동일
     * => 설정 정보로 AutoAppConfig 클래스를 넘겨줌
     */
    @Test
    void basicScan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        MemberService memberService = ac.getBean(MemberService.class); //MemberService 스프링 빈 조회
        Assertions.assertThat(memberService).isInstanceOf(MemberService.class); //ok

        OrderServiceImpl bean = ac.getBean(OrderServiceImpl.class);
        MemberRepository memberRepository = bean.getMemberRepository();
        System.out.println("memberRepository = " + memberRepository);
    }
}
