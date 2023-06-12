package com.momstouch.momstouchbe.domain.order.dto;

import com.momstouch.momstouchbe.domain.order.service.MenuInfo;
import com.momstouch.momstouchbe.domain.order.service.OptionGroupSelectInfo;
import lombok.*;

import java.util.List;

public class OrderRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateOrderRequest {

        private String address;
        private String phoneNumber;
        private Long shopId;
        private List<MenuInfo> orderMenuList;
    }

}
