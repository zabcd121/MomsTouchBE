package com.momstouch.momstouchbe.domain.member.Service;

import com.momstouch.momstouchbe.domain.member.model.Account;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.model.OAuthAttributes;

import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        httpSession.setAttribute("user", (member));
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    @Transactional
    public Member saveOrUpdate(OAuthAttributes attributes) {

        Map<String, Object> googleAttribute = attributes.getAttributes();
        String sub = (String)googleAttribute.get("sub");
        Optional<Member> byEmail = memberRepository.findBySub(sub);

        if(byEmail.isPresent()) {
            return byEmail.get();
        }

        Member member = Member.builder()
                .email(attributes.getEmail())
                .account(Account.builder()
                        .name(attributes.getName())
                        .loginId(sub)
                        .password(UUID.randomUUID().toString())
                        .role("ROLE_MEMBER")
                        .build())
                .build();

        memberRepository.save(member);

        return member;



//        Member member = memberRepository.findByEmail(attributes.getEmail())
//                .map(entity -> entity.update(attributes.getName()))
//                .orElse(attributes.toEntity());
//
//        return memberRepository.save(member);
    }


}
