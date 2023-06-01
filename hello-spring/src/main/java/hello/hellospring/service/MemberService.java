package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
//회원 서비스 개발
// => test 자동 생성 단축키 : cmd + shift + T

//AOP
//AOP가 필요한 상황 : 공통 관심 사항(cross-cutting concern) vs 핵심 관심 사항(core concern)
// => 모든 메소드의 호출 시간을 측정하고 싶다면? - 회원 가입 시간, 회원 조회 시간을 측정하고 싶다면?
// => memberController + 시간측정 로직 >> memberService + 시간측정 로직 >> memberRepository + 시간측정 로직
// 문제점)
// => 회원가입, 회원 조회에 시간을 측정하는 기능은 핵심 관심 사항이 아니다. 시간을 측정하는 로직은 공통 관심 사항이다.
// => 시간을 측정하는 로직과 핵심 비즈니스의 로직이 섞여서 유지보수가 어렵다. 시간을 측정하는 로직을 별도의 공통 로직으로 만들기 매우 어렵다.
// => 시간을 측정하는 로직을 변경할 때 모든 로직을 찾아가면서 변경해야 한다.
// 해결법)
// => AOP 적용 : Aspect Oriented Programming
// => 공통 관심 사항(cross-cutting concern) vs 핵심 관심 사항(core concern) 분리
// => 시간 측정 AOP 등록 - TimeTraceAop.java
// => 회원가입, 회원 조회등 핵심 관심사항과 시간을 측정하는 공통 관심 사항을 분리한다. 시간을 측정하는 로직을 별도의 공통 로직으로 만들었다.
// => 핵심 관심 사항을 깔끔하게 유지할 수 있다.
// => 변경이 필요하면 이 로직만 변경하면 된다.
// => 원하는 적용 대상을 선택할 수 있다.

// 스프링의 AOP 동작 방식
// => AOP 적용 전 의존관계 VS AOP 적용 후 의존관계
// => AOP 적용 전 전체 그림 VS AOP 적용 후 전체 그림
// => 실제 Proxy가 주입되는지 콘솔에 출력해서 확인하기


//자바 코드로 직접 스프링 빈 등록하기 위해 @Service 제거 - SpringConfig.java
//@Service
@Transactional
public class MemberService {

    // => 기존에는 회원 서비스가 메모리 회원 리포지토리를 직접 생성하게 했다. 하지만, 이번에는 회원 서비스가 메모리 회원 리포지토리를 직접 생성하지 않고,
    //private final MemberRepository memberRepository = new MemoryMemberRepository();

    // => 회원 리포지토리의 코드가 회원 서비스 코드를 DI 가능하게 변경한다. - 외부에서 MemberRepository 객체를 주입해줌 - DI (의존성주입)
    private final MemberRepository memberRepository;

    //자바 코드로 직접 스프링 빈 등록하기 위해 @Autowired 제거 - SpringConfig.java
    //@Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository=memberRepository;
    }

    //시스템 메소드
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 회원가입
     */
    public long join(Member member){

        //방법1.
        /*
        Optional<Member> result = memberRepository.findByName(member.getName()); //중복회원검증
        result.ifPresent(m -> { //만약 member 객체에 값이 있다면 예외전달(Optional로 구현) , 만약 member객체가 null이 아니라면 예외전달(기존객체로 구현)
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
        */

        //방법2.
        validateDuplicateMember(member); //중복회원검증
        memberRepository.save(member);
        return member.getId();
    }
    /**
     * 회원가입 + 시간측정 로직
     */
    /*
    public Long join(Member member) {
        long start = System.currentTimeMillis();
        try {
            validateDuplicateMember(member); //중복 회원 검증
            memberRepository.save(member);
            return member.getId();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("join " + timeMs + "ms");
        }
    }
    */
    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    /**
     *전체 회원 조회 + 시간측정 로직
     */
    /*
    public List<Member> findMembers() {
        long start = System.currentTimeMillis();
        try {
            return memberRepository.findAll();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("findMembers " + timeMs + "ms");
        }
    }
    */

    /**
     * 아이디로 회원 조회
     */
    public Optional<Member> findOne(Long memberId) {

        return memberRepository.findById(memberId);
    }

}
