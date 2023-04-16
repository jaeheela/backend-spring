package hello.servlet.basic;

import lombok.Getter;
import lombok.Setter;

// JSON 형식의 파싱을 하기 위한 객체 - lombok 이용(@Getter , @Setter)
@Getter @Setter
public class HelloData {

    private String username;
    private int age;

}
