package com.momstouch.momstouchbe.domain.shop.dto;

import com.momstouch.momstouchbe.domain.shop.model.Category;
import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.domain.shop.model.OptionGroupSpecification;
import com.momstouch.momstouchbe.domain.shop.model.OptionSpecification;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse.*;

public class MenuResponse {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class MenuSearchResponse {
        private Long menuId;
        private String name;
        private BigDecimal price;
        private String description;
        private String imageUrl;
        private Category category;

        public static MenuSearchResponse of(Menu menu) {
            return new MenuSearchResponse(menu.getId(), menu.getName(), menu.getPrice().getAmount(), menu.getDescription(), menu.getImageURL(), menu.getCategory());
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class MenuDetailSearchResponse {
        private Long menuId;
        private String name;
        private BigDecimal price;
        private String description;
        private String imageUrl;
        private Category category;
        private DiscountPolicyValueResponse discountPolicy;
        private List<OptionGroupResponse> optionGroupList;

        public static MenuDetailSearchResponse of(Menu menu) {
            menu.getDiscountPolicy();
            return new MenuDetailSearchResponse(menu.getId(), menu.getName(), menu.getPrice().getAmount(), menu.getDescription(), menu.getImageURL(), menu.getCategory(),
                    DiscountPolicyValueResponse.of(menu.getDiscountPolicy()),
                    menu.getOptionGroupList().stream()
                            .map(optionGroup -> OptionGroupResponse.of(optionGroup))
                            .toList());
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class OptionGroupResponse {
        private Long optionGroupId;
        private String name;
        private List<OptionResponse> optionList;

        public static OptionGroupResponse of(OptionGroupSpecification optionGroupSpecification) {
            return new OptionGroupResponse(
                    optionGroupSpecification.getId(),
                    optionGroupSpecification.getName(),
                    optionGroupSpecification.getOptionList().stream()
                            .map(option -> OptionResponse.of(option))
                            .toList());
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class OptionResponse {
        private Long optionId;
        private String name;
        private Integer price;

        public static OptionResponse of(OptionSpecification optionSpecification) {
            return new OptionResponse(optionSpecification.getId(), optionSpecification.getName(), optionSpecification.getPrice().getAmount().intValueExact());
        }
    }
}
