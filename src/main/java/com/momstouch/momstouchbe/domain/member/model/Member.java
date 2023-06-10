package com.momstouch.momstouchbe.domain.member.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String email; //가입 이메일

    @Embedded
    private Account account;


    public static Member createMember(String loginId, String password, String name ,String email) {
        return Member.builder()
                .account(Account.builder()
                        .loginId(loginId)
                        .password(password)
                        .name(name)
                        .role("ROLE_MEMBER")
                        .build())
                .email(email)
                .build();
    }
    public String getRole(){
        return account.getRole();
    }

    public Member update(String name){
        return Member.builder()
                .account(Account.builder()
                        .name(name)
                        .build())
                .build();
    }


    public String getName() {
        return  account.getName();
    }


}
