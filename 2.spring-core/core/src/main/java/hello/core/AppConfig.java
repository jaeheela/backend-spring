package hello.core;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//AppConfig는 애플리케이션의 실제 동작에 필요한 구현 객체를 생성한다.
// => MemberServiceImpl , MemoryMemberRepository , OrderServiceImpl , FixDiscountPolicy

//AppConfig는 생성한 객체 인스턴스의 참조(레퍼런스)를 생성자를 통해서 주입(연결)해준다.
// => MemberServiceImpl -> MemoryMemberRepository
// => OrderServiceImpl -> MemoryMemberRepository , FixDiscountPolicy
// => 참고로 지금은 각 클래스에 생성자가 없어서 컴파일 오류가 발생한다. 바로 다음에 코드에서 생성자를 만든다.

//스프링 컨테이너
// => ApplicationContext 를 스프링 컨테이너라 한다.
// => 기존에는 개발자가 AppConfig 를 사용해서 직접 객체를 생성하고 DI를 했지만, 이제부터는 스프링 컨테이너를 통해서 사용한다.
// => 스프링 컨테이너는 @Configuration 이 붙은 AppConfig 를 설정(구성) 정보로 사용한다.
// => 여기서 @Bean 이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다. 이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라 한다.
// => 스프링 빈은 @Bean 이 붙은 메서드의 명을 스프링 빈의 이름으로 사용한다. ( memberService , orderService )
// => 이전에는 개발자가 필요한 객체를 AppConfig 를 사용해서 직접 조회했지만, 이제부터는 스프링 컨테이너를 통해서 필요한 스프링 빈(객체)를 찾아야 한다.
// => 스프링 빈은 applicationContext.getBean() 메서드를 사용해서 찾을 수 있다.
// => 기존에는 개발자가 직접 자바코드로 모든 것을 했다면 이제부터는 스프링 컨테이너에 객체를 스프링 빈으로 등록하고, 스프링 컨테이너에서 스프링 빈을 찾아서 사용하도록 변경되었다.
// => 코드가 약간 더 복잡해진 것 같은데, 스프링 컨테이너를 사용하면 어떤 장점이 있을까?

//스프링 컨테이너 생성법
// => 1. 스프링 컨테이너 생성 : ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
// => ApplicationContext를 스프링 컨테이너라 한다. ApplicationContext는 인터페이스이다.
// => 스프링 컨테이너는 XML을 기반으로 만들 수 있고, 애노테이션 기반의 자바 설정 클래스로 만들 수 있다. 직전에 AppConfig를 사용했던 방식이 애노테이션 기반의 자바 설정 클래스로 스프링 컨테이너를 만든 것이다.
// => 자바 설정 클래스를 기반으로 스프링 컨테이너(ApplicationContext)를 만들어보자. : new AnnotationConfigApplicationContext(AppConfig.class); 이 클래스는 ApplicationContext 인터페이스의 구현체이다.
// => 참고로 더 정확히는 스프링 컨테이너를 부를 때 BeanFactory , ApplicationContext로 구분해서 이야기한다. 이 부분은 뒤에서 설명하겠다.
// => BeanFactory 를 직접 사용하는 경우는 거의 없으므로 일반적으로 ApplicationContext를 스프링 컨테이너라 한다

//스프링 컨테이너의 생성 과정
//1. 스프링 컨테이너 생성
// => new AnnotationConfigApplicationContext(AppConfig.class) : 스프링 컨테이너를 생성할 때는 구성 정보를 지정해주어야 한다. 여기서는 AppConfig.class 를 구성 정보로 지정했다.
//2. 스프링 빈 등록
// => 스프링 컨테이너는 파라미터로 넘어온 설정 클래스 정보를 사용해서 스프링 빈을 등록한다.
// => 빈 이름은 메서드 이름을 사용한다. 빈 이름을 직접 부여할 수도 있다. : @Bean(name="memberService2")
// => 주의) 빈 이름은 항상 다른 이름을 부여해야 한다. 같은 이름을 부여하면, 다른 빈이 무시되거나, 기존 빈을 덮어버리거나 설정에 따라 오류가 발생한다.
//3. 스프링 빈 의존관계 설정 - 준비
//4. 스프링 빈 의존관계 설정 - 완료
// => 스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입(DI)한다. 단순히 자바 코드를 호출하는 것 같지만, 차이가 있다. 이 차이는 뒤에 싱글톤 컨테이너에서 설명한다.
// => 참고로 스프링은 빈을 생성하고, 의존관계를 주입하는 단계가 나누어져 있다. 그런데 이렇게 자바 코드로 스프링 빈을 등록하면 생성자를 호출하면서 의존관계 주입도 한번에 처리된다.
// => 여기서는 이해를 돕기 위해 개념적으로 나누어 설명했다. 자세한 내용은 의존관계 자동 주입에서 다시 설명하겠다.
// => 정리) 스프링 컨테이너를 생성하고, 설정(구성) 정보를 참고해서 스프링 빈도 등록하고, 의존관계도 설정했다. 이제 스프링 컨테이너에서 데이터를 조회해보자.
// => 컨테이너에 등록된 모든 빈 조회 : 스프링 컨테이너에 실제 스프링 빈들이 잘 등록 되었는지 확인해보자. - hello.core.beanfind.ApplicationContextInfoTest.java

