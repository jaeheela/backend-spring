package hello.core.discount;
import hello.core.annotataion.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//정률 할인 정책 구현체

@Component
//@Qualifier("mainDiscountPolicy") //해결법2.
//@Primary //해결법3. 무조건 너가 우선순위야
@MainDiscountPolicy //직접 만든 어노테이션 사용
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