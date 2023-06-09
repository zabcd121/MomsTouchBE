package com.momstouch.momstouchbe.domain.shop.api;

import com.momstouch.momstouchbe.domain.shop.application.MenuSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuApiController {

    private final MenuSearchService menuSearchService;

    @GetMapping("/shop/{shopId}/menus")
    public ResponseEntity searchAllMenu(@PathVariable Long shopId) {
        return new ResponseEntity<>(menuSearchService.searchAllMenuBy(shopId), HttpStatus.OK);
    }
}
