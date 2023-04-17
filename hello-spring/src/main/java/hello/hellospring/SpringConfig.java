package hello.hellospring;

import hello.hellospring.repository.JdbcMemberRepository;
import hello.hellospring.repository.JdbcTemplateMemberRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//스프링 빈 환경 설정 파일 - [자바 코드로 직접 스프링 빈 등록]
@Configuration
public class SpringConfig {

    // => DataSource는 데이터베이스 커넥션을 획득할 때 사용하는 객체다.
    // => 스프링 부트는 데이터베이스 커넥션 정보를 바탕으로 DataSource를 생성하고 스프링 빈으로 만들어둔다.
    // => 그래서 DI를 받을 수 있다.
    private final DataSource dataSource;

    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        //return new MemoryMemberRepository();
        //return new JdbcMemberRepository(dataSource); //스프링 설정 변경
        return new JdbcTemplateMemberRepository(dataSource); //JdbcTemplate을 사용하도록 스프링 설정 변경
    }
}
