package com.momstouch.momstouchbe.domain.shop.api;

import com.momstouch.momstouchbe.domain.shop.application.ShopService;
import com.momstouch.momstouchbe.domain.shop.dto.ShopResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/members/{memberId}/shops")
    public ResponseEntity<ShopListResponse> searchAllShop(@PathVariable Long memberId) {
        return new ResponseEntity<>(shopService.searchAllShop(memberId), HttpStatus.OK);
    }
}
