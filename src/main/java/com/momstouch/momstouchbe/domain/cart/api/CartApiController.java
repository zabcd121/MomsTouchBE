package com.momstouch.momstouchbe.domain.cart.api;

import com.momstouch.momstouchbe.domain.cart.application.CartService;
import com.momstouch.momstouchbe.domain.cart.dto.CartRequest;
import com.momstouch.momstouchbe.domain.cart.dto.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.momstouch.momstouchbe.domain.cart.dto.CartRequest.*;
import static com.momstouch.momstouchbe.domain.cart.dto.CartResponse.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    @PostMapping("/members/{memberId}/carts")
    public ResponseEntity addCartMenu(@PathVariable Long memberId,
                                      @RequestBody CartMenuAddRequest cartMenuAddRequest) {

        cartService.addCartMenu(memberId, cartMenuAddRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/{memberId}/carts")
    public ResponseEntity<CartSearchResponse> searchCartMenuList(@PathVariable Long memberId) {
        return new ResponseEntity<>(cartService.searchCartMenuList(memberId), HttpStatus.OK);
    }
}
