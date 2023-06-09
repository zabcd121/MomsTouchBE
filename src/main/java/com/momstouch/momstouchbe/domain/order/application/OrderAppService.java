package com.momstouch.momstouchbe.domain.order.application;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.order.dto.OrderRequest;
import com.momstouch.momstouchbe.domain.order.service.MenuInfo;
import com.momstouch.momstouchbe.domain.order.service.OptionGroupSelectInfo;
import com.momstouch.momstouchbe.domain.order.service.OrderService;
import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.MenuRepository;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.momstouch.momstouchbe.domain.order.dto.OrderRequest.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderAppService {

    private final OrderService orderService;
    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;

    public Long order(CreateOrderRequest createOrderRequest, Authentication authentication) {

        Long shopId = createOrderRequest.getShopId();

        Shop shop = shopRepository.findById(shopId).orElseThrow(NoClassDefFoundError::new);
        Member member = Member.createMember("temp","temp","김현석바보","ROLE_USER");

        List<MenuInfo> orderMenuList = createOrderRequest.getOrderMenuList();
        OrderInfo orderInfo = OrderInfo.of(member,shop,orderMenuList,createOrderRequest.getAddress(), createOrderRequest.getPhoneNumber());

        Long orderId = orderService.createOrder(orderInfo);
        return orderId;
    }
}
