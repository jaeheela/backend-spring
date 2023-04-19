package hello.core.order;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

//주문과 할인 도메인 개발 - 주문 서비스 구현체
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    // => 주문 생성 요청이 오면, 회원 정보를 조회 >> 할인 정책 적용 >> 다음 주문 객체를 생성해서 반환한다.
    // => 메모리 회원 리포지토리와, 고정 금액 할인 정책을 구현체로 생성한다.
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}