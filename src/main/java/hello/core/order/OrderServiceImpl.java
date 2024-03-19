package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {


    //오직 생성자 주입 방식만 ("3.final 키워드")를 사용할 수 있다.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

    // 생성자 주입을 사용하면, 주입 데이터를 ("2.누락)" 했을 때 컴파일 오류가 발생
    // 기억하자! 컴파일 오류는 세상에서 가장 빠르고, 좋은 오류다!
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    } //대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없다.
    //생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없다. 따라서 ("1.불변")하게 설계 가능

    /*
    *   정 리
    - 생성자 주입 방식을 선택하는 이유는 여러가지가 있지만, 프레임워크에 의존하지 않고, 순수한 자바 언어의 특징을 잘 살리는 방법이기도 하다.
    - 기본으로 생성자 주입을 사용하고, 필수 값이 아닌 경우에는 수정자 주입 방식을 옵션으로 부여하면 된다. 생성자주입과 수정자 주입을 동시에 사용할 수 있다.
    - 항상 생성자 주입을 선택해라! 그리고 가끔 옵션이 필요하면 수정자 주입을 선택해라. 필드 주입은 사용하지 않는게 좋다.
    * */

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
