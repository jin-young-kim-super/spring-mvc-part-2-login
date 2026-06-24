package hello.login;


import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LoginCheckInterceptor;
import hello.login.web.interceptor.LoginInterceptor;
import jakarta.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {



    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 서블릿 필터와는 다르게 whitelist 같은 거 만들 필요 없이 등록 과정에서
        // path 등록을 손쉽게 할 수가 있다.
        // 협업 시 이 등록 코드만 보고, 어떤 파일에 로그인 검증 로직이 적용되는지 한 눈에 알아 볼 수가 있다.

        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/*.ico","/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add","/login","/logout","/css/**","/*.ico","/error");
    }

    // @ServletComponent, @WebServlet을 이용하여 순수 서블릿 등록이 가능하다.
    // 그러나 이 방법은 필터의 순서 적용이 안 되기 때문에 스프링이 제공하는 FilterRegistrationBean을 사용한다.
    // -> 순수 서블릿 등록이 가능한 이유는 필터는 톰캣이 서블릿(디스패처 서블릿)을 호출하기 전단계이기 때문에 순수 서블릿을 등록해도 톰캣이 잘 실행시켜 준다.
    // FilterRegistrationBean은 스프링이 제공하는 필터 기능이지만, 안을 들여다보면 서블릿이 제공하는 Filter를 상속하고 있기 때문에 이것 또한 톰캣이 잘 실행시킴.
   // @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter());
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }

    //@Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
        filterFilterRegistrationBean.setOrder(2);
        // addURlPattern("/*")로 지정한 이유
        // -> 우리는 미래에 개발될 새로운 요청에 대해서도 LoginCheckFilter를 적용해야 한다.
        // 그러나 여기서 만약 일일이 구체적인 URL을 매핑을 해 버리면, 새롭게 개발될 요청에 대해서도 로그인 인증이
        // 필요시 기입을 해 줘야 한다. 그래서 로그인 인증 체크 여부는 LogCheckFilter의 whitelist를 통해
        // 적용 여부를 결정하고 "/*" 지정을 통해 미래에 개발될 요청 URL에 대해서 일단은 LogCheckFilter까지는 가게 한다.
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

}
