package hello.core.discount;
import hello.core.member.Grade;
import hello.core.member.Member;

//주문과 할인 도메인 개발 - 정률 할인 정책 구현체
public class RateDiscountPolicy implements DiscountPolicy {
    private int discountPercent = 10; //10% 할인

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;
        } else {
            return 0;
        }
    }
}