package jpa.practice.repository;
import jpa.practice.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

//순수 JPA기반 리포지토리 만들기
// =>  우선 순수한 JPA기반 리포지토리를 만들어 볼 것입니다.
// =>  기본 CRUD 기능을 구현할 것입니다.
@Repository
public class MemberJpaRepository implements MemberRepository {
    //JPA는 EntityManager로 동작
    private final EntityManager em;
    public MemberJpaRepository(EntityManager em) {
        this.em = em;
    }
    //Create(저장)
    @Override
    public void save(Member member){em.persist(member);}

    //Read(조회)
    @Override
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    @Override
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    @Override
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
    // Update(변경 → 변경감지 사용)
    // => JPA에서 변경은 따로 메서드를 이용해서 하지 않는다.
    // => 변경감지를 이용하면 트랜잭션내에서 객체으 속성이 변경되면 트랜잭션 종료 전 JPA에서는 변경 감지를 하여 업데이트를 해준다.

    //Delete(삭제)
    @Override
    public void delete(Long id) {

    }
}