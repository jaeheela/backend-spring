package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;




//상품 등록 처리 - @ModelAttribute
// => 이제 상품 등록 폼에서 전달된 데이터로 실제 상품을 등록 처리해보자. 상품 등록 폼은 다음 방식으로 서버에 데이터를 전달한다.
//POST - HTML Form
//content-type: application/x-www-form-urlencoded
//메시지 바디에 쿼리 파리미터 형식으로 전달 itemName=itemA&price=10000&quantity=10 예) 회원 가입, 상품 주문, HTML Form 사용
//
// 요청 파라미터 형식을 처리해야 하므로 @RequestParam 을 사용하자 상품 등록 처리 - @RequestParam
//addItemV1 - BasicItemController에 추가

//먼저 @RequestParam String itemName : itemName 요청 파라미터 데이터를 해당 변수에 받는다. Item 객체를 생성하고 itemRepository 를 통해서 저장한다.
//저장된 item 을 모델에 담아서 뷰에 전달한다.
//중요: 여기서는 상품 상세에서 사용한 item.html 뷰 템플릿을 그대로 재활용한다. 실행해서 상품이 잘 저장되는지 확인하자.
//상품 등록 처리 - @ModelAttribute
//@RequestParam 으로 변수를 하나하나 받아서 Item 을 생성하는 과정은 불편했다. 이번에는 @ModelAttribute 를 사용해서 한번에 처리해보자.

@Controller
@RequestMapping("/basic/items")
//@RequiredArgsConstructor : final이 붙은 멤버변수만 사용해서 생성자를 자동으로 만들어준다.
// => 이렇게 생성자가 딱 1개만 있으면 스프링이 해당 생성자에 @Autowired 로 의존관계를 주입해준다. 따라서 final 키워드를 빼면 안된다!
// => 그러면 ItemRepository 의존관계 주입이 안된다. (스프링 핵심원리 - 기본편 강의 참고)
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;


    //상품 목록
    // => http://localhost:8080/basic/items
    @GetMapping
    public String items(Model model) { //itemRepository에서 모든 상품 조회 >> 모델에 담음 >> 뷰 템플릿 호출
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    //상품 상세
    // => http://localhost:8080/basic/items/1
    // => http://localhost:8080/basic/items/2
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) { //PathVariable 로 넘어온 상품ID로 상품 조회 >> 모델에 담음 >> 뷰 템플릿 호출
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    //상품 등록
    // => http://localhost:8080/basic/items/add
    @GetMapping("/add")
    public String addForm() { //단순히 뷰 템플릿만 호출
        return "basic/addForm";
    }

    //상품 등록 처리1
    // => [POST] http://localhost:8080/basic/items/add
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }
    //상품 등록 처리2
    // => [POST] http://localhost:8080/basic/items/add
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {

        itemRepository.save(item);
        //model.addAttribute("item", item); //자동 추가, 생략 가능

        return "basic/item";
    }

    //상품 등록 처리3
    // => [POST] http://localhost:8080/basic/items/add
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    //상품 등록 처리4
    // => [POST] http://localhost:8080/basic/items/add
    //@PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    //상품 등록 처리5
    // => [POST] http://localhost:8080/basic/items/add
    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    //상품 등록 처리6
    // => [POST] http://localhost:8080/basic/items/add
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    //
    // =>
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    //테스트용 데이터 추가
    // => 테스트용 데이터가 없으면 회원 목록 기능이 정상 동작하는지 확인하기 어렵다.
    // => @PostConstruct : 해당 빈의 의존관계가 모두 주입되고 나면 초기화 용도로 호출된다. 여기서는 간단히 테스트용 테이터를 넣기 위해서 사용했다.
    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}

