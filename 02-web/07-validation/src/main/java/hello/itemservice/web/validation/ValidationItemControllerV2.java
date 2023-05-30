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
import org.springframework.validation.ValidationUtils;
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
// => cmd + r >> 변경할 경로 작성 : validation/v2/ >> replace all
// => 상위폴더 커서두고(v2) >> cmd + shift + r (v2폴더 밑으로 4개의 파일 한번에 변경 가능) >> 변경할 경로 작성 : validation/v2/ >> replace all
// => 실행 : http://localhost:8080/validation/v2/items 실행 후 웹 브라우저의 URL이 validation/v2 으로 잘 유지되는지 확인해자.


//BindingResult에 검증 오류를 적용하는 3가지 방법
// => 1. @ModelAttribute 의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 FieldError 생성해서 BindingResult 에 넣어준다.
// => 2. 개발자가 직접 넣어준다. - 메소드 내 검증 로직
// => 3. Validator 사용

//BindingResult객체 VS Errors객체
// => org.springframework.validation.BindingResult VS org.springframework.validation.Errors
// => BindingResult 는 인터페이스이고, Errors 인터페이스를 상속받고 있다.
// => 실제 넘어오는 구현체는 BeanPropertyBindingResult 라는 것인데, 둘다 구현하고 있으므로 BindingResult 대신에 Errors 를 사용해도 된다.
// => Errors 인터페이스는 단순한 오류 저장과 조회 기능을 제공한다. BindingResult 는 여기에 더해서 추가적인 기능들을 제공한다.
// => addError() 도 BindingResult 가 제공하므로 여기서는 BindingResult 를 사용하자.
// => 주로 관례상 BindingResult 를 많이 사용한다.


@Slf4j //로그 출력을 위해 추가
@Controller
@RequestMapping("validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    //방법1. ItemValidator 직접 호출 - 스프링 빈으로 주입 받음
    private final ItemValidator itemValidator;

    // http://localhost:8080/validation/v2/items
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    // http://localhost:8080/validation/v2/items/{itemId}
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    // http://localhost:8080/validation/v2/items/{itemId}/edit
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

    // http://localhost:8080/validation/v2/items/add
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }


    /**
     * BindingResult1 - BindingResult , FieldError , ObjectError 를 사용해서 오류 메시지를 처리하는 방법1
     * BindingResult : 오류 정보( FieldError )를 BindingResult 에 담아서 컨트롤러를 정상 호출
     * 문제점 - 오류 발생시 사용자 입력 값 모두 사라짐
     */
    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //BindingResult 객체
        // => 스프링이 제공하는 검증 오류를 보관하는 객체이다. 검증 오류가 발생하면 여기에 보관하면 된다.
        // => BindingResult 가 있으면 @ModelAttribute 에 데이터 바인딩 시 오류가 발생해도 컨트롤러가 호출된다!
        // => @ModelAttribute에 바인딩 시 타입 오류가 발생하면?
        // => : BindingResult 가 없으면 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동한다.
        // => : BindingResult 가 있으면 오류 정보( FieldError )를 BindingResult 에 담아서 컨트롤러를 정상 호출한다.
        // => 주의!!! bindingResult 파라미터의 위치는 @ModelAttribute Item item 다음에 와야 한다.
        // => 주의!!! BindingResult 는 Model에 자동으로 포함된다.

        //step1. 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //FieldError 객체 (필드 오류)
            // => 필드에 오류가 있으면 FieldError 객체를 생성해서 bindingResult 에 담아두면 된다. (ObjectError의 자식임)
            // => FieldError(String objectName, String field, String defaultMessage)
            // => : objectName - @ModelAttribute 이름, field - 오류가 발생한 필드 이름, defaultMessage - 오류 기본 메시지
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
        }

        //step2. 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                //ObjectError 객체 (글로벌 오류)
                // => 특정 필드를 넘어서는 오류가 있으면 ObjectError 객체를 생성해서 bindingResult 에 담아두면 된다.
                // => ObjectError(String objectName, String defaultMessage)
                // => : objectName - @ModelAttribute의 이름, defaultMessage - 오류 기본 메시지
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        //step3. 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) { //model에 담을 필요없음
            log.info("errors={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //step4. 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     * BindingResult2 - BindingResult , FieldError , ObjectError 를 사용해서 오류 메시지를 처리하는 방법2
     * 오류 발생시 사용자 입력 값 유지
     */
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //FieldError 객체 (필드 오류)
            // => public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
            // => : objectName - 오류가 발생한 객체 이름, field - 오류가 발생한 필드 이름, "rejectedValue" - 사용자가 입력한 값(거절된 값)
            // => defaultMessage - 오류 기본 메시지, bindingFailure - 타입 오류 같은 바인딩 실패인지 검증 실패인지 구분 값
            // => codes - 메시지 코드, arguments - 메시지에서 사용하는 인자, defaultMessage - 기본 오류 메시지
            // rejectedValue(스프링의 바인딩 오류 처리)
            // => 타입 오류로 바인딩에 실패하면 스프링은 FieldError 를 생성하면서 사용자가 입력한 값을 rejectedValue에 넣어둔다.그리고 해당 오류를 BindingResult 에 담아서 컨트롤러를 호출한다.
            // => 따라서 타입 오류 같은 바인딩 실패시에도 사용자의 오류 메시지를 정상 출력할 수 있다.
            // => bindingFailure 는 타입 오류 같은 바인딩이 실패했는지 여부를 적어주면 된다 여기서는 바인딩이 실패한 것은 아니기 때문에 false 를 사용한다.
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

    /**
     * 오류 코드와 메시지 처리1 -오류 메시지를 체계적으로 다루기
     * FieldError , ObjectError
     */
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //FieldError 객체 (필드 오류)
        // => public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
        // => : objectName - 오류가 발생한 객체 이름, field - 오류가 발생한 필드 이름, rejectedValue - 사용자가 입력한 값(거절된 값)
        // => defaultMessage - 오류 기본 메시지, bindingFailure - 타입 오류 같은 바인딩 실패인지 검증 실패인지 구분 값
        // => "codes" - 메시지 코드, "arguments" - 메시지에서 사용하는 인자, defaultMessage - 기본 오류 메시지
        // codes, arguments
        // => 오류 발생시 오류 코드로 메시지를 찾기 위해 사용된다.
        // => 1. errors.properties 파일 생성 (errors 메시지 파일 - 위치 : src/main/resources/errors.properties
        // => messages.properties 를 사용해도 되지만, 오류 메시지를 구분하기 쉽게 해보자.
        // => 2. 스프링 부트가 해당 메시지 파일을 인식할 수 있게 다음 설정 추가
        // => application.properties파일 : spring.messages.basename=messages,errors
        // => 이렇게하면 messages.properties , errors.properties 두 파일을 모두 인식한다. (생략하면 messages.properties 를 기본으로 인식한다.)
        // => 참고로 errors_en.properties 파일을 생성하면 오류 메시지도 국제화 처리를 할 수 있다. 이제 errors 에 등록한 메시지를 사용하도록 코드를 변경해보자.
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

    /**
     * 오류 코드와 메시지 처리2 - 오류 코드 자동화하기
     * 오류 코드와 메시지 처리3,4,5,6
     * FieldError , ObjectError 대신
     * BindingResult.rejectValue() , BindingResult.reject() 사용해 기존 코드를 단순화 가능
     *
     * 순서
     *  => 1. rejectValue() 호출
     *  => 2. MessageCodesResolver 를 사용해서 검증 오류 코드로 메시지 코드들을 생성 (String errorCode)
     *  => 3. new FieldError() 를 생성하면서 메시지 코드들을 보관
     *  => 4. th:erros 에서 메시지 코드들로 메시지를 순서대로 메시지에서 찾고, 노출
     */
    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //BindingResult 는 이미 본인이 검증해야 할 객체인 target 을 알고 있음
        log.info("objectName={}", bindingResult.getObjectName()); //objectName=item //@ModelAttribute name
        log.info("target={}", bindingResult.getTarget()); //target=Item(id=null, itemName=상품, price=100, quantity=1234)

        // 오류 코드와 메시지 처리 6
        // 지금까지 학습한 메시지 코드 전략의 강점 확인하기
        // => price 필드에 문자 "A"를 입력 >> 로그 확인 : BindingResult 에 FieldError 가 담겨있고, 다음과 같은 메시지 코드들이 생성됨 : codes[typeMismatch.item.price,typeMismatch.price,typeMismatch.java.lang.Integer,typeMismatch]
        // 스프링은 타입 오류가 발생하면 typeMismatch 라는 오류 코드를 사용함 - 4가지 메시지 코드
        // => typeMismatch.item.price
        // => typeMismatch.price
        // => typeMismatch.java.lang.Integer
        // => typeMismatch
        // => 이 오류 코드가 MessageCodesResolver 를 통하면서 4가지 메시지 코드가 생성된 것이다. 실행해보자. 아직 errors.properties 에 메시지 코드가 없기 때문에 스프링이 생성한 기본 메시지가 출력된다. Failed to convert property value of type java.lang.String to required type java.lang.Integer for property price; nested exception is java.lang.NumberFormatException: For input string: "A"
        // => error.properties 에 다음 내용을 추가하자 : typeMismatch.java.lang.Integer=숫자를 입력해주세요. typeMismatch=타입 오류입니다.
        // => 다시 실행해보자. 결과적으로 소스코드를 하나도 건들지 않고, 원하는 메시지를 단계별로 설정할 수 있다.
        // => 정리: 메시지 코드 생성 전략은 그냥 만들어진 것이 아니다. 조금 뒤에서 Bean Validation을 학습하면 그 진가를 더 확인할 수 있다.


        /*
        // ValidationUtils 사용하면 if문 없이 한줄로 가능 - 아래와 동일한 코드
        // => 제공하는 기능은 Empty , 공백 같은 단순한 기능만 제공
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
        */
        if (!StringUtils.hasText(item.getItemName())) {
            //BindingResult.rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
            // => field : 오류 필드명
            // => errorCode : 오류 코드(주의!! 메시지에 등록된 코드가 아닌 messageResolver를 위한 오류 코드)
            // => errorArgs : 오류 메시지에서 {0} 을 치환하기 위한 값
            // => defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지
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
                //BindingResult.reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage)
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


    //방법2.
    @InitBinder // 해당 컨트롤러에만 영향줌 - 컨트롤러 요청올 때마다 항상 새로 dataBinder 생성되어 검증기를 넣어둠
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
        //dataBinder.addValidators(UserValidator);
    }

    /**
     * Validator 분리 - 복잡한 검증 로직을 별도로 분리
     * 컨트롤러에서 검증 로직이 차지하는 부분은 매우 크면 별도의 클래스로 역할을 분리하는 것이 좋음, 재사용도 가능 (ItemValidator.java)
     *
     * 방법 1 - @RequiredArgsConstructor
     * => 스프링 빈으로 주입 받음 @RequiredArgsConstructor
     * => ItemValidator 직접 호출
     *
     * 방법 2 - @InitBinder
     * => WebDataBinder를 통해서 사용
     * => 스프링이 Validator 인터페이스를 별도로 제공하는 이유는 체계적으로 검증 기능을 도입하기 위해서다.그런데 앞에서는 검증기를 직접 불러서 사용했고, 이렇게 사용해도 된다.
     * => 그런데 Validator 인터페이스를 사용해서 검증기를 만들면 스프링의 추가적인 도움을 받을 수 있다.
     * => WebDataBinder 는 스프링의 파라미터 바인딩의 역할을 해주고 검증 기능도 내부에 포함한다.
     * => WebDataBinder 에 검증기를 추가하면 해당 컨트롤러에서는 검증기를 자동으로 적용할 수 있다.
     * => @InitBinder
     * => : 해당 컨트롤러에만 영향을 준다
     * => : 글로벌 설정도 가능하다 - ItemServiceApplication.java에 추가 가능
     */
    //@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

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


    /**
     * @Validated 적용
     * => validator를 직접 호출하는 부분이 사라지고, 대신에 검증 대상 앞에 @Validated 가 붙었다. 실행해보면 기존과 동일하게 잘 동작하는 것을 확인할 수 있다.
     *
     * 동작 방식
     * => @Validated : 검증기를 실행하라는 애노테이션
     * => 이 애노테이션이 붙으면 앞서 WebDataBinder 에 등록한 검증기를 찾아서 실행함
     */
    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
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


    //참고
    // => 검증시 @Validated @Valid 둘다 사용가능하다.
    // => javax.validation.@Valid 를 사용하려면 build.gradle 의존관계 추가가 필요하다.
    // => implementation 'org.springframework.boot:spring-boot-starter-validation'
    // => @Validated 는 스프링 전용 검증 애노테이션이고, @Valid 는 자바 표준 검증 애노테이션이다.
    // => 자세한 내용은 다음 Bean Validation에서 설명하겠다.

}

