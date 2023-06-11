package com.momstouch.momstouchbe.domain.shop.api;

import com.momstouch.momstouchbe.domain.shop.application.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.momstouch.momstouchbe.domain.shop.dto.MenuRequest.*;
import static com.momstouch.momstouchbe.domain.shop.dto.MenuResponse.*;
import static com.momstouch.momstouchbe.domain.shop.dto.ShopResponse.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuApiController {

    private final MenuService menuService;

    @GetMapping("/shop/{shopId}/menus")
    public ResponseEntity<ShopMenuListResponse> searchAllMenu(@PathVariable Long shopId) {
        return new ResponseEntity<>(menuService.searchAllMenuInShop(shopId), HttpStatus.OK);
    }

    @PostMapping("/shop/{shopId}/menus")
    public ResponseEntity addMenu(@PathVariable Long shopId, @RequestPart("image") MultipartFile image, @RequestPart MenuCreateRequest menu) {
        menuService.addMenu(shopId, image,menu);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shop/{shopId}/menus/{menuId}")
    public ResponseEntity<MenuDetailSearchResponse> searchMenuDetail(@PathVariable Long shopId, @PathVariable Long menuId) {
        return new ResponseEntity<>(menuService.searchMenuDetail(shopId, menuId), HttpStatus.OK);
    }

    @PutMapping("/shop/{shopId}/menus/{menuId}")
    public ResponseEntity updateMenuDetail(@PathVariable Long shopId,
                                           @PathVariable Long menuId,
                                           @RequestPart(value = "image", required = false) MultipartFile image,
                                           @RequestPart MenuUpdateRequest menu) {
        menuService.updateMenuDetail(shopId, menuId, image, menu);
        return ResponseEntity.ok().build();
    }
}
