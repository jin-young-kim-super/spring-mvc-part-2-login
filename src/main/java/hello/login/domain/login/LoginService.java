package hello.login.domain.login;


import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

// LoginService는 핵심 비지니즈 로직이라서, Domain 폴더에 생성

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {

//        Optional<Member> findMemberOptional = memberRepository.findByLongId(loginId);
//        Member member = findMemberOptional.get();
//        if(member.getPassword().equals(password)) {
//            return member;
//        } else
//        {
//            return null;
//        }

        return memberRepository.findByLongId(loginId)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);

    }

}
