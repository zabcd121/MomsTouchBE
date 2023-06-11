package com.momstouch.momstouchbe.domain.cart.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momstouch.momstouchbe.domain.cart.application.CartService;
import com.momstouch.momstouchbe.domain.cart.model.Cart;
import com.momstouch.momstouchbe.domain.cart.model.repository.CartQueryRepository;
import com.momstouch.momstouchbe.domain.cart.model.repository.CartRepository;
import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.DiscountPolicyRepository;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.momstouch.momstouchbe.global.domain.Money;
import com.momstouch.momstouchbe.setup.DiscountPolicySetup;
import com.momstouch.momstouchbe.setup.MemberSetup;
import com.momstouch.momstouchbe.setup.ShopSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.util.List;

import static com.momstouch.momstouchbe.domain.cart.dto.CartRequest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CartApiControllerTest {
    @Autowired
    EntityManager em;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ShopSetup shopSetup;

    @Autowired
    MemberSetup memberSetup;

    @Autowired
    DiscountPolicySetup discountPolicySetup;

    @Autowired
    DiscountPolicyRepository discountPolicyRepository;

    @Autowired
    CartQueryRepository cartQueryRepository;

    @Autowired
    CartService cartService;

    @Test
    void 장바구니추가_성공() throws Exception {
        Member member = memberSetup.saveMember("test", "test1234!", "홍길동", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member, "맘스터치 금오공대점", "햄버거집입니다.", "구미시 대학로61", "010-1234-5678", LocalTime.of(10, 0), LocalTime.of(21, 0), 5000);

        Long discountPolicyId = discountPolicySetup.saveAmountDiscountPolicy(shop, 10000, 1000);

        Menu menu1 = getMenu1(discountPolicyId);

        shop.addMenu(menu1);

        em.flush();

        CartMenuAddRequest cartMenuAddRequest = getCarMenuAddRequest(menu1);

        ResultActions resultActions = mvc.perform(
                post("/api/members/{memberId}/carts", member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartMenuAddRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Cart findCart = cartQueryRepository.findByMemberId(member.getId());
        assertEquals(cartMenuAddRequest.getMenuId(), findCart.getCartMenuList().get(0).getMenuId());
        assertEquals(cartMenuAddRequest.getQuantity(), findCart.getCartMenuList().get(0).getQuantity());
        assertEquals(cartMenuAddRequest.getCartMenuOptionGroupList().get(0).getMenuOptionGroupId(), findCart.getCartMenuList().get(0).getCartMenuOptionGroupList().get(0).getMenuOptionGroupId());
        assertEquals(cartMenuAddRequest.getCartMenuOptionGroupList().get(0).getCartMenuOptionList().get(0).getMenuOptionId(),
                findCart.getCartMenuList().get(0).getCartMenuOptionGroupList().get(0).getCartMenuOptionList().get(0).getMenuOptionId());

    }

    @Test
    void 장바구니추가_실패_운영시간X() throws Exception {
        Member member = memberSetup.saveMember("test", "test1234!", "홍길동", "ROLE_OWNER");
        LocalTime now = LocalTime.now();
        Shop shop = shopSetup.saveShop(member, "맘스터치 금오공대점", "햄버거집입니다.", "구미시 대학로61", "010-1234-5678", LocalTime.of(now.getHour()+1, 30), LocalTime.of(now.getHour()+4, 0), 5000);

        Long discountPolicyId = discountPolicySetup.saveAmountDiscountPolicy(shop, 10000, 1000);

        Menu menu1 = getMenu1(discountPolicyId);

        shop.addMenu(menu1);

        em.flush();

        CartMenuAddRequest cartMenuAddRequest = getCarMenuAddRequest(menu1);

        mvc.perform(
                        post("/api/members/{memberId}/carts", member.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(cartMenuAddRequest))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }

    @Test
    void 장바구니조회_성공() throws Exception {
        Member member = memberSetup.saveMember("test", "test1234!", "홍길동", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member, "맘스터치 금오공대점", "햄버거집입니다.", "구미시 대학로61", "010-1234-5678", LocalTime.of(10, 0), LocalTime.of(21, 0), 5000);

        Long discountPolicyId = discountPolicySetup.saveAmountDiscountPolicy(shop, 10000, 1000);

        Menu menu1 = getMenu1(discountPolicyId);

        shop.addMenu(menu1);

        em.flush();
        CartMenuAddRequest cartMenuAddRequest = getCarMenuAddRequest(menu1);
        cartService.addCartMenu(member.getId(), cartMenuAddRequest);
        em.flush();

        mvc.perform(
                get("/api/members/{memberId}/carts", member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Cart cart = cartQueryRepository.findByMemberId(member.getId());
        assertEquals(1, cart.getCartMenuList().size());
        assertEquals(2, cart.getCartMenuList().get(0).getCartMenuOptionGroupList().size());
        assertEquals(1, cart.getCartMenuList().get(0).getCartMenuOptionGroupList().get(0).getCartMenuOptionList().size());
        assertEquals(1, cart.getCartMenuList().get(0).getCartMenuOptionGroupList().get(1).getCartMenuOptionList().size());


    }

    Menu getMenu1(Long discountPolicyId) {
        return Menu.builder()
                .category(Category.MAIN)
                .name("싸이버거")
                .description("풍미좋은 햄버거")
                .price(Money.of(6500))
                .discountPolicy(discountPolicyRepository.getReferenceById(discountPolicyId))
                .optionGroupList(
                        List.of(
                                OptionGroupSpecification.builder().name("단품, 세트 선택")
                                        .optionList(
                                                List.of(OptionSpecification.builder().name("단품선택").price(Money.of(0)).build(),
                                                        OptionSpecification.builder().name("세트변경").price(Money.of(1000)).build())
                                        ).build(),
                                OptionGroupSpecification.builder().name("사이드 선택")
                                        .optionList(
                                                List.of(OptionSpecification.builder().name("감자튀김").price(Money.of(1500)).build(),
                                                        OptionSpecification.builder().name("치즈스틱").price(Money.of(2000)).build())
                                        ).build(),
                                OptionGroupSpecification.builder().name("음료 선택")
                                        .optionList(
                                                List.of(OptionSpecification.builder().name("콜라").price(Money.of(1000)).build(),
                                                        OptionSpecification.builder().name("사이다").price(Money.of(1000)).build(),
                                                        OptionSpecification.builder().name("커피").price(Money.of(1500)).build())
                                        ).build()
                        )
                ).build();
    }

    CartMenuAddRequest getCarMenuAddRequest(Menu menu) {
        return CartMenuAddRequest.builder()
                .menuId(menu.getId())
                .discountPolicyId(menu.getDiscountPolicy().getId())
                .quantity(2)
                .price(menu.getPrice().getAmount().intValueExact())
                .cartMenuOptionGroupList(
                        List.of(
                                CartMenuOptionGroupRequest.builder()
                                        .menuOptionGroupId(menu.getOptionGroupList().get(0).getId())
                                        .cartMenuOptionList(
                                                List.of(CartMenuOptionRequest.builder()
                                                        .menuOptionId(menu.getOptionGroupList().get(0).getOptionList().get(0).getId())
                                                        .price(1000)
                                                        .build())
                                        ).build(),
                                CartMenuOptionGroupRequest.builder()
                                        .menuOptionGroupId(menu.getOptionGroupList().get(1).getId())
                                        .cartMenuOptionList(
                                                List.of(CartMenuOptionRequest.builder()
                                                        .menuOptionId(menu.getOptionGroupList().get(1).getOptionList().get(0).getId())
                                                        .price(1000)
                                                        .build()
                                        )).build()
                        )
                ).build();
    }
}