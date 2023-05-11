package hello.hellospring.repository;
import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 스프링 데이터 JPA 이용 - 회원 리포지토리
// => 스프링 부트와 JPA만 사용해도 개발 생산성이 정말 많이 증가하고, 개발해야할 코드도 확연히 줄어듭니다.
// => 여기에 스프링 데이터 JPA를 사용하면, 기존의 한계를 넘어 마치 마법처럼 리포지토리에 구현 클래스 없이 인터페이스 만으로 개발을 완료할 수 있습니다.
// => 그리고 반복 개발해온 기본 CRUD 기능도 스프링 데이터 JPA가 모두 제공합니다.
// => 스프링 부트와 JPA라는 기반 위에, 스프링 데이터 JPA라는 환상적인 프레임워크를 더하면 개발이 정말 즐거워집니다.
// => 지금까지 조금이라도 단순하고 반복이라 생각했던 개발 코드들이 확연하게 줄어듭니다.
// => 따라서 개발자는 핵심 비즈니스 로직을 개발하는데, 집중할 수 있습니다.
// => 실무에서 관계형 데이터베이스를 사용한다면 스프링 데이터 JPA는 이제 선택이 아니라 필수 입니다.
// => 주의) 스프링 데이터 JPA는 JPA를 편리하게 사용하도록 도와주는 기술입니다.
// => 따라서 JPA를 먼저 학습한 후에 스프링 데이터 JPA를 학습해야 합니다. 앞의 JPA 설정을 그대로 사용합니다.
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
    //JPQL : select * from member m where m.name=?
    @Override
    Optional<Member> findByName(String name);
}
