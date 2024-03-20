package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);

        // 다른 프로토타입 객체이다.
    }

    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);

        //ClientBean이 생성될 때, prototypeBean을 주입받고 내부에 보관하여,
        //ClientBean 안에 있는 prototypeBean은 "prototype scope"일지라도 같은 객체이다.
        //"prototype bean"은 사용할 때 마다 새로 생성해서 쓰길 원해서 만드는 것인데, 싱글톤 빈과 함께 계속 유지되는 것이 문제이다.
        // 문제 해결방법은 다음 commit에서 다룬다.

        /* 참고
        * 여러개의 빈에서 같은 프로토타입 빈을 주입 받으면,
        *   "주입받는 시점"에서는 여러개의 프로토타입 빈이 생성된다.
        *   "사용하는 시점"에서는 특정 싱글톤 빈 내부에 보관(한 번 주입받은)된 객체를 사용한다(같다)
        * */

    }

    @Scope("singleton") // 안 써도 되지만 눈에 보이라고 적음
    static class ClientBean {
        private final PrototypeBean prototypeBean;

        @Autowired // 생성자 한 개라 안써도 되지만 눈에 보이라고 적음
        public ClientBean(PrototypeBean prototypeBean) { //@RequiredArgsConstructor 로 처리해도 되지만 눈에 보이라고 적음
            this.prototypeBean = prototypeBean;
        }

        public int logic() {
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

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
