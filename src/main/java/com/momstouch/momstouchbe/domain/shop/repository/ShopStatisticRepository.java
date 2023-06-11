package com.momstouch.momstouchbe.domain.shop.repository;

import com.momstouch.momstouchbe.domain.order.model.QOrder;
import com.momstouch.momstouchbe.domain.order.model.QOrderMenu;
import com.momstouch.momstouchbe.domain.order.model.QOrderOption;
import com.momstouch.momstouchbe.domain.order.model.QOrderOptionGroup;
import com.momstouch.momstouchbe.domain.shop.model.QMenu;
import com.momstouch.momstouchbe.domain.shop.model.QShop;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.momstouch.momstouchbe.domain.order.model.QOrder.order;
import static com.momstouch.momstouchbe.domain.order.model.QOrderMenu.*;
import static com.momstouch.momstouchbe.domain.shop.dto.StatisticResponse.*;

@Repository
@RequiredArgsConstructor
public class ShopStatisticRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public OrderStatisticResponse orderStatisticByShopId(Long shopId) {
        return jpaQueryFactory
                .select(Projections.constructor(OrderStatisticResponse.class,order.count(),order.totalPrice.amount.sum()))
                .from(order)
                .where(order.shop.id.eq(shopId))
                .fetchOne();
    }

    public List<OrderMenuStatisticResponse> orderMenuStatisticByOrderIdList(Long shopId) {
        return jpaQueryFactory
                .select(Projections.constructor(OrderMenuStatisticResponse.class,
                        orderMenu.menu.id,
                        orderMenu.menu.name,
                        orderMenu.menu.description,
                        orderMenu.count,
                        orderMenu.menu.category))
                .from(orderMenu)
                .join(orderMenu.menu, QMenu.menu)
                .join(orderMenu.order, order)
                .join(order.shop,QShop.shop)
                .where(QShop.shop.id.eq(shopId))
                .groupBy(orderMenu.menu)
                .fetch();
    }

}
