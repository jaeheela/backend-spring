package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class StatefulServiceTest {

    //테스트를 위한 스프링 빈 등록
    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

    /**
     * 상태를 유지할 경우 발생하는 문제점
     * <p>
     * => 최대한 단순히 설명하기 위해, 실제 쓰레드는 사용하지 않았다.
     * => ThreadA가 사용자A 코드를 호출하고 ThreadB가 사용자B 코드를 호출한다 가정하자.
     * => StatefulService 의 price 필드는 공유되는 필드인데, 특정 클라이언트가 값을 변경한다.
     * => 사용자A의 주문금액은 10000원이 되어야 하는데, 20000원이라는 결과가 나왔다.
     * => 실무에서 이런 경우를 종종 보는데, 이로인해 정말 해결하기 어려운 큰 문제들이 터진다.(몇년에 한번씩 꼭 만난다.)
     * <p>
     * => 진짜 공유필드는 조심해야 한다! 스프링 빈은 "항상 무상태(stateless)"로 설계하자.
     */
    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);

        //ThreadA: A사용자 10000원 주문
        statefulService1.order("userA", 10000);  //name = userA price = 10000

        //ThreadB: B사용자 20000원 주문
        statefulService2.order("userB", 20000); //name = userB price = 20000

        //ThreadA: 사용자A 주문 금액 조회
        int price = statefulService1.getPrice();

        //ThreadA: 사용자A는 10000원을 기대했지만, 기대와 다르게 20000원 출력 (같은 객체라 바꿔치기됨)
        System.out.println("price = " + price); //price = 20000

        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    /*
    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);

        //ThreadA: A사용자 10000원 주문
        int userA = statefulService1.order("userA", 10000);//name = userA price = 10000

        //ThreadB: B사용자 20000원 주문
        int userB = statefulService2.order("userB", 20000);//name = userB price = 20000

        System.out.println("userA = " + userA); //userA = 10000
        System.out.println("userB = " + userB); //userB = 20000

    }
    */


}
