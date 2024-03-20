package hello.core.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "request") //value 없이 써도 됨.
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
