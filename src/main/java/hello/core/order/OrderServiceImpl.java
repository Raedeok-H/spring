package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {


    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }


    /*
    * 생성자가 1개이면 @Autowired를 생략할 수 있음(생성자 주입 -> 왠만하면 생성자 주입을 사용하기)
        생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.
        불변, 필수 의존관계에 보통 사용
    * */

    // 스프링 컨테이너의 생성 과정은 크게
    // [1.스프링 컨테이너 생성] -> [2.빈생성] -> [3.빈들의 의존관계 주입] 단계로 볼 수 있다.
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    /* 수정자(setter) 주입
    * @Autowired
     public void setMemberRepository(MemberRepository memberRepository) {
     this.memberRepository = memberRepository;
     }
     * 선택, 변경 가능성이 있는 의존관계에 사용
     * 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법
     *       -> 자바빈 프로퍼티 :자바에서는 과거부터 필드의 값을 직접 변경하지 않고, setXxx, getXxx 라는 메서드를 통해서 값을 읽거나 수정하는 규칙.
     * 참고: @Autowired 의 기본 동작은 주입할 대상이 없으면 오류가 발생한다. 주입할 대상이 없어도 동작하게 하려면 @Autowired(required = false) 로 지정하면 된다.
    **/

    /*필드 주입
    * @Autowired
      private MemberRepository memberRepository;
      @Autowired
      private DiscountPolicy discountPolicy;
      * 코드가 간결해서 많은 개발자들을 유혹하지만 외부에서 변경이 불가능해서 테스트 하기 힘들다는 치명적인 단점.
      * DI 프레임워크가 없으면 아무것도 할 수 없다.
      * 사용하지 말자!(스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별한 용도로 사용)
    * */

    /*일반 메서드 주입
    @Autowired
     public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
     this.memberRepository = memberRepository;
     this.discountPolicy = discountPolicy;
     }
     * 한번에 여러 필드를 주입 받을 수 있다. 일반적으로 잘 사용하지 않는다.
     * 참고: 당연한 이야기이지만 의존관계 자동 주입은 스프링 컨테이너가 관리하는 스프링 빈이어야 동작한다.
    **/

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
