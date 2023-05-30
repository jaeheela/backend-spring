package hello.hellospring;

import hello.hellospring.aop.TimeTraceAop;
import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

//스프링 빈 환경 설정 파일 - [자바 코드로 직접 스프링 빈 등록]
@Configuration
public class SpringConfig {

    //스프링 데이터 JPA 사용할 때 이용
    // => 스프링 데이터 JPA가 SpringDataJpaMemberRepository 를 스프링 빈으로 자동 등록해준다.
    //스프링 데이터 JPA 제공 클래스 : JpaRepository
    //스프링 데이터 JPA 제공 기능 : 인터페이스를 통한 기본적인 CRUD | findByName() , findByEmail() 처럼 메서드 이름 만으로 조회 기능 제공 | 페이징 기능 자동 제공
    // => 참고로 실무에서는 JPA와 스프링 데이터 JPA를 기본으로 사용하고, 복잡한 동적 쿼리는 Querydsl이라는 라이브러리를 사용하면 된다.
    // => Querydsl을 사용하면 쿼리도 자바 코드로 안전하게 작성할 수 있고, 동적 쿼리도 편리하게 작성할 수 있다.
    // => 이 조합으로 해결하기 어려운 쿼리는 JPA가 제공하는 네이티브 쿼리를 사용하거나, 앞서 학습한 스프링 JdbcTemplate를 사용하면 된다. - 자세한 내용 : 실전! 스프링 데이터 JPA 참고
    private final MemberRepository memberRepository;

    //@Autowired //하나는 생략가능
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }
}