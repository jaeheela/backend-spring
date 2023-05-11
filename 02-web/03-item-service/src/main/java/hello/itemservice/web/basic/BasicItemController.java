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

@Controller
@RequestMapping("/basic/items")
//@RequiredArgsConstructor : final이 붙은 멤버변수만 사용해서 생성자를 자동으로 만들어준다.
// => 이렇게 생성자가 딱 1개만 있으면 스프링이 해당 생성자에 @Autowired 로 의존관계를 주입해준다. 따라서 final 키워드를 빼면 안된다!
// => 그러면 ItemRepository 의존관계 주입이 안된다. (스프링 핵심원리 - 기본편 강의 참고)
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    //1.
    //상품 목록 페이지
    // => [GET] http://localhost:8080/basic/items
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll(); //itemRepository에서 모든 상품 조회
        model.addAttribute("items", items); //모델에 담음
        return "basic/items"; //뷰 템플릿 호출
    }

    //테스트용 데이터 추가 : 테스트용 데이터가 없으면 회원 목록 기능이 정상 동작하는지 확인하기 어렵다.
    // => @PostConstruct : 해당 빈의 의존관계가 모두 주입되고 나면 초기화 용도로 호출된다. 여기서는 간단히 테스트용 테이터를 넣기 위해서 사용했다.
    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    //2.
    //상품 상세 페이지
    // => [GET] http://localhost:8080/basic/items/1
    // => [GET] http://localhost:8080/basic/items/2
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId); //PathVariable 로 넘어온 상품ID로 상품 조회
        model.addAttribute("item", item); // 모델에 담음
        return "basic/item"; //뷰 템플릿 호출
    }

    //3.
    //상품 등록 페이지
    // => [GET] http://localhost:8080/basic/items/add
    @GetMapping("/add")
    public String addForm() { //단순히 뷰 템플릿만 호출
        return "basic/addForm";
    }

    //4.
    // => 이제 상품 등록 폼에서 전달된 데이터로 실제 상품을 등록 처리해보자. 상품 등록 폼은 다음 방식으로 서버에 데이터를 전달한다.
    // => POST - HTML Form
    // => content-type: application/x-www-form-urlencoded
    // => 메시지 바디에 쿼리 파리미터 형식으로 전달 itemName=itemA&price=10000&quantity=10
    // => 예) 회원 가입, 상품 주문, HTML Form 사용
    // => 요청 파라미터 형식을 처리해야 하므로 @RequestParam 을 사용하자
    // => 중요: 여기서는 상품 상세에서 사용한 item.html 뷰 템플릿을 그대로 재활용한다. 실행해서 상품이 잘 저장되는지 확인하자.

    //상품 등록 처리1  - @RequestParam
    // => [POST] http://localhost:8080/basic/items/add
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName, //@RequestParam String itemName : itemName 요청 파라미터 데이터를 해당 변수에 받음
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {
        Item item = new Item(); //Item 객체 생성
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        itemRepository.save(item); //itemRepository 를 통해 저장
        model.addAttribute("item", item); //저장된 item을 모델에 담아서 뷰에 전달
        return "basic/item";
    }

    //상품 등록 처리2 - @ModelAttribute
    // => @RequestParam 으로 변수를 하나하나 받아서 Item 을 생성하는 과정은 불편했다.
    // => 이번에는 @ModelAttribute 를 사용해서 한번에 처리해보자.
    //@ModelAttribute의 기능
    // 1. 요청 파라미터 처리 : Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력해준다.
    // 2. Model 추가 : @ModelAttribute 는 중요한 한가지 기능이 더 있는데, 바로 모델(Model)에 @ModelAttribute 로 지정한 객체를 자동으로 넣어준다.
    // => 지금 코드를 보면 model.addAttribute("item", item) 가 주석처리 되어 있어도 잘 동작하는 것을 확인할 수 있다.
    // => 모델에 데이터를 담을 때는 이름이 필요하다. 이름은 @ModelAttribute 에 지정한 name(value) 속성을 사용한다.
    // => 만약 다음과 같이 @ModelAttribute 의 이름을 다르게 지정하면 다른 이름으로 모델에 포함된다.
    // => @ModelAttribute("hello") Item item : 이름을 hello로 지정
    // => model.addAttribute("hello", item); : 모델에 hello이름으로 저장
    // => [POST] http://localhost:8080/basic/items/add
    // => 주의) 실행전에 이전 버전인 addItemV 에 @PostMapping("/add") 를 꼭 주석처리 해주어야 한다. 그렇지 않으면 중복 매핑으로 오류가 발생한다.
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item,
                            Model model) {
        itemRepository.save(item);
        //model.addAttribute("item", item); //자동 추가, 생략 가능
        return "basic/item";
    }

    /**
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
     * */
    //상품 등록 처리3  - ModelAttribute 이름 생략
    // => @ModelAttribute 의 이름을 생략할 수 있다.
    // => 주의) @ModelAttribute의 이름을 생략하면 모델에 저장될 때 클래스명을 사용한다. 이때 클래스의 첫글자만 소문자로 변경해서 등록한다.
    // => 예) @ModelAttribute클래스명 >> 모델에 자동 추가되는이름 : Item >> item , HelloWorld >> helloWorld
    // => [POST] http://localhost:8080/basic/items/add
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     * */
    //상품 등록 처리4 -  ModelAttribute 전체 생략
    // =>  @ModelAttribute 자체도 생략가능하다. 대상 객체는 모델에 자동 등록된다. 나머지 사항은 기존과 동일하다.
    // => [POST] http://localhost:8080/basic/items/add
    //@PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }


    /**
     * PRG - Post/Redirect/Get
     */
    //상품 등록 처리5  - PRG - Post/Redirect/Get
    // => 사실 지금까지 진행한 상품 등록 처리 컨트롤러는 심각한 문제가 있다.
    // => (addItemV1 ~ addItemV4) 상품 등록을 완료하고 웹 브라우저의 새로고침 버튼을 클릭해보자.
    // => 상품이 계속해서 중복 등록되는 것을 확인할 수 있다.
    // => 그 이유는 다음 그림을 통해서 확인할 수 있다.
    // => POST 등록 후 새로 고침 : 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
    // => 상품 등록 폼에서 데이터를 입력하고 저장을 선택하면 POST /add + 상품 데이터를 서버로 전송한다.
    // => 이 상태에서 새로 고침을 또 선택하면 마지막에 전송한 POST /add + 상품 데이터를 서버로 다시 전송하게 된다.
    // => 그래서 내용은 같고, ID만 다른 상품 데이터가 계속 쌓이게 된다.
    // => 이 문제를 어떻게 해결할 수 있을까? 다음 그림을 보자.
    // => 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
    // => 새로 고침 문제를 해결하려면 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라, 상품 상세 화면으로 리다이렉트를 호출해주면 된다.
    // => 웹 브라우저는 리다이렉트의 영향으로 상품 저장 후에 실제 상품 상세 화면으로 다시 이동한다.
    // => 따라서 마지막에 호출한 내용이 상품 상세 화면인 GET /items/{id} 가 되는 것이다.
    // => 이후 새로고침을 해도 상품 상세 화면으로 이동하게 되므로 새로 고침 문제를 해결할 수 있다.
    // => 상품 등록 처리 이후에 뷰 템플릿이 아니라 상품 상세 화면으로 리다이렉트 하도록 코드를 작성해보자.
    // => 이런 문제 해결 방식을 PRG Post/Redirect/Get 라 한다.
    // => 주의) "redirect:/basic/items/" + item.getId() : redirect에서 [+item.getId()]처럼 URL에 변수를 더해서 사용하는 것은
    // => URL 인코딩이 안되기 때문에 위험하다.
    // => 다음에 설명하는 RedirectAttributes를 사용하자.
    // => [POST] http://localhost:8080/basic/items/add
    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    //상품 등록 처리6 - RedirectAttributes
    // => 상품을 저장하고 상품 상세 화면으로 리다이렉트 한 것 까지는 좋았다.
    // => 그런데 고객 입장에서 저장이 잘 된 것인지 안 된 것인지 확신이 들지 않는다.
    // => 그래서 저장이 잘 되었으면 상품 상세 화면에 "저장되었습니다"라는 메시지를 보여달라는 요구사항이 왔다. 간단하게 해결해보자.
    // => 리다이렉트 할 때 간단히 status=true 를 추가해보자.
    // => 그리고 뷰 템플릿에서 이 값이 있으면, 저장되었습니다. 라는 메시지를 출력해보자.
    // => 실행해보면 다음과 같은 리다이렉트 결과가 나온다. http://localhost:8080/basic/items/3?status=true
    // => RedirectAttributes 를 사용하면 URL 인코딩도 해주고, pathVarible , 쿼리 파라미터까지 처리해준다.
    // => redirect:/basic/items/{itemId}
    // => : pathVariable 바인딩 - {itemId}
    // => : 나머지는 쿼리 파라미터로 처리 - ?status=true
    // => [POST] http://localhost:8080/basic/items/add
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    //5.
    //상품 수정 페이지
    // => [GET] http://localhost:8080/basic/items/1/edit
    // => [GET] http://localhost:8080/basic/items/2/edit
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId); //수정에 필요한 정보 조회
        model.addAttribute("item", item);
        return "basic/editForm"; //수정용 폼 뷰 호출
    }

    //6.
    //상품 수정 처리
    // => 상품 수정은 상품 등록과 전체 프로세스가 유사하다.
    // => [GET] /items/{itemId}/edit : 상품수정폼
    // => [POST] /items/{itemId}/edit : 상품 수정 처리
    //리다이렉트
    // => 상품 수정은 마지막에 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리다이렉트를 호출한다.
    // => 스프링은 redirect:/... 으로 편리하게 리다이렉트를 지원한다.
    // => redirect:/basic/items/{itemId}
    // => 컨트롤러에 매핑된 @PathVariable 의 값은 redirect 에도 사용 할 수 있다.
    // => redirect:/basic/items/{itemId} : {itemId}는 @PathVariable Long itemId 의 값을 그대로 사용한다.
    // =>  참고로 리다이렉트에 대한 자세한 내용은 모든 개발자를 위한 HTTP 웹 기본 지식 강의를 참고하자.
    // => 참고로 HTML Form 전송은 PUT, PATCH를 지원하지 않는다. GET, POST만 사용할 수 있다.
    // => PUT, PATCH는 HTTP API 전송시에 사용한다.
    // => 스프링에서 HTTP POST로 Form 요청할 때 히든 필드를 통해서 PUT, PATCH 매핑을 사용하는 방법이 있지만, HTTP 요청상 POST 요청이다.
    // => [POST] http://localhost:8080/basic/items/1/edit
    // => [POST] http://localhost:8080/basic/items/2/edit
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

}

