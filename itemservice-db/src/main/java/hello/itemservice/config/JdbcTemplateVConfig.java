package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jdbctemplate.JdbcTemplateItemRepositoryV1;
import hello.itemservice.repository.jdbctemplate.JdbcTemplateItemRepositoryV2;
import hello.itemservice.repository.jdbctemplate.JdbcTemplateItemRepositoryV3;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
// JdbcTemplate 적용 - 구성과 실행
// => 실제 코드가 동작하도록 구성하고 실행해보자.
// => ItemRepository 구현체로 JdbcTemplateItemRepositoryV1 이 사용되도록 했다.
// => 이제 메모리 저장소가 아니라 실제 DB에 연결하는 JdbcTemplate이 사용된다.
@Configuration
@RequiredArgsConstructor
public class JdbcTemplateVConfig {

    private final DataSource dataSource;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        //return new JdbcTemplateItemRepositoryV1(dataSource);
        //return new JdbcTemplateItemRepositoryV2(dataSource);
        return new JdbcTemplateItemRepositoryV3(dataSource);
    }

}
