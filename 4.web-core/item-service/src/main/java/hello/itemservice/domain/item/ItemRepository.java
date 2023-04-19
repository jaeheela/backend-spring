package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//요구사항 분석 - 상품을 관리할 수 있는 서비스
// => 상품 도메인 모델 : 상품 ID, 상품명 가격, 수량
// => 상품 관리 기능 : 상품 목록, 상품 상세, 상품 등록, 상품 수정

//상품 도메인 개발
//Item - 상품 객체 >> ItemRepository - 상품 저장소 >> ItemRepositoryTest - 상품 저장소 테스트
@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>(); //static
    private static long sequence = 0L; //static

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }

}
