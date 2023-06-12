package com.momstouch.momstouchbe.domain.member.model;

import com.momstouch.momstouchbe.global.vo.BaseTime;
import lombok.*;
import org.springframework.security.core.Authentication;

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
        List roleList = new ArrayList();

        if(!account.getRole().equals("")) {
            roleList.add(account.getRole());
        }

        return roleList;
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

    public boolean isEquals(Authentication authentication) {
        return account.isEquals(authentication);
    }


}
