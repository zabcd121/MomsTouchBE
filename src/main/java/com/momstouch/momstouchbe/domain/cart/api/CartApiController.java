package com.momstouch.momstouchbe.domain.cart.api;

import com.momstouch.momstouchbe.domain.cart.application.CartService;
import com.momstouch.momstouchbe.domain.cart.dto.CartRequest;
import com.momstouch.momstouchbe.domain.cart.dto.CartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.momstouch.momstouchbe.domain.cart.dto.CartRequest.*;
import static com.momstouch.momstouchbe.domain.cart.dto.CartResponse.*;

@Tag(name = "장바구니")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    @Operation(summary = "장바구니에 상품 담기")
    @PostMapping("/members/{memberId}/carts")
    public ResponseEntity addCartMenu(@PathVariable Long memberId,
                                      @RequestBody CartMenuAddRequest cartMenuAddRequest) {

        cartService.addCartMenu(memberId, cartMenuAddRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니에 담은 목록 보기")
    @GetMapping("/members/{memberId}/carts")
    public ResponseEntity<CartSearchResponse> searchCartMenuList(@PathVariable Long memberId) {
        return new ResponseEntity<>(cartService.searchCartMenuList(memberId), HttpStatus.OK);
    }
}
