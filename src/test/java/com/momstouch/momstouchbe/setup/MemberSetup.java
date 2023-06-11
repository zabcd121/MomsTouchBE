package com.momstouch.momstouchbe.setup;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberSetup {

    @Autowired
    private MemberRepository memberRepository;

    public Member saveMember(String loginId, String password, String name, String role) {
        return memberRepository.save(
                Member.createMember(loginId, password, name, role,"email")
        );
    }
}
