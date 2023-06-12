package com.momstouch.momstouchbe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momstouch.momstouchbe.domain.cart.application.CartService;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.DiscountPolicyRepository;
import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.order.application.OrderAppService;
import com.momstouch.momstouchbe.domain.order.application.OrderInfo;
import com.momstouch.momstouchbe.domain.order.dto.OrderRequest.CreateOrderRequest;
import com.momstouch.momstouchbe.domain.order.model.Order;
import com.momstouch.momstouchbe.domain.order.model.OrderStatus;
import com.momstouch.momstouchbe.domain.order.model.repository.OrderRepository;
import com.momstouch.momstouchbe.domain.order.service.MenuInfo;
import com.momstouch.momstouchbe.domain.order.service.OrderService;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.momstouch.momstouchbe.domain.shop.model.repository.MenuRepository;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopSearchableRepository;
import com.momstouch.momstouchbe.global.vo.BaseTime;
import com.momstouch.momstouchbe.global.vo.Money;
import com.momstouch.momstouchbe.setup.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.momstouch.momstouchbe.domain.cart.dto.CartRequest.*;
import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountRequest.*;
import static com.momstouch.momstouchbe.domain.shop.dto.MenuRequest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @Autowired CartService cartService;
    @Autowired
    OrderInfoSetup orderInfoSetup;

    @Autowired MenuInfoSetup menuInfoSetup;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderAppService orderAppService;
    @Autowired
    OrderRepository orderRepository;

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

        Long discountPolicyId = discountPolicyService.createTimeDiscountPolicy(shop, LocalTime.of(23, 0, 0), 1000); //할인 정책 메뉴가 만원 넘을시 1000원 할인

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
    void 메뉴_수정() throws Exception {
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
                "누네띠네", "학교앞가게", "학교앞", "010-0000-1111",
                LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0), 20000);

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
                        post("/api/discountPolicy/{discountPolicyId}", timeDiscountPolicyId)
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
    void 주문_장바구니_운영시간이_아닐때() throws Exception {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "집게리아 사장이된 김현석", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member,
                "집게리아", "집게리아에 오신걸 환영합니다.", "바다속", "010-0000-1111",
                LocalTime.of(0, 0, 0), LocalTime.of(0, 0, 1), 8000);

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

        CartMenuAddRequest request = CartMenuAddRequest.builder()
                .menuId(menu.getId())
                .discountPolicyId(timeDiscountPolicyId)
                .quantity(1)
                .price(5800)
                .build();

        mvc.perform(
                        post("/api/members/{memberId}/carts", member.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf())
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(value = "장바구니에 주메뉴가 없는 상태에서 주문을 시도할 경우 예외가 발생하는가?")
    void 장바구니에_주_메뉴가_없을때() throws Exception {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "집게리아 사장이된 김현석", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member,
                "집게리아","집게리아에 오신걸 환영합니다." , "바다속","010-0000-1111",
                LocalTime.of(0,0,0),LocalTime.of(23,59,59),8000);

        Long timeDiscountPolicyId = discountPolicyService.createTimeDiscountPolicy(shop, LocalTime.of(12, 0, 0), 1000);


        Menu menu = Menu.builder()
                .category(Category.SIDE)
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

        Menu menu2 = Menu.builder()
                .category(Category.SIDE)
                .name("치킨무")
                .description("현석이는 치킨무를 싫어합니다")
                .price(Money.of(500))
                .discountPolicy(discountPolicyRepository.getReferenceById(timeDiscountPolicyId))
                .optionGroupList(List.of()).build();

        shop.addMenu(menu);
        shop.addMenu(menu2);

        em.flush();
        em.clear();

        MenuInfo menuInfo1 = menuInfoSetup.of(menu, menu.getOptionGroupList(), 1);
        MenuInfo menuInfo2 = menuInfoSetup.of(menu2, menu2.getOptionGroupList(), 2);

        OrderInfo orderInfo = orderInfoSetup.of(shop, member, List.of(menuInfo1, menuInfo2));

        CreateOrderRequest request = CreateOrderRequest.builder()
                .address("디지털관 337호")
                .phoneNumber("010-1234-5678")
                .shopId(shop.getId())
                .orderMenuList(List.of(menuInfo1, menuInfo2))
                .build();

        Assertions.assertThatThrownBy(() -> {
            orderService.createOrder(orderInfo);
        }).isInstanceOf(IllegalStateException.class);

        mvc.perform(
                        post("/api/order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf())
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(value = "주문 총 가격이 8000원 미만일 경우 예외가 발생하는가?")
    void 주문_장바구니에_가격이_8000원_미만일때() throws Exception {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "집게리아 사장이된 김현석", "ROLE_OWNER");
        Shop shop = shopSetup.saveShop(member,
                "집게리아", "집게리아에 오신걸 환영합니다.", "바다속", "010-0000-1111",
                LocalTime.of(0, 0, 0), LocalTime.of(0, 0, 1), 8000);

        Long timeDiscountPolicyId = discountPolicyService.createTimeDiscountPolicy(shop, LocalTime.of(1, 0, 0), 1000);

        Menu menu = Menu.builder()
                .category(Category.MAIN)
                .name("집게 버거")
                .description("사장님의 추천픽")
                .price(Money.of(1000))
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

        List<OptionGroupSpecification> selectOption = List.of(
                OptionGroupSpecification.builder().name("단품, 세트 선택")
                        .optionList(List.of(OptionSpecification.builder().name("세트변경").price(Money.of(1000)).build())).build(),
                OptionGroupSpecification.builder().name("사이드 선택")
                        .optionList(List.of(OptionSpecification.builder().name("감자튀김").price(Money.of(1500)).build())).build(),
                OptionGroupSpecification.builder().name("음료 선택")
                        .optionList(List.of(OptionSpecification.builder().name("콜라").price(Money.of(1000)).build())).build()
        );

        MenuInfo menuInfo1 = menuInfoSetup.of(menu, selectOption, 1);

        OrderInfo orderInfo = orderInfoSetup.of(shop, member, List.of(menuInfo1));

        CreateOrderRequest request = CreateOrderRequest.builder()
                .address("디지털관 337호")
                .phoneNumber("010-1234-5678")
                .shopId(shop.getId())
                .orderMenuList(List.of(menuInfo1))
                .build();

        Assertions.assertThatThrownBy(() -> {
            orderService.createOrder(orderInfo);
        }).isInstanceOf(IllegalStateException.class);

        mvc.perform(
                        post("/api/order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(csrf())
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(value = "주문의 최초 상태는 주문인가? + 주문 금액이 (모든)할인 정책을 반영하고 있는가?")
    void 주문_생성_테스트() throws Exception {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "김현석", "ROLE_USER");
        Shop shop = shopSetup.saveShop(member,
                "누네띠네", "학교앞가게", "학교앞", "010-0000-1111",
                LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0), 20000);

        Long rateDiscountPolicy = discountPolicyService.createRateDiscountPolicy(shop, 8000, 10.0);
        Long discountPolicyId = discountPolicyService.createAmountDiscountPolicy(shop, 10000, 1000);
        Long timeDiscountPolicyId = discountPolicyService.createTimeDiscountPolicy(shop, LocalTime.of(23, 59, 59), 1000);

        Menu menu1 = Menu.builder()
                .category(Category.MAIN)
                .name("싸이버거")
                .description("풍미좋은 햄버거")
                .price(Money.of(6500)) //6500 + 1500 = 8000
                .discountPolicy(discountPolicyService.findById(rateDiscountPolicy).get())
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

        Menu menu3 = Menu.builder()
                .category(Category.MAIN)
                .name("싸이순살뿌치")
                .description("맛있는 순살 치킨입니다.")
                .price(Money.of(10_000)) //(10000 + 3000) = 13000
                .discountPolicy(discountPolicyService.findById(timeDiscountPolicyId).get())
                .optionGroupList(
                        List.of(
                                OptionGroupSpecification.builder().name("소스 선택")
                                        .optionList(List.of(OptionSpecification.builder().name("특제 소스").price(Money.of(1000)).build())).build(),
                                OptionGroupSpecification.builder().name("음료 선택")
                                        .optionList( //1000
                                                List.of(OptionSpecification.builder().name("콜라").price(Money.of(1000)).build(),
                                                        OptionSpecification.builder().name("사이다").price(Money.of(1000)).build()
                                                )).build()
                        )
                ).build();

        shop.addMenu(menu1);
        shop.addMenu(menu2);
        shop.addMenu(menu3);

        em.flush();
        em.clear();

        MenuInfo menuInfo1 = menuInfoSetup.of(menu1, menu1.getOptionGroupList(), 1);
        MenuInfo menuInfo2 = menuInfoSetup.of(menu2, menu2.getOptionGroupList(), 2);
        MenuInfo menuInfo3 = menuInfoSetup.of(menu3, menu3.getOptionGroupList(), 1);

        OrderInfo orderInfo = orderInfoSetup.of(shop, member, List.of(menuInfo1, menuInfo2, menuInfo3));

        Long orderId = orderService.createOrder(orderInfo);


        Optional<Order> byId = orderService.findById(orderId);

        Assertions.assertThat(byId.isPresent()).isTrue();

        Order order = byId.get();

        //계산 금액
        /**
         * 메뉴1 : 정률 정책 8000원 이상시 10% 할인 -> 8000원 -> 800원 할인
         * 메뉴2 : 정량 정책 10000원 이상시 1000원 할인 -> 18000 -> 1000원 할인
         * 메뉴3 : 조조 할인 적용시 1000원 할인 -> 13000 -> 1000원 할인
         *  총 금액 39000원 -> 2800원 할인 -> 36200
         */
        Assertions.assertThat(order.getTotalPrice()).isEqualTo(Money.of(36200));
    }

    @Test
    @DisplayName(value = "상태가 “배달중”인 주문에 대해 취소가 불가한가?")
    void 주문_고객_배달중_상태_변경() throws Exception {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "김현석", "ROLE_USER");
        Shop shop = shopSetup.saveShop(member,
                "누네띠네", "학교앞가게", "학교앞", "010-0000-1111",
                LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0), 10000);

        Long rateDiscountPolicy = discountPolicyService.createRateDiscountPolicy(shop, Integer.MAX_VALUE, 10.0);

        Menu menu1 = Menu.builder()
                .category(Category.MAIN)
                .name("싸이버거")
                .description("풍미좋은 햄버거")
                .price(Money.of(6500)) //6500 + 1500 = 8000
                .discountPolicy(discountPolicyService.findById(rateDiscountPolicy).get())
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

        MenuInfo menuInfo1 = menuInfoSetup.of(menu1, menu1.getOptionGroupList(), 2);
        OrderInfo orderInfo = orderInfoSetup.of(shop, member, List.of(menuInfo1));
        Long orderId = orderService.createOrder(orderInfo);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderAppService.accept(orderId, authentication); // 관리자가 주문을 승낙함


        orderAppService.deliver(orderId, authentication); // 배달중으로 변경

        ResultActions perform = mvc.perform( //취소 요청
                delete("/api/order/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        perform.andDo(print())
                .andExpect(status().isBadRequest());

        Order order = orderService.findById(orderId).get();
        OrderStatus orderStatus = order.getOrderStatus();
        Assertions.assertThat(orderStatus).isEqualTo(OrderStatus.DELIVERY);
    }


    @Test
    @DisplayName(value = "점주는 상태가 “주문”인 주문에 대해 접수가 가능하고 상태가 “배달중”으로 변경되는가?")
    void 주문_점주_주문_접수() throws Exception {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "김현석", "ROLE_USER");
        Shop shop = shopSetup.saveShop(member,
                "누네띠네", "학교앞가게", "학교앞", "010-0000-1111",
                LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0), 10000);

        Long rateDiscountPolicy = discountPolicyService.createRateDiscountPolicy(shop, Integer.MAX_VALUE, 10.0);

        Menu menu1 = Menu.builder()
                .category(Category.MAIN)
                .name("싸이버거")
                .description("풍미좋은 햄버거")
                .price(Money.of(6500)) //6500 + 1500 = 8000
                .discountPolicy(discountPolicyService.findById(rateDiscountPolicy).get())
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

        MenuInfo menuInfo1 = menuInfoSetup.of(menu1, menu1.getOptionGroupList(), 2);
        OrderInfo orderInfo = orderInfoSetup.of(shop, member, List.of(menuInfo1));
        Long orderId = orderService.createOrder(orderInfo); // 주문 생성

        mvc.perform( // 주문 승낙 요청
                        post("/api/order/{orderId}", orderId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());

        Order order = orderService.findById(orderId).get();
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ACCEPT);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderAppService.deliver(orderId, authentication);

        em.flush();
        em.clear();

        Order deliverOrder = orderService.findById(order.getId()).get();
        Assertions.assertThat(deliverOrder.getOrderStatus()).isEqualTo(OrderStatus.DELIVERY);
    }

    @Test
    @DisplayName(value = "점주가 배달완료를 수행하면 해당 주문의 상태가 “완료”로 변경되는가?")
    void 배달_완료() throws Exception {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "김현석", "ROLE_USER");
        Shop shop = shopSetup.saveShop(member,
                "누네띠네", "학교앞가게", "학교앞", "010-0000-1111",
                LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0), 10000);

        Long rateDiscountPolicy = discountPolicyService.createRateDiscountPolicy(shop, Integer.MAX_VALUE, 10.0);

        Menu menu1 = Menu.builder()
                .category(Category.MAIN)
                .name("싸이버거")
                .description("풍미좋은 햄버거")
                .price(Money.of(6500)) //6500 + 1500 = 8000
                .discountPolicy(discountPolicyService.findById(rateDiscountPolicy).get())
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

        MenuInfo menuInfo1 = menuInfoSetup.of(menu1, menu1.getOptionGroupList(), 2);
        OrderInfo orderInfo = orderInfoSetup.of(shop, member, List.of(menuInfo1));
        Long orderId = orderService.createOrder(orderInfo); // 주문 생성

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderAppService.accept(orderId,authentication);
        orderAppService.deliver(orderId,authentication);

        em.flush();
        em.clear();

        mvc.perform( // 주문 완료 요청
                        put("/api/order/{orderId}/complete", orderId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());

        Order order = orderService.findById(orderId).get();
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETE);
    }

    @Test
    void 일분안에주문접수안할시취소_성공() throws Exception {
        Member member = memberSetup.saveMember("loginId", UUID.randomUUID().toString(), "김현석", "ROLE_USER");
        Shop shop = shopSetup.saveShop(member,
                "누네띠네", "학교앞가게", "학교앞", "010-0000-1111",
                LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0), 20000);

        Long rateDiscountPolicy = discountPolicyService.createRateDiscountPolicy(shop, 8000, 10.0);

        Menu menu1 = Menu.builder()
                .category(Category.MAIN)
                .name("싸이버거")
                .description("풍미좋은 햄버거")
                .price(Money.of(6500)) //6500 + 1500 = 8000
                .discountPolicy(discountPolicyService.findById(rateDiscountPolicy).get())
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

        LocalDateTime now = LocalDateTime.now();
        Order newOrder = Order.builder()
                .member(member)
                .shop(shop)
                .orderStatus(OrderStatus.ORDER)
                .address("서울시 강남구 대학로 1길 203호")
                .phoneNumber("010-0000-1111")
                .totalPrice(Money.of(30000))
                .orderDateTime(LocalDateTime.now().minusMinutes(1).minusSeconds(1))
                .build();
        orderRepository.save(newOrder);
        em.flush();
        em.clear();

        Order findOrder = orderService.findById(newOrder.getId()).get();

        mvc.perform(
                post("/api/order/{orderId}", findOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        ).andDo(print())
                        .andExpect(status().is4xxClientError());

        Assertions.assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
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
