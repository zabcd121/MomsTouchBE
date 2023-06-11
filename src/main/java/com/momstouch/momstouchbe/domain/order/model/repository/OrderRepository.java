package com.momstouch.momstouchbe.domain.order.model.repository;

import com.momstouch.momstouchbe.domain.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long>, OrderQueryRepository {
}
