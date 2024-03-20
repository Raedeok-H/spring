package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {
    /*
    * 스프링 빈의 이벤트 라이프사이클
    스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 _> 초기화 콜백 -> 사용 -> 소멸전 콜백 ->스프링 종료
        초기화 콜백: 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
        소멸전 콜백: 빈이 소멸되기 직전에 호출*/

    /*참고: 객체의 생성과 초기화를 분리하자.
        생성자는 필수 정보(파라미터)를 받고, 메모리를 할당해서 객체를 생성하는 책임을 가진다. 반면에 초기화는 이렇
        게 생성된 값들을 활용해서 외부 커넥션을 연결하는등 무거운 동작을 수행한다.
        따라서 생성자 안에서 무거운 초기화 작업을 함께 하는 것 보다는 객체를 생성하는 부분과 초기화 하는 부분을 명
        확하게 나누는 것이 유지보수 관점에서 좋다. 물론 초기화 작업이 내부 값들만 약간 변경하는 정도로 단순한 경우
        에는 생성자에서 한번에 다 처리하는게 더 나을 수 있다.

    참고: 싱글톤 빈들은 스프링 컨테이너가 종료될 때 싱글톤 빈들도 함께 종료되기 때문에 스프링 컨테이너가 종료
        되기 직전에 소멸전 콜백이 일어난다. 뒤에서 설명하겠지만 싱글톤 처럼 컨테이너의 시작과 종료까지 생존하는 빈
        도 있지만, 생명주기가 짧은 빈들도 있는데 이 빈들은 컨테이너와 무관하게 해당 빈이 종료되기 직전에 소멸전 콜
        백이 일어난다. 자세한 내용은 스코프에서 알아보겠다.
    * */

    /*
    * 스프링은 크게 3가지 방법으로 빈 생명주기 콜백을 지원한다.
    - 인터페이스(InitializingBean, DisposableBean)
    - 설정 정보에 초기화 메서드, 종료 메서드 지정
    - @PostConstruct, @PreDestroy 애노테이션 지원
    다음 Commit 부터 하나씩 알아보자.*/

    @Test
    public void lifeCycleTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig {
        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello.dev");
            return networkClient;
        }
    }

}
