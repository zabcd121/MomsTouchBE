package com.momstouch.momstouchbe.domain.member.model;

import lombok.*;
import org.springframework.security.core.Authentication;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Account account;

    public static Member createMember(String loginId, String password, String name, String role) {
        return Member.builder()
                .account(Account.builder()
                        .loginId(loginId)
                        .password(password)
                        .name(name)
                        .role(role)
                        .build())
                .build();
    }

    public boolean equals(Authentication authentication) {
        return account.equals(authentication);
    }


}
