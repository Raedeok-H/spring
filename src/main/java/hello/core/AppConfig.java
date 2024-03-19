package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 스프링 기반으로 변경
@Configuration //AppConfig에 설정을 구성한다는 뜻의 @Configuration
public class AppConfig {
    /*
    * 스프링 컨테이너는 @Configuration 이 붙은 AppConfig 를 설정(구성) 정보로 사용한다.
    * 여기서 @Bean 이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다.
    * 이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라 한다.*/
    @Bean //스프링 컨테이너에 스프링 빈으로 등록
    public MemberService memberService() { // 스프링 빈은 @Bean 이 붙은 메서드의 명을 스프링 빈의 이름으로 사용한다. ( memberService ,orderService )
        return new MemberServiceImpl(memberRepository());
    }

    /* 빈의 이름을 직접 등록하는 방법 =>
    @Bean(name="memberService2")
    주의: 빈 이름은 항상 다른 이름을 부여해야 한다. 같은 이름을 부여하면, 다른 빈이 무시되거나,
    기존 빈을 덮어버리거나 설정에 따라 오류가 발생한다.*/
    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
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
