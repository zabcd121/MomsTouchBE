package com.momstouch.momstouchbe.domain.shop.dto;

import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.shop.model.Category;
import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.domain.shop.model.OptionGroupSpecification;
import com.momstouch.momstouchbe.domain.shop.model.OptionSpecification;
import com.momstouch.momstouchbe.global.domain.Money;
import lombok.*;

import java.util.List;

public class MenuRequest {

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class MenuCreateRequest {
        private String name;
        private String description;
        private Integer price;
        private Category category; //category=MAIN
        private List<OptionGroupRequest> optionGroupList;
        private Long discountPolicyId;

        public Menu toEntity(String imageURL, DiscountPolicy discountPolicy) {
            return Menu.builder()
                    .optionGroupList(
                            optionGroupList.stream()
                                    .map(optionGroupRequest -> optionGroupRequest.toEntity())
                                    .toList()
                    )
                    .discountPolicy(discountPolicy)
                    .name(name)
                    .description(description)
                    .price(Money.of(price))
                    .imageURL(imageURL)
                    .category(category)
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class OptionGroupRequest {
        private String name;
        private List<OptionRequest> optionList;

        public OptionGroupSpecification toEntity() {
            return OptionGroupSpecification.builder()
                    .optionList(
                            optionList.stream()
                                    .map(optionRequest -> optionRequest.toEntity())
                                    .toList()
                    )
                    .name(name)
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class OptionRequest {
        private String name;
        private Integer price;

        public OptionSpecification toEntity() {
            return OptionSpecification.builder()
                    .name(name)
                    .price(Money.of(price))
                    .build();
        }
    }

}
