package hello.core;
// basePackages : 탐색할 패키지의 시작 위치를 지정
// basePackageClasses : 지정한 클래스의 패키지를 탐색 시작 위치로 지정
// 만약 지정하지 않으면 @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치가 됨

// @Component 스캔의 권장 방법
// 프로젝트 시작 루트, ~~Config 같은 메인 설정 정보를 두고, @ComponentScan 애노테이션을 붙이고, basePackages 지정은 생략한다.

// 컴포넌트 스캔은 @Component 뿐만 아니라 @Component 를 포함하고 있는 것을 모두 스캔한다.
// ex)
// @Component : 컴포넌트 스캔에서 사용
// @Controller : 스프링 MVC 컨트롤러에서 사용
// @Service : 스프링 비즈니스 로직에서 사용
// @Repository : 스프링 데이터 접근 계층에서 사용
// @Configuration : 스프링 설정 정보에서 사용

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
// @Component 애노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록 => @Configuration 이 컴포넌트 스캔의 대상이 된 이유도 @Configuration 소스코드를 열어보면 @Component 애노테이션이 붙어있기 때문이다.
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class))
//보통 설정 정보를 컴포넌트 스캔 대상에서 제외하지는 않지만, 기존 예제 코드를 최대한 남기고 유지하기 위해서 이 방법을 선택했다.
public class AutoAppConfig {

}
