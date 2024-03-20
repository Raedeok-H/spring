package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {
    private final LogDemoService logDemoService;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        System.out.println("myLogger = " + myLogger.getClass()); //myLogger = class hello.core.common.MyLogger$$SpringCGLIB$$0  => 가짜 프록시 클래스
                                                                 //CGLIB(바이트코드 조작 라이브러리)를 확인할 수 있다. -> appConfig에서 @Configuration 애너테이션 설명과 같음
                                                                 //ac.getBean("myLogger", MyLogger.class)처럼 조회해도
                                                                 // 프록시 객체가 조회되고, 의존관계 주입도 프록시 객체가 주입됨

                                                                 // ! 가짜 프록시 객체는 요청이 오면 그때 내부에서 진짜 빈을 요청하는 위임 로직이 들어있다.
                                                                 //  가짜 프록시 객체는 실제 request scope와는 관계가 없다.
                                                                 //  그냥 가짜이고, 내부에 단순한 위임 로직만 있고, 싱글톤 처럼 동작함.
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        logDemoService.logic("testId");

        return "OK";
    }
}
