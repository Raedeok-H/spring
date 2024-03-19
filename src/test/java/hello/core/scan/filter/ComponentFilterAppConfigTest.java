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

public class ComponentFilterAppConfigTest {

    @Test
    void filterScan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);

        BeanA beanA = ac.getBean("beanA", BeanA.class);
        assertThat(beanA).isNotNull();

        Assertions.assertThrows(
                NoSuchBeanDefinitionException.class, () -> ac.getBean("beanB", BeanB.class)
        );
    }

    @Configuration
    //includeFilters : 컴포넌트 스캔 대상을 추가로 지정한다.
    //excludeFilters : 컴포넌트 스캔에서 제외할 대상을 지정한다.
    @ComponentScan(
            includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes =MyIncludeComponent.class),
            excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes =MyExcludeComponent.class),
                            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BeanA.class)}
    ) //@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BeanA.class) 처럼 추가하면 BeanA도 제외할 수 있다.
    static class ComponentFilterAppConfig {
    }
    /*
    * FilterType 옵션  => 잘 쓰지 않긴 함(옵션을 변경하면서 사용하기보다, 스프링의 기본 설정에 최대한 맞추어 사용하는 것을 권장)
    FilterType은 5가지 옵션이 있다.
        ANNOTATION: 기본값, 애노테이션을 인식해서 동작한다.
            ex) org.example.SomeAnnotation
        ASSIGNABLE_TYPE: 지정한 타입과 자식 타입을 인식해서 동작한다.
            ex) org.example.SomeClass
        ASPECTJ: AspectJ 패턴 사용
            ex) org.example..*Service+
        REGEX: 정규 표현식
            ex) org\.example\.Default.*
        CUSTOM: TypeFilter 이라는 인터페이스를 구현해서 처리
            ex) org.example.MyTypeFilter*/

}
