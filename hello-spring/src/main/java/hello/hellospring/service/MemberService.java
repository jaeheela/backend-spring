package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {
    //직접 객체를 생성하는 것이 아닌
    //private final MemberRepository memberRepository= new MemoryMemberRepository();


    //외부에서 MemberRepository 객체를 주입해줌 - DI (의존성주입)
    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository){
        this.memberRepository=memberRepository;
    }


    /**
     * 회원가입
     */
    public long join(Member member){

        /*
        //방법1.
        //중복회원검증
        Optional<Member> result = memberRepository.findByName(member.getName());

        //=> 기존처럼 구현 - 만약 member객체가 null이 아니라면? 예외전달
        //=> Optional 클래스로 구현 - 만약 member 객체에 값이 있다면? 예외전달
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
        */

        //방법2.
        validateDuplicateMember(member); //중복회원검증
        memberRepository.save(member);
        return member.getId();

    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
            .ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 회원입니다.");
            });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers(){
            return memberRepository.findAll();
    }

    /**
     * 아이디로 회원 조회
     */
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
