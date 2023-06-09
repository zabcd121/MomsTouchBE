package com.momstouch.momstouchbe.domain.order.service;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.order.application.OrderInfo;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.setup.OrderInfoSetup;
import com.momstouch.momstouchbe.setup.ShopSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired private OrderService orderService;
    @Autowired private ShopSetup shopSetup;
    @Autowired private OrderInfoSetup orderInfoSetup;

    @Test
    public void 주문_생성_테스트() {
        Member member = Member.createMember("loginId", UUID.randomUUID().toString(),"김현석","ROLE_USER");
        Shop shop = shopSetup.saveShop(member,
                "누네띠네","학교앞가게" , "학교앞","010-0000-1111",
                LocalTime.of(9,0,0),LocalTime.of(23,0,0),20000);
//TODO: 남은 로직 구현
    }
}