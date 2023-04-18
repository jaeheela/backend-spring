package hello.hellospring;

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

    //순수 JDBC, JdbcTemplate,JPA을 사용하도록 DataSource 객체 추가
    // => DataSource는 데이터베이스 커넥션을 획득할 때 사용하는 객체다. 스프링 부트는 데이터베이스 커넥션 정보를 바탕으로 DataSource를 생성하고 스프링 빈으로 만들어둔다. 그래서 DI를 받을 수 있다.
    //private final DataSource dataSource;

    //순수 JDBC, JdbcTemplate 사용할 때 이용 - 1
    /*
    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    */

    //JPA 사용할 때 이용 - 2
    /*
    private final EntityManager em;
    public SpringConfig(DataSource dataSource, EntityManager em) {
        this.dataSource = dataSource;
        this.em = em;
    }
    */

    //스프링 데이터 JPA 사용할 때 이용 - 3
    // => 스프링 데이터 JPA가 SpringDataJpaMemberRepository 를 스프링 빈으로 자동 등록해준다.
    //스프링 데이터 JPA 제공 클래스 : JpaRepository
    //스프링 데이터 JPA 제공 기능 : 인터페이스를 통한 기본적인 CRUD | findByName() , findByEmail() 처럼 메서드 이름 만으로 조회 기능 제공 | 페이징 기능 자동 제공
    // => 참고로 실무에서는 JPA와 스프링 데이터 JPA를 기본으로 사용하고, 복잡한 동적 쿼리는 Querydsl이라는 라이브러리를 사용하면 된다.
    // => Querydsl을 사용하면 쿼리도 자바 코드로 안전하게 작성할 수 있고, 동적 쿼리도 편리하게 작성할 수 있다.
    // => 이 조합으로 해결하기 어려운 쿼리는 JPA가 제공하는 네이티브 쿼리를 사용하거나, 앞서 학습한 스프링 JdbcTemplate를 사용하면 된다. - 자세한 내용 : 실전! 스프링 데이터 JPA 참고
    private final MemberRepository memberRepository;
    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }


    /*
    @Bean
    public MemberService memberService() { return new MemberService(memberRepository()); }

    @Bean
    public MemberRepository memberRepository() {
        //return new MemoryMemberRepository();
        //return new JdbcMemberRepository(dataSource); //순수 JDBC 회원 리포지토리를 시용하도록 스프링 설정 변경
        //return new JdbcTemplateMemberRepository(dataSource); //JdbcTemplate 회원 리포지토리를 사용하도록 스프링 설정 변경
        return new JpaMemberRepository(em); //JPA 회원 리포지토리를 사용하도록 스프링 설정 변경
    }
    */
}
