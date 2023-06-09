package hello.core.autowired;
import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

//조회한 빈이 모두 필요할 때, List, Map
// => 의도적으로 정말 해당 타입의 스프링 빈이 다 필요한 경우도 있다.
// => 예를 들어서 할인 서비스를 제공하는데, 클라이언트가 할인의 종류(rate, fix)를 선택할 수 있다고 가정해보자.
// => 스프링을 사용하면 소위 말하는 전략 패턴을 매우 간단하게 구현할 수 있다.
public class AllBeanTest {

    /**
     * 스프링 빈 등록해서 사용 테스트
     *
     * 스프링 컨테이너를 생성하면서 스프링 빈 등록하기
     *  => 스프링 컨테이너는 생성자에 클래스 정보를 받는다. 여기에 클래스 정보를 넘기면 해당 클래스가 스프링 빈으로 자동 등록된다.
     *  => new AnnotationConfigApplicationContext(AutoAppConfig.class,DiscountService.class);
     *  => 이 코드는 2가지로 나누어 이해할 수 있다.
     *  => : 1. new AnnotationConfigApplicationContext()를 통해 스프링 컨테이너를 생성한다.
     *  => : 2. AutoAppConfig.class , DiscountService.class 를 파라미터로 넘기면서 해당 클래스를 자동으로 스프링 빈으로 등록한다.
     *  => 정리하면 스프링 컨테이너를 생성하면서, 해당 컨테이너에 동시에 AutoAppConfig , DiscountService 를 스프링 빈으로 자동 등록한다.
     */
    @Test
    void findAllBean() {
        //AutoAppConfig, DiscountService 스프링 빈으로 등록
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);

        //discountService 사용
        DiscountService discountService = ac.getBean(DiscountService.class);

        //Member 객체 생성
        Member member = new Member(1L, "userA", Grade.VIP);
        int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");
        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(discountPrice).isEqualTo(1000);
    }

    /**
     * DiscountService 클래스
     */
    static class DiscountService {
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

        /**
         * 생성자
         * => 1. Map으로 모든 DiscountPolicy 를 주입 받음 (이때  fixDiscountPolicy , rateDiscountPolicy 가 주입됨)
         * => 2. Map<String, DiscountPolicy> : map의 키에 스프링 빈의 이름을 넣어줌, 그 값으로 DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담아줌
         * => 3. List<DiscountPolicy> : DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담아줌, 만약 해당하는 타입의 스프링 빈이 없으면, 빈 컬렉션이나 Map을 주입함
         */
        //@Autowired 생략가능
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap); //policyMap = {fixDiscountPolicy=hello.core.discount.FixDiscountPolicy@c5ee75e, rateDiscountPolicy=hello.core.discount.RateDiscountPolicy@48a12036}
            System.out.println("policies = " + policies);   //policies = [hello.core.discount.FixDiscountPolicy@c5ee75e, hello.core.discount.RateDiscountPolicy@48a12036]
        }

        /**
         * iscountCode로 "fixDiscountPolicy"가 넘어오면 map에서 fixDiscountPolicy 스프링 빈을 찾아서 실행하는 메서드
         * => 물론 “rateDiscountPolicy”가 넘어오면 rateDiscountPolicy 스프링 빈을 찾아서 실행함
         */
        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            System.out.println("discountCode = " + discountCode); //discountCode = fixDiscountPolicy
            System.out.println("discountPolicy = " + discountPolicy); //discountPolicy = hello.core.discount.FixDiscountPolicy@c5ee75e
            return discountPolicy.discount(member, price);
        }
    }
}

//자동, 수동의 올바른 실무 운영 기준
// 1. 편리한 자동 기능을 기본으로 사용하자
// => : 그러면 어떤 경우에 컴포넌트 스캔과 자동 주입을 사용하고, 어떤 경우에 설정 정보를 통해서 수동으로 빈을 등록하고, 의존관계도 수동으로 주입해야 할까?
// => : 결론부터 이야기하면, 스프링이 나오고 시간이 갈 수록 점점 "자동을 선호"하는 추세다.
// => : 스프링은 @Component 뿐만 아니라 @Controller , @Service , @Repository 처럼 계층에 맞추어 일반적인 애플리케이션 로직을 자동으로 스캔할 수 있도록 지원한다.
// => : 거기에 더해서 최근 스프링 부트는 컴포넌트 스캔을 기본으로 사용하고, 스프링 부트의 다양한 스프링 빈들도 조건이 맞으면 자동으로 등록하도록 설계했다.
// => : 설정 정보를 기반으로 애플리케이션을 구성하는 부분과 실제 동작하는 부분을 명확하게 나누는 것이 이상적이지만,
// => : 개발자 입장에서 스프링 빈을 하나 등록할 때 @Component만 넣어주면 끝나는 일을 @Configuration 설정 정보에 가서 @Bean을 적고, 객체를 생성하고, 주입할 대상을 일일이 적어주는 과정은 상당히 번거롭다.
// => : 또 관리할 빈이 많아서 설정 정보가 커지면 설정 정보를 관리하는 것 자체가 부담이 된다.
// => : 그리고 결정적으로 자동 빈 등록을 사용해도 OCP, DIP를 지킬 수 있다.

// 2. 수동 빈 등록은 언제 사용?
// => : 애플리케이션은 크게 "업무 로직"과 "기술 지원 로직"으로 나눌 수 있음
// => "업무 로직 빈"
// => : 웹을 지원하는 컨트롤러, 핵심 비즈니스 로직이 있는 서비스, 데이터 계층의 로직을 처리하는 리포지토리 등
// => : 보통 비즈니스 요구사항을 개발할 때 추가되거나 변경됨
// => : 숫자도 매우 많고, 한번 개발해야 하면 컨트롤러, 서비스, 리포지토리 처럼 어느정도 유사한 패턴이 있다.
// => : 이런 경우 "자동 기능"을 적극 사용하는 것이 좋다.
// => : 보통 문제가 발생해도 어떤 곳에서 문제가 발생했는지 명확하게 파악하기 쉽다.
// => : 문제가 발생했을 때 어디가 문제인지 명확하게 잘 드러남

// => "기술 지원 로직 빈"
// => : 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용
// => : 업무 로직을 지원하기 위한 하부 기술이나 공통 기술들(데이터베이스 연결 , 공통 로그 처리 등)
// => : 업무 로직과 비교해서 그 수가 매우 적고, 보통 애플리케이션 전반에 걸쳐서 광범위하게 영향을 미친다.
// => : 기술 지원 로직은 적용이 잘 되고 있는지 아닌지 조차 파악하기 어려운 경우가 많음
// => : 그래서 이런 기술 지원 로직들은 가급적 수동 빈 등록을 사용해서 명확하게 드러내는 것이 좋음
// => : 애플리케이션에 광범위하게 영향을 미치는 "기술 지원 객체"는 "수동 빈"으로 등록해서 딱! 설정 정보에 바로 나타나게 하는 것이 유지보수 하기 좋음

