package com.momstouch.momstouchbe.domain.order.application;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.order.dto.OrderResponse;
import com.momstouch.momstouchbe.domain.order.model.Order;
import com.momstouch.momstouchbe.domain.order.model.OrderStatus;
import com.momstouch.momstouchbe.domain.order.service.MenuInfo;
import com.momstouch.momstouchbe.domain.order.service.OrderService;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.MenuRepository;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;

import static com.momstouch.momstouchbe.domain.order.dto.OrderRequest.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderAppService {

    private final OrderService orderService;
    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;
    private final OrderAlarmService orderAlarmService;

    public OrderResponse findOrderById(Long orderId) {
        Order order = orderService.findByIdWithAll(orderId).orElseThrow();
        return OrderResponse.of(order);
    }

    public Long order(CreateOrderRequest createOrderRequest, Authentication authentication) {

        Long shopId = createOrderRequest.getShopId();

        Shop shop = shopRepository.findById(shopId).orElseThrow();
        //TODO : authentication에서 조회
        Member member = Member.createMember("temp","temp","김현석바보","ROLE_USER","email");

        List<MenuInfo> orderMenuList = createOrderRequest.getOrderMenuList();
        OrderInfo orderInfo = OrderInfo.of(member,shop,orderMenuList,createOrderRequest.getAddress(), createOrderRequest.getPhoneNumber());

        Long orderId = orderService.createOrder(orderInfo);
        Order order = orderService.findById(orderId).orElseThrow();

        orderAlarmService.send(shop.getOwner(), order, "맘스터치 주문!");
        return orderId;
    }

    private boolean template(Order order, Authentication authentication,Runnable orderCallback) {
        Shop shop = order.getShop();
        Member member = shop.getOwner(); // TODO : 나중에 securityContext에서 조회
        if(!member.equals(authentication)) throw new AccessDeniedException("member.equals(authentication) 실패");
        if(!shop.isOwn(member)) throw new AccessDeniedException("shop.isOwn(member) 실패");
        orderCallback.run();
        orderAlarmService.send(order.getMember(), order, "주문 상태 변경!");
        return true;
    }
    @Transactional
    public boolean accept(Long orderId, Authentication authentication) {
        Order order = orderService.findById(orderId).orElseThrow();
        if(!order.getOrderStatus().equals(OrderStatus.ORDER)) throw new IllegalStateException();
        return template(order,authentication, order::accept);
    }

    @Transactional
    public boolean deliver(Long orderId, Authentication authentication) {
        Order order = orderService.findById(orderId).orElseThrow();
        if(!order.getOrderStatus().equals(OrderStatus.ORDER)) throw new IllegalStateException();
        return template(order,authentication, order::deliver);
    }

    @Transactional
    public boolean cancel(Long orderId, Authentication authentication) {
        Order order = orderService.findById(orderId).orElseThrow();
        if(!order.getOrderStatus().equals(OrderStatus.ORDER)) throw new IllegalStateException();
        return template(order,authentication, order::cancel);
    }

    @Transactional
    public boolean complete(Long orderId, Authentication authentication) {
        Order order = orderService.findById(orderId).orElseThrow();
        if(!order.getOrderStatus().equals(OrderStatus.DELIVERY)) throw new IllegalStateException();
        return template(order,authentication, order::complete);
    }
}
