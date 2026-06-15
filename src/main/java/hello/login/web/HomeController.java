package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    //@GetMapping("/")
    public String home() {
        return "/home";
    }


    @GetMapping("/")
    // required=false : 로그인을 하지 않은 사람도 들어 올 수 있어야 하기 때문이다.
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

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


}