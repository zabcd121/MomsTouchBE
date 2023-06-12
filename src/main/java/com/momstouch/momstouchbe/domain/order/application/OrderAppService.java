package com.momstouch.momstouchbe.domain.order.application;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import com.momstouch.momstouchbe.domain.order.dto.OrderResponse;
import com.momstouch.momstouchbe.domain.order.model.Order;
import com.momstouch.momstouchbe.domain.order.model.OrderStatus;
import com.momstouch.momstouchbe.domain.order.service.MenuInfo;
import com.momstouch.momstouchbe.domain.order.service.OrderService;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.MenuRepository;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopRepository;
import com.momstouch.momstouchbe.global.jwt.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.momstouch.momstouchbe.domain.order.dto.OrderRequest.*;
import static com.momstouch.momstouchbe.domain.order.dto.OrderResponse.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderAppService {

    private final OrderService orderService;
    private final MemberRepository memberRepository;
    private final ShopRepository shopRepository;
    //private final OrderAlarmService orderAlarmService;

    private final MemberDetailsService memberDetailsService;

    public OrderResponse findOrderById(Long orderId) {
        Order order = orderService.findByIdWithAll(orderId).orElseThrow();
        return of(order);
    }

    public OrderListResponse findMyOrderList(Authentication authentication) {
        if(authentication == null) throw new AccessDeniedException("로그인 필요");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Member member = memberRepository.findBySub(userDetails.getUsername()).orElseThrow();

        List<Order> allMyOrder = orderService.findAllMyOrder(member.getId());

        return OrderListResponse.of(allMyOrder);
    }

    public OrderListResponse findOrderListByShopId(Authentication authentication,Long shopId) {
//        if(authentication == null) throw new AccessDeniedException("로그인 필요");
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Member member = memberRepository.findBySub(userDetails.getUsername()).orElseThrow();
//        Shop shop = shopRepository.findById(shopId).orElseThrow();
//        if(!shop.getOwner().isEquals(authentication)) {
//            throw new IllegalArgumentException("가게 주인이 아님");
//        }

        List<Order> orderListByShopId = orderService.findOrderListByShopId(shopId);
        return OrderListResponse.of(orderListByShopId);
    }

    @Transactional
    public Long order(CreateOrderRequest createOrderRequest, Authentication authentication) {

        Long shopId = createOrderRequest.getShopId();

        Shop shop = shopRepository.findById(shopId).orElseThrow();

        //TODO : authentication에서 조회로 수정
//        Member member = Member.createMember("temp","temp","김현석바보","ROLE_USER","email");
//        memberRepository.save(member);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Member member = memberRepository.findBySub(userDetails.getUsername()).orElseThrow();

        List<MenuInfo> orderMenuList = createOrderRequest.getOrderMenuList();
        OrderInfo orderInfo = OrderInfo.of(member,shop,orderMenuList,createOrderRequest.getAddress(), createOrderRequest.getPhoneNumber());

        Long orderId = orderService.createOrder(orderInfo);
        Order order = orderService.findById(orderId).orElseThrow();

        //orderAlarmService.send(shop.getOwner(), order, "맘스터치 주문!");
        return orderId;
    }

    private boolean template(Order order, Authentication authentication,Runnable orderCallback) {
        Shop shop = order.getShop();
        Member member = shop.getOwner();
        boolean equals = member.isEquals(authentication);
        if(!equals) throw new AccessDeniedException("member.equals(authentication) 실패");
        if(!shop.isOwn(member)) throw new AccessDeniedException("shop.isOwn(member) 실패");
        orderCallback.run();
        //orderAlarmService.send(order.getMember(), order, "주문 상태 변경!");
        return true;
    }
    @Transactional
    public boolean accept(Long orderId, Authentication authentication) {
        Order order = orderService.findById(orderId).orElseThrow();
        if(!order.getOrderStatus().equals(OrderStatus.ORDER)) throw new IllegalStateException(); //TODO :

        if(LocalDateTime.now().isAfter(order.getOrderDateTime().plus(1, ChronoUnit.MINUTES))) {
            order.cancel();
//            orderService.cancel(order);
            throw new IllegalStateException("주문 접수 가능 시간 초과");
        }

        return template(order,authentication, order::accept);
    }

    @Transactional
    public boolean deliver(Long orderId, Authentication authentication) {
        Order order = orderService.findById(orderId).orElseThrow();
        if(!order.getOrderStatus().equals(OrderStatus.ACCEPT)) throw new IllegalStateException(); //TODO :
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
        if(!order.getOrderStatus().equals(OrderStatus.DELIVERY)) throw new IllegalStateException(); //TODO :
        return template(order,authentication, order::complete);
    }
}
