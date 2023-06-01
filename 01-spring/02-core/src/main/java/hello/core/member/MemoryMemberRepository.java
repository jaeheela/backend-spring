package hello.core.member;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//메모리 회원 저장소 구현체
// => 데이터베이스가 아직 확정이 안되었다. 그래도 개발은 진행해야 하니 가장 단순한 메모리 회원 저장소를 구현해서 우선 개발을 진행하자.
// => 참고로 HashMap은 동시성 이슈가 발생할 수 있다. 이런 경우 ConcurrentHashMap 을 사용하자.
// => 예외처리는 따로 하지 않음 - 원래는 해야함

@Component //memoryMemberRepository
public class MemoryMemberRepository implements MemberRepository{

    //cmd + shift + enter = ";"까지 같이 찍어줌
    private static Map<Long,Member> store = new ConcurrentHashMap<>(); //메모리 회원 저장소

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
