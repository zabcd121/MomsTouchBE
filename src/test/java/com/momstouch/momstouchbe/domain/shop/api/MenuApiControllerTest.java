package com.momstouch.momstouchbe.domain.shop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momstouch.momstouchbe.domain.discountpolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.DiscountPolicyRepository;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopSearchableRepository;
import com.momstouch.momstouchbe.global.vo.Money;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Arrays;
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
    EntityManager em;

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

        Long discountPolicyId = discountPolicySetup.saveAmountDiscountPolicy(shop, 10000, 1000);

        Menu menu1 = getMenu1(discountPolicyId);
        Menu menu2 = getMenu2(discountPolicyId);
        shop.addMenu(menu1);
        shop.addMenu(menu2);

        ResultActions resultActions = mvc.perform(
                        get("/api/shop/{shopId}/menus", shop.getId())
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
        Long discountPolicyId = discountPolicySetup.saveAmountDiscountPolicy(shop, 10000, 1000);
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
                        multipart("/api/shop/{shopId}/menus", shop.getId())
                                .file("image", mockMultipartFileSetup.getMockMultipartFile().getBytes())
                                .file(new MockMultipartFile("menu", "menu", "application/json", objectMapper.writeValueAsString(menuCreateRequest).getBytes(StandardCharsets.UTF_8)))
                                .characterEncoding("UTF-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isOk());

        Shop findShop = shopSearchableRepository.findWithMenuListByShopId(shop.getId());
        Assertions.assertEquals(1, findShop.getMenuList().size(), "메뉴가 추가되었는지 확인");
    }

    @Test
    void 메뉴상세조회_성공() throws Exception {
        Member member = memberSetup.saveMember("test", "test1234!", "홍길동", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member, "맘스터치 금오공대점", "햄버거집입니다.", "구미시 대학로61", "010-1234-5678", LocalTime.of(10, 0), LocalTime.of(21, 0), 5000);

        Long discountPolicyId = discountPolicySetup.saveAmountDiscountPolicy(shop, 10000, 1000);

        Menu menu1 = getMenu1(discountPolicyId);

        shop.addMenu(menu1);
        em.flush();

        ResultActions resultActions = mvc.perform(
                        get("/api/shop/{shopId}/menus/{menuId}", shop.getId(), menu1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(menu1.getName()))
                .andExpect(jsonPath("$.description").value(menu1.getDescription()))
                .andExpect(jsonPath("$.price").value(menu1.getPrice().getAmount().intValueExact()))
                .andExpect(jsonPath("$.discountPolicy.type").value(AmountDiscountPolicy.class.getName()))
                .andExpect(jsonPath("$.optionGroupList.size()").value(menu1.getOptionGroupList().size()))
                .andExpect(jsonPath("$.optionGroupList[0].name").value(menu1.getOptionGroupList().get(0).getName()))
                .andExpect(jsonPath("$.optionGroupList[0].optionList.size()").value(menu1.getOptionGroupList().get(0).getOptionList().size()))
                .andExpect(jsonPath("$.optionGroupList[0].optionList[0].name").value(menu1.getOptionGroupList().get(0).getOptionList().get(0).getName()))
                .andExpect(jsonPath("$.optionGroupList[0].optionList[0].price").value(menu1.getOptionGroupList().get(0).getOptionList().get(0).getPrice().getAmount().intValueExact()))
                .andExpect(jsonPath("$.optionGroupList[0].optionList[1].name").value(menu1.getOptionGroupList().get(0).getOptionList().get(1).getName()))
                .andExpect(jsonPath("$.optionGroupList[0].optionList[1].price").value(menu1.getOptionGroupList().get(0).getOptionList().get(1).getPrice().getAmount().intValueExact()))
                .andExpect(jsonPath("$.optionGroupList[1].name").value(menu1.getOptionGroupList().get(1).getName()))
                .andExpect(jsonPath("$.optionGroupList[1].optionList.size()").value(menu1.getOptionGroupList().get(1).getOptionList().size()))
                .andExpect(jsonPath("$.optionGroupList[1].optionList[0].name").value(menu1.getOptionGroupList().get(1).getOptionList().get(0).getName()))
                .andExpect(jsonPath("$.optionGroupList[1].optionList[0].price").value(menu1.getOptionGroupList().get(1).getOptionList().get(0).getPrice().getAmount().intValueExact()))
                .andExpect(jsonPath("$.optionGroupList[1].optionList[1].name").value(menu1.getOptionGroupList().get(1).getOptionList().get(1).getName()))
                .andExpect(jsonPath("$.optionGroupList[1].optionList[1].price").value(menu1.getOptionGroupList().get(1).getOptionList().get(1).getPrice().getAmount().intValueExact()));

    }

    @Test
    void 메뉴수정_성공() throws Exception {
        Member member = memberSetup.saveMember("test", "test1234!", "홍길동", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member, "맘스터치 금오공대점", "햄버거집입니다.", "구미시 대학로61", "010-1234-5678", LocalTime.of(10, 0), LocalTime.of(21, 0), 5000);

        Long discountPolicyId = discountPolicySetup.saveAmountDiscountPolicy(shop, 10000, 1000);

        Menu menu1 = getMenu1(discountPolicyId);

        shop.addMenu(menu1);
        em.flush();

        MenuUpdateRequest menuUpdateRequest = MenuUpdateRequest.builder()
                .name("불싸이버거")
                .description("매우 싸이 버거")
                .price(4700)
                .discountPolicyId(discountPolicyId)
                .optionGroupList(
                        List.of(
                                OptionGroupRequest.builder().name("단품, 세트 선택 변경")
                                        .optionList(
                                                Arrays.asList(OptionRequest.builder().name("1. 단품").price(0).build(),
                                                        OptionRequest.builder().name("2. 세트변경").price(3000).build()))
                                        .build(),
                                OptionGroupRequest.builder()
                                        .name("사이드 추가")
                                        .optionList(
                                                Arrays.asList(OptionRequest.builder().name("감자튀김2").price(2500).build(),
                                                        OptionRequest.builder().name("치즈스틱2").price(1000).build()))
                                        .build(),
                                OptionGroupRequest.builder().name("음료 선택")
                                        .optionList(
                                                Arrays.asList(OptionRequest.builder().name("환타").price(1600).build(),
                                                        OptionRequest.builder().name("맥콜").price(1600).build(),
                                                        OptionRequest.builder().name("탐스제로").price(1600).build())
                                        ).build(),
                                OptionGroupRequest.builder().name("추가 배달료 선택")
                                        .optionList(
                                                Arrays.asList(OptionRequest.builder().name("거의동").price(1000).build(),
                                                        OptionRequest.builder().name("양호동").price(1500).build(),
                                                        OptionRequest.builder().name("신평동").price(2500).build())
                                        ).build()
                        ))
                .build();

        MockMultipartHttpServletRequestBuilder builder = multipart("/api/shop/{shopId}/menus/{menuId}", shop.getId(), menu1.getId());
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        ResultActions resultActions = mvc.perform(
                        builder
                                .file("image", mockMultipartFileSetup.getMockMultipartFile().getBytes())
                                .file(new MockMultipartFile("menu", "menu", "application/json", objectMapper.writeValueAsString(menuUpdateRequest).getBytes(StandardCharsets.UTF_8)))
                                .characterEncoding("UTF-8")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Shop findShop = shopSearchableRepository.findWithMenuListByShopId(shop.getId());
        Assertions.assertEquals(menuUpdateRequest.getName(), findShop.getMenuList().get(0).getName(), "메뉴 이름이 변경되었는지 확인");
        Assertions.assertEquals(menuUpdateRequest.getDescription(), findShop.getMenuList().get(0).getDescription(), "메뉴 설명이 변경되었는지 확인");
        Assertions.assertEquals(menuUpdateRequest.getPrice(), findShop.getMenuList().get(0).getPrice().getAmount().intValueExact(), "메뉴 가격이 변경되었는지 확인");
        Assertions.assertEquals(menuUpdateRequest.getPrice(), findShop.getMenuList().get(0).getPrice().getAmount().intValueExact(), "메뉴 가격이 변경되었는지 확인");
        Assertions.assertEquals(menuUpdateRequest.getOptionGroupList().size(), findShop.getMenuList().get(0).getOptionGroupList().size(), "메뉴 옵션 그룹 개수가 변경되었는지 확인");
        Assertions.assertEquals(menuUpdateRequest.getOptionGroupList().get(0).getName(), findShop.getMenuList().get(0).getOptionGroupList().get(0).getName(), "옵션 그룹 이름 변경 확인");
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

    Menu getMenu2(Long discountPolicyId) {
        return Menu.builder()
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
    }

}