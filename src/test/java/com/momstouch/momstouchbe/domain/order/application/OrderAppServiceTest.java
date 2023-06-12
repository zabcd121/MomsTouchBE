package com.momstouch.momstouchbe.domain.order.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import com.momstouch.momstouchbe.domain.order.model.Order;
import com.momstouch.momstouchbe.domain.order.service.MenuInfo;
import com.momstouch.momstouchbe.domain.order.service.OrderService;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.momstouch.momstouchbe.domain.shop.model.repository.MenuRepository;
import com.momstouch.momstouchbe.global.vo.Money;
import com.momstouch.momstouchbe.setup.MemberSetup;
import com.momstouch.momstouchbe.setup.MenuInfoSetup;
import com.momstouch.momstouchbe.setup.OrderInfoSetup;
import com.momstouch.momstouchbe.setup.ShopSetup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderAppServiceTest {

    @Autowired OrderAppService orderAppService;
    @Autowired EntityManager em;
    @Autowired
    MemberSetup memberSetup;
    @Autowired
    ShopSetup shopSetup;
    @Autowired
    DiscountPolicyService discountPolicyService;
    @Autowired
    OrderService orderService;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuInfoSetup menuInfoSetup;
    @Autowired OrderInfoSetup orderInfoSetup;
    @Autowired MemberRepository memberRepository;

//    @Test
        //TODO: 시큐리티 추가하고 나머지 테스트
    void 주인이_아닐때() {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "김현석", "ROLE_USER");
        Shop shop = shopSetup.saveShop(member,
                "누네띠네","학교앞가게" , "학교앞","010-0000-1111",
                LocalTime.of(9,0,0),LocalTime.of(23,0,0),20000);

        Long discountPolicyId = discountPolicyService.createAmountDiscountPolicy(shop,Integer.MAX_VALUE, 1000);

        Menu menu1 = Menu.builder()
                .category(Category.MAIN)
                .name("싸이버거")
                .description("풍미좋은 햄버거")
                .price(Money.of(6500)) //6500 + 1500 = 8000
                .discountPolicy(discountPolicyService.findById(discountPolicyId).get())
                .optionGroupList(
                        List.of(
                                OptionGroupSpecification.builder().name("단품, 세트 선택")
                                        .optionList( //0
                                                List.of(OptionSpecification.builder().name("단품선택").price(Money.of(0)).build())
                                        ).build(),
                                OptionGroupSpecification.builder().name("음료 선택")
                                        .optionList( //1500
                                                List.of(OptionSpecification.builder().name("커피").price(Money.of(1500)).build())
                                        ).build()
                        )
                ).build();
        shop.addMenu(menu1);
        MenuInfo menuInfo1 = menuInfoSetup.of(menu1, menu1.getOptionGroupList(), 1);
        OrderInfo orderInfo = orderInfoSetup.of(shop, member, List.of(menuInfo1));

        Long orderId = orderService.createOrder(orderInfo);

        //TODO :나머지
    }

//    @Test
        //TODO : 나중에 시큨리티 넣고 테스트
    void 동일_인물이_아닌경우() {

    }


    @Test
    void accept_실패() {

        Long orderId = setOrder();
        Order order = orderService.findById(orderId).get();
        order.deliver();

        em.flush();
        em.clear();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        Assertions.assertThatThrownBy(() -> {
            orderAppService.accept(orderId,authentication);
        }).isInstanceOf(IllegalStateException.class);
    }
    @Test
    void deliver_실패() {

        Long orderId = setOrder();
        Order order = orderService.findById(orderId).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        em.flush();
        em.clear();

        Assertions.assertThatThrownBy(() -> {
            orderAppService.deliver(orderId,authentication);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void complete_실패() {

        Long orderId = setOrder();
        Order order = orderService.findById(orderId).get();

        em.flush();
        em.clear();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        Assertions.assertThatThrownBy(() -> {
            orderAppService.complete(orderId,authentication);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 최소_주문_가격_테스트() {

    }

    @Transactional
    public Long setOrder(){
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "김현석", "ROLE_USER");
        Shop shop = shopSetup.saveShop(member,
                "누네띠네","학교앞가게" , "학교앞","010-0000-1111",
                LocalTime.of(0,0,0),LocalTime.of(23,59,59),6500);

        Long discountPolicyId = discountPolicyService.createAmountDiscountPolicy(shop,Integer.MAX_VALUE, 1000);

        Menu menu1 = Menu.builder()
                .category(Category.MAIN)
                .name("싸이버거")
                .description("풍미좋은 햄버거")
                .price(Money.of(6500)) //6500 + 1500 = 8000
                .discountPolicy(discountPolicyService.findById(discountPolicyId).get())
                .optionGroupList(
                        List.of(
                                OptionGroupSpecification.builder().name("단품, 세트 선택")
                                        .optionList( //0
                                                List.of(OptionSpecification.builder().name("단품선택").price(Money.of(0)).build())
                                        ).build(),
                                OptionGroupSpecification.builder().name("음료 선택")
                                        .optionList( //1500
                                                List.of(OptionSpecification.builder().name("커피").price(Money.of(1500)).build())
                                        ).build()
                        )
                ).build();
        shop.addMenu(menu1);

        em.flush();
        em.clear();

        MenuInfo menuInfo1 = menuInfoSetup.of(menu1, menu1.getOptionGroupList(), 1);
        OrderInfo orderInfo = orderInfoSetup.of(shop, member, List.of(menuInfo1));

        Long orderId = orderService.createOrder(orderInfo);

        return orderId;
    }

}