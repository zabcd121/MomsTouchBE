package com.momstouch.momstouchbe.domain.member.model;

import com.momstouch.momstouchbe.global.vo.BaseTime;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class Member extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String email; //가입 이메일

    @Embedded
    private Account account;

    public static Member createMember(String loginId, String password, String name, String role,String email) {
        return Member.builder()
                .account(Account.builder()
                        .loginId(loginId)
                        .password(password)
                        .name(name)
                        .role(role)
                        .build())
                //TODO: 수정
                .email(email)
                .build();
    }

    public List<String> getAuthorities() {
        return List.of(account.getRole());
    }

    public String getRole(){
        return account.getRole();
    }


    public String getName() {
        return  account.getName();
    }

    public boolean isEquals(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername().equals(account.getLoginId());
    }


}
