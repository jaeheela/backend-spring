package hello.itemservice.domain;

import lombok.Data;
//프로젝트 구조 설명1 - 기본
//상품 자체를 나타내는 객체
// => 속성 : 이름, 가격, 수량
@Data
public class Item {

    private Long id;

    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
