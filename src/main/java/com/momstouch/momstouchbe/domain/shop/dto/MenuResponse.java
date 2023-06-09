package com.momstouch.momstouchbe.domain.shop.dto;

import com.momstouch.momstouchbe.domain.shop.model.Category;
import com.momstouch.momstouchbe.domain.shop.model.Menu;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

public class MenuResponse {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class MenuSearchResponse {
        private String name;
        private BigDecimal price;
        private String description;
        private String imageUrl;
        private Category category;

        public static MenuSearchResponse of(Menu menu) {
            return new MenuSearchResponse(menu.getName(), menu.getPrice().getAmount(), menu.getDescription(), menu.getImageURL(), menu.getCategory());
        }
    }
}
