package hello.core.order;
import hello.core.AppConfig;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

//주문과 할인 정책 테스트
class OrderServiceTest {
    //MemberService memberService = new MemberServiceImpl();
    //OrderService orderService = new OrderServiceImpl();

    MemberService memberService;
    OrderService orderService;
    @BeforeEach //테스트 코드에서 @BeforeEach 는 각 테스트를 실행하기 전에 호출된다.
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }
    @Test
    void createOrder() {
        long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);
        Order order = orderService.createOrder(memberId, "itemA", 10000);
        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

    /**
     * 필드 인젝션 주입의 단점을 보기 위한 테스트
     * => 스프링 도움 없이 순수한 자바 테스르로 하면 NullPointerException 발생함
     * => 필드 인젝션을 사용하면 순수 자바로만 테스하려면 setter, getter 필요함
     * => 즉, 순수한 테스트 만들 수 없음...!!!!
     */
//    @Test
//    void fieldInjectTionTest(){
//        OrderServiceImpl orderService = new OrderServiceImpl();
//        //setter,getter 로직 필요
//        orderService.createOrder(1L,"itemA",10000); //NullPointerException
//    }

/*
    @Test
    @DisplayName("순수 자바 코드로만 creadeOrder 서비스 테스트하는 법")
    void createOrder2() {
        MemoryMemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L,"name",Grade.VIP));

        OrderServiceImpl orderService = new OrderServiceImpl(memberRepository, new FixDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA",10000);
        assertThat(order.getDiscountPrice()).isEqualTo(1000); //ok
    }

 */
}