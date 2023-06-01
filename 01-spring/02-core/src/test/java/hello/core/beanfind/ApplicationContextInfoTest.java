package hello.core.beanfind;
import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;


//컨테이너에 등록된 모든 빈 조회
// => 스프링 컨테이너에 실제 스프링 빈들이 잘 등록 되었는지 확인
class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    /**
     * 스프링에 등록된 모든 빈 정보 출력
     *  => ac.getBeanDefinitionNames() : 스프링에 등록된 모든 빈 이름을 조회한다.
     *  => ac.getBean() : 빈 이름으로 빈 객체(인스턴스)를 조회한다.
     */
    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        //iter + tab : 자동완성 for문
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name=" + beanDefinitionName + " object=" + bean);
        }
    }
    //name=appConfig object=hello.core.AppConfig$$EnhancerBySpringCGLIB$$f0d047d2@50b8ae8d
    //name=memberService object=hello.core.member.MemberServiceImpl@255990cc
    //name=orderService object=hello.core.order.OrderServiceImpl@51c929ae
    //name=memberRepository object=hello.core.member.MemoryMemberRepository@3c8bdd5b
    //name=discountPolicy object=hello.core.discount.RateDiscountPolicy@29d2d081

    /**
     * 스프링이 내부에서 사용하는 빈은 제외하고, 내가 등록한 빈만 출력
     * => 스프링이 내부에서 사용하는 빈은 getRole() 로 구분할 수 있다.
     * => ROLE_APPLICATION : 일반적으로 사용자가 정의한 빈 ( = 직접 등록한 애플리케이션 빈)
     * => ROLE_INFRASTRUCTURE : 스프링이 내부에서 사용하는 빈
     */
    @Test
    @DisplayName("애플리케이션 빈 출력하기") void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name=" + beanDefinitionName + " object=" + bean);
            }
        }
    }
    //name=org.springframework.context.annotation.internalConfigurationAnnotationProcessor object=org.springframework.context.annotation.ConfigurationClassPostProcessor@3ad394e6
    //name=org.springframework.context.annotation.internalAutowiredAnnotationProcessor object=org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@6058e535
    //name=org.springframework.context.annotation.internalCommonAnnotationProcessor object=org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@42deb43a
    //name=org.springframework.context.event.internalEventListenerProcessor object=org.springframework.context.event.EventListenerMethodProcessor@1deb2c43
    //name=org.springframework.context.event.internalEventListenerFactory object=org.springframework.context.event.DefaultEventListenerFactory@3bb9efbc
    //name=appConfig object=hello.core.AppConfig$$EnhancerBySpringCGLIB$$f0d047d2@1cefc4b3
    //name=memberService object=hello.core.member.MemberServiceImpl@2b27cc70
    //name=orderService object=hello.core.order.OrderServiceImpl@6f6a7463
    //name=memberRepository object=hello.core.member.MemoryMemberRepository@1bdaa23d
    //name=discountPolicy object=hello.core.discount.RateDiscountPolicy@79f227a9
}