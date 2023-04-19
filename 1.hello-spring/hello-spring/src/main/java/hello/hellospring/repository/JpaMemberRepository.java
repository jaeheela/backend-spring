package hello.hellospring.repository;

// JPA 이용 - 회원 리포지토리
// => JPA는 기존의 반복 코드는 물론이고, 기본적인 SQL도 JPA가 직접 만들어서 실행해준다.
// => JPA를 사용하면, SQL과 데이터 중심의 설계에서 객체 중심의 설계로 패러다임을 전환을 할 수 있다.
// => JPA를 사용하면 개발 생산성을 크게 높일 수 있다.
// JPA를 사용하기 위한 환경 설정
// 1. build.gradle 파일에 JPA, h2 데이터베이스 관련 라이브러리 추가
// => //implementation 'org.springframework.boot:spring-boot-starter-jdbc'
// => implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
// => runtimeOnly 'com.h2database:h2'
// => spring-boot-starter-data-jpa 는 내부에 jdbc 관련 라이브러리를 포함한다. 따라서 jdbc는 제거해도 된다.
// 2. 스프링 부트에 JPA 설정 추가
// => resources/application.properties에서..
// => spring.datasource.url=jdbc:h2:tcp://localhost/~/test
// => spring.datasource.driver-class-name=org.h2.Driver
// => spring.datasource.username=sa
// => spring.jpa.show-sql=true --[show-sql] : JPA가 생성하는 SQL을 출력
// => spring.jpa.hibernate.ddl-auto=none --[ddl-auto] : JPA는 테이블을 자동으로 생성하는 기능을 제공하는데 none를 사용하면 해당 기능을 끔, create를 사용하면 엔티티 정보를 바탕으로 테이블도 직접 생성해줌. 해보자
// => 주의!: 스프링부트 2.4부터는 spring.datasource.username=sa 를 꼭 추가해주어야 한다. 그렇지 않으면 오류가 발생한다.
// 3. JPA 엔티티이용해 Member객체 매핑 - Member
// 4. 서비스 계층에 트랜잭션 추가 (JPA 사용하기 위해서는 트랜잭션 항상 필요) - MemberService
// => import org.springframework.transaction.annotation.Transactional
// => @Transactional
// => public class MemberService {}
// => org.springframework.transaction.annotation.Transactional 를 사용하자. 스프링은 해당 클래스의 메서드를 실행할 때 트랜잭션을 시작하고, 메서드가 정상 종료되면 트랜잭션을 커밋한다.
// => 만약 런타임 예외가 발생하면 롤백한다. JPA를 통한 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
// 5. JPA를 사용하도록 스프링 설정 변경 - SpringConfig


// => PK 기반의 insert, update, delete 는 JPA가 자동으로 매핑해서 업데이트해줌
// => PK 기반이 아닌 나머지는 JPQL 문으로 작성 필요 - select문, join하는 select문 ..
// => [select m from Member m where m.name = :name] : JPQL이라는 쿼리 언어
// => 테이블 대상이 아닌 객체(엔티티)를 대상으로 쿼리문을 작성하면 SQL로 번역이 됨
import hello.hellospring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {
    //JPA는 EntityManager로 동작
    private final EntityManager em;
    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public Member save(Member member) {
        em.persist(member); //JPA가 insert 쿼리문 만들어서 DB에 넣어줌(영구저장)
        return member;
    }
    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id); //JPA가 select 쿼리문 만들어서 조회해줌
        return Optional.ofNullable(member);
    }
    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class).
                setParameter("name", name).getResultList();
        return result.stream().findAny();
    }
}
