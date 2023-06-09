package com.momstouch.momstouchbe.domain.shop.dto;

import com.momstouch.momstouchbe.domain.shop.model.Shop;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.momstouch.momstouchbe.domain.shop.dto.MenuResponse.*;

public class ShopResponse {

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class ShopMenuListResponse {
        private Long shopId;
        private String shopName;

        @Builder.Default
        private List<MenuSearchResponse> menuList = new ArrayList<>();

        public static ShopMenuListResponse of(Shop shop) {
            return ShopMenuListResponse.builder()
                    .shopId(shop.getId())
                    .shopName(shop.getName())
                    .menuList(shop.getMenuList().stream()
                            .map(menu -> MenuSearchResponse.of(menu))
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
