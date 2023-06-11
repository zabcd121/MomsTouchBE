package com.momstouch.momstouchbe.domain.member.model;

import lombok.*;
import org.springframework.security.core.Authentication;

import javax.persistence.Embeddable;
@Data
@Embeddable
@Getter
//@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    private String loginId;
    private String password;
    private String name;
    private String role;  //ROLE_TYPE

    @Builder
    private Account(String loginId, String password, String name, String role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;

        if(!role.startsWith("ROLE_")) {
            throw new IllegalArgumentException();
        }

        this.role = role;
    }

    public boolean equals(Authentication authentication) {
        //TODO: 본인 확인 로직
        return true;
    }
}
