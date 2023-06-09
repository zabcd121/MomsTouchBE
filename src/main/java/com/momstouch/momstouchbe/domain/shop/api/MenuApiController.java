package com.momstouch.momstouchbe.domain.shop.api;

import com.momstouch.momstouchbe.domain.shop.application.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.momstouch.momstouchbe.domain.shop.dto.MenuRequest.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuApiController {

    private final MenuService menuService;

    @GetMapping("/shop/{shopId}/menus")
    public ResponseEntity searchAllMenu(@PathVariable Long shopId) {
        return new ResponseEntity<>(menuService.searchAllMenuBy(shopId), HttpStatus.OK);
    }

    @PostMapping("/shop/{shopId}/menus")
    public ResponseEntity addMenu(@PathVariable Long shopId, @RequestPart("image") MultipartFile image, @RequestPart MenuCreateRequest menu) {
        menuService.addMenu(shopId, image,menu);
        return ResponseEntity.ok().build();
    }

}
