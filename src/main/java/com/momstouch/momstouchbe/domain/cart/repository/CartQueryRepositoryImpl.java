package com.momstouch.momstouchbe.domain.cart.repository;

import com.momstouch.momstouchbe.domain.cart.model.Cart;
import com.momstouch.momstouchbe.domain.cart.model.QCart;
import com.momstouch.momstouchbe.domain.cart.model.QCartMenu;
import com.momstouch.momstouchbe.domain.cart.model.repository.CartQueryRepository;
import com.momstouch.momstouchbe.domain.member.model.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.momstouch.momstouchbe.domain.cart.model.QCart.*;
import static com.momstouch.momstouchbe.domain.cart.model.QCartMenu.*;
import static com.momstouch.momstouchbe.domain.member.model.QMember.*;

@Repository
@RequiredArgsConstructor
public class CartQueryRepositoryImpl implements CartQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Cart findByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(cart)
                .join(cart.member, member).fetchJoin()
                .join(cart.cartMenuList, cartMenu).fetchJoin()
                .where(cart.member.id.eq(memberId))
                .fetchOne();
    }

    @Override
    public boolean existsByMemberId(Long memberId) {
        return queryFactory
                .select(cart.id)
                .from(cart)
                .where(cart.member.id.eq(memberId))
                .fetchFirst() != null;
    }

}
