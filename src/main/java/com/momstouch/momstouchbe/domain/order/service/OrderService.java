package com.momstouch.momstouchbe.domain.order.service;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.order.application.OrderInfo;
import com.momstouch.momstouchbe.domain.order.model.Order;
import com.momstouch.momstouchbe.domain.order.model.OrderMenu;
import com.momstouch.momstouchbe.domain.order.model.OrderOption;
import com.momstouch.momstouchbe.domain.order.model.OrderOptionGroup;
import com.momstouch.momstouchbe.domain.order.model.repository.OrderRepository;
import com.momstouch.momstouchbe.domain.order.service.validation.OrderValidationService;
import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.global.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidationService orderValidationService;


    @Transactional
    public Long createOrder(OrderInfo orderInfo) {
        List<MenuInfo> orderMenuList = orderInfo.getOrderMenuList();

        Member member = orderInfo.getMember();
        Shop shop = orderInfo.getShop();

        Order order = Order.builder()
                .member(member)
                .shop(shop)
                .address(orderInfo.getAddress())
                .phoneNumber(orderInfo.getPhoneNumber())
                .build();

        for (MenuInfo menuInfo : orderMenuList) {
            Menu menu = menuInfo.getMenu();
            List<OptionGroupSelectInfo> optionGroupSelectInfoList = menuInfo.getOptionGroupSelectInfoList();

            OrderMenu orderMenu = OrderMenu.builder()
                    .count(menuInfo.getCount())
                    .menu(menu)
                    .build();

            for (OptionGroupSelectInfo optionGroupSelectInfo : optionGroupSelectInfoList) {
                OrderOptionGroup orderOptionGroup = OrderOptionGroup
                        .builder()
                        .name(optionGroupSelectInfo.getName())
                        .build();

                List<OptionSelectInfo> optionSelectInfoList = optionGroupSelectInfo.getOptionSelectInfoList();

                for (OptionSelectInfo optionSelectInfo : optionSelectInfoList) {
                    OrderOption orderOption = new OrderOption(optionSelectInfo.getName(), optionSelectInfo.getPrice());
                    orderOptionGroup.addOrderOption(orderOption);
                }
                orderMenu.addOrderOptionGroup(orderOptionGroup);
            }

            orderMenu.order(order);
        }

        //TODO : OrderValidationService -> 사이드만 있는지 메뉴의 OptionSpectification의 가격, 이름 일치하는지 검사
        Money orderTotalPrice = order.getTotalPrice();
        order.setTotalPrice(orderTotalPrice);

        boolean validate = orderValidationService.validate(order);

        //TODO : validation 테스트 필요함
        if(!validate) {
            throw new IllegalStateException();
        }

        orderRepository.save(order);
        return order.getId();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findByIdWithAll(Long id) {
        Order order = orderRepository.findByIdWithAll(id);

        return Optional.of(order);
    }

}
