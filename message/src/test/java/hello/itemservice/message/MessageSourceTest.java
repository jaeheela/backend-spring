package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MessageSourceTest {

    // => MessageSource 인터페이스 : MessageSource 인터페이스를 보면 코드를 포함한 일부 파라미터로 메시지를 읽어오는 기능을 제공한다.
    // => public interface MessageSource {
    // =>       String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale);
    // =>       String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException;
    // => }
    @Autowired
    MessageSource ms;

    //가장 단순한 테스트
    //ms.getMessage("hello", null, null)
    // => code: hello, args: null,locale: null
    // => 가장 단순한 테스트는 메시지 코드로 hello 를 입력하고 나머지 값은 null 을 입력했다.
    // => locale 정보가 없으면 basename 에서 설정한 기본 이름 메시지 파일을 조회한다.
    // => basename 으로 messages 를 지정 했으므로 messages.properties 파일에서 데이터 조회한다.
    @Test
    void helloMessage() {
        String result = ms.getMessage("hello", null, null);
        assertThat(result).isEqualTo("안녕");
    }

    //메시지가 없는 경우
    // => 메시지가 없는 경우에는 NoSuchMessageException 이 발생한다.
    @Test
    void notFoundMessageCode() {
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    //기본 메시지
    // => 메시지가 없어도 기본 메시지( defaultMessage )를 사용하면 기본 메시지가 반환된다.
    @Test
    void notFoundMessageCodeDefaultMessage() {
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
    }

    //매개변수 사용
    // => 다음 메시지의 {0} 부분은 매개변수를 전달해서 치환할 수 있다.
    // => hello.name=안녕 {0} >> Spring 단어를 매개변수로 전달 >> 안녕 Spring
    @Test
    void argumentMessage() {
        String message = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertThat(message).isEqualTo("안녕 Spring");
    }

    //국제화 파일 선택1
    // => locale 정보를 기반으로 국제화 파일을 선택한다.
    // => Locale이 en_US 의 경우 messages_en_US messages_en messages 순서로 찾는다.
    // => Locale 에 맞추어 구체적인 것이 있으면 구체적인 것을 찾고, 없으면 디폴트를 찾는다고 이해하면 된다.
    // => ms.getMessage("hello", null, null) : locale 정보가 없으므로 messages 를 사용
    // => ms.getMessage("hello", null, Locale.KOREA) : locale 정보가 있지만, message_ko 가 없으므로 messages 를 사용
    // => 만약 Locale 정보가 없는 경우 Locale.getDefault() 을 호출해서 시스템의 기본 로케일을 사용한다.
    // => 예) locale = null 인 경우 >> 시스템 기본 locale 이 ko_KR 이므로 messages_ko.properties 조회 >> 시도 >> 조회 실패 >> messages.properties 조회
    @Test
    void defaultLang() {
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
    }

    //국제화 파일 선택2
    // => ms.getMessage("hello", null, Locale.ENGLISH) : locale 정보가 Locale.ENGLISH 이므로 messages_en 을 찾아서 사용
    @Test
    void enLang() {
        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }
}
