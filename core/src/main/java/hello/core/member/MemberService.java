package hello.core.member;

//회원 서비스 - 회원 서비스 인테페이스
public interface MemberService {
    void join(Member member);
    Member findMember(Long memberId);
}
