package com.momstouch.momstouchbe.domain.member.model;

import lombok.*;

import javax.persistence.Embeddable;
@Data
@Embeddable
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    private String loginId;  //구글 제공 id
    private String password;  //구글 제공 pw
    private String name;
    private String role;  //ROLE_TYPE


}
