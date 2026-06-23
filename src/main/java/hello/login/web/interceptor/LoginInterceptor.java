package hello.login.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        // postHandle, afterCompletion에서도 uuid를 공유하기 위해 request임시 저장소에 넣어둠
        request.setAttribute(LOG_ID,uuid);

        //@RequestMapping : HandlerMethod
        //정적 리소스 호출(/resource/static에 있는 리소스 호출) 시  : ResourceHttpRequestHandlerMethod
        // -> 어떤 핸들러냐에 따라 다운 캐스팅 필요
        if(handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler; //  호출할 컨트롤러 메서드의 모든 정보 포함
        }

        // handler : 어떤 핸들러가 호출되느지 확인용
        log.info("REQUEST [{}][{}][{}]",uuid,requestURI,handler);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]",modelAndView);
    }

    // 종료 로그는 postHandle이 아니라 afterCopletion에서 해야 한다.
    // -> 왜냐하면 예외 발생 시 postHandle이 호출이 안 되기 떄문이다.
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String)request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}][{}]",logId,requestURI,handler);

        // 예외 발생 시
        if(ex != null){
            log.error("afterCompletion error!!", ex);
        }
    }
}
