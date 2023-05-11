package hello.itemservice.web.message;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/message/items")
@RequiredArgsConstructor
public class MessageItemController {

    private final ItemRepository itemRepository;

    //타임리프 메시지 적용
    // => 타임리프의 메시지 표현식 #{...} 를 사용하면 스프링의 메시지를 편리하게 조회할 수 있다.
    // => 예를 들어서 방금 등록한 상품이라는 이름을 조회하려면 #{label.item} 이라고 하면 된다.
    // => 렌더링 전 : <div th:text="#{label.item}"></h2>
    // => 렌더링 후 : <div>상품</h2>
    // => 타임리프 템플릿 파일에 메시지를 적용해보자. 적용 대상 : addForm.html, editForm.html, item.html, items.html

    //페이지 이름에 적용, 레이블에 적용, 버튼, 테이블에 적용 - ID, 상품명, 가격, 수량
    // => <h2>상품 등록 폼</h2> >> <h2 th:text="#{page.addItem}">상품 등록</h2>
    // => <h2>상품 목록 </h2> >> <h2 th:text="#{page.items}">상품 목록</h2>
    // => <h2>상품 상세 </h2> >> <h2 th:text="#{page.item}">상품 상세</h2>
    // => <h2>상품 수정</h2> >> <h2 th:text="#{page.updateItem}">상품 수정</h2>
    // => <label for="itemId">상품 ID</label> >> <label for="itemId" th:text="#{label.item.id}">상품 ID</label>
    // => <label for="itemName">상품명</label> >> <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
    // => <label for="price">가격</label> >> <label for="price" th:text="#{label.item.price}">가격</label>
    // => <label for="quantity">수량</label> >> <label for="quantity" th:text="#{label.item.quantity}">수량</label>
    // => <button type="submit">저장</button> >> <button type="submit" th:text="#{button.save}">저장</button>
    // => <button type="button">취소</button> >> <button type="button" th:text="#{button.cancel}">취소</button>
    // => <th>ID</th> >> <th th:text="#{label.item.id}">ID</th>
    // => <th>상품명</th> >> <th th:text="#{label.item.itemName}">상품명</th>
    // => <th>가격</th> >> <th th:text="#{label.item.price}">가격</th>
    // => <th>수량</th> >> <th th:text="#{label.item.quantity}">수량</th>
    // => 실행: 잘 동작하는지 확인하기 위해 messages.properties 파일의 내용을 가격에서 금액으로 변경해서 확인해보자. 정상 동작하면 다시 돌려두자.
    // => 참고로 파라미터는 다음과 같이 사용할 수 있다.
    // => hello.name=안녕 {0} >> <p th:text="#{hello.name(${item.itemName})}"></p>

    //국제화
    //웹 애플리케이션에 국제화를 적용해보자. 먼저 영어 메시지 파일 추가 >> 사실 이것으로 국제화 작업은 거의 끝났다.
    // => 앞에서 템플릿 파일에는 모두 #{...} 를 통해서 메시지를 사용하도록 적용해두었기 때문이다.
    // => 웹으로 확인하기 >> 웹 브라우저의 언어 설정 값을 변경하면서 국제화 적용을 확인해보자. >> 크롬 브라우저 설정 언어를 검색하고, 우선 순위를 변경하면 된다.
    // => 우선순위를 영어로 변경하고 테스트해보자. 웹 브라우저의 언어 설정 값을 변경하면 요청시 Accept-Language 의 값이 변경된다.
    // => Accept-Language 는 클라이언트가 서버에 기대하는 언어 정보를 담아서 요청하는 HTTP 요청 헤더이다.
    //스프링의 국제화 메시지 선택
    // => 앞서 MessageSource 테스트에서 보았듯이 메시지 기능은 Locale 정보를 알아야 언어를 선택할 수 있다.
    // => 결국 스프링도 Locale 정보를 알아야 언어를 선택할 수 있는데, 스프링은 언어 선택시 기본으로 Accept-Language 헤더의 값을 사용한다.
    //LocaleResolver
    // => 스프링은 Locale 선택 방식을 변경할 수 있도록 LocaleResolver 라는 인터페이스를 제공하는데, 스프링 부트는 기본으로 Accept-Language 를 활용하는 AcceptHeaderLocaleResolver 를 사용한다.
    // => LocaleResolver 인터페이스 : public interface LocaleResolver { Locale resolveLocale(HttpServletRequest request); void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale); }
    // => LocaleResolver 변경 : 만약 Locale 선택 방식을 변경하려면 LocaleResolver 의 구현체를 변경해서 쿠키나 세션 기반의 Locale 선택 기능을 사용할 수 있다.
    // => 예를 들어서 고객이 직접 Locale을 선택하도록 하는 것이다. 관련해서 LocaleResolver 를 검색하면 수 많은 예제가 나오니 필요한 분들은 참고하자.
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "message/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "message/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "message/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/message/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "message/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/message/items/{itemId}";
    }

}

