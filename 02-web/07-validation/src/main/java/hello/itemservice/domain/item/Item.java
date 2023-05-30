package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
//Bean Validation - 소개
// => 검증 기능을 지금처럼 매번 코드로 작성하는 것은 상당히 번거롭다. - 프로젝트V2의 검증로직 작성 파트
// => 특히 특정 필드에 대한 검증 로직은 대부분 빈 값인지 아닌지, 특정 크기를 넘는지 아닌지와 같이 매우 일반적인 로직이다.
// => 검증 로직을 모든 프로젝트에 적용할 수 있게 공통화하고, 표준화 한 것이 바로 Bean Validation
// => Bean Validation을 잘 활용하면, 애노테이션 하나로 검증 로직을 매우 편리하게 적용할 수 있다.
// => 표준 기술 (Bean Validation 2.0(JSR-380) = 기술 표준 , 일반적으로 사용하는 구현체 = 하이버네이트 Validator)
// => 구현체 이름이 하이버네이트가 붙어서 그렇지 ORM과는 관련이 없음
// => 쉽게 이야기해서 검증 애노테이션과 여러 인터페이스의 모음 (like JPA = 표준 기술 , 그 구현체 = 하이버네이트)

//하이버네이트 Validator 관련 링크
// => 공식 사이트: http://hibernate.org/validator/
// => 공식 메뉴얼: https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/
// => 검증 애노테이션 모음: https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#validator-defineconstraints-spec

//Bean Validation - 시작
// Bean Validation 의존관계 추가 필요
// => 의존관계 추가 : Bean Validation을 사용하려면 다음 의존관계를 추가해야 한다.
// => build.gradle : implementation 'org.springframework.boot:spring-boot-starter-validation'
// => spring-boot-starter-validation 의존관계를 추가하면 라이브러리가 추가 된다.
// => Jakarta Bean Validation : jakarta.validation-api - Bean Validation인터페이스 , hibernate-validator - 구현체



//참고
// => javax.validation.constraints.NotNull VS org.hibernate.validator.constraints.Range
// => javax.validation 으로 시작하면 특정 구현에 관계없이 제공되는 표준 인터페이스이고, org.hibernate.validator 로 시작하면 하이버네이트 validator 구현체를 사용할 때만 제공되는 검증 기능이다.
// => 실무에서 대부분 하이버네이트 validator를 사용하므로 자유롭게 사용해도 된다.



@Data
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000")
public class Item {

    //검증 애노테이션
    // => @NotBlank : 빈값 + 공백만 있는 경우를 허용하지 않는다.
    // => @NotNull : null 을 허용하지 않는다.
    // => @Range(min = 1000, max = 1000000) : 범위 안의 값이어야 한다.
    // => @Max(9999) : 최대 9999까지만 허용한다.
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    /*
    @NotNull(groups = UpdateCheck.class) //수정 요구사항 추가
    private Long id;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = {SaveCheck.class})
    private Integer quantity;
     */
    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
