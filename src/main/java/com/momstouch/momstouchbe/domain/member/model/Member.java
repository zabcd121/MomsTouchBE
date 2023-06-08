package com.momstouch.momstouchbe.domain.member.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
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


}
