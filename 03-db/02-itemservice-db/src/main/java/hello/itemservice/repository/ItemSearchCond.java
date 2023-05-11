package hello.itemservice.repository;

import lombok.Data;
//프로젝트 구조 설명1 - 기본
//ItemSearchCond
// => 검색 조건으로 사용된다. 상품명, 최대 가격이 있다.
// => 참고로 상품명의 일부만 포함되어도 검색이 가능해야 한다. ( like 검색)
// => cond는 condition을 줄여서 사용했다.
// => 이 프로젝트에서 검색 조건은 뒤에 Cond를 붙이도록 규칙을 정했다.
@Data
public class ItemSearchCond {

    private String itemName;
    private Integer maxPrice;

    public ItemSearchCond() {
    }

    public ItemSearchCond(String itemName, Integer maxPrice) {
        this.itemName = itemName;
        this.maxPrice = maxPrice;
    }
}
