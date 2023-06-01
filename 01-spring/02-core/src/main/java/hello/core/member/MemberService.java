package hello.core.member;

//회원 서비스 - 회원 서비스 인테페이스
public interface MemberService {
    //회원가입
    void join(Member member);
    //회원조회
    Member findMember(Long memberId);
}
