package com.momstouch.momstouchbe.domain.shop.dto;

import com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse;
import com.momstouch.momstouchbe.domain.discountpolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.RateDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.TimeDiscountPolicy;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse.*;
import static com.momstouch.momstouchbe.domain.shop.dto.MenuResponse.*;

public class ShopResponse {

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class ShopListResponse {
        @Builder.Default
        private List<ShopSimpleResponse> shopList = new ArrayList<>();

        public static ShopListResponse of(List<Shop> ownerShopList) {
            return ShopListResponse.builder()
                    .shopList(ownerShopList.stream()
                            .map(ShopSimpleResponse::of)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class ShopSimpleResponse {
        private Long shopId;
        private String shopName;

        public static ShopSimpleResponse of(Shop shop) {
            return ShopSimpleResponse.builder()
                    .shopId(shop.getId())
                    .shopName(shop.getName())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class ShopMenuListResponse {
        private Long shopId;
        private String shopName;
        private LocalTime openTime;
        private LocalTime closedTime;
        private DiscountListResponse discountList;

        @Builder.Default
        private List<MenuSearchResponse> menuList = new ArrayList<>();

        public static ShopMenuListResponse of(Shop shop) {
            List<DiscountPolicy> discountPolicyList = shop.getDiscountPolicyList();
            return ShopMenuListResponse.builder()
                    .shopId(shop.getId())
                    .shopName(shop.getName())
                    .openTime(shop.getOpenTime())
                    .closedTime(shop.getClosedTime())
                    .discountList(
                            DiscountListResponse.of(
                                discountPolicyList.stream()
                                    .filter(policy -> policy instanceof AmountDiscountPolicy)
                                    .map(policy -> (AmountDiscountPolicy) policy)
                                    .toList(),
                                discountPolicyList.stream()
                                    .filter(policy -> policy instanceof RateDiscountPolicy)
                                    .map(policy -> (RateDiscountPolicy) policy)
                                    .toList(),
                                discountPolicyList.stream()
                                    .filter(policy -> policy instanceof TimeDiscountPolicy)
                                    .map(policy -> (TimeDiscountPolicy) policy)
                                    .toList()))
                    .menuList(shop.getMenuList().stream()
                            .map(menu -> MenuSearchResponse.of(menu))
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
