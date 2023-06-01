package hello.core.order;
import hello.core.annotataion.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


//주문 서비스 구현체
@Component
public class OrderServiceImpl implements OrderService {

    /**
     * 조회 빈이 2개 이상일 경우
     * 문제점
     *  => @Autowired는 타입(Type)으로 조회한다. 그래서 마치  ac.getBean(DiscountPolicy.class) 코드와 유사하게 동작한다. (실제로는 더 많은 기능을 제공한다.)
     *  => 스프링 빈 조회에서 학습했듯이 타입으로 조회하면 선택된 빈이 2개 이상일 때 문제가 발생한다.
     *  => DiscountPolicy의 하위 타입인 FixDiscountPolicy , RateDiscountPolicy 둘다 스프링 빈으로 선언하면, NoUniqueBeanDefinitionException 오류가 발생한다.
     *  => : NoUniqueBeanDefinitionException : 하나의 빈을 기대했는데 fixDiscountPolicy , rateDiscountPolicy 2개가 발견되었다고 알려준다.
     *  => 이때 하위 타입으로 지정할 수도 있지만, 하위 타입으로 지정하는 것은 DIP를 위배하고 유연성이 떨어진다. 그리고 이름만 다르고, 완전히 똑같은 타입의 스프링 빈이 2개 있을 때 해결이 안된다.
     *  => 스프링 빈을 수동 등록해서 문제를 해결해도 되지만, 의존 관계 자동 주입에서 해결하는 여러 방법이 있다
     */
    /*
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
    */

    /**
     * 조회 빈이 2개 이상일 경우
     * => 해결법 1.
     * => @Autowired 필드 명 매칭 - 필드명을 빈 이름으로 변경
     * => setter주입이라 비권장
     * => @Autowired 는 타입 매칭을 시도하고, 이때 여러 빈이 있으면 필드 이름, 파라미터 이름으로 빈 이름을 추가 매칭한다.
     * => 필드 명을 빈 이름으로 변경 - 필드 명이 rateDiscountPolicy 이므로 정상 주입된다.
     * => 필드 명 매칭은 먼저 타입 매칭을 시도 하고 그 결과에 여러 빈이 있을 때 추가로 동작하는 기능이다.
     *
     * @Autowired 매칭 정리
     * => 1. 타입 매칭
     * => 2. 타입 매칭의 결과가 2개 이상일 때 - 필드 명, 파라미터 명으로 빈 이름 매칭
     *
     * => 기존코드
     * @Autowired
     * private DiscountPolicy discountPolicy;
     * @Autowired
     * private MemberRepository memberRepository;
     */
    //@Autowired
    //private DiscountPolicy rateDiscountPolicy;
    //@Autowired
    //private MemberRepository memberRepository;

    /**
     * 조회 빈이 2개 이상일 경우
     * => 해결법2.
     * => @Qualifier 사용
     * => @Qualifier는 추가 구분자를 붙여주는 방법이다.
     * => 주입시 추가적인 방법을 제공하는 것이지 빈 이름을 변경하는 것은 아니다. 빈 등록시 @Qualifier를 붙여 준다.
     *
     * @Qualifier 매칭 순서
     * => 1. @Qualifier끼리 매칭
     * => 2. 빈 이름 매칭
     * => 3. NoSuchBeanDefinitionException 예외 발생
     *
     * => @Qualifier 로 주입할 때 @Qualifier("mainDiscountPolicy") 를 못찾으면 어떻게 될까? 그러면 mainDiscountPolicy라는 이름의 스프링 빈을 추가로 찾는다.
     * => 하지만 경험상 @Qualifier는 @Qualifier 를 찾는 용도로만 사용하는게 명확하고 좋다.
     *
     * => 직접 빈 등록시에도 @Qualifier를 동일하게 사용할 수 있음
     *    @Bean
     *    @Qualifier("mainDiscountPolicy")
     *    public DiscountPolicy discountPolicy() { return new ... }
     *
     * => 수정자 자동 주입할 때도 가능
     *     @Autowired
     *     public DiscountPolicy setDiscountPolicy(@Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
     *         this.discountPolicy = discountPolicy;
     *     }
     */
    /*
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired //생성자 자동 주입
    public OrderServiceImpl(MemberRepository memberRepository,@Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
    */


    /**
     * 조회 빈이 2개 이상일 경우
     * 해결법3.
     * => @Primary 사용 (우선순위를 정하는 방법)
     * => @Autowired 시에 여러 빈이 매칭되면 @Primary가 우선권을 가진다.
     * => rateDiscountPolicy가 우선권을 가지도록 하자.
     *
     * => 코드를 실행해보면 문제 없이 @Primary 가 잘 동작하는 것을 확인할 수 있다.
     * => 여기까지 보면 @Primary 와 @Qualifier 중에 어떤 것을 사용하면 좋을지 고민이 될 것이다.
     * => @Qualifier 의 단점은 주입 받을 때 다음과 같이 모든 코드에 @Qualifier 를 붙여주어야 한다는 점이다.
     * => 반면에 @Primary를 사용하면 이렇게 @Qualifier를 붙일 필요가 없다.
     *
     * @Primary, @Qualifier 활용
     *  => 코드에서 자주 사용하는 메인 데이터베이스의 커넥션을 획득하는 스프링 빈이 있고, 코드에서 특별한 기능으로 가끔 사용하는 서브 데이터베이스의 커넥션을 획득하는 스프링 빈이 있다고 생각해보자.
     *  => 메인 데이터베이스의 커넥션을 획득하는 스프링 빈은 @Primary 를 적용해서 조회하는 곳에서 @Qualifier 지정 없이 편리하게 조회하고,
     *  => 서브 데이터베이스 커넥션 빈을 획득할 때는 @Qualifier 를 지정해서 명시적으로 획득 하는 방식으로 사용하면 코드를 깔끔하게 유지할 수 있다.
     *  => 물론 이때 메인 데이터베이스의 스프링 빈을 등록할 때 @Qualifier 를 지정해주는 것은 상관없다.
     *
     * 우선순위
     *  => @Primary 는 기본값 처럼 동작하는 것이고, @Qualifier 는 매우 상세하게 동작한다.
     *  => 이런 경우 어떤 것이 우선권을 가져갈까? 스프링은 자동보다는 수동이, 넒은 범위의 선택권 보다는 좁은 범위의 선택권이 우선 순위가 높다.
     *  => 따라서 여기서도 @Qualifier 가 우선권이 높다.
     */
    /*
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired //생성자 자동 주입
    public OrderServiceImpl(MemberRepository memberRepository,DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

     */


    /**
     * 애노테이션 직접 만들어 사용
     *
     * => 수정자 자동 주입도 가능
     *     @Autowired
     *     public DiscountPolicy setDiscountPolicy(@MainDiscountPolicy DiscountPolicy discountPolicy) {
     *         this.discountPolicy = discountPolicy;
     *     }
     */
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    //생성자 자동 주입
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }


    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    /**
     * 테스트 용도 - MemberRepository를 조회할 수 있는 기능
     */
    public MemberRepository getMemberRepository() { return memberRepository; }
}