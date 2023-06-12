package com.momstouch.momstouchbe.domain.shop.api;

import com.momstouch.momstouchbe.domain.shop.application.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.momstouch.momstouchbe.domain.shop.dto.MenuRequest.*;
import static com.momstouch.momstouchbe.domain.shop.dto.MenuResponse.*;
import static com.momstouch.momstouchbe.domain.shop.dto.ShopResponse.*;

@Tag(name = "메뉴")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuApiController {

    private final MenuService menuService;

    @Operation(summary = "가게의 메뉴 목록을 조회")
    @GetMapping("/shop/{shopId}/menus")
    public ResponseEntity<ShopMenuListResponse> searchAllMenu(@PathVariable Long shopId) {
        return new ResponseEntity<>(menuService.searchAllMenuInShop(shopId), HttpStatus.OK);
    }

    @Operation(summary = "가게의 새로운 메뉴를 추가")
    @PostMapping("/shop/{shopId}/menus")
    public ResponseEntity addMenu(@PathVariable Long shopId, @RequestPart MultipartFile image, @RequestPart MenuCreateRequest menu) {
        System.out.println("image: " + image.getName());
        System.out.println("menu: " + menu.getName());
        try{
            menuService.addMenu(shopId, image,menu);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "가게의 메뉴 상세 내역을 조회")
    @GetMapping("/shop/{shopId}/menus/{menuId}")
    public ResponseEntity<MenuDetailSearchResponse> searchMenuDetail(@PathVariable Long shopId, @PathVariable Long menuId) {
        return new ResponseEntity<>(menuService.searchMenuDetail(shopId, menuId), HttpStatus.OK);
    }

    @Operation(summary = "가게의 메뉴를 수정")
    @PutMapping("/shop/{shopId}/menus/{menuId}")
    public ResponseEntity updateMenuDetail(@PathVariable Long shopId,
                                           @PathVariable Long menuId,
                                           @RequestPart(value = "image", required = false) MultipartFile image,
                                           @RequestPart MenuUpdateRequest menu) {
        menuService.updateMenuDetail(shopId, menuId, image, menu);
        return ResponseEntity.ok().build();
    }
}
