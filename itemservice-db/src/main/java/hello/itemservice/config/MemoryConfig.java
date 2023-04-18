package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.memory.MemoryItemRepository;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//프로젝트 구조 설명2 - 설정
// 스프링 부트 설정 분석 - MemoryConfig
// => ItemServiceV1 , MemoryItemRepository 를 스프링 빈으로 등록하고 생성자를 통해 의존관계를 주입한다.
// => 참고로 여기서는 서비스와 리포지토리는 구현체를 편리하게 변경하기 위해, 이렇게 수동으로 빈을 등록했다.
// => 컨트롤러는 컴포넌트 스캔을 사용한다.
@Configuration
public class MemoryConfig {

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new MemoryItemRepository();
    }

}
