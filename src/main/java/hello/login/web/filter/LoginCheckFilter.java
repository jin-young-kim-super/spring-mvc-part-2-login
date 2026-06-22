package hello.login.web.filter;


import hello.login.web.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

/**
 * 이번 시간에는 상품 관리/수정/등록 페이지 뿐 아니라, 미래에 개발될 페이지에 대해서도 로그인 안 된 유저는
 * 튕기게 개발을 해보겠다.
 */

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/","/members/add","/login", "/logout","/css/*"};


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            log.info("인증 체크 필터 시작{}",requestURI);
            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행{}",requestURI);
                HttpSession session = httpRequest.getSession();
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자의 요청{}",requestURI);
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                }
            }
            chain.doFilter(request,response);
        }catch (Exception e){
            throw  e; // 반드시 톰캣까지 예외를 보내줘야 한다.
                      // 만약 throw를 하지 않고 여기서 먹어 버리면, 톰캣은 이 요청을 정상 처리로 인식한다.
        }finally {
            log.info("인증 체크 필터 종료{}",requestURI);
        }

    }

    // whitelist의 경우 로그인 인증 체크 x
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist,requestURI);
    }
}