//스프링 컨테이너 (BeanFactory와 ApplicationContext)
// => BeanFactory : 스프링 컨테이너의 최상위 인터페이스다. 스프링 빈을 관리하고 조회하는 역할을 담당한다. getBean() 을 제공한다. 지금까지 우리가 사용했던 대부분의 기능은 BeanFactory가 제공하는 기능이다.
// => ApplicationContext : BeanFactory 기능을 모두 상속받아서 제공한다. 빈을 관리하고 검색하는 기능을 BeanFactory가 제공해주는데, 그러면 둘의 차이가 뭘까? 애플리케이션을 개발할 때는 빈을 관리하고 조회하는 기능은 물론이고, 수 많은 부가기능이 필요하다.
// => ApplicatonContext가 제공하는 부가기능
// => 메시지소스를 활용한 국제화 기능 : 예를 들어서 한국에서 들어오면 한국어로, 영어권에서 들어오면 영어로 출력
// => 환경변수 : 로컬, 개발, 운영등을 구분해서 처리
// => 애플리케이션 이벤트 : 이벤트를 발행하고 구독하는 모델을 편리하게 지원
// => 편리한 리소스 조회 : 파일, 클래스패스, 외부 등에서 리소스를 편리하게 조회
// => 정리하자명 ApplicationContext는 BeanFactory의 기능을 상속받는다. >> ApplicationContext는 빈 관리기능 + 편리한 부가 기능을 제공한다. >> BeanFactory를 직접 사용할 일은 거의 없다. 부가기능이 포함된 ApplicationContext를 사용한다. >> BeanFactory나 ApplicationContext를 스프링 컨테이너라 한다.

//스프링 컨테이너의 다양한 설정 형식 지원
// => 스프링 컨테이너는 다양한 형식의 설정 정보를 받아드릴 수 있게 유연하게 설계되어 있다. - 자바 코드, XML, Groovy 등등
//1.애노테이션 기반 자바 코드 설정 사용
// => 지금까지 했던 것이다. new AnnotationConfigApplicationContext(AppConfig.class) AnnotationConfigApplicationContext 클래스를 사용하면서 자바 코드로된 설정 정보를 넘기면 된다.
//2.XML 설정 사용
// => 최근에는 스프링 부트를 많이 사용하면서 XML기반의 설정은 잘 사용하지 않는다. 아직 많은 레거시 프로젝트 들이 XML로 되어 있고, 또 XML을 사용하면 컴파일 없이 빈 설정 정보를 변경할 수 있는 장점도 있으므로 한번쯤 배워두는 것도 괜찮다.
// => GenericXmlApplicationContext 를 사용하면서 xml 설정 파일을 넘기면 된다. - appConfing.xml , XmlAppContext.java
// => xml 기반의 appConfig.xml 스프링 설정 정보와 자바 코드로 된 AppConfig.java 설정 정보를 비교해보면 거의 비슷하다는 것을 알 수 있다.
// => xml 기반으로 설정하는 것은 최근에 잘 사용하지 않으므로 이정도로 마무리 하고, 필요하면 스프링 공식 레퍼런스 문서를 확인하자. : https://spring.io/projects/spring-framework

