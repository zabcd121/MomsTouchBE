package com.momstouch.momstouchbe.domain.shop.api;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import com.momstouch.momstouchbe.domain.shop.application.ShopService;
import com.momstouch.momstouchbe.domain.shop.dto.ShopResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.momstouch.momstouchbe.domain.shop.dto.ShopResponse.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopApiController {

    private final ShopService shopService;
    private final MemberRepository memberRepository;

    @GetMapping("/members/shops")
    public ResponseEntity<ShopListResponse> searchAllShop() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new AccessDeniedException("로그인이 필요합니다.");
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Member member = memberRepository.findBySub(principal.getUsername()).orElseThrow();
        return new ResponseEntity<>(shopService.searchAllShop(member.getId()), HttpStatus.OK);
    }
}
