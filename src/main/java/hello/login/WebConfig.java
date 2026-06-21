package hello.login;


import hello.login.web.filter.LogFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    // @ServletComponent, @WebServlet을 이용하여 순수 서블릿 등록이 가능하다.
    // 그러나 이 방법은 필터의 순서 적용이 안 되기 때문에 스프링이 제공하는 FilterRegistrationBean을 사용한다.
    // -> 순수 서블릿 등록이 가능한 이유는 필터는 톰캣이 서블릿(디스패처 서블릿)을 호출하기 전단계이기 때문에 순수 서블릿을 등록해도 톰캣이 잘 실행시켜 준다.
    // FilterRegistrationBean은 스프링이 제공하는 필터 기능이지만, 안을 들여다보면 서블릿이 제공하는 Filter를 상속하고 있기 때문에 이것 또한 톰캣이 잘 실행시킴.
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter());
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }

}
