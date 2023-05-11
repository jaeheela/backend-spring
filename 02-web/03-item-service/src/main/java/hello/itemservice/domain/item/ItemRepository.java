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

    // => 참고로 멀티 스레드 환경에서 스토어에 동시에 접근할 때는 실제로 HashMap 사용하면 안됨
    // => 동시에 접근할 경우 값이 보일 수 있으므로 HashMap -> currentHashmap , long -> 으로 변경해서 사용
    // 스프링 컨테이너는 어차피 싱글톤이라 static 안써도 되지만, 다른 프레임워크 사용 시 static 표현안하면 다수의 객체 생성되니 주의
    private static final Map<Long, Item> store = new HashMap<>(); //static 사용함
    private static long sequence = 0L; //static 사용함

    //아이템 저장
    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    //아이템 조회
    public Item findById(Long id) {
        return store.get(id);
    }

    //아이템 전체 조회
    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    //아이템 수정
    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    //테스트하려고 만듦 - clearStore 호츌하면 hashmap 데이타 다 날아감
    public void clearStore() {
        store.clear();
    }

}
