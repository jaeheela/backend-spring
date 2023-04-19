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

//단위테스트 - 스프링 컨테이너 없이 순수한 자바로만 테스트 진행 - 실행속도가 빠름, 작성할 수 있으면 하기
//=> 통합테스트를 자주 하는 것 보다는 단위테스트를 잘 만드는 것이 훨씬 더 좋음
class MemberServiceTest {

    //MemberService memberService = new MemberService();
    //MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    //@BeforeEach 어노테이션
    // => 각 테스트 실행 전에 호출된다. 테스트가 서로 영향이 없도록 항상 새로운 객체를 생성하고, 의존관계도 새로 맺어준다
    @BeforeEach
    public void beforeEach(){
        memberRepository=new MemoryMemberRepository();
        memberService=new MemberService(memberRepository);
    }

    //@AfterEach 어노테이션
    // => 각 테스트가 종료될 때 마다 이 기능을 실행한다. 여기서는 메모리 DB에 저장된 데이터를 삭제한다.
    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //given(테스트 기반이 되는 데이타 - 상황)
        Member member = new Member();
        member.setName("hello");

        //when(검증할 데이타 - 행동)
        Long saveId = memberService.join(member);

        //then(결과 데이타 - 결과)
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

        //방법1. try~catch문으로 검증
        /*
        try {
            memberService.join(member2);
            fail();
        }catch (IllegalStateException e){
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
        */
        //방법2. IllegalStateException로 검증 - memberService.join(member2) 로직이 실행되면 IllegalStateException 예외가 발생해야 한다.
        //assertThrows(IllegalStateException.class,() -> memberService.join(member2));

        //방법3. 메세지로 검증 - memberService.join(member2) 로직이 실행되면 IllegalStateException 예외가 발생해야 한다.
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));;//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다."); //e.getMessage()의 기대값 = 이미 존재하는 회원입니다.
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}