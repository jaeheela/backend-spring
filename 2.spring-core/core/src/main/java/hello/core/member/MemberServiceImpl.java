package hello.core.member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//회원 서비스 - 회원 서비스 구현체

// => 각 클래스가 컴포넌트 스캔의 대상이 되도록 MemberServiceImpl에 @Component 추가
// => 이전에 AppConfig에서는 @Bean 으로 직접 설정 정보를 작성했고, 의존관계도 직접 명시했다.
// => 이제는 이런 설정 정보 자체가 없기 때문에, 의존관계 주입도 이 클래스 안에서 해결해야 한다.
// => @Autowired 는 의존관계를 자동으로 주입해준다. 자세한 룰은 조금 뒤에 설명하겠다.
// => @Autowired 를 사용하면 생성자에서 여러 의존관계도 한번에 주입받을 수 있다.
@Component
public class MemberServiceImpl implements MemberService{

    // => 설계 변경으로 MemberServiceImpl 은 MemoryMemberRepository 를 의존하지 않는다! 단지 MemberRepository 인터페이스만 의존한다.
    // => MemberServiceImpl 입장에서 생성자를 통해 어떤 구현 객체가 들어올지(주입될지)는 알 수 없다.
    // => MemberServiceImpl 의 생성자를 통해서 어떤 구현 객체를 주입할지는 오직 외부( AppConfig )에서 결정된다.
    // => MemberServiceImpl 은 이제부터 의존관계에 대한 고민은 외부에 맡기고 실행에만 집중하면 된다.
    //private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) { // 생성자 주입
        this.memberRepository = memberRepository;
    }

    public void join(Member member) {

        memberRepository.save(member);
    }
    public Member findMember(Long memberId) {

        return memberRepository.findById(memberId);
    }

    //@Configuration과 싱글톤의 관계를 알기 위해  MemberServiceImpl에 테스트하기 위한 코드추가
    // => 테스트를 위해 MemberRepository를 조회할 수 있는 기능을 추가한다.
    // => 기능 검증을 위해 잠깐 사용하는 것이니 인터페이스에 조회기능까지 추가하지는 말자.
    // => ConfigurationSingletonTest에 이용됨
    //테스트 용도
    public MemberRepository getMemberRepository() { return memberRepository;}
}
