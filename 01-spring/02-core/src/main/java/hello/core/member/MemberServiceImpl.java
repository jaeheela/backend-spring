package hello.core.member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//회원 서비스 구현체
@Component
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Autowired //ac.getBean(MemberRepository.class) 처럼 동작함 - @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) { // 생성자 주입
        this.memberRepository = memberRepository;
    }

    public void join(Member member) {
        memberRepository.save(member);
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    /**
     * 테스트 용도 - MemberRepository를 조회할 수 있는 기능
     * => @Configuration과 싱글톤의 관계를 알기 위해 MemberServiceImpl에 테스트하기 위한 코드추가
     * => 기능 검증을 위해 잠깐 사용하는 것이니 인터페이스에 조회기능까지 추가하지는 말자.
     * => ConfigurationSingletonTest에 이용
     */
    public MemberRepository getMemberRepository() { return memberRepository;}
}
