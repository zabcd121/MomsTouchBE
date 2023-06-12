package com.momstouch.momstouchbe.domain.member.dto;

import com.momstouch.momstouchbe.domain.member.model.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MemberResponse {

    private Long memberId;
    private String name;
    private List<String> roles;
    private String email;
    private String createdAt;
    public static MemberResponse of(Member member) {

        return builder()
                .memberId(member.getId())
                .name(member.getName())
                .roles(member.getAuthorities())
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
