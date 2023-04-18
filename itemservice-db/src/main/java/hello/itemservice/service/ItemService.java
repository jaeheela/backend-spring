package hello.itemservice.service;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

//서비스 분석 - ItemService 인터페이스
// => 서비스의 구현체를 쉽게 변경하기 위해 인터페이스를 사용했다.
// => 참고로 서비스는 구현체를 변경할 일이 많지는 않기 때문에 사실 서비스에 인터페이스를 잘 도입하지는 않는다.
// => 여기서는 예제 설명 과정에서 구현체를 변경할 예정이어서 인터페이스를 도입했다.
public interface ItemService {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCond itemSearch);
}
