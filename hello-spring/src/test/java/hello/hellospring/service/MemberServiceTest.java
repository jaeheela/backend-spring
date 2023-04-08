package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    //MemberService memberService = new MemberService();
    //MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    //@BeforeEach
    //=> 각 테스트 실행 전에 호출됨
    //=>테스트가 서로 영향이 없도록 항상 새로운 객체를 생성하고, 의존관계도 새로 맺어줌
    @BeforeEach
    public void beforeEach(){
        memberRepository=new MemoryMemberRepository();
        memberService=new MemberService(memberRepository);
    }

    //@AfterEach
    //=> 각 테스트 실행 후에 호출됨
    //=> 메소드가 끝날때마다 레파지토리를 지워줌으로서 테스트메소드의 실행 순서가 상관없어짐
    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //given(테스트 기반이 되는 데이타) - 이러한 상황이 주어졌는데,
        Member member = new Member();
        member.setName("hello");

        //when(검증할 데이타) - 이렇게 실행했을 때
        Long saveId = memberService.join(member);

        //then(결과 데이타) - 이러한 결과가 나와야해
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() throws Exception {

        //Given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //When
        memberService.join(member1);

        //방법1.
        /*
        try {
            memberService.join(member2);
            fail();
        }catch (IllegalStateException e){
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
        */
        //방법2. - IllegalStateException 검증
        //memberService.join(member2) 로직이 실행되면 IllegalStateException 예외가 발생되어야해!
        assertThrows(IllegalStateException.class,() -> memberService.join(member2));

        //방법2. - 메세지 검증
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}