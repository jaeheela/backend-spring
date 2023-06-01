package hello.core.scan.filter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.context.annotation.ComponentScan.Filter;

//설정 정보와 전체 테스트
public class ComponentFilterAppConfigTest {

    /**
     * ComponentFilterAppConfig 클래스
     *
     * includeFilters 에 MyIncludeComponent 애노테이션을 추가해서 BeanA가 스프링 빈에 등록
     * excludeFilters 에 MyExcludeComponent 애노테이션을 추가해서 BeanB는 스프링 빈에 등록안함
     *
     * @Filter 필터
     * => includeFilters : 컴포넌트 스캔 대상을 추가로 지정
     * => excludeFilters : 컴포넌트 스캔에서 제외할 대상을 지정
     *
     * FilterType 의 5가지 옵션
     * => ANNOTATION: 기본값, 애노테이션을 인식해서 동작 ex) org.example.SomeAnnotation
     * => ASSIGNABLE_TYPE: 지정한 타입과 자식 타입을 인식해서 동작 ex) org.example.SomeClass
     * => ASPECTJ: AspectJ 패턴 사용 ex) org.example..*Service+
     * => REGEX: 정규 표현식 ex) org\.example\.Default.*
     * => CUSTOM: TypeFilter 이라는 인터페이스를 구현해서 처리 ex) org.example.MyTypeFilter
     *
     * => 참고로 @Component 면 충분하기 때문에, includeFilters를 사용할 일은 거의 없다. excludeFilters는 여러가지 이유로 간혹 사용할 때가 있지만 많지는 않다.
     * => 특히 최근 스프링 부트는 컴포넌트 스캔을 기본으로 제공하는데, 개인적으로는 옵션을 변경하면서 사용하기 보다는 스프링의 기본 설정에 최대한 맞추어 사용하는 것을 권장하고, 선호하는 편이다.
     */
    @Configuration
    @ComponentScan(
            includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class), //컴포넌트 스캔 대상을 추가
            excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class) //컴포넌트 스캔에서 제외
    )
    static class ComponentFilterAppConfig {
    }

    /**
     * 스프링 빈 조회 테스트
     */
    @Test
    void filterScan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanA beanA = ac.getBean("beanA", BeanA.class);
        assertThat(beanA).isNotNull(); //스프링빈이 맞나요? ok
        Assertions.assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> ac.getBean("beanB", BeanB.class)); //스프링 빈으로 조회할 수 없나요? ok
    }



}
