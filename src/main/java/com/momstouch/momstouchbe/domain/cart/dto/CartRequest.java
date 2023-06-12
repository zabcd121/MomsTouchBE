package com.momstouch.momstouchbe.domain.cart.dto;

import com.momstouch.momstouchbe.domain.cart.model.CartMenuOptionGroup;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class CartRequest {

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter @Setter
    public static class CartMenuAddRequest {
        private Long menuId;
        private String menuName;
        private Long discountPolicyId;
        private Integer quantity;
        private Integer price;

        @Builder.Default
        private List<CartMenuOptionGroupRequest> cartMenuOptionGroupList = new ArrayList<>();


    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter @Setter
    public static class CartMenuOptionGroupRequest {
        private Long menuOptionGroupId;
        private String menuOptionGroupName;

        @Builder.Default
        private List<CartMenuOptionRequest> cartMenuOptionList = new ArrayList<>();
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter @Setter
    public static class CartMenuOptionRequest {
        private Long menuOptionId;
        private String menuOptionName;
        private Integer price;
    }
}
