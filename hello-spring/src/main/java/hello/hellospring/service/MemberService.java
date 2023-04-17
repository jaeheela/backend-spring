package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
//회원 서비스 개발

// [return값 만드는 단축키] : cmd + option + V
// [리팩토링 단축키] : ctrl + T >> Abstract Method 이용체
// [테스트 케이스 생성 단축키] : cmd + shift + T
// [디렉토리, 패키지, 클래스 생성 목록 보기] : cmd + n
// [현재 포커스] : ctrl + shift + r
// [이전 실행] : ctrl + r
// [한줄 복사] : cmd + d
// [한 줄 삭제] : cmd + delete
// [Undo] : cmd + z
// [Redo] : shift + cmd + z
// [라인 합치기] : ctrl + shift + j
// [라인 옮기기] : shift + alt + up/down (문법 에러 상관 없이 이동) shift + cmd + up /down (문법 에러 없는 한도 내에 이동)
// [Move Element] : shift + alt + cmd + Right / Left
// [인자값 즉시 보기 - 코드에서 생성자의 인자값을 즉시 확인하는 방법] : cmd + p
// [코드 구현부 즉시 보기 - 코드에서 메소드의 구현 부를 즉시 확인하는 방법 - 메소드, 생성자, 클래스 모두 적용 가능] : option + space
// [Doc 즉시 보기] : F1
// [포커스 - 단어별 이동] : option + right
// [포커스 - 단어별 선택] : option + shift + right
// [포커스 - 포커스 범위 한 단계씩 늘리기] : option + up
// [포커스 - 포커스 범위 한 단계식 줄이기] : option + down
// [포커스 - 포커스 뒤로/앞으로 가기] : cmd + [ (cmd + ])
// [포커스 - 멀티 포커스] : option + option + up/down
// [포커스 - 오류 라인으로 자동 포커스] : F2
// [라인 첫/끝 이동] : function + left/right
// [라인 전체 선택] : shift + cmd + left/ right
// [Page Up/Down] : function + up/down
// [현재 파일에서 검색] : cmd + f
// [현재 파일에서 교체] : cmd + r
// [전체에서 검색] : cmd + shift + f
// [전체에서 교체] : cmd + shift + r
// [정규식으로 검색 , 교체] : cmd + r > Regex 체크 or cmd + shift + r > Regex 체크
// [파일 검색] : shift + cmd + o
// [메소드 검색] : option + cmd + o
// [Action 검색] : shift + cmd + a
// [최근 열었던 파일 목록 보기] : cmd + e
// [최근 수정했던 파일 목록 보기] : cmd + shift + e
// [스마트 자동 완성] : Ctrl + shift + space
// [스태틱 메소드 자동 완성] : ctrl + space * 2
// [Getter / Setter / 생성자 자동완성] : cmd + n
// [Override 메소드 자동완성] : ctrl + i
// [Live  Template 목록 보기] : cmd + j
// [변수 추출] :  cmd + option + v
// [파라미터 추출] : cmd + option + p
// [메소드 추출} : cmd  + option + m
// [innter class 추출]: F6
// [이름 일괄 변경] : shift + F6
// [변수 타입 일괄 변경] : shfit + cmd + F6
// [쓰지 않는 import 정리] : ctrl + option + o >> shift + cmd + a >> optimize impoirt on 활성화
// [코드 자동 정렬] : cmd + option + L

//자바 코드로 직접 스프링 빈 등록하기 위해 @Service 제거 - SpringConfig.java
//@Service
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


    /**
     * 회원가입
     */
    public long join(Member member){
        /*
        //방법1.
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
