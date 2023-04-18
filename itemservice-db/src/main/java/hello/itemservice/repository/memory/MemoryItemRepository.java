package hello.itemservice.repository.memory;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;
//프로젝트 구조 설명1 - 기본
//MemoryItemRepository
// => ItemRepository 인터페이스를 구현한 메모리 저장소이다.
// => 메모리이기 때문에 자바를 다시 실행하면 기존에 저장된 데이터가 모두 사라진다.
// => save, update, findById는 쉽게 이해할 수 있을 것이다.
@Repository
public class MemoryItemRepository implements ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>(); //static
    private static long sequence = 0L; //static

    @Override
    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    // => findById 는 Optional을 반환해야 하기 때문에 Optional.ofNullable을 사용했다.
    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    // => findAll 은 ItemSearchCond 이라는 검색 조건을 받아서 내부에서 데이터를 검색하는 기능을 한다.
    // => 데이터베이스로 보면 where 구문을 사용해서 필요한 데이터를 필터링 하는 과정을 거치는 것이다.
    // => 여기서 자바 스트림을 사용한다.
    // => itemName이나, maxPrice가 null 이거나 비었으면 해당 조건을 무시한다.
    // => itemName이나, maxPrice에 값이 있을 때만 해당 조건으로 필터링 기능을 수행한다.
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
        return store.values().stream()
                .filter(item -> {
                    if (ObjectUtils.isEmpty(itemName)) {
                        return true;
                    }
                    return item.getItemName().contains(itemName);
                }).filter(item -> {
                    if (maxPrice == null) {
                        return true;
                    }
                    return item.getPrice() <= maxPrice;
                })
                .collect(Collectors.toList());
    }

    // => clearStore() 메모리에 저장된 Item을 모두 삭제해서 초기화한다. 테스트 용도로만 사용한다.
    public void clearStore() {
        store.clear();
    }

}
