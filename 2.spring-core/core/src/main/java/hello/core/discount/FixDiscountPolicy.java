package hello.core.discount;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//주문과 할인 도메인 개발 - 정액 할인 정책 구현체

// => 각 클래스가 컴포넌트 스캔의 대상이 되도록 FixDiscountPolicy에 @Component 추가
@Component
//@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy {
    private int discountFixAmount = 1000; //1000원 할인

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) { //VIP면 1000원 할인, 아니면 할인 없음
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}