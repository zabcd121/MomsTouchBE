package com.momstouch.momstouchbe.domain.order.application;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.order.service.MenuInfo;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class OrderInfo {

    private List<MenuInfo> orderMenuList;
    private Shop shop;
    private Member member;
    private String address;
    private String phoneNumber;

    public static OrderInfo of(Member member,Shop shop, List<MenuInfo> orderMenuList, String address,String phoneNumber) {
        return builder()
                .member(member)
                .shop(shop)
                .orderMenuList(orderMenuList)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }

}
