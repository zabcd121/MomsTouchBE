package com.momstouch.momstouchbe.domain.order.service;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.order.model.Order;
import com.momstouch.momstouchbe.domain.order.model.OrderMenu;
import com.momstouch.momstouchbe.domain.order.model.OrderStatus;
import com.momstouch.momstouchbe.domain.order.model.repository.OrderRepository;
import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.global.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;


    @Transactional
    public Long createOrder(Member member,
                            Shop shop,
                            List<Menu> orderMenuList,
                            String address,
                            String phoneNumber) {

        Money totalPrice = Money.ZERO;
        for (Menu menu : orderMenuList) {
            totalPrice = totalPrice.plus(menu.getTotalPrice());
        }

        Order order = Order.builder()
                .member(member)
                .shop(shop)
                .orderMenuList()
                .address(address)
                .phoneNumber(phoneNumber)
                .totalPrice(totalPrice)
                .build();

        orderRepository.save(order);
        return order.getId();
    }

}
