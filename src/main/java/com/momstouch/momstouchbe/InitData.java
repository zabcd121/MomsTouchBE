package com.momstouch.momstouchbe;

import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.order.application.OrderInfo;
import com.momstouch.momstouchbe.domain.order.service.MenuInfo;
import com.momstouch.momstouchbe.domain.order.service.OptionGroupSelectInfo;
import com.momstouch.momstouchbe.domain.order.service.OptionSelectInfo;
import com.momstouch.momstouchbe.domain.order.service.OrderService;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.momstouch.momstouchbe.global.vo.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InitData {

    private final InitService initService;
    @PostConstruct
    public void init() throws Exception {
        initService.init();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService {
        private final EntityManager em;
        private final OrderService orderService;

        private final DiscountPolicyService discountPolicyService;
        public void init(){
            Member member = Member.createMember("114396781105370304636", UUID.randomUUID().toString(),"김현석","ROLE_OWNER","dog1239121@gmail.com");
            Shop shop = Shop.builder()
                    .owner(member)
                    .name("맘스터치")
                    .phoneNumber("054-478-3214")
                    .address("경상북도 구미시")
                    .openTime(LocalTime.of(0,0,1))
                    .closedTime(LocalTime.of(23,59,59))
                    .minOrderPrice(Money.of(10000))
                    .build();

            em.persist(member);
            em.persist(shop);

            Long amountDiscountPolicy = discountPolicyService.createAmountDiscountPolicy(shop, 10000, 1000);
            Long rateDiscountPolicy = discountPolicyService.createRateDiscountPolicy(shop, 10000, 10.0);
            Long timeDiscountPolicy = discountPolicyService.createTimeDiscountPolicy(shop, LocalTime.of(10, 0, 0), 1000);

            Menu menu1 = getMenu1(amountDiscountPolicy);
            Menu menu2 = getMenu2(rateDiscountPolicy);
            Menu menu3 = getMenu3(timeDiscountPolicy);
            shop.addMenu(menu1); shop.addMenu(menu2); shop.addMenu(menu3);

            em.flush();
            em.clear();

            MenuInfo menuInfo1 = menuInfo(menu1, menu1.getOptionGroupList(), 1);
            MenuInfo menuInfo2 = menuInfo(menu2, menu2.getOptionGroupList(), 2);

            OrderInfo orderInfo = orderInfo(shop, member, List.of(menuInfo1, menuInfo2));

            //Long orderId = orderService.createOrder(orderInfo);
        }

        private Menu getMenu1(Long discountPolicyId) {
            return Menu.builder()
                    .category(Category.MAIN)
                    .name("싸이버거")
                    .description("풍미좋은 햄버거")
                    .imageURL("/api/images/menus/2023/6/12/1663630773-JQZAP.png")
                    .price(Money.of(6500)) //6500 + 1500 = 8000
                    .discountPolicy(discountPolicyService.findById(discountPolicyId).get())
                    .optionGroupList(
                            List.of(
                                    OptionGroupSpecification.builder().name("단품, 세트 선택")
                                            .optionList( //0
                                                    List.of(OptionSpecification.builder().name("단품선택").price(Money.of(0)).build(),
                                                            OptionSpecification.builder().name("세트변경").price(Money.of(2500)).build())
                                            ).build(),
                                    OptionGroupSpecification.builder().name("사이드 선택")
                                            .optionList( //1500
                                                    List.of(OptionSpecification.builder().name("감자").price(Money.of(1500)).build(),
                                                            OptionSpecification.builder().name("콜라").price(Money.of(1500)).build())
                                            ).build()
                            )
                    ).build();
        }

        private Menu getMenu2(Long discountPolicyId) {
            return Menu.builder()
                    .category(Category.MAIN)
                    .name("화이트갈릭버거")
                    .description("마요 갈릭 버거입니다.")
                    .imageURL("/api/images/menus/2023/6/12/1680133163-UWULA.png")
                    .price(Money.of(5500)) //(5500 + 3500) *2 = 18000
                    .discountPolicy(discountPolicyService.findById(discountPolicyId).get())
                    .optionGroupList(
                            List.of(
                                    OptionGroupSpecification.builder().name("단품, 세트 선택")
                                            .optionList( //0
                                                    List.of(OptionSpecification.builder().name("단품선택").price(Money.of(0)).build(),
                                                            OptionSpecification.builder().name("세트변경").price(Money.of(2500)).build())
                                            ).build(),
                                    OptionGroupSpecification.builder().name("사이드 선택")
                                            .optionList( //1500
                                                    List.of(OptionSpecification.builder().name("감자").price(Money.of(1500)).build(),
                                                            OptionSpecification.builder().name("콜라").price(Money.of(1500)).build())
                                            ).build()
                            )
                    ).build();
        }

        private Menu getMenu3(Long discountPolicyId) {
            return Menu.builder()
                    .category(Category.MAIN)
                    .name("싸이플렉스갈릭버거")
                    .description("플렉스를 할 수 있는 버거입니다.")
                    .imageURL("/api/images/menus/2023/6/13/1674626662-ITQZP.png")
                    .price(Money.of(6500)) //(5500 + 3500) *2 = 18000
                    .discountPolicy(discountPolicyService.findById(discountPolicyId).get())
                    .optionGroupList(
                            List.of(
                                    OptionGroupSpecification.builder().name("단품, 세트 선택")
                                            .optionList( //0
                                                    List.of(OptionSpecification.builder().name("단품선택").price(Money.of(0)).build(),
                                                            OptionSpecification.builder().name("세트변경").price(Money.of(2500)).build())
                                            ).build(),
                                    OptionGroupSpecification.builder().name("사이드 선택")
                                            .optionList( //1500
                                                    List.of(OptionSpecification.builder().name("감자").price(Money.of(1500)).build(),
                                                            OptionSpecification.builder().name("콜라").price(Money.of(1500)).build())
                                            ).build()
                            )
                    ).build();
        }

        private MenuInfo menuInfo(Menu menu, List<OptionGroupSpecification> optionGroupSpecificationList , Integer count){
            List<OptionGroupSelectInfo> optionGroupSelectInfoList = optionGroupSpecificationList.stream()
                    .map(optionGroupSpecification -> {
                        return OptionGroupSelectInfo.builder()
                                .name(optionGroupSpecification.getName())
                                .optionSelectInfoList(optionGroupSpecification.getOptionList().stream()
                                        .map(optionSpecification -> {
                                            return OptionSelectInfo.builder()
                                                    .name(optionSpecification.getName())
                                                    .price(optionSpecification.getPrice())
                                                    .build();
                                        })
                                        .toList()
                                ).build();
                    })
                    .toList();
            return MenuInfo
                    .builder()
                    .menuId(menu.getId())
                    .optionGroupSelectInfoList(optionGroupSelectInfoList)
                    .count(count)
                    .build();
        }

        private OrderInfo orderInfo(Shop shop, Member member,List<MenuInfo> orderMenuList) {
            return OrderInfo.of(member, shop, orderMenuList, "ADDRESS", "010-1232-3123");
        }
    }

}
