package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;


    //@GetMapping("/")
    public String home() {
        return "/home";
    }


    //@GetMapping("/")
    // required=false : 로그인을 하지 않은 사람도 들어 올 수 있어야 하기 때문이다.
    public String homeLoginV1(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        // 로그인을 아직 하지 않은 사람(쿠키를 가지고 있지 않은 사람)
        if(memberId == null) {
            return "/home";
        }

        // 로그인한 사람
        // 로그인이 됐어도 세션 쿠키가 너무 오래돼서 DB에 조회가 안 될 수가 있다.
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null) {
            return "/home";
        }

        // 이번에 진짜로 로그인 상태 유지 중지 사람
        model.addAttribute("member",loginMember);
        return "/loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        Member loginMember = (Member)sessionManager.getSession(request);

        if(loginMember == null) {
            return "/home";
        }

        // 이번에 진짜로 로그인 상태 유지 중지 사람
        model.addAttribute("member",loginMember);
        return "/loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);

        if(session == null) {
            return "/home";
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("member",loginMember);
        return "/loginHome";
    }

   // @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        // @SessionAttribute = request.getSession(false) + (Member)session.getAttribute(SessionConst.LOGIN_MEMBER)
        // -> 세션을 활용하기 위해 일일이 HttpSevletRequest를 사용하는 게 번거롭다. 그래서 @SessionAttribute(스프링이 제공)을 사용하면 코드가 훨씬 깔끔해 진다.
        // @SessionAttribute는 세션을 조회만 하지 새롭게 생성을 하지는 않는다. 그래서 이미 로그인한 유저의 세션을 다루고 싶을 떄만 사용 가능!
        if(loginMember == null) {
            return "/home";
        }

        model.addAttribute("member",loginMember);
        return "/loginHome";
    }

    // 만약 LoginMemberArgumentResolver을 구현하지 않았으면, @Login은 그냥 무시되고 Argument Resolver에서 @ModelAttribute를 처리하는 Argumente Resolver 구현체가 호출이 된다.
    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {

        if(loginMember == null) {
            return "/home";
        }

        model.addAttribute("member",loginMember);
        return "/loginHome";
    }
}