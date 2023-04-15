package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    //회원등록
    Member save(Member member);

    //회원조회
    //Optional : Java8에 추가된 클래스 - 조회기능 이용 시 nullPointerException 발생할 수 있으므로
    //Optional로 감싸서 반환받는 것을 권장
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();

}
