package com.momstouch.momstouchbe.global.jwt;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws IllegalArgumentException {
        System.out.println("MemberDetailsService loadUserByUsername: 진입");
        Member member = memberRepository.findBySub(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        log.info("loadUserByUsername 통과 ");
        return createUserDetails(member);
    }

    private UserDetails createUserDetails(Member member) {
        List<SimpleGrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());

        return new User(member.getAccount().getLoginId(),
                member.getAccount().getPassword(),
                grantedAuthorities);
    }


}
