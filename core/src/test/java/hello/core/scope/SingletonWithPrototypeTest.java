package hello.core.scope;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//프로토타입 스코프와 싱글톤 빈과 함께 사용시 Provider로 문제 해결
//정리
// => 그러면 프로토타입 빈을 언제 사용할까? 매번 사용할 때 마다 의존관계 주입이 완료된 새로운 객체가 필요하면 사용하면 된다.
// => 그런데 실무에서 웹 애플리케이션을 개발해보면, 싱글톤 빈으로 대부분의 문제를 해결할 수 있기 때문에 프로토타입 빈을 직접적으로 사용하는 일은 매우 드물다.
// => ObjectProvider , JSR330 Provider 등은 프로토타입 뿐만 아니라 DL이 필요한 경우는 언제든지 사용할 수 있다.
// => 참고로 스프링이 제공하는 메서드에 @Lookup 애노테이션을 사용하는 방법도 있지만, 이전 방법들로 충분하고, 고려해야할 내용도 많아서 생략하겠다.

// 실무에서 자바 표준인 JSR-330 Provider를 사용할 것인가?
// 아니면 스프링이 제공하는 ObjectProvider를 사용할 것인가?
// => ObjectProvider는 DL을 위한 편의 기능을 많이 제공해주고 스프링 외에 별도의 의존관계 추가가 필요 없기 때문에 편리하다.
// => 만약(정말 그럴일은 거의 없겠지만) 코드를 스프링이 아닌 다른 컨테이너에서도 사용할 수 있어야 한다면 JSR-330 Provider를 사용해야한다.
// => 스프링을 사용하다 보면 이 기능 뿐만 아니라 다른 기능들도 자바 표준과 스프링이 제공하는 기능이 겹칠때가 많이 있다.
// => 대부분 스프링이 더 다양하고 편리한 기능을 제공해주기 때문에, 특별히 다른 컨테이너를 사용할 일이 없다면, 스프링이 제공하는 기능을 사용하면 된다.
public class SingletonWithPrototypeTest {

    /**
     * 스프링 컨테이너에 프로토타입 빈 직접 요청1
     *  => 1. 클라이언트A는 스프링 컨테이너에 프로토타입 빈을 요청한다.
     *  => 2. 스프링 컨테이너는 프로토타입 빈을 새로 생성해서 반환(x01)한다. 해당 빈의 count 필드 값은 0이다.
     *  => 3. 클라이언트는 조회한 프로토타입 빈에 addCount() 를 호출하면서 count 필드를 +1 한다.
     *  => 결과적으로 프로토타입 빈(x01)의 count는 1이 된다.
     *
     * 스프링 컨테이너에 프로토타입 빈 직접 요청2
     *  => 1. 클라이언트B는 스프링 컨테이너에 프로토타입 빈을 요청한다.
     *  => 2. 스프링 컨테이너는 프로토타입 빈을 새로 생성해서 반환(x02)한다. 해당 빈의 count 필드 값은 0이다.
     *  => 3. 클라이언트는 조회한 프로토타입 빈에 addCount() 를 호출하면서 count 필드를 +1 한다.
     *  => 결과적으로 프로토타입 빈(x02)의 count는 1이 된다.
     */
    @Test
    @DisplayName("스프링 컨테이너에 프로토타입 빈 직접 요청")
    void prototypeFind() {
        //자동 빈 등록 : PrototypeBean
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("프로토타입 스코프와 싱글톤 빈과 함께 사용시 Provider로 문제 해결")
    void singletonClientUsePrototype() {
        //자동 빈 등록 : ClientBean, PrototypeBean
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic(); //클라이언트A
        assertThat(count1).isEqualTo(1);
        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic(); //클라이언트B
        assertThat(count2).isEqualTo(1);
    }
    //PrototypeBean.init hello.core.scope.SingletonWithPrototypeTest1$PrototypeBean@3da05287
    //PrototypeBean.init hello.core.scope.SingletonWithPrototypeTest1$PrototypeBean@115667d

    /**
     *  프로토타입 스코프와 싱글톤 빈과 함께 사용시 Provider로 문제 해결
     *  => JSR-330 Provider
     *  => : javax.inject.Provider 라는 JSR-330 자바 표준을 사용하는 방법이다.
     *  => 이 방법을 사용하려면 다음 라이브러리를 gradle에 추가해야 한다.
     *  => 스프링부트 3.0 미만 : javax.inject:javax.inject:1 라이브러리를 gradle에 추가
     *  => 스프링부트 3.0 이상 : jakarta.inject:jakarta.inject-api:2.0.1 라이브러리를 gradle에 추가
     *
     *  => 실행해보면 provider.get() 을 통해서 항상 새로운 프로토타입 빈이 생성되는 것을 확인할 수 있다.
     *  => provider 의 get() 을 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다. (DL)
     *  => 자바 표준이고, 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기는 훨씬 쉬워진다.
     *  => Provider 는 지금 딱 필요한 DL 정도의 기능만 제공한다.
     *
     * 특징
     *  => get() 메서드 하나로 기능이 매우 단순하다.
     *  => 별도의 라이브러리가 필요하다.
     *  => 자바 표준이므로 스프링이 아닌 다른 컨테이너에서도 사용할 수 있다.
     */
    //싱글톤 빈
    @Scope("singleton")
    static class ClientBean {
        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
    }

    //프로토타입 빈
    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }
        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }
        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }


}
