package hello.core.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

//회원 도메인 설계의 문제점
// => 이 코드의 설계상 문제점은 무엇일까요?
// => 다른 저장소로 변경할 때 OCP 원칙을 잘 준수할까요?
// => DIP를 잘 지키고 있을까요?
// => 의존관계가 인터페이스 뿐만 아니라 구현까지 모두 의존하는 문제점이 있음 , 주문까지 만들고나서 문제점과 해결 방안을 설명
public class MemberServiceTest {
    MemberService memberService = new MemberServiceImpl();
    @Test
    void join() {
        //given (주어진 상황)
        Member member = new Member(1L, "memberA", Grade.VIP);
        //when (행위)
        memberService.join(member);
        Member findMember = memberService.findMember(1L);
        //then (결과)
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
