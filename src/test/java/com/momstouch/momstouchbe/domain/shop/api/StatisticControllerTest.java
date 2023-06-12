package com.momstouch.momstouchbe.domain.shop.api;

import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.order.application.OrderInfo;
import com.momstouch.momstouchbe.domain.order.service.MenuInfo;
import com.momstouch.momstouchbe.domain.order.service.OrderService;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.momstouch.momstouchbe.domain.shop.model.repository.MenuRepository;
import com.momstouch.momstouchbe.global.vo.Money;
import com.momstouch.momstouchbe.setup.MemberSetup;
import com.momstouch.momstouchbe.setup.MenuInfoSetup;
import com.momstouch.momstouchbe.setup.OrderInfoSetup;
import com.momstouch.momstouchbe.setup.ShopSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class StatisticControllerTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    MockMvc mvc;
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
    @Autowired
    OrderInfoSetup orderInfoSetup;

    Long basicOrderId;
    Member member;
    Shop shop;
    Menu menu1;
    Menu menu2;

    @Test
    public void 통계_테스트_() throws Exception {
        member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "누네띠네 사장이된 김현석", "ROLE_OWNER");
        shop = shopSetup.saveShop(member,
                "누네띠네","학교앞가게" , "학교앞","010-0000-1111",
                LocalTime.of(9,0,0),LocalTime.of(23,0,0),20000);

        Long discountPolicyId = discountPolicyService.createAmountDiscountPolicy(shop,Integer.MAX_VALUE, 1000);

        menu1 = Menu.builder()
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


        menu2 = Menu.builder()
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
        shop.addMenu(menu1); shop.addMenu(menu2);

        em.flush();
        em.clear();

        MenuInfo menuInfo1 = menuInfoSetup.of(menu1, menu1.getOptionGroupList(), 1);
        MenuInfo menuInfo2 = menuInfoSetup.of(menu2, menu2.getOptionGroupList(), 2);

        OrderInfo orderInfo = orderInfoSetup.of(shop, member, List.of(menuInfo1, menuInfo2));

        basicOrderId = orderService.createOrder(orderInfo);

        mvc.perform(
                get("/api/statistic/shop/{shopId}" , shop.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8")
                    .accept(MediaType.APPLICATION_JSON)
                    .with(csrf())
        )
                .andDo(print())

                .andExpect(jsonPath("$.orderStatistic").isNotEmpty())
                    .andExpect(jsonPath("$.orderStatistic.totalOrderCount").value(1))
                    .andExpect(jsonPath("$.orderStatistic.totalOrderPrice").value(26000))
                .andExpect(jsonPath("$.menuStatistic.size()").value(2))
                    .andExpect(jsonPath("$.menuStatistic[0].count").value(1))
                    .andExpect(jsonPath("$.menuStatistic[1].count").value(2))
                .andExpect(status().isOk());



    }
}