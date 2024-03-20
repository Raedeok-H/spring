package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig {
        /*
        * 설정 정보 사용 특징
            메서드 이름을 자유롭게 줄 수 있다.
            스프링 빈이 스프링 코드에 의존하지 않는다.
            코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있다.*/

                    // 빈 등록 초기화, 소멸 메소드 설정 방법
        @Bean(initMethod = "init", destroyMethod = "close") //destroyMethod 는 close, shutdown 등의 메소드 이름을 자동으로 추론하여 적용시킬 수 있다.
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello.dev");
            return networkClient;
        }
    }

}
