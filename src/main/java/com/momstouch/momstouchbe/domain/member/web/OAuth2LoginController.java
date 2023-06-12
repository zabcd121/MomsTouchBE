package com.momstouch.momstouchbe.domain.member.web;

import com.momstouch.momstouchbe.domain.member.dto.MemberResponse;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Tag(name = "oAuth 로그인")
@RestController
public class OAuth2LoginController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    private MemberRepository memberRepository;

    @Operation(summary = "로그인 페이지 이동")
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 로그인 페이지로 이동
    }

    @Operation(summary = "관리자 페이지 이동")
    @GetMapping("/admins")
    public String adminPage() {
        return "admins";
    }

    @Operation(summary = "구글 oAuth페이지로 이동")
    @GetMapping("/login/oauth2/code/google")
    public String oauth2Callback(@PathVariable String registrationId,
                                 OAuth2AuthenticationToken authenticationToken,
                                 HttpServletRequest request) {
        OAuth2AuthorizedClient authorizedClient = getAuthorizedClient(registrationId, authenticationToken);
        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        // 사용자 정보를 가져오는 API 호출 등의 추가 작업 수행
        // ...

        // 로그인 처리 및 리다이렉트
        // ...

        return "redirect:/admins"; // 로그인 성공 시 홈 페이지로 리다이렉트
    }

    private OAuth2AuthorizedClient getAuthorizedClient(String registrationId, OAuth2AuthenticationToken authenticationToken) {
        return authorizedClientService.loadAuthorizedClient(
                registrationId,
                authenticationToken.getName());
    }

    @Operation(summary = "사용자 정보 조회")
    @GetMapping("/api/user/")
    public ResponseEntity<MemberResponse> findUserInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new AccessDeniedException("로그인 필요");
        UserDetails userDetails = (UserDetails) authentication;

        Member member = memberRepository.findBySub(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(MemberResponse.of(member));
    }
}



