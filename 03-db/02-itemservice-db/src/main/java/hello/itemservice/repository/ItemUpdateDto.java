package hello.itemservice.repository;

import lombok.Data;
//프로젝트 구조 설명1 - 기본
//ItemUpdateDto
// => 상품을 수정할 때 사용하는 객체이다.
// => 단순히 데이터를 전달하는 용도로 사용되므로 DTO를 뒤에 붙였다.

//DTO(data transfer object) - 데이터 전송 객체
// => DTO는 기능은 없고 데이터를 전달만 하는 용도로 사용되는 객체를 뜻한다.
// => 참고로 DTO에 기능이 있으면 안되는가? 그것은 아니다. 객체의 주 목적이 데이터를 전송하는 것이라면 DTO라 할 수 있다.
// => 객체 이름에 DTO를 꼭 붙여야 하는 것은 아니다. 대신 붙여두면 용도를 알 수 있다는 장점은 있다.
// => 이전에 설명한 ItemSearchCond도 DTO 역할을 하지만, 이 프로젝트에서 Cond는 검색 조건으로 사용한다는 규칙을 정했다.
// => 따라서 DTO를 붙이지 않아도 된다. ItemSearchCondDto 이렇게 하면 너무 복잡해진다. 그리고 Cond 라는 것만 봐도 용도를 알 수 있다.
// => 참고로 이런 규칙은 정해진 것이 없기 때문에 해당 프로젝트 안에서 일관성 있게 규칙을 정하면 된다.
@Data
public class ItemUpdateDto {
    private String itemName;
    private Integer price;
    private Integer quantity;

    public ItemUpdateDto() {
    }

    public ItemUpdateDto(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
