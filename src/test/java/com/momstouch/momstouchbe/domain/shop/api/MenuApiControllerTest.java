package com.momstouch.momstouchbe.domain.shop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.DiscountPolicyRepository;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopSearchableRepository;
import com.momstouch.momstouchbe.global.domain.Money;
import com.momstouch.momstouchbe.setup.DiscountPolicySetup;
import com.momstouch.momstouchbe.setup.MemberSetup;
import com.momstouch.momstouchbe.setup.MockMultipartFileSetup;
import com.momstouch.momstouchbe.setup.ShopSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.List;

import static com.momstouch.momstouchbe.domain.shop.dto.MenuRequest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MenuApiControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMultipartFileSetup mockMultipartFileSetup;

    @Autowired
    ShopSetup shopSetup;

    @Autowired
    MemberSetup memberSetup;

    @Autowired
    DiscountPolicySetup discountPolicySetup;

    @Autowired
    ShopSearchableRepository shopSearchableRepository;

    @Autowired
    DiscountPolicyRepository discountPolicyRepository;



    @Test
    void 메뉴조회_성공() throws Exception {

        Member member = memberSetup.saveMember("test", "test1234!", "홍길동", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member, "맘스터치 금오공대점", "햄버거집입니다.", "구미시 대학로61", "010-1234-5678", LocalTime.of(10, 0), LocalTime.of(21, 0), 5000);

        Long discountPolicyId = discountPolicySetup.saveAmountDiscountPolicy(shop,10000, 1000);

        Menu menu1 = Menu.builder()
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
        Menu menu2 = Menu.builder()
                .category(Category.MAIN)
                .name("화이트갈릭버거")
                .description("마요 갈릭 버거입니다.")
                .price(Money.of(5500))
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
        shop.addMenu(menu1);
        shop.addMenu(menu2);

        ResultActions resultActions = mvc.perform(
                get("/api/shop/{shopId}/menus", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.menuList.size()").value(2))
                .andExpect(jsonPath("$.menuList[0].name").value(menu1.getName()))
                .andExpect(jsonPath("$.menuList[1].name").value(menu2.getName()));

    }

    @Test
    void 메뉴추가_성공() throws Exception {

        Member member = memberSetup.saveMember("test", "test1234!", "홍길동", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member, "맘스터치 금오공대점", "햄버거집입니다.", "구미시 대학로61", "010-1234-5678", LocalTime.of(10, 0), LocalTime.of(21, 0), 5000);
        Long discountPolicyId = discountPolicySetup.saveAmountDiscountPolicy(shop,10000, 1000);
        MenuCreateRequest menuCreateRequest = MenuCreateRequest.builder()
                .category(Category.MAIN)
                .name("싸이버거")
                .description("풍미좋은 햄버거")
                .price(6500)
                .discountPolicyId(discountPolicyId)
                .optionGroupList(
                        List.of(
                                OptionGroupRequest.builder().name("단품, 세트 선택")
                                        .optionList(
                                                List.of(OptionRequest.builder().name("단품").price(0).build(),
                                                        OptionRequest.builder().name("세트").price(3000).build()))
                                        .build(),
                                OptionGroupRequest.builder()
                                        .name("사이드 추가")
                                        .optionList(
                                                List.of(OptionRequest.builder().name("콜라").price(1500).build(),
                                                        OptionRequest.builder().name("감자튀김").price(2000).build()))
                                        .build()
                        ))
                .build();

        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.multipart("/api/shop/{shopId}/menus", shop.getId())
                        .file("image", mockMultipartFileSetup.getMockMultipartFile().getBytes())
                        .file(new MockMultipartFile("menu", "menu", "application/json", objectMapper.writeValueAsString(menuCreateRequest).getBytes(StandardCharsets.UTF_8)))
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isOk());

        Shop findShop = shopSearchableRepository.findMenuListByShopId(shop.getId());
        Assertions.assertEquals(1, findShop.getMenuList().size(), "메뉴가 추가되었는지 확인");
    }



}