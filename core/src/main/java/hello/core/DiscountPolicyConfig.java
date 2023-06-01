package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//비즈니스 로직의 수동 빈 등록 경우
// => "비즈니스 로직" 중 다형성을 적극 활용할 때
// => DiscountService 가 의존관계 자동 주입으로 Map<String, DiscountPolicy> 에 주입을 받는 상황을 생각해보자.
// => 여기에 어떤 빈들이 주입될 지, 각 빈들의 이름은 무엇일지 코드만 보고 한번에 쉽게 파악할 수 있을까?
// => 내가 개발했으니 크게 관계가 없지만, 만약 이 코드를 다른 개발자가 개발해서 나에게 준 것이라면 어떨까?
// => 자동 등록을 사용하고 있기 때문에 파악하려면 여러 코드를 찾아봐야 한다.
// => 이런 경우 수동 빈으로 등록하거나 또는 자동으로하면 특정 패키지에 같이 묶어두는게 좋다!
// => 핵심은 딱 보고 이해가 되어야 한다!
// => 이 설정 정보만 봐도 한눈에 빈의 이름은 물론이고, 어떤 빈들이 주입될지 파악할 수 있다.
// => 그래도 빈 자동 등록을 사용하고 싶으면 파악하기 좋게 DiscountPolicy 의 구현 빈들만 따로 모아서 특정 패키지에 모아두자.

// 참고
// => 스프링과 스프링 부트가 자동으로 등록하는 수 많은 빈들은 예외다.
// => 이런 부분들은 스프링 자체를 잘 이해하고 스프링의 의도대로 잘 사용하는게 중요하다.
// => 스프링 부트의 경우 "DataSource" 같은 데이터베이스 연결에 사용하는 기술 지원 로직까지 내부에서 자동으로 등록하는데, 이런 부분은 메뉴얼을 잘 참고해서 스프링 부트가 의도한 대로 편리하게 사용하면 된다.
// => 반면에 스프링 부트가 아니라 내가 직접 기술 지원 객체를 스프링 빈으로 등록한다면 수동으로 등록해서 명확하게 드러내는 것이 좋다.

//정리
// => 편리한 자동 기능을 기본으로 사용하자
// => 직접 등록하는 기술 지원 객체는 수동 등록
// => 다형성을 적극 활용하는 비즈니스 로직은 수동 등록을 고민해보자

//스프링 빈으로 수동 등록
//@Configuration
//public class DiscountPolicyConfig {
//    @Bean
//    public DiscountPolicy rateDiscountPolicy() {
//        return new RateDiscountPolicy();
//    }
//    @Bean
//    public DiscountPolicy fixDiscountPolicy() {
//        return new FixDiscountPolicy();
//    }
//}

