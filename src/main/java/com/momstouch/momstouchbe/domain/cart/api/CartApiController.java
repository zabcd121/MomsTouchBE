package com.momstouch.momstouchbe.domain.cart.api;

import com.momstouch.momstouchbe.domain.cart.dto.CartRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.momstouch.momstouchbe.domain.cart.dto.CartRequest.*;

@RestController
@RequestMapping("/api")
public class CartApiController {

    @PostMapping("/members/{memberId}/carts")
    public ResponseEntity addCartMenu(@PathVariable Long memberId,
                                      @RequestBody CartMenuAddRequest cartMenuAddRequest) {

        return null;
    }
}
