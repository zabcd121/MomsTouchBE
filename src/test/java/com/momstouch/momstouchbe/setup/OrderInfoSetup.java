package com.momstouch.momstouchbe.setup;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.order.application.OrderInfo;
import com.momstouch.momstouchbe.domain.order.service.MenuInfo;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderInfoSetup {

    public static OrderInfo of(Shop shop, Member member,List<MenuInfo> orderMenuList) {
        return OrderInfo.builder()
                .shop(shop)
                .member(member)
                .address("address")
                .phoneNumber("1234")
                .orderMenuList(orderMenuList)
                .build();
    }
}
