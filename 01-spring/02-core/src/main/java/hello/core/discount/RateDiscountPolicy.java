package hello.core.discount;
import hello.core.annotataion.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//주문과 할인 도메인 개발 - 정률 할인 정책 구현체

// => 각 클래스가 컴포넌트 스캔의 대상이 되도록 RateDiscountPolicy에 @Component 추가
//@Component
@MainDiscountPolicy //직접 만든 어노테이션 사용
//@Qualifier("mainDiscountPolicy")
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