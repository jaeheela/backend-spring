package hello.itemservice.validation;

import org.junit.jupiter.api.Test;
import org.springframework.lang.Nullable;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.FieldError;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

import static org.assertj.core.api.Assertions.*;

public class MessageCodesResolverTest {
    //MessageCodesResolver 인터페이스
    // => 검증 오류 코드로 메시지 코드들을 생성하는 인터페이스 (기본 구현체 - DefaultMessageCodesResolver)
    // => 주로 ObjectError , FieldError와 함께 사용
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    /**
     * ObjectError
     */
    @Test
    void messageCodesResolverObject() {

        //DefaultMessageCodesResolver의 기본 메시지 생성 규칙
        // => 객체 오류의 경우 다음 순서로 2가지 생성
        // => 예) error code : required, object name : item
        // => 1.: required.item : errorCode.objectName
        // => 2.: required : errorCode
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        /*
        // iter + TAB : 향상된 for문 단축키
        // sout + TAB : System.out.println 단축키
        for(String messageCode : messageCodes){
            System.out.println("messageCode = "+messageCode);
        }
        */
        //messageCode = required.item - 디테일이 1st
        //messageCode = required  - 범용적인것이 2nd
        assertThat(messageCodes).containsExactly("required.item", "required");

        // reject("totalPriceMin")
        // => required.item
        // => required
        new ObjectError("item", messageCodes, null, null);

    }

    /**
     * FieldError
     */
    @Test
    void messageCodesResolverField() {

        //DefaultMessageCodesResolver의 기본 메시지 생성 규칙
        // => 필드 오류의 경우 다음 순서로4가지 메시지 코드 생성
        // => 예) error code: typeMismatch, object name : "user", field : "age", field type : int
        // => 1. "typeMismatch.user.age" : errorCode.objectName.field.fieldType
        // => 2. "typeMismatch.age" : errorCode.objectName.field
        // => 3. "typeMismatch.int" : errorCode.objectName
        // => 4. "typeMismatch" : errorCode
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        for(String messageCode : messageCodes){
            System.out.println("messageCode = "+messageCode);
        }
        //messageCode = required.item.itemName
        //messageCode = required.itemName
        //messageCode = required.java.lang.String
        //messageCode = required
        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );

        //rejectValue("itemName", "required")
        // => required.item.itemName
        // => required.itemName
        // => required.java.lang.String
        // => required
        new FieldError("item","itemName",null,false,messageCodes,null,null);
    }

}

//동작 방식
// => rejectValue() , reject() 는 내부에서 MessageCodesResolver 를 사용한다. 여기에서 메시지 코드들을 생성한다.
// => FieldError , ObjectError 의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있다.
// => MessageCodesResolver 를 통해서 생성된 순서대로 오류 코드를 보관한다.
// => 이 부분을 BindingResult 의 로그를 통해서 확인해보자.
// => codes [range.item.price, range.price, range.java.lang.Integer, range]

//오류 메시지 출력
// => 타임리프 화면을 렌더링 할 때 th:errors 가 실행된다.
// => 만약 이때 오류가 있다면 생성된 오류 메시지 코드를 순서대로 돌아가면서 메시지를 찾는다. 그리고 없으면 디폴트 메시지를 출력한다.