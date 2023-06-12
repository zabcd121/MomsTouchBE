package com.momstouch.momstouchbe.setup;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import com.momstouch.momstouchbe.global.jwt.MemberDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberSetup {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    MemberDetailsService memberDetailsService;


    public Member saveMember(String loginId, String password, String name, String role) {
        return memberRepository.save(
                Member.createMember(loginId, password, name, role,"email")
        );
    }

    public Authentication getAuthentication(Member member) {
        UserDetails userDetails = memberDetailsService.loadUserByUsername(member.getAccount().getLoginId());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,"UUID", List.of(new SimpleGrantedAuthority(member.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
