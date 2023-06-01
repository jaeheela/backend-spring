package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutoWiredTest {

    /**
     * 옵션 처리
     * => 주입할 스프링 빈이 없어도 동작해야 할 때가 있다.
     * => 그런데 @Autowired 만 사용하면 required 옵션의 기본값이 true 로 되어 있어서 자동 주입 대상이 없으면 오류가 발생한다.
     *
     *  자동 주입 대상을 옵션으로 처리하는 방법
     *   => 1. @Autowired(required=false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
     *   => 2. org.springframework.lang.@Nullable : 자동 주입할 대상이 없으면 null이 입력됨
     *   => 3. Optional<> : 자동 주입할 대상이 없으면 Optional.empty 가 입력됨
     */
    @Test
    void AutoWiredTest() {
        //스프링 빈으로 등록
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }


    /**
     * => Member는 스프링 빈이 아니다.
     * => setNoBean1() 은 @Autowired(required=false) 이므로 호출 자체가 안된다.
     * => 참고로 @Nullable, Optional은 스프링 전반에 걸쳐서 지원된다.
     * => 예를 들어서 생성자 자동 주입에서 특정 필드에만 사용해도 된다.
     */
    static class TestBean {

        //호출 안됨
        @Autowired(required = false)
        public void setNoBean1(Member noBean1) { //Member는 스프링 빈이 아님
            System.out.println("setNoBean1 = " + noBean1); //출력안됨
        }

        //null 호출
        @Autowired
        public void setNoBean2(@Nullable Member noBean2) { //Member는 스프링 빈이 아님
            System.out.println("setNoBean2 = " + noBean2); //setNoBean2 = null
        }

        //Optional.empty 호출
        @Autowired(required = false)
        public void setNoBean3(Optional<Member> noBean3) { //Member는 스프링 빈이 아님
            System.out.println("setNoBean3 = " + noBean3); //setNoBean3 = Optional.empty
        }
    }

}