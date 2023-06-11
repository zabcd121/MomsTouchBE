package com.momstouch.momstouchbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountRequest;
import com.momstouch.momstouchbe.domain.discountpolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.DiscountPolicyRepository;
import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.shop.dto.MenuRequest;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.momstouch.momstouchbe.domain.shop.model.repository.MenuRepository;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopSearchableRepository;
import com.momstouch.momstouchbe.global.domain.Money;
import com.momstouch.momstouchbe.setup.DiscountPolicySetup;
import com.momstouch.momstouchbe.setup.MemberSetup;
import com.momstouch.momstouchbe.setup.MockMultipartFileSetup;
import com.momstouch.momstouchbe.setup.ShopSetup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
import java.util.UUID;

import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountRequest.*;
import static com.momstouch.momstouchbe.domain.shop.dto.MenuRequest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class TestClass {
    @Autowired EntityManager em;
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired MockMultipartFileSetup mockMultipartFileSetup;
    @Autowired ShopSetup shopSetup;
    @Autowired MemberSetup memberSetup;
    @Autowired DiscountPolicyService discountPolicyService;
    @Autowired ShopSearchableRepository shopSearchableRepository;
    @Autowired DiscountPolicyRepository discountPolicyRepository;
    @Autowired MenuRepository menuRepository;


    @Test()
    @DisplayName(value = "메뉴가 올바르게 등록 되는가? ")
    void 메뉴_등록() throws Exception {
        Member member = memberSetup.saveMember("rlagustjr",
                UUID.randomUUID().toString(),
                "김현석",
                "ROLE_OWNER");

        Shop shop = shopSetup.saveShop(member,
                "맘스터치 금오공대점",
                "햄버거집입니다.",
                "구미시 대학로61",
                "010-1234-5678",
                LocalTime.of(10, 0),
                LocalTime.of(21, 0), 5000);

        Long discountPolicyId = discountPolicyService.createTimeDiscountPolicy(shop, LocalTime.of(23,0,0), 1000); //할인 정책 메뉴가 만원 넘을시 1000원 할인

        String menuName = "싸이버거";
        String menuDescription = "풍미좋은 햄버거";

        MenuCreateRequest menuCreateRequest = MenuCreateRequest.builder()
                .category(Category.MAIN)                //주문할 메뉴는 싸이버거
                .name(menuName)
                .description(menuDescription)
                .price(6500)                        //메뉴 가격은 6500원
                .discountPolicyId(discountPolicyId)
                .optionGroupList(       //선택할 수 있는 옵션의 목록
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
        List<Menu> menuList = findShop.getMenuList();
        Assertions.assertThat(menuList.size()).isEqualTo(1);
        Menu menu = menuList.get(0);

        Assertions.assertThat(menu.getName()).isEqualTo(menuName);
        Assertions.assertThat(menu.getDescription()).isEqualTo(menuDescription);
        Assertions.assertThat(menu.getOptionGroupList().size()).isEqualTo(2);
        DiscountPolicy discountPolicy = menu.getDiscountPolicy();

        Assertions.assertThat(discountPolicy.getId()).isEqualTo(discountPolicyId);
    }

    @Test()
    @DisplayName(value = "동일한 메뉴명에 대하여 예외처리를 수행하였는가?")
    void 동일한_메뉴_등록() throws Exception {
        Member member = memberSetup.saveMember("rlagustjr",
                UUID.randomUUID().toString(),
                "김현석",
                "ROLE_OWNER");

        Shop shop = shopSetup.saveShop(member, //주문할 가게
                "맘스터치 금오공대점",
                "햄버거집입니다.",
                "구미시 대학로61",
                "010-1234-5678",
                LocalTime.of(10, 0),
                LocalTime.of(21, 0), 5000);

        Long discountPolicyId = discountPolicyService.createRateDiscountPolicy(shop, 10000, 10.0);

        String menuName = "싸이버거";
        String menuDescription = "풍미좋은 햄버거";

        MenuCreateRequest menuCreateRequest = MenuCreateRequest.builder()
                .category(Category.MAIN)                // 메뉴는 싸이버거
                .name(menuName)
                .description(menuDescription)
                .price(6500)                        //메뉴 가격은 6500원
                .discountPolicyId(discountPolicyId)
                .optionGroupList(       //선택할 수 있는 옵션의 목록
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

        mvc.perform(
                multipart("/api/shop/{shopId}/menus", shop.getId())
                        .file("image", mockMultipartFileSetup.getMockMultipartFile().getBytes())
                        .file(new MockMultipartFile("menu", "menu", "application/json", objectMapper.writeValueAsString(menuCreateRequest).getBytes(StandardCharsets.UTF_8)))
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON));

        MenuCreateRequest menuCreateFailRequest = MenuCreateRequest.builder()
                .category(Category.MAIN)
                .name(menuName)
                .description("등록에 실패할 메뉴 설명")
                .price(7000)
                .discountPolicyId(discountPolicyId)
                .optionGroupList(List.of())
                .build();

        ResultActions resultActions = mvc.perform(
                        multipart("/api/shop/{shopId}/menus", shop.getId())
                                .file("image", mockMultipartFileSetup.getMockMultipartFile().getBytes())
                                .file(new MockMultipartFile("menu", "menu", "application/json", objectMapper.writeValueAsString(menuCreateFailRequest).getBytes(StandardCharsets.UTF_8)))
                                .characterEncoding("UTF-8")
                                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(value = "메뉴 이름, 가격, 메뉴 설명이 올바르게 수정되는가?")
    void 메뉴_수정() throws Exception{
        Member member = memberSetup.saveMember("test", "test1234!", "홍길동", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member, "맘스터치 금오공대점", "햄버거집입니다.", "구미시 대학로61", "010-1234-5678", LocalTime.of(10, 0), LocalTime.of(21, 0), 5000);

        Long discountPolicyId = discountPolicyService.createAmountDiscountPolicy(shop, 10000, 1000);

        Menu menu = Menu.builder()
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

        shop.addMenu(menu);
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

        MockMultipartHttpServletRequestBuilder builder = multipart("/api/shop/{shopId}/menus/{menuId}", shop.getId(), menu.getId());
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        mvc.perform(
            builder
                .file("image", mockMultipartFileSetup.getMockMultipartFile().getBytes())
                .file(new MockMultipartFile("menu", "menu", "application/json", objectMapper.writeValueAsString(menuUpdateRequest).getBytes(StandardCharsets.UTF_8)))
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

        Shop findShop = shopSearchableRepository.findWithMenuListByShopId(shop.getId());
        assertEquals(menuUpdateRequest.getName(), findShop.getMenuList().get(0).getName(), "메뉴 이름이 변경되었는지 확인");
        assertEquals(menuUpdateRequest.getDescription(), findShop.getMenuList().get(0).getDescription(), "메뉴 설명이 변경되었는지 확인");
        assertEquals(menuUpdateRequest.getPrice(), findShop.getMenuList().get(0).getPrice().getAmount().intValueExact(), "메뉴 가격이 변경되었는지 확인");
        assertEquals(menuUpdateRequest.getPrice(), findShop.getMenuList().get(0).getPrice().getAmount().intValueExact(), "메뉴 가격이 변경되었는지 확인");
        assertEquals(menuUpdateRequest.getOptionGroupList().size(), findShop.getMenuList().get(0).getOptionGroupList().size(), "메뉴 옵션 그룹 개수가 변경되었는지 확인");
        assertEquals(menuUpdateRequest.getOptionGroupList().get(0).getName(), findShop.getMenuList().get(0).getOptionGroupList().get(0).getName(), "옵션 그룹 이름 변경 확인");
    }

    @Test
    @DisplayName(value = "할인 정책을 설정할 수 있는가?")
    void 할인_설정() throws Exception {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "누네띠네 사장이된 김현석", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member,
                "누네띠네","학교앞가게" , "학교앞","010-0000-1111",
                LocalTime.of(9,0,0),LocalTime.of(23,0,0),20000);

        Long timeDiscountPolicyId = discountPolicyService.createTimeDiscountPolicy(shop, LocalTime.of(12, 0, 0), 1000);

        Menu menu = Menu.builder()
                .category(Category.MAIN)
                .name("인크레더블 버거")
                .description("계란과 새우 환상의 조합")
                .price(Money.of(7100))
                .discountPolicy(discountPolicyRepository.getReferenceById(timeDiscountPolicyId))
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
                                                        OptionSpecification.builder().name("사과유자차").price(Money.of(3500)).build())
                                        ).build()
                        )
                ).build();

        shop.addMenu(menu);
        em.flush();
        em.clear();
        Long menuId = menu.getId();
        UpdateDiscountPolicyRequest updateDiscountPolicyRequest = new UpdateDiscountPolicyRequest(menuId);
        mvc.perform(
                post("/api/discountPolicy/{discountPolicyId}",timeDiscountPolicyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDiscountPolicyRequest))
                        .with(csrf())
        ).andDo(print())
                .andExpect(status().isOk());

        Menu findMenu = menuRepository.findById(menuId).orElseThrow();
        DiscountPolicy discountPolicy = findMenu.getDiscountPolicy();

        Assertions.assertThat(discountPolicy.getId()).isEqualTo(timeDiscountPolicyId);

    }

    @Test
    @DisplayName(value = "운영 시간이 아닌 시점에서 장바구니 담기 예외가 발생하는가?")
    void 주문_장바구니_운영시간이_아닐때() {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "집게리아 사장이된 김현석", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member,
                "집게리아","집게리아에 오신걸 환영합니다." , "바다속","010-0000-1111",
                LocalTime.of(0,0,0),LocalTime.of(23,59,59),8000);

        Long timeDiscountPolicyId = discountPolicyService.createTimeDiscountPolicy(shop, LocalTime.of(12, 0, 0), 1000);

        Menu menu = Menu.builder()
                .category(Category.MAIN)
                .name("집게 버거")
                .description("사장님의 추천픽")
                .price(Money.of(5800))
                .discountPolicy(discountPolicyRepository.getReferenceById(timeDiscountPolicyId))
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
                                                        OptionSpecification.builder().name("해파리 냉채").price(Money.of(2500)).build())
                                        ).build()
                        )
                ).build();

        shop.addMenu(menu);
        em.flush();
        em.clear();

        //TODO : 나머지 해야함
//        mvc.perform(
//                post("/api/members/{memberId}/{carts}",)
//        )
    }

}
