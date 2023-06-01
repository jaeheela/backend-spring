package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    //상품 등록 폼
    //입력 폼 처리
    // => th:object : 커맨드 객체를 지정 ( 사용하기 위해 해당 오브젝트 정보를 넘겨주기 >> 등록 폼이기 때문에 데이터가 비어있는 빈 오브젝트를 만들어서 뷰에 전달)
    // => *{...} : 선택 변수 식, th:object 에서 선택한 객체에 접근함
    // => th:field : HTML 태그의 id , name , value 속성을 자동으로 처리해줌
    // => 렌더링 전 : <input type="text" th:field="*{itemName}" />
    // => 렌더링 후 : <input type="text" id="itemName" name="itemName" th:value="*{itemName}" />

    // => th:object="${item}"
    // => <form> 에서 사용할 객체를 지정한다. 선택 변수 식( *{...} )을 적용할 수 있다.
    // => th:field="*{itemName}"
    // => *{itemName} 는 선택 변수 식을 사용했는데, ${item.itemName} 과 같다. 앞서 th:object 로 item을 선택했기 때문에 선택 변수 식을 적용할 수 있다.
    // => th:field 는 id , name , value 속성을 모두 자동으로 만들어준다. 참고로 해당 예제에서 id 속성을 제거해도 th:field 가 자동으로 만들어준다.
    // => [id : th:field 에서 지정한 변수 이름과 같다. >> id="itemName"] [name : th:field 에서 지정한 변수 이름과 같다. >> name="itemName"] [value : th:field 에서 지정한 변수의 값을 사용한다. value=""]
    // => 렌더링 전 : <input type="text" id="itemName" th:field="*{itemName}" class="form-control" placeholder="이름을 입력하세요">
    // => 렌더링 후 : <input type="text" id="itemName" class="form-control" placeholder="이름을 입력하세요" name="itemName" value="">

    // => 판매 여부 >> 판매 오픈 여부 >> "체크 박스"로 선택할 수 있다.
    // => 등록 지역 >> 서울, 부산, 제주 >> "체크 박스"로 다중 선택할 수 있다.
    // => 상품 종류 >> 도서, 식품, 기타 >> "라디오 버튼"으로 하나만 선택할 수 있다. >> ENUM 사용, 설명을 위해 description 필드 추가
    // => 배송 방식 >> 빠른 배송, 일반 배송, 느린 배송 : "셀렉트 박스"로 하나만 선택할 수 있다. >> DeliveryCode 클래스 사용 >> code 는 FAST 같은 시스템에서 전달하는 값이고, displayName 은 빠른 배송 같은 고객에게 보여주는 값이다.
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "form/addForm";
    }

    //상품 등록 처리
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {

        //체크박스
        //타임리프 미사용 후 실행 로그
        // => FormItemController : item.open=true //체크 박스를 선택하는 경우
        // => FormItemController : item.open=null //체크 박스를 선택하지 않는 경우
        // => 체크 박스를 체크하면 HTML Form에서 open=on 이라는 값이 넘어간다. 스프링은 on 이라는 문자를 true 타입으로 변환해준다. (스프링 타입 컨버터가 이 기능을 수행하는데, 뒤에서 설명한다.)
        // => 주의 - 체크 박스를 선택하지 않을 때) HTML에서 체크 박스를 선택하지 않고 폼을 전송하면 open 이라는 필드 자체가 서버로 전송되지 않는다.
        // => 로깅설정 후 실행하면 HTTP 메시지 바디에 open 의 이름도 전송이 되지 않는 것을 확인할 수 있다. itemName=itemA&price=10000&quantity=10 , 서버에서 Boolean 타입을 찍어보면 결과가 null 인 것을 확인할 수 있다.
        // => HTML checkbox는 선택이 안되면 클라이언트에서 서버로 값 자체를 보내지 않는다. 수정의 경우에는 상황에 따라서 이 방식이 문제가 될 수 있다.
        // => 사용자가 의도적으로 체크되어 있던 값을 체크를 해제해도 저장시 아무 값도 넘어가지 않기 때문에, 서버 구현에 따라서 값이 오지 않은 것으로 판단해서 값을 변경하지 않을 수도 있다.
        // => 이런 문제를 해결하기 위해서 스프링 MVC는 약간의 트릭을 사용하는데, 히든 필드를 하나 만들어서, _open처럼 기존 체크 박스 이름 앞에 언더스코어(_)를 붙여서 전송하면 체크를 해제했다고 인식할 수 있다.
        // => 히든 필드는 항상 전송된다. 따라서 체크를 해제한 경우 여기에서 open 은 전송되지 않고, _open 만 전송되는데, 이 경우 스프링 MVC는 체크를 해제했다고 판단한다.
        // => 체크 해제를 인식하기 위한 히든 필드 : <input type="hidden" name="_open" value="on"/>
        //hidden태그 사용 후 실행 로그
        // => FormItemController : item.open=true //체크 박스를 선택하는 경우
        // => FormItemController : item.open=false //체크 박스를 선택하지 않는 경우
        // => 체크 박스 체크) open=on&_open=on >> 체크 박스를 체크하면 스프링 MVC가 open 에 값이 있는 것을 확인하고 사용한다. 이때 _open 은 무시한다.
        // => 체크 박스 미체크) _open=on >> 체크 박스를 체크하지 않으면 스프링 MVC가 _open 만 있는 것을 확인하고, open 의 값이 체크되지 않았다고 인식한다. 이 경우 서버에서 Boolean 타입을 찍어보면 결과가 null 이 아니라 false 인 것을 확인할 수 있다.
        // => 개발할 때 마다 이렇게 히든 필드를 추가하는 것은 상당히 번거롭다. 타임리프가 제공하는 폼 기능을 사용하면 이런 부분을 자동으로 처리할 수 있다.
        // => 타임리프를 사용하면 체크 박스의 히든 필드와 관련된 부분도 함께 해결해준다. HTML 생성 결과를 보면 히든 필드 부분이 자동으로 생성되어 있다.
        // => 타임리프 사용 : <input type="hidden" name="_open" value="on"/>
        //타임리프 사용 후 실행 로그
        // => FormItemController : item.open=true //체크 박스를 선택하는 경우
        // => FormItemController : item.open=false //체크 박스를 선택하지 않는 경우
        //타임리프의 체크 확인
        // => checked="checked" : 체크 박스에서 판매 여부를 선택해서 저장하면, 조회시에 checked 속성이 추가된 것을 확인할 수 있다.
        // => 이런 부분을 개발자가 직접 처리하려면 상당히 번거롭다. 타임리프의 th:field 를 사용하면, 값이 true 인 경우 체크를 자동으로 처리해준다.
        log.info("item.open={}", item.getOpen()); //상품이 등록되는 곳에 다음과 같이 로그를 남겨서 값이 잘 넘어오는지 확인

        //멀티 체크박스
        // => 멀티 체크박스는 같은 이름의 여러 체크박스를 만들 수 있다.:th:for="${#ids.prev('regions')}"
        // => 그런데 문제는 이렇게 반복해서 HTML 태그를 생성할 때, 생성된 HTML 태그 속성에서 name 은 같아도 되지만, id 는 모두 달라야 한다.
        // => 따라서 타임리프는 체크박스를 each루프 안에서 반복해서 만들 때 임의로 1,2,3 숫자를 뒤에 붙여준다. - each로 체크박스가 반복 생성된 결과 id 뒤에 숫자가 추가된다.
        // => <input type="checkbox" value="SEOUL" class="form-check-input" id="regions1" name="regions">
        // => <input type="checkbox" value="BUSAN" class="form-check-input" id="regions2" name="regions">
        // => <input type="checkbox" value="JEJU" class="form-check-input" id="regions3" name="regions">
        // => HTML의 id 가 타임리프에 의해 동적으로 만들어지기 때문에 <label for="id 값"> 으로 label 의 대상이 되는 id 값을 임의로 지정하는 것은 곤란하다.
        // -> 타임리프는 ids.prev(...) , ids.next(...) 을 제공해서 동적으로 생성되는 id 값을 사용할 수 있도록 한다.
        //타임리프 HTML 생성 결과
        // => <div class="form-check form-check-inline"> <input type="checkbox" value="SEOUL" class="form-check-input" id="regions1" name="regions"> <input type="hidden" name="_regions" value="on"/> <label for="regions1" class="form-check-label">서울</label> </div>
        // => <div class="form-check form-check-inline"> <input type="checkbox" value="BUSAN" class="form-check-input" id="regions2" name="regions"> <input type="hidden" name="_regions" value="on"/> <label for="regions2" class="form-check-label">부산</label> </div>
        // => <div class="form-check form-check-inline"> <input type="checkbox" value="JEJU" class="form-check-input" id="regions3" name="regions"> <input type="hidden" name="_regions" value="on"/> <label for="regions3" class="form-check-label">제주</label> </div>
        // => <label for="id 값"> 에 지정된 id 가 checkbox 에서 동적으로 생성된 regions1 , regions2 , regions3 에 맞추어 순서대로 입력된 것을 확인할 수 있다.
        //서울, 부산 선택
        // => regions=SEOUL&_regions=on&regions=BUSAN&_regions=on&_regions=on
        // => FormItemController : item.regions=[SEOUL, BUSAN]
        //지역 선택X
        // =>  _regions=on&_regions=on&_regions=on
        // => FormItemController : item.regions=[]
        // => _regions 는 앞서 설명한 기능이다. 웹 브라우저에서 체크를 하나도 하지 않았을 때, 클라이언트가 서버에 아무런 데이터를 보내지 않는 것을 방지한다.
        // => 참고로 _regions 조차 보내지 않으면 결과는 null 이 된다. _regions 가 체크박스 숫자만큼 생성될 필요는 없지만, 타임리프가 생성되는 옵션 수 만큼 생성해서 그런 것이니 무시하자.
        //타임리프의 체크 확인
        // => checked="checked" : 멀티 체크 박스에서 등록 지역을 선택해서 저장하면, 조회시에 checked 속성이 추가된 것을 확인할 수 있다.
        // => 타임리프는 th:field 에 지정한 값과 th:value 의 값을 비교해서 체크를 자동으로 처리해준다.
        log.info("item.regions={}", item.getRegions());

        //라디오 버튼
        // => itemTypes 를 등록 폼, 조회, 수정 폼에서 모두 사용하므로 @ModelAttribute 의 특별한 사용법을 적용하자.
        // => ItemType.values() 를 사용하면 해당 ENUM의 모든 정보를 배열로 반환한다. 예) [BOOK, FOOD, ETC]
        //음식 선택
        // => itemType=FOOD //만약 선택하지 않으면 아무 값도 넘어가지 않는다.
        // => FormItemController : item.itemType=FOOD: 값이 있을 때
        // => FormItemController : item.itemType=null: 값이 없을 때
        // => 체크 박스는 수정시 체크를 해제하면 아무 값도 넘어가지 않기 때문에, 별도의 히든 필드로 이런 문제를 해결했다.
        // => 라디오 버튼은 이미 선택이 되어 있다면, 수정시에도 항상 하나를 선택하도록 되어 있으므로 체크 박스와 달리 별도의 히든 필드를 사용할 필요가 없다.
        //타임리프 HTML 생성 결과
        // => <div class="form-check form-check-inline"> <input type="radio" value="BOOK" class="form-check-input" id="itemType1" name="itemType"> <label for="itemType1" class="form-check-label">도서</label>
        // => <div class="form-check form-check-inline"> <input type="radio" value="FOOD" class="form-check-input"id="itemType2" name="itemType" checked="checked"> <label for="itemType2" class="form-check-label">식품</label>
        // => <div class="form-check form-check-inline"> <input type="radio" value="ETC" class="form-check-input" id="itemType3" name="itemType"> <label for="itemType3" class="form-check-label">기타</label>
        // => 선택한 식품( FOOD )에 checked="checked" 가 적용된 것을 확인할 수 있다.
        log.info("item.itemType={}", item.getItemType());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}"; //상품 상세로 리다이렉트
    }

    //상품 수정 폼
    // => 수정 폼은 앞서 설명한 내용과 같다. 수정 폼의 경우 id , name , value 를 모두 신경써야 했는데, 많은 부분이 th:field 덕분에 자동으로 처리되는 것을 확인할 수 있다.
    // => 렌더링 전 : <input type="text" id="itemName" th:field="*{itemName}" class="form-control">
    // => 렌더링 후 : <input type="text" id="itemName" class="form-control" name="itemName" value="itemA">
    // => 정리하자면 th:object , th:field 덕분에 폼을 개발할 때 약간의 편리함을 얻었다. 쉽고 단순해서 크게 어려움이 없었을 것이다.
    // => 사실 이것의 진짜 위력은 뒤에 설명할 검증(Validation)에서 나타난다. 이후 검증 부분에서 폼 처리와 관련된 부분을 더 깊이있게 알아보자.
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/editForm";
    }

    //상품 수정 처리
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

    //상품 목록
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    //상품 상세
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    //@ModelAttribute의 특별한 사용법
    // => 등록 폼, 상세화면, 수정 폼에서 모두 서울, 부산, 제주라는 체크 박스를 반복해서 보여주어야 한다.
    // => 이렇게 하려면 각각의 컨트롤러에서 model.addAttribute(...) 을 사용해서 체크 박스를 구성하는 데이터를 반복해서 넣어주어야 한다.
    // => @ModelAttribute 는 이렇게 컨트롤러에 있는 별도의 메서드에 적용할 수 있다.
    // => 이렇게하면 해당 컨트롤러를 요청할 때 regions 에서 반환한 값이 자동으로 모델( model )에 담기게 된다.
    // => 물론 이렇게 사용하지 않고, 각각의 컨트롤러 메서드에서 모델에 직접 데이터를 담아서 처리해도 된다.
    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    //타임리프에서 ENUM 직접 사용하기
    // => 이렇게 모델에 ENUM을 담아서 전달하는 대신에 타임리프는 자바 객체에 직접 접근할 수 있다.
    // => 타임리프에서 ENUM 직접 접근 : <div th:each="type : ${T(hello.itemservice.domain.item.ItemType).values()}">
    // => ${T(hello.itemservice.domain.item.ItemType).values()} 스프링EL 문법으로 ENUM을 직접 사용할 수 있다.
    // => ENUM에 values() 를 호출하면 해당 ENUM의 모든 정보가 배열로 반환된다.
    // => 그런데 이렇게 사용하면 ENUM의 패키지 위치가 변경되거나 할때 자바 컴파일러가 타임리프까지 컴파일 오류를 잡을 수 없으므로 추천하지는 않는다.
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values(); //Enum 값을 배열로 반환받음
    }

    //셀렉트 박스
    // => 셀렉트 박스는 여러 선택지 중에 하나를 선택할 때 사용할 수 있다. 이번시간에는 셀렉트 박스를 자바 객체를 활용해서 개발해보자.
    // => DeliveryCode 라는 자바 객체를 사용하는 방법으로 진행하겠다.
    // => DeliveryCode 를 등록 폼, 조회, 수정 폼에서 모두 사용하므로 @ModelAttribute 의 특별한 사용법을 적용하자.
    // => 참고로 @ModelAttribute 가 있는 deliveryCodes() 메서드는 컨트롤러가 호출 될 때 마다 사용되므로 deliveryCodes 객체도 계속 생성된다.
    // => 사실 이런 부분은 미리 생성해두고 재사용하는 것이 더 효율적이다.
    //타임리프 HTML 생성 결과
    // => <select class="form-select" id="deliveryCode" name="deliveryCode"> <option value="">==배송 방식 선택==</option>
    // => <option value="FAST" selected="selected">빠른 배송</option>
    // => <option value="NORMAL">일반 배송</option>
    // => <option value="SLOW">느린 배송</option> </select>
    // => selected="selected" : 빠른 배송을 선택한 예시인데, 선택된 샐랙트 박스가 유지되는 것을 확인할 수 있다.
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

}

