package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
//프로젝트 준비 V2
// => 앞서 만든 기능을 유지하기 위해, 컨트롤러와 템플릿 파일을 복사하자.
// => ValidationItemControllerV2 컨트롤러 생성
// => hello.itemservice.web.validation.ValidationItemControllerV1 복사 >> hello.itemservice.web.validation.ValidationItemControllerV2 붙여넣기
// => URL 경로 변경: validation/v1/ >>  validation/v2/
// => 템플릿 파일 복사 : validation/v1 디렉토리의 모든 템플릿 파일을 validation/v2 디렉토리로 복사
// => /resources/templates/validation/v1/ >> /resources/templates/validation/v2/ : addForm.html, editForm.html, item.html, items.html
// => /resources/templates/validation/v2/ 하위 4개 파일 모두 URL 경로 변경: validation/v1/ >> validation/v2/ : addForm.html, editForm.html, item.html, items.html
// => cmd + c >> cmd + r >> 변경할 경로 작성 : validation/v2/ >> replace all
// => 상위폴더 커서두고(v2) >> cmd + shift + r (v2폴더 밑으로 4개의 파일 한번에 변경 가능) >> 변경할 경로 작성 : validation/v2/ >> replace all
// => 실행 : http://localhost:8080/validation/v2/items 실행 후 웹 브라우저의 URL이 validation/v2 으로 잘 유지되는지 확인해자.
@Slf4j
@Controller
@RequestMapping("validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    //BindingResult1
    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //BindingResult1
        // => 지금부터 스프링이 제공하는 검증 오류 처리 방법을 알아보자. 여기서 핵심은 BindingResult이다. 우선 코드로 확인해보자.
        // => @Slf4j : 로그 출력을 위해 추가
        // => 주의) BindingResult bindingResult 파라미터의 위치는 @ModelAttribute Item item 다음에 와야 한다.
        //FieldError 객체 (필드 오류)
        // => 생성자 요약 : public FieldError(String objectName, String field, String defaultMessage) {}
        // => 필드에 오류가 있으면 FieldError 객체를 생성해서 bindingResult 에 담아두면 된다.
        // => objectName : @ModelAttribute 이름
        // => field : 오류가 발생한 필드 이름
        // => defaultMessage : 오류 기본 메시지
        //ObjectError 객체 (글로벌 오류)
        // => 생성자 요약
        // => public ObjectError(String objectName, String defaultMessage) {}
        // => 특정 필드를 넘어서는 오류가 있으면 ObjectError 객체를 생성해서 bindingResult 에 담아두면 된다.
        // => objectName : @ModelAttribute 의 이름
        // => defaultMessage : 오류 기본 메시지
        //BindingResult 객체
        // => 스프링이 제공하는 검증 오류를 보관하는 객체이다. 검증 오류가 발생하면 여기에 보관하면 된다.
        // => BindingResult 가 있으면 @ModelAttribute 에 데이터 바인딩 시 오류가 발생해도 컨트롤러가 호출된다!
        // => 예) @ModelAttribute에 바인딩 시 타입 오류가 발생하면?
        // => BindingResult 가 없으면 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동한다.
        // => BindingResult 가 있으면 오류 정보( FieldError )를 BindingResult 에 담아서 컨트롤러를 정상 호출한다.

        //BindingResult에 검증 오류를 적용하는 3가지 방법
        // => 1. @ModelAttribute 의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 FieldError 생성해서 BindingResult 에 넣어준다.
        // => 2. 개발자가 직접 넣어준다.
        // => 3. Validator 사용 - 이것은 뒤에서 설명
        //타입 오류 확인
        // => 숫자가 입력되어야 할 곳에 문자를 입력해서 타입을 다르게 해서 BindingResult 를 호출하고 bindingResult 의 값을 확인해보자.
        // => 주의) BindingResult 는 검증할 대상 바로 다음에 와야한다. 순서가 중요하다. 예를 들어서 @ModelAttribute Item item , 바로 다음에 BindingResult 가 와야 한다.
        // => 주의) BindingResult 는 Model에 자동으로 포함된다.

        //BindingResult와 Errors
        // => org.springframework.validation.Errors
        // => org.springframework.validation.BindingResult
        // => BindingResult 는 인터페이스이고, Errors 인터페이스를 상속받고 있다.
        // => 실제 넘어오는 구현체는 BeanPropertyBindingResult 라는 것인데, 둘다 구현하고 있으므로 BindingResult 대신에 Errors 를 사용해도 된다.
        // => Errors 인터페이스는 단순한 오류 저장과 조회 기능을 제공한다. BindingResult 는 여기에 더해서 추가적인 기능들을 제공한다.
        // => addError() 도 BindingResult 가 제공하므로 여기서는 BindingResult 를 사용하자. 주로 관례상 BindingResult 를 많이 사용한다.

        //정리
        // => BindingResult , FieldError , ObjectError 를 사용해서 오류 메시지를 처리하는 방법을 알아보았다.
        // => 그런데 오류가 발생하는 경우 고객이 입력한 내용이 모두 사라진다. 이 문제를 해결해보자.
        //FieldError, ObjectError
        // => 목표: 사용자 입력 오류 메시지가 화면에 남도록 하자. 예) 가격을 1000원 미만으로 설정시 입력한 값이 남아있어야 한다.
        // => FieldError , ObjectError 에 대해서 더 자세히 알아보자.

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) { //model에 담을 필요없음
            log.info("errors={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //BindingResult2
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //BindingResult2
        //FieldError의 생성자
        // => FieldError 는 두 가지 생성자를 제공한다.
        // => public FieldError(String objectName, String field, String defaultMessage);
        // => public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage);
        //파라미터 목록
        // => objectName : 오류가 발생한 객체 이름
        // => field : 오류 필드
        // => rejectedValue : 사용자가 입력한 값(거절된 값)
        // => bindingFailure : 타입 오류 같은 바인딩 실패인지 검증 실패인지 구분 값
        // => codes : 메시지 코드
        // => arguments : 메시지에서 사용하는 인자
        // => defaultMessage : 기본 오류 메시지
        // => ObjectError도 유사하게 두 가지 생성자를 제공한다. 코드를 참고하자.

        //오류 발생시 사용자 입력 값 유지
        // => new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~1,000,000 까지 허용합니다.") :
        // => 사용자의 입력 데이터가 컨트롤러의 @ModelAttribute 에 바인딩되는 시점에 오류가 발생하면 모델 객체에 사용자 입력 값을 유지하기 어렵다.
        // => 예를 들어서 가격에 숫자가 아닌 문자가 입력된다면 가격은 Integer 타입이므로 문자를 보관할 수 있는 방법이 없다.
        // => 그래서 오류가 발생한 경우 사용자 입력 값을 보관하는 별도의 방법이 필요하다. 그리고 이렇게 보관한 사용자 입력 값을 검증 오류 발생시 화면에 다시 출력하면 된다.
        // => FieldError 는 오류 발생시 사용자 입력 값을 저장하는 기능을 제공한다. 여기서 rejectedValue 가 바로 오류 발생시 사용자 입력 값을 저장하는 필드다.
        // => bindingFailure 는 타입 오류 같은 바인딩이 실패했는지 여부를 적어주면 된다. 여기서는 바인딩이 실패한 것은 아니기 때문에 false 를 사용한다.

        //타임리프의 사용자 입력 값 유지
        // => th:field="*{price}" :
        // => 타임리프의 th:field 는 매우 똑똑하게 동작하는데, 정상 상황에는 모델 객체의 값을 사용하지만, 오류가 발생하면 FieldError 에서 보관한 값을 사용해서 값을 출력한다.

        //스프링의 바인딩 오류 처리
        // => 타입 오류로 바인딩에 실패하면 스프링은 FieldError 를 생성하면서 사용자가 입력한 값을 넣어둔다.
        // => 그리고 해당 오류를 BindingResult 에 담아서 컨트롤러를 호출한다.
        // => 따라서 타입 오류 같은 바인딩 실패시에도 사용자의 오류 메시지를 정상 출력할 수 있다.

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수 입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null ,null, "수량은 최대 9,999 까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item",null ,null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //오류 코드와 메시지 처리1
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //오류 코드와 메시지 처리1
        // => 목표 : 오류 메시지를 체계적으로 다루어보자.
        // => FieldError의 2가지 생성자
        // => public FieldError(String objectName, String field, String defaultMessage);
        // => public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
        // => 파라미터 목록
        // => objectName : 오류가 발생한 객체 이름
        // => field : 오류 필드
        // => rejectedValue : 사용자가 입력한 값(거절된 값)
        // => bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
        // => codes : 메시지 코드
        // => arguments : 메시지에서 사용하는 인자
        // => defaultMessage : 기본 오류 메시지
        // => FieldError , ObjectError 의 생성자는 codes , arguments 를 제공한다. 이것은 오류 발생시 오류 코드로 메시지를 찾기 위해 사용된다.
        //errors 메시지 파일 생성
        // => messages.properties 를 사용해도 되지만, 오류 메시지를 구분하기 쉽게 errors.properties 라는 별도의 파일로 관리해보자.
        // => 먼저 스프링 부트가 해당 메시지 파일을 인식할 수 있게 다음 설정을 추가한다. - application.properties파일 : spring.messages.basename=messages,errors
        // => 이렇게하면 messages.properties , errors.properties 두 파일을 모두 인식한다. (생략하면 messages.properties 를 기본으로 인식한다.)
        // =>  src/main/resources/errors.properties : errors.properties 추가
        // => 참고: errors_en.properties 파일을 생성하면 오류 메시지도 국제화 처리를 할 수 있다. 이제 errors 에 등록한 메시지를 사용하도록 코드를 변경해보자.

        // => codes : required.item.itemName 를 사용해서 메시지 코드를 지정한다.
        // => 메시지 코드는 하나가 아니라 배열로 여러 값을 전달할 수 있는데, 순서대로 매칭해서 처음 매칭되는 메시지가 사용된다.
        // => arguments : Object[]{1000, 1000000} 를 사용해서 코드의 {0} , {1} 로 치환할 값을 전달한다.
        // => 실행해보면 메시지, 국제화에서 학습한 MessageSource 를 찾아서 메시지를 조회하는 것을 확인할 수 있다.

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) { //range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"} ,new Object[]{9999}, null));
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"} ,new Object[]{10000, resultPrice}, null));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //오류 코드와 메시지 처리2
    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //오류 코드와 메시지 처리2
        // => 목표 : FieldError , ObjectError 는 다루기 너무 번거롭다. 오류 코드도 좀 더 자동화 할 수 있지 않을까? 예) item.itemName 처럼?
        // => 컨트롤러에서 BindingResult 는 검증해야 할 객체인 target 바로 다음에 온다. 따라서 BindingResult 는 이미 본인이 검증해야 할 객체인 target 을 알고 있다.
        // => 다음을 컨트롤러에서 실행해보자.
        // => log.info("objectName={}", bindingResult.getObjectName()); //objectName=item //@ModelAttribute name
        // => log.info("target={}", bindingResult.getTarget()); //target=Item(id=null, itemName=상품, price=100, quantity=1234)

        //rejectValue() , reject()
        // => BindingResult 가 제공하는 rejectValue() , reject() 를 사용하면 FieldError , ObjectError 를 직접 생성하지 않고, 깔끔하게 검증 오류를 다룰 수 있다.
        // => rejectValue() , reject() 를 사용해서 기존 코드를 단순화해보자.
        // => 실행하면 오류 메시지가 정상 출력된다. 그런데 errors.properties 에 있는 코드를 직접 입력하지 않았는데 어떻게 된 것일까?

        //rejectValue() void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
        // => field : 오류 필드명
        // => errorCode : 오류 코드(이 오류 코드는 메시지에 등록된 코드가 아니다. 뒤에서 설명할 messageResolver를 위한 오류 코드이다.)
        // => errorArgs : 오류 메시지에서 {0} 을 치환하기 위한 값
        // => defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지

        //bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null)
        //=> 앞에서 BindingResult 는 어떤 객체를 대상으로 검증하는지 target을 이미 알고 있다고 했다.
        //=> 따라서 target(item)에 대한 정보는 없어도 된다. 오류 필드명은 동일하게 price 를 사용했다.
        //축약된 오류 코드
        //=> FieldError() 를 직접 다룰 때는 오류 코드를 range.item.price 와 같이 모두 입력했다.
        //-> 그런데 rejectValue() 를 사용하고 부터는 오류 코드를 range 로 간단하게 입력했다.
        //=> 그래도 오류 메시지를 잘 찾아서 출력한다. 무언가 규칙이 있는 것 처럼 보인다.
        //=> 이부분을 이해하려면 MessageCodesResolver 를 이해해야 한다. 왜 이런식으로 오류 코드를 구성하는지 바로 다음에 자세히 알아보자.
        //=> errors.properties 파일 : range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
        //=> reject() void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
        //=> 앞의 내용과 같다.

        // 정리
        // => 1. rejectValue() 호출
        // => 2. MessageCodesResolver 를 사용해서 검증 오류 코드로 메시지 코드들을 생성
        // => 3. new FieldError() 를 생성하면서 메시지 코드들을 보관
        // => 4. th:erros 에서 메시지 코드들로 메시지를 순서대로 메시지에서 찾고, 노출

        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        // ValidationUtils
        // ValidationUtils 사용 전 : if (!StringUtils.hasText(item.getItemName())) { bindingResult.rejectValue("itemName", "required", "기본: 상품 이름은 필수입니다."); }
        // ValidationUtils 사용 후 : ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
        // => 다음과 같이 한줄로 가능, 제공하는 기능은 Empty , 공백 같은 단순한 기능만 제공
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.rejectValue("itemName", "required");
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 10000000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

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
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
    }

    //Validator 분리1
    //@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //Validator 분리1
        // => 목표 : 복잡한 검증 로직을 별도로 분리하자.
        // => 컨트롤러에서 검증 로직이 차지하는 부분은 매우 크다. 이런 경우 별도의 클래스로 역할을 분리하는 것이 좋다.
        // => 그리고 이렇게 분리한 검증 로직을 재사용 할 수도 있다. ItemValidator 를 만들자.
        // => 스프링은 검증을 체계적으로 제공하기 위해 다음 인터페이스를 제공한다.
        // => public interface Validator { boolean supports(Class<?> clazz); void validate(Object target, Errors errors); }
        // => supports() {} : 해당 검증기를 지원하는 여부 확인(뒤에서 설명)
        // => validate(Object target, Errors errors) : 검증 대상 객체와 BindingResult
        //ItemValidator 직접 호출하기
        // => ItemValidator 를 스프링 빈으로 주입 받아서 직접 호출했다.
        // => 실행해보면 기존과 완전히 동일하게 동작하는 것을 확인할 수 있다. 검증과 관련된 부분이 깔끔하게 분리되었다.

        //Validator 분리2
        // => 스프링이 Validator 인터페이스를 별도로 제공하는 이유는 체계적으로 검증 기능을 도입하기 위해서다.
        // => 그런데 앞에서는 검증기를 직접 불러서 사용했고, 이렇게 사용해도 된다.
        // => 그런데 Validator 인터페이스를 사용해서 검증기를 만들면 스프링의 추가적인 도움을 받을 수 있다.
        //WebDataBinder를 통해서 사용하기
        // => WebDataBinder 는 스프링의 파라미터 바인딩의 역할을 해주고 검증 기능도 내부에 포함한다.
        // => ValidationItemControllerV2에 다음 코드를 추가하자
        // => 이렇게 WebDataBinder 에 검증기를 추가하면 해당 컨트롤러에서는 검증기를 자동으로 적용할 수 있다.
        // => @InitBinder -> 해당 컨트롤러에만 영향을 준다. 글로벌 설정은 별도로 해야한다. (마지막에 설명)

        itemValidator.validate(item, bindingResult);

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    //@Validated 적용
    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //@Validated 적용
        // => validator를 직접 호출하는 부분이 사라지고, 대신에 검증 대상 앞에 @Validated 가 붙었다.
        // => 실행해보면 기존과 동일하게 잘 동작하는 것을 확인할 수 있다.
        //동작 방식
        // => @Validated 는 검증기를 실행하라는 애노테이션이다.
        // => 이 애노테이션이 붙으면 앞서 WebDataBinder 에 등록한 검증기를 찾아서 실행한다.
        // => 그런데 여러 검증기를 등록한다면 그 중에 어떤 검증기가 실행되어야 할지 구분이 필요하다. 이때 supports() 가 사용된다.
        // => 여기서는 supports(Item.class) 호출되고, 결과가 true 이므로 ItemValidator 의 validate() 가 호출된다.
        // =>  @Component public class ItemValidator implements Validator { @Override public boolean supports(Class<?> clazz) { return Item.class.isAssignableFrom(clazz); } @Override public void validate(Object target, Errors errors) {...} }
        //글로벌 설정 - 모든 컨트롤러에 다 적용 - ItemServiceApplication.java

        //참고
        // => 검증시 @Validated @Valid 둘다 사용가능하다.
        // => javax.validation.@Valid 를 사용하려면 build.gradle 의존관계 추가가 필요하다.
        // => implementation 'org.springframework.boot:spring-boot-starter-validation'
        // => @Validated 는 스프링 전용 검증 애노테이션이고, @Valid 는 자바 표준 검증 애노테이션이다.
        // => 자세한 내용은 다음 Bean Validation에서 설명하겠다.

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

