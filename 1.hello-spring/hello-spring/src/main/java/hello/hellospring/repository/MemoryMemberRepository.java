package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.*;
//회원 도메인과 리포지토리 만들기 - 회원 리포지토리 메모리 구현체

//HashMap 객체
// => 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려

//회원 리포지토리 테스트 케이스 작성
// => 개발한 기능을 실행해서 테스트 할 때 자바의 main 메서드를 통해서 실행하거나, 웹 애플리케이션의 컨트롤러를 통해서 해당 기능을 실행한다.
// => 이러한 방법은 준비하고 실행하는데 오래 걸리고, 반복 실행하기 어렵고 여러 테스트를 한번에 실행하기 어렵다는 단점이 있다.
// => 자바는 JUnit이라는 프레임워크로 테스트를 실행해서 이러한 문제를 해결한다.

//자바 코드로 직접 스프링 빈 등록하기 위해 @Repository 제거 - SpringConfig.java
//@Repository
public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store =new HashMap();
    private static long sequence=0L;

    @Override
    public Member save(@NotNull Member member) {
        member.setId(++sequence);//시퀀스 값 1증가 후 저장
        store.put(member.getId(),member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); //[store.get(id)]가 null이어도 감싸서 반환 가능
    }

    @Override
    public Optional<Member> findByName(String name) {
        //자바의 람다표현식 이용
        return store.values().stream()
                .filter(member -> member.getName().equals(name)) //member.getName()이 name과 같은 경우인가요? 루프돌리기
                .findAny(); //같은 값이 있다면 반환, 없다면 Optional에 null을 감싸서 반환
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); //store에 저장된 Member 객체가 List로 반환될 것임
    }


    public void clearStore(){
        store.clear();
    }
}
