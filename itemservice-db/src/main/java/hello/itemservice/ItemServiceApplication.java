package hello.itemservice;

import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
//프로젝트 구조 설명2 - 설정

@Slf4j
//@Import(MemoryConfig.class) //MemoryConfig 설정 파일로 사용
@Import(JdbcTemplateVConfig.class) //JdbcTemplateVConfig 설정 파일로 사용
//@Import(MyBatisConfig.class) //MyBatisConfig 설정 파일로 사용
//@Import(JpaConfig.class) //JpaConfig 설정 파일로 사용
//@Import(SpringDataJpaConfig.class) //SpringDataJpaConfig 설정 파일로 사용
//@Import(QuerydslConfig.class) //QuerydslConfig 설정 파일로 사용
//@Import(V2Config.class) //V2Config 설정 파일로 사용
@SpringBootApplication(scanBasePackages = "hello.itemservice.web") //여기서는 컨트롤러만 컴포넌트 스캔을 사용하고, 나머지는 직접 수동 등록한다. 그래서 컴포넌트 스캔 경로를 hello.itemservice.web 하위로 지정했다.
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	// => @Profile("local") : 특정 프로필의 경우에만 해당 스프링 빈을 등록한다. 여기서는 local 이라는 이름의 프로필이 사용되는 경우에만 testDataInit 이라는 스프링 빈을 등록한다. 이 빈은 앞서 본 것인데, 편의상 초기 데이터를 만들어서 저장하는 빈이다.
	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}

	/*
	//@Profile("test") : 프로필이 test인 경우에만 데이터소스를 스프링 빈으로 등록한다. , 테스트 케이스에서만 이 데이터소스를 스프링 빈으로 등록해서 사용하겠다는 뜻이다.
	// => dataSource() : jdbc:h2:mem:db 이 부분이 중요하다. 데이터소스를 만들때 이렇게만 적으면 임베디드 모드(메모리 모드)로 동작하는 H2 데이터베이스를 사용할 수 있다.
	// => DB_CLOSE_DELAY=-1 : 임베디드 모드에서는 데이터베이스 커넥션 연결이 모두 끊어지면 데이터베이스도 종료되는데, 그것을 방지하는 설정이다.
	// => 이 데이터소스를 사용하면 메모리 DB를 사용할 수 있다.
	@Bean
	@Profile("test")
	public DataSource dataSource() {
		log.info("메모리 데이터베이스 초기화");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
	}
	*/

}
