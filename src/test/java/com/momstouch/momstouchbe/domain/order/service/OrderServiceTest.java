package com.momstouch.momstouchbe.domain.order.service;

import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.DiscountPolicyRepository;
import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.order.application.OrderInfo;
import com.momstouch.momstouchbe.domain.order.model.Order;
import com.momstouch.momstouchbe.domain.order.model.OrderMenu;
import com.momstouch.momstouchbe.domain.order.model.OrderStatus;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.momstouch.momstouchbe.global.domain.Money;
import com.momstouch.momstouchbe.setup.MemberSetup;
import com.momstouch.momstouchbe.setup.MenuInfoSetup;
import com.momstouch.momstouchbe.setup.OrderInfoSetup;
import com.momstouch.momstouchbe.setup.ShopSetup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired private OrderService orderService;
    @Autowired private ShopSetup shopSetup;
    @Autowired private OrderInfoSetup orderInfoSetup;

    @Autowired private MenuInfoSetup menuInfoSetup;

    @Autowired private MemberSetup memberSetup;

    @Autowired private DiscountPolicyService discountPolicyService;
    @Test
    public void 주문_생성_테스트() {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "김현석", "ROLE_USER");
        Shop shop = shopSetup.saveShop(member,
                "누네띠네","학교앞가게" , "학교앞","010-0000-1111",
                LocalTime.of(9,0,0),LocalTime.of(23,0,0),20000);

        Long discountPolicyId = discountPolicyService.createAmountDiscountPolicy(shop,10000, 1000);

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


        Menu menu2 = Menu.builder()
                .category(Category.MAIN)
                .name("화이트갈릭버거")
                .description("마요 갈릭 버거입니다.")
                .price(Money.of(5500)) //(5500 + 3500) *2 = 18000
                .discountPolicy(discountPolicyService.findById(discountPolicyId).get())
                .optionGroupList(
                        List.of(
                                OptionGroupSpecification.builder().name("단품, 세트 선택")
                                        .optionList( //1000
                                                List.of(OptionSpecification.builder().name("세트변경").price(Money.of(1000)).build())
                                        ).build(),
                                OptionGroupSpecification.builder().name("사이드 선택")
                                        .optionList( //1500
                                                List.of(OptionSpecification.builder().name("감자튀김").price(Money.of(1500)).build())
                                        ).build(),
                                OptionGroupSpecification.builder().name("음료 선택")
                                        .optionList( //1000
                                                List.of(OptionSpecification.builder().name("콜라").price(Money.of(1000)).build())
                                        ).build()
                        )
                ).build();

        MenuInfo menuInfo1 = menuInfoSetup.of(menu1, menu1.getOptionGroupList(), 1);
        MenuInfo menuInfo2 = menuInfoSetup.of(menu2, menu2.getOptionGroupList(), 2);

        OrderInfo orderInfo = orderInfoSetup.of(shop, member, List.of(menuInfo1, menuInfo2));

        Long orderId = orderService.createOrder(orderInfo);


        Optional<Order> byId = orderService.findById(orderId);

        Assertions.assertThat(byId.isPresent()).isTrue();

        Order order = byId.get();

        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDER);
        Assertions.assertThat(order.getShop()).isEqualTo(shop);
        Assertions.assertThat(order.getMember()).isEqualTo(member);
        List<OrderMenu> orderMenuList = order.getOrderMenuList();
        Assertions.assertThat(orderMenuList.size()).isEqualTo(2);
        Assertions.assertThat(order.getTotalPrice()).isEqualTo(Money.of(26000));
    }
}