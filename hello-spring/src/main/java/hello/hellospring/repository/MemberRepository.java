package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//회원 도메인과 리포지토리 만들기 - 회원 리포지토리 인터페이스
// => 단순히 말해 데이타 넣어, 빼, 검색해, 삭제해..!의 로직 구현함

//Optional 객체 -Java8에 추가
// => 조회기능 이용 시 nullPointerException 발생할 수 있으므로 Optional로 감싸서 반환받는 것을 권장
public interface MemberRepository {
    //회원등록
    Member save(Member member);
    //회원조회
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();

}
