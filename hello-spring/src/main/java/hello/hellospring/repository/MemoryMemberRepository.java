package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
