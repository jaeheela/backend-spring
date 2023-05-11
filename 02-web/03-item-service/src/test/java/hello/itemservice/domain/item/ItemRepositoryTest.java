package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
//요구사항 분석 - 상품을 관리할 수 있는 서비스
// => 상품 도메인 모델 : 상품 ID, 상품명 가격, 수량
// => 상품 관리 기능 : 상품 목록, 상품 상세, 상품 등록, 상품 수정

//상품 도메인 개발
//Item - 상품 객체 >> ItemRepository - 상품 저장소 >> ItemRepositoryTest - 상품 저장소 테스트
class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    //테스트 메소드 하나 실행 시 호출되어 clear 해줄것임
    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Item item = new Item("itemA", 10000, 10);

        //when
        Item savedItem = itemRepository.save(item);

        //then
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isEqualTo(savedItem);//findItem 과 savedItem이 같니?
    }

    @Test
    void findAll() {
        //given
        Item item1 = new Item("item1", 10000, 10);
        Item item2 = new Item("item2", 20000, 20);

        itemRepository.save(item1);
        itemRepository.save(item2);

        //when
        List<Item> result = itemRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);//기대값이 2 나오니?
        assertThat(result).contains(item1, item2); //item1, item2 포함되고 있니?
    }

    @Test
    void updateItem() {
        //given
        Item item = new Item("item1", 10000, 10);

        Item savedItem = itemRepository.save(item);
        Long itemId = savedItem.getId();

        //when
        Item updateParam = new Item("item2", 20000, 30);
        itemRepository.update(itemId, updateParam);

        Item findItem = itemRepository.findById(itemId);

        //then
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName()); //이름이 같니?
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice()); //가격이 같니?
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity()); //수량이 같니?
    }
}
