package com.momstouch.momstouchbe.domain.order.model.repository;

import com.momstouch.momstouchbe.domain.member.model.QMember;
import com.momstouch.momstouchbe.domain.order.model.Order;
import com.momstouch.momstouchbe.domain.order.model.QOrder;
import com.momstouch.momstouchbe.domain.order.model.QOrderMenu;
import com.momstouch.momstouchbe.domain.shop.model.QShop;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.momstouch.momstouchbe.domain.order.model.QOrder.order;

@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Order findByIdWithAll(Long orderId) {
        return jpaQueryFactory
                .select(order)
                .from(order)
                .join(order.orderMenuList, QOrderMenu.orderMenu).fetchJoin()
                .join(order.member, QMember.member).fetchJoin()
                .join(order.shop, QShop.shop).fetchJoin()
                .where(order.id.eq(orderId))
                .fetchOne();
    }

    public List<Order> findByMemberIdWithAll(Long memberId) {
        return jpaQueryFactory
                .select(order).distinct()
                .from(order)
                .join(order.orderMenuList, QOrderMenu.orderMenu).fetchJoin()
                .join(order.member, QMember.member).fetchJoin()
                .join(order.shop, QShop.shop).fetchJoin()
                .where(order.member.id.eq(memberId))
                .fetch();
    }

    public List<Order> findOrderListByShopId(Long shopId) {
        return jpaQueryFactory
                .select(order).distinct()
                .from(order)
                .join(order.orderMenuList, QOrderMenu.orderMenu).fetchJoin()
                .join(order.member, QMember.member).fetchJoin()
                .join(order.shop, QShop.shop).fetchJoin()
                .where(order.shop.id.eq(shopId))
                .fetch();
    }
}
