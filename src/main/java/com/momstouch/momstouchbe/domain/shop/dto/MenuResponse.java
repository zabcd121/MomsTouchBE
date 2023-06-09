package com.momstouch.momstouchbe.domain.menu.dto;

import com.momstouch.momstouchbe.domain.menu.model.Category;
import com.momstouch.momstouchbe.domain.menu.model.Menu;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class MenuResponse {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class MenuSearchResponse {
        private String name;
        private Integer price;
        private String description;
        private String imageUrl;
        private Category category;

        public static MenuSearchResponse of(Menu menu) {
            return new MenuSearchResponse(menu.getName(), menu.getPrice(), menu.getDescription(), menu.getImageURL(), menu.getCategory());
        }
    }
}
