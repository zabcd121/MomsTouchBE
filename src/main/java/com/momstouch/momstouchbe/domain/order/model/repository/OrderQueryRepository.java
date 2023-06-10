package com.momstouch.momstouchbe.domain.order.model.repository;

import com.momstouch.momstouchbe.domain.order.model.Order;

public interface OrderQueryRepository {

    Order findByIdWithAll(Long orderId);
}
