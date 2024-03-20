package hello.core.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) //값이 두개 이상일 경우 [키 = 밸류] 형태로 들어가야해서 키의 생략이 안 됨.
                                                                    // 적용 대상이 클래스면 TARGET_CLASS 를 선택
                                                                    // 적용 대상이 인터페이스면 INTERFACES 를 선택
            //이렇게 하면 MyLogger의 가짜 프록시 클래스를 만들어두고 HTTP request와 상관 없이 가짜 프록시 클래스를 다른 빈에 미리 주입해 둘 수 있다
public class MyLogger {
    private String uuid; //UUID를 사용해서 HTTP 요청을 구분
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "["+ requestURL + "] " +message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create:" + this);
    }

    @PreDestroy //웹 스코프에서는 호출 됨 <==> 프로토타입 스코프랑 다름
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close:" + this);
    }
}
