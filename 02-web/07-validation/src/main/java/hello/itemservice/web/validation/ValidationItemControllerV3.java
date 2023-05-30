package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
//프로젝트 준비 V3 - Bean Validation
// => 앞서 만든 기능을 유지하기 위해, 컨트롤러와 템플릿 파일을 복사하자.
// => ValidationItemControllerV3 컨트롤러 생성
// => hello.itemservice.web.validation.ValidationItemControllerV2 복사 >> hello.itemservice.web.validation.ValidationItemControllerV3 붙여넣기
// => URL 경로 변경: validation/v2/ >> validation/v3/
// => 템플릿 파일 복사 > validation/v2 디렉토리의 모든 템플릿 파일을 validation/v3 디렉토리로 복사
// => /resources/templates/validation/v2/ >> /resources/templates/validation/v3/ : addForm.html, editForm.html, item.html, items.html
// => resources/templates/validation/v3/ 하위 4개 파일 모두 URL 경로 변경: validation/v2/ >> validation/v3/ : addForm.html, editForm.html, item.html, items.html
// => cmd + c >> cmd + r >> 변경할 경로 작성 : validation/v2/ >> replace all
// => 실행 : http://localhost:8080/validation/v3/items 실행 후 웹 브라우저의 URL이 validation/v3 으로 잘 유지되는지 확인해자.

//스프링 MVC는 어떻게 Bean Validator를 사용?
// => 스프링 부트가 spring-boot-starter-validation 라이브러리를 넣으면 자동으로 Bean Validator를 인지하고 스프링에 통합한다.
// => 스프링 부트는 자동으로 글로벌 Validator로 등록한다.
// => LocalValidatorFactoryBean 을 글로벌 Validator로 등록한다.
// => 이 Validator는 @NotNull 같은 애노테이션을 보고 검증을 수행한다.
// => 이렇게 글로벌 Validator가 적용되어 있기 때문에, @Valid , @Validated 만 적용하면 된다.
// => 검증 오류가 발생하면, FieldError , ObjectError 를 생성해서 BindingResult 에 담아준다.
// => 주의!) 직접 글로벌 Validator를 직접 등록하면 스프링 부트는 Bean Validator를 글로벌 Validator 로 등록하지 않는다. 따라서 애노테이션 기반의 빈 검증기가 동작하지 않으므로 제거하자. - ItemServiceApplication.java
// 참고
// => 검증시 @Validated @Valid 둘다 사용가능하다. javax.validation.@Valid 를 사용하려면 build.gradle 의존관계 추가가 필요하다. (이전에 추가했다.) : implementation 'org.springframework.boot:spring-boot-starter-validation'
// => @Validated 는 스프링 전용 검증 애노테이션이고, @Valid 는 자바 표준 검증 애노테이션이다.  둘중 아무거나 사용해도 동일하게 작동하지만, @Validated 는 내부에 groups 라는 기능을 포함하고 있다.(기능이 더 많음)
// 검증 순서
// => @ModelAttribute 각각의 필드에 타입 변환 시도 >> 성공하면 다음으로 >> 실패하면 typeMismatch 로 FieldError 추가 >>  Validator 적용
// 바인딩에 성공한 필드만 Bean Validation 적용
// => BeanValidator는 바인딩에 실패한 필드는 BeanValidation을 적용하지 않는다.
// => 생각해보면 타입 변환에 성공해서 바인딩에 성공한 필드여야 BeanValidation 적용이 의미 있다. (일단 모델 객체에 바인딩 받는 값이 정상으로 들어와야 검증도 의미가 있다.)
// => @ModelAttribute 각각의 필드 타입 변환시도 변환에 성공한 필드만 BeanValidation 적용
// => : 예) itemName 에 문자 "A" 입력 >> 타입 변환 성공 >> itemName 필드에 BeanValidation 적용
// => : 예) price 에 문자 "A" 입력 >> "A"를 숫자 타입 변환 시도 실패 >> typeMismatch FieldError 추가 >> price 필드는 BeanValidation 적용 X


//Bean Validation - 에러 코드
// => Bean Validation이 기본으로 제공하는 오류 메시지를 좀 더 자세히 변경하고 싶으면 어떻게 하면 될까?
// => Bean Validation을 적용하고 bindingResult에 등록된 검증 오류 코드를 보자.
// => 오류 코드가 애노테이션 이름으로 등록된다. 마치 typeMismatch 와 유사하다.
// => NotBlank 라는 오류 코드를 기반으로 MessageCodesResolver 를 통해 다양한 메시지 코드가 순서대로 생성된다.
// => @NotBlank
// => NotBlank.item.itemName >> NotBlank.itemName >> NotBlank.java.lang.String >> NotBlank
// => @Range
// => Range.item.price >> Range.price >> Range.java.lang.Integer >> Range

//메시지 등록
// => 이제 메시지를 등록해보자. - errors.properties에 추가


@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;

    // http://localhost:8080/validation/v3/items
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    // http://localhost:8080/validation/v3/items/{itemId}
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    // http://localhost:8080/validation/v3/items/add
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

   @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v3/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItem2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v3/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }


    // http://localhost:8080/validation/v3/items/{itemId}/edit
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }


    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

    //@PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}

