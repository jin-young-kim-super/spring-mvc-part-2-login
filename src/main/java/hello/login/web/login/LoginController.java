package hello.login.web.login;


import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginservice;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "/login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) {

        if(bindingResult.hasErrors()) {
            return "/login/loginForm";
        }

        // Bean Validation의 @ScriptError의 한계 : 이전 시간에 @ScriptAssert는 단순 필드 조합만 가지고는 검증이 잘 안돼서 사용을 잘 안 한다고 하였다.
        // 여기 이 코드가 바로 그 예시이다. 지금 여기서 login() 메서드를 통해 DB 조회를 통해서 검증을 하고 있다.
        // @SciprtAssert는 바로 이러한 한계를 가지고 있기에 잘 사용 안한다.
        Member loginMember = loginservice.login(loginForm.getLoginId(), loginForm.getPassword());
        if(loginMember == null) {
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "/login/loginForm";
        }

        // 로그인 처리 성공 로직
        // 쿠키에 만료 날짜를 입력하지 않았으므로, 세션 쿠키(브라우져 종료 시 쿠시 소멸)이다.
        Cookie cookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(cookie);
        return "redirect:/";

    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
