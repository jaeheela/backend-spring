package hello.core;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration과 싱글톤
// memberService 빈을 만드는 코드 : memberRepository() 호출 >> new MemoryMemberRepository() 호출
// => orderService 빈을 만드는 코드 : memberRepository() 호출 >> new MemoryMemberRepository() 호출
// => 결과적으로 각각 다른 2개의 MemoryMemberRepository 가 생성되면서 싱글톤이 깨지는 것 처럼 보인다.
// => 스프링 컨테이너는 이 문제를 어떻게 해결할까? 직접 테스트 해보자.
// => 테스트 데이터를 추가하고, configurationTest 에서 테스트를 진행하자.
// => 스프링 컨테이너가 각각 @Bean을 호출해서 스프링 빈을 생성한다.
// => 그래서 memberRepository() 는 다음과 같이 총 3번이 호출되어야 하는 것 아닐까? 그런데 출력 결과는 모두 1번만 호출된다.
// @Configuration과 바이트코드 조작의 마법
// => 스프링 컨테이너는 싱글톤 레지스트리다.
// => 따라서 스프링 빈이 싱글톤이 되도록 보장해주어야 한다.
// => 그런데 스프링이 자바 코드까지 어떻게 하기는 어렵다. 저 자바 코드를 보면 분명 3번 호출되어야 하는 것이 맞다.
// => 그래서 스프링은 클래스의 바이트코드를 조작하는 라이브러리를 사용한다.
// => 모든 비밀은 "@Configuration" 을 적용한 AppConfig 에 있다.
// @Bean 만 적용
// => @Configuration 을 적용하지 않고, @Bean 만 적용하면 어떻게 될까?
// => @Configuration 을 붙이면 바이트코드를 조작하는 CGLIB 기술을 사용해서 싱글톤을 보장하지만, 만약 @Bean만 적용하면 어떻게 될까?
// => //@Configuration 삭제 : 출력 결과  MemberRepository가 총 3번 호출된 것을 알 수 있다.
// => 1번은 @Bean에 의해 스프링 컨테이너에 등록하기 위해서이고, 2번은 각각 memberRepository() 를 호출하면서 발생한 코드다.
//정리
// => @Bean만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.
// => memberRepository() 처럼 의존관계 주입이 필요해서 메서드를 직접 호출할 때 싱글톤을 보장하지 않는다.
// => 크게 고민할 것이 없다. 스프링 설정 정보는 항상 @Configuration 을 사용하자.

//스프링 기반으로 변경 - 스프링 컨테이너로 만듦
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        //soutm + tab
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        //return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

    //AppConfig@CGLIB 예상 코드
    // => @Bean이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고,
    // => 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어진다.
    // =>  덕분에 싱글톤이 보장되는 것이다.
    // => 참고로 AppConfig@CGLIB는 AppConfig의 자식 타입이므로, AppConfig 타입으로 조회 할 수 있다.
    /*
    @Bean
    public MemberRepository memberRepository() {
        if (memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면?) {
            return 스프링 컨테이너에서 찾아서 반환;
        } else { //스프링 컨테이너에 없으면
            기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록;
            return 반환;
        }
    }
    */
}
