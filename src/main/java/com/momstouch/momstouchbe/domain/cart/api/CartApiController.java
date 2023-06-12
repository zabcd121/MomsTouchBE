package com.momstouch.momstouchbe.domain.cart.api;

import com.momstouch.momstouchbe.domain.cart.application.CartService;
import com.momstouch.momstouchbe.domain.cart.dto.CartRequest;
import com.momstouch.momstouchbe.domain.cart.dto.CartResponse;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.momstouch.momstouchbe.domain.cart.dto.CartRequest.*;
import static com.momstouch.momstouchbe.domain.cart.dto.CartResponse.*;

@Tag(name = "장바구니")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;
    private final MemberRepository memberRepository;

    @Operation(summary = "장바구니에 상품 담기")
    @PostMapping("/carts")
    public ResponseEntity addCartMenu(@RequestBody CartMenuAddRequest cartMenuAddRequest) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null) throw new AccessDeniedException("로그인이 필요합니다.");
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            Member member = memberRepository.findBySub(principal.getUsername()).orElseThrow();
            cartService.addCartMenu(member.getId(), cartMenuAddRequest);
        } catch(IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니에 담은 목록 보기")
    @GetMapping("/carts")
    public ResponseEntity<CartSearchResponse> searchCartMenuList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new AccessDeniedException("로그인이 필요합니다.");
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Member member = memberRepository.findBySub(principal.getUsername()).orElseThrow();
        return new ResponseEntity<>(cartService.searchCartMenuList(member.getId()), HttpStatus.OK);
    }
}