//스프링 빈 설정 메타 정보 - BeanDefinition
// => 스프링은 어떻게 이런 다양한 설정 형식을 지원하는 것일까? 그 중심에는 BeanDefinition 이라는 추상화가 있다. 쉽게 이야기해서 역할과 구현을 개념적으로 나눈 것이다!
// => XML을 읽어서 BeanDefinition을 만들면 된다. 자바 코드를 읽어서 BeanDefinition을 만들면 된다.
// => 스프링 컨테이너는 자바 코드인지, XML인지 몰라도 된다. 오직 BeanDefinition만 알면 된다. BeanDefinition을 빈 설정 메타정보라 한다. @Bean , <bean> 당 각각 하나씩 메타 정보가 생성된다. 스프링 컨테이너는 이 메타정보를 기반으로 스프링 빈을 생성한다.
// => 코드 레벨로 조금 더 깊이 있게 들어가보자.
// => 1. AnnotationConfigApplicationContext 는 AnnotatedBeanDefinitionReader 를 사용해서 AppConfig.class 를 읽고 BeanDefinition 을 생성한다.
// => 2. GenericXmlApplicationContext 는 XmlBeanDefinitionReader 를 사용해서 appConfig.xml 설정 정보를 읽고 BeanDefinition 을 생성한다.
//=> 3. 새로운 형식의 설정 정보가 추가되면, XxxBeanDefinitionReader를 만들어서 BeanDefinition 을 생성하면 된다.
//BeanDefinition 정보
// => BeanClassName: 생성할 빈의 클래스 명(자바 설정 처럼 팩토리 역할의 빈을 사용하면 없음)
// => factoryBeanName: 팩토리 역할의 빈을 사용할 경우 이름, 예) appConfig
// => factoryMethodName: 빈을 생성할 팩토리 메서드 지정, 예) memberService
// => Scope: 싱글톤(기본값)
// => lazyInit: 스프링 컨테이너를 생성할 때 빈을 생성하는 것이 아니라, 실제 빈을 사용할 때 까지 최대한 생성을 지연처리 하는지 여부
// => InitMethodName: 빈을 생성하고, 의존관계를 적용한 뒤에 호출되는 초기화 메서드 명
// => DestroyMethodName: 빈의 생명주기가 끝나서 제거하기 직전에 호출되는 메서드 명
// => Constructor arguments, Properties: 의존관계 주입에서 사용한다. (자바 설정 처럼 팩토리 역할의 빈을 사용하면 없음)
//정리
// => BeanDefinition을 직접 생성해서 스프링 컨테이너에 등록할 수 도 있다. 하지만 실무에서 BeanDefinition을 직접 정의하거나 사용할 일은 거의 없다. 어려우면 그냥 넘어가면 된다^^!
// => BeanDefinition에 대해서는 너무 깊이있게 이해하기 보다는, 스프링이 다양한 형태의 설정 정보를 BeanDefinition으로 추상화해서 사용하는 것 정도만 이해하면 된다.
// => 가끔 스프링 코드나 스프링 관련 오픈 소스의 코드를 볼 때, BeanDefinition 이라는 것이 보일 때가 있다. 이때 이러한 메커니즘을 떠올리면 된다.


//스프링 기반으로 변경 - 스프링 컨테이너로 만듦
@Configuration
public class AppConfig {
//public class AppConfig {

    //1.
    //AppConfig 리팩터링 전 : 현재 AppConfig를 보면 중복이 있고, 역할에 따른 구현이 잘 안보인다. 중복을 제거하고, 역할에 따른 구현이 보이도록 리팩터링 하자.
    /*
    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
    */

    //2.
    /*
    //AppConfig 리팩터링 후 : new MemoryMemberRepository() 이 부분이 중복 제거되었다. 이제 MemoryMemberRepository 를 다른 구현체로 변경할 때 한 부분만 변경하면 된다.
    // => AppConfig 를 보면 역할과 구현 클래스가 한눈에 들어온다. 애플리케이션 전체 구성이 어떻게 되어있는지 빠르게 파악할 수 있다.
    public MemberService memberService() {

        return new MemberServiceImpl(memberRepository());
    }
    public OrderService orderService() {

        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public DiscountPolicy discountPolicy() {
        //return new FixDiscountPolicy();
        return new RateDiscountPolicy(); //정책 역할을 담당하는 구현을 FixDiscountPolicy 객체로 변경했다.

    }
    */

    //3. 스프링 기반으로 변경
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(
                memberRepository(),
                discountPolicy());
    }
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
