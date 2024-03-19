package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTest {
    /*
    주입할 스프링 빈이 없어도 동작해야 할 때가 있다.
    * 자동 주입 대상을 옵션으로 처리하는 방법은 다음과 같다.
      1.@Autowired(required=false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
      2.org.springframework.lang.@Nullable : 자동 주입할 대상이 없으면 null이 입력된다.
      3.Optional<> : 자동 주입할 대상이 없으면 Optional.empty 가 입력된다.
    */
    //호출 안됨

    @Test
    void AutowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    static class TestBean {
        // 호출 안 됨.
        @Autowired(required = false)
        public void setNoBean1(Member member) {
            System.out.println("setNoBean1 = " + member);
        }

        //null 호출
        @Autowired
        public void setNoBean2(@Nullable Member member) {
            System.out.println("setNoBean2 = " + member);
        }

        //Optional.empty 호출
        @Autowired(required = false)
        public void setNoBean3(Optional<Member> member) {
            System.out.println("setNoBean3 = " + member);
        }
    }
    /*출력결과
    setNoBean2 = null
    setNoBean3 = Optional.empty
    * */


}
