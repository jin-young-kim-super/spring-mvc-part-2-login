package hello.login.web.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    // 필터가 실행되면 가장 먼저 doFilter가 실행된다
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");
        // ServletRequest는 HttpServletRequest 인터페이스의 부모 인터페이스이다.
        // -> ServletRequest에는 기능이 많이 없으므로, HttpServeltRequest로 다운 캐스팅을 해야 한다.
        // 물론 HttpServletRequest의 구현체는 톰캣이 이미 만들어 놓은 상태이다
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        try{
            log.info("REQUEST [{}][{}]",uuid,requestURI);
            chain.doFilter(request,response); // 다음 필터 호출(필터가 없으면 바로 디스패처 서블릿 호출)
        }catch (Exception e) {
            throw e;
        }finally {
            log.info("RESPONSE [{}][{}]",uuid,requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
