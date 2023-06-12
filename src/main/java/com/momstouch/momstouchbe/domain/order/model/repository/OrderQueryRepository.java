package com.momstouch.momstouchbe.domain.order.model.repository;

import com.momstouch.momstouchbe.domain.order.model.Order;

import java.util.List;

public interface OrderQueryRepository {

    Order findByIdWithAll(Long orderId);
    List<Order> findByMemberIdWithAll(Long memberId);
    List<Order> findOrderListByShopId(Long shopId);
}
