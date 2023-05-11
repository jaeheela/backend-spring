package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//프로젝트 설정 V1
// => 이전 프로젝트에 이어서 검증(Validation) 기능을 학습해보자. 이전 프로젝트를 일부 수정해서 validation-start 라는 프로젝트에 넣어두었다.
//프로젝트 설정 순서
// => validation-start 의 폴더 이름을 validation 변경 >> 프로젝트 임포트 >> File Open >> 해당 프로젝트의 build.gradle >> Open as Project
// => ItemServiceApplication.main()을 실행해서 프로젝트가 정상 수행되는지 확인
// => 실행 : http://localhost:8080
// => 실행 : http://localhost:8080/validation/v1/items

//검증 직접 처리 - 소개
//상품 저장 성공
// => 사용자가 상품 등록 폼에서 정상 범위의 데이터를 입력
// => 서버 검증 로직 통과 >> 상품 저장 >> 상품 상세 화면으로 redirect
//상품 저장 검증 실패
// => 고객이 상품 등록 폼에서 상품명을 입력하지 않거나, 가격, 수량 등이 너무 작거나 커서 검증 범위를 넘어섬
// => 서버 검증 로직 실패 >> 고객에게 다시 상품 등록 폼을 보여주고, 어떤 값을 잘못 입력했는지 친절하게 알려주기
@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

        //1. 검증 오류 결과를 보관
        // => 만약 검증시 오류가 발생하면 어떤 검증에서 오류가 발생했는지 정보를 담아둔다.
        Map<String, String> errors = new HashMap<>();

        //2. 검증 로직
        // => import org.springframework.util.StringUtils; 추가 필요
        // => 검증시 오류가 발생하면 errors 에 담아둔다.
        // => 이때 어떤 필드에서 오류가 발생했는지 구분하기 위해 오류가 발생한 필드명을 key 로 사용한다.
        // => 이후 뷰에서 이 데이터를 사용해서 고객에게 친절한 오류 메시지를 출력할 수 있다.
        // => 상품 수정의 검증은 더 효율적인 검증 처리 방법을 학습한 다음에 진행한다.
        if (!StringUtils.hasText(item.getItemName())) {
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
        }
        //특정 필드의 범위를 넘어서는 검증 로직 - 특정 필드가 아닌 복합 룰 검증
        // => 특정 필드를 넘어서는 오류를 처리해야 할 수도 있다.
        // => 이때는 필드 이름을 넣을 수 없으므로 globalError 라는 key 를 사용한다.
        // => 가격 1000원, 수량 1개를 선택하면 다음과 같은 HTML 결과 화면을 볼 수 있다. <div><p class="field-error">가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = 1000</ p></div>
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }

        //3. 검증 실패 로직
        // => 만약 검증에서 오류 메시지가 하나라도 있으면 오류 메시지를 출력하기 위해 model 에 errors 를 담고, 입력 폼이 있는 뷰 템플릿으로 보낸다.
        if (!errors.isEmpty()) {
            log.info("errors = {} ", errors);
            model.addAttribute("errors", errors);
            return "validation/v1/addForm"; //다시 입력 폼으로
        }

        //4. 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }
    //정리
    // => 만약 검증 오류가 발생하면 입력 폼을 다시 보여준다.
    // => 검증 오류들을 고객에게 친절하게 안내해서 다시 입력할 수 있게 한다.
    // => 검증 오류가 발생해도 고객이 입력한 데이터가 유지된다.
    //남은 문제점
    // => 뷰 템플릿에서 중복 처리가 많다. 뭔가 비슷하다.
    // => 타입 오류 처리가 안된다.
    // => Item 의 price , quantity 같은 숫자 필드는 타입이 Integer 이므로 문자 타입으로 설정하는 것이 불가능하다.
    // => 숫자 타입에 문자가 들어오면 오류가 발생한다.
    // => 그런데 이러한 오류는 스프링MVC에서 컨트롤러에 진입하기도 전에 예외가 발생하기 때문에, 컨트롤러가 호출되지도 않고, 400 예외가 발생하면서 오류 페이지를 띄워준다.
    // => Item의 price에 문자를 입력하는 것 처럼 타입 오류가 발생해도 고객이 입력한 문자를 화면에 남겨야 한다.
    // => 만약 컨트롤러가 호출된다고 가정해도 Item 의 price 는 Integer 이므로 문자를 보관할 수가 없다.
    // => 결국 문자는 바인딩이 불가능하므로 고객이 입력한 문자가 사라지게 되고, 고객은 본인이 어떤 내용을 입력해서 오류가 발생했는지 이해하기 어렵다.
    // => 결국 고객이 입력한 값도 어딘가에 별도로 관리가 되어야 한다.
    // => 지금부터 스프링이 제공하는 검증 방법을 하나씩 알아보자. - V2 ~ V4

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}

