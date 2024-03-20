package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
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
        assertThat(count2).isEqualTo(1); //ObjectProvider 사용으로 다른 객체가 된다
    }

    @Scope("singleton")
    static class ClientBean {
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanProvider; //테스트니까 간단하게 필드주입으로 함
        //ObjectProvider 사용, ObjectFactory 로 바꾸어도 동작함(ObjectProvider가 ObjectFactory를 상속받았기 때문)
                            // ObjectFactory에는 getObject()메소드만 있고 ObjectProvider는 편의 기능(옵션,스트림처리) 을 더 제공
                            // ObjectProvider는 "스프링에 의존"

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject(); // 얘가 찾아줌 -> getObject()를 호출할 때, 스프링 컨테이너에서 프로토타입 빈을 찾아서 반환함
                                                                             // 직접 찾는것이 아니라 찾아주는 기능만 제공
                                                                             // 스프링이 제공하는 기능을 사용하지만, 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기는 훨씬쉬워진다.
                                                                             // ObjectProvider 는 지금 딱 필요한 DL 정도의 기능만 제공한다.
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
