package com.momstouch.momstouchbe.domain.shop.repository;


import com.momstouch.momstouchbe.domain.discountpolicy.model.QDiscountPolicy;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopSearchableRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.momstouch.momstouchbe.domain.discountpolicy.model.QDiscountPolicy.*;
import static com.momstouch.momstouchbe.domain.shop.model.QMenu.*;
import static com.momstouch.momstouchbe.domain.shop.model.QOptionGroupSpecification.*;
import static com.momstouch.momstouchbe.domain.shop.model.QShop.*;

@Repository
@RequiredArgsConstructor
public class ShopSearchRepository implements ShopSearchableRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Shop findWithMenuListByShopId(Long shopId) {
        return queryFactory
                .selectFrom(shop)
                .distinct()
                .leftJoin(shop.menuList, menu).fetchJoin()
                //.leftJoin(shop.discountPolicyList, discountPolicy).fetchJoin()
//                .leftJoin(menu.discountPolicy, discountPolicy).fetchJoin()
                .where(shop.id.eq(shopId))
                .fetchOne();
    }

    @Override
    public List<Shop> findAllByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(shop)
                .distinct()
                .leftJoin(shop.menuList, menu).fetchJoin()
                //.leftJoin(shop.discountPolicyList, discountPolicy).fetchJoin()
                .where(shop.owner.id.eq(memberId))
                .fetch();
    }

    @Override
    public Menu findMenuByMenuId(Long menuId) {
        return queryFactory
                .selectFrom(menu)
                .distinct()
                .leftJoin(menu.discountPolicy, discountPolicy).fetchJoin()
                .where(menu.id.eq(menuId))
                .fetchOne();
    }

    @Override
    public Menu findMenuWithOptionGroupByMenuId(Long menuId) {
        return queryFactory
                .selectFrom(menu)
                .distinct()
                .leftJoin(menu.optionGroupList, optionGroupSpecification).fetchJoin()
                .leftJoin(menu.discountPolicy, discountPolicy).fetchJoin()
                .where(menu.id.eq(menuId))
                .fetchOne();
    }


//    @Override
//    public List<ShopResponse.ShopMenuListSearchResponse> findMenuListByShopId(Long shopId) {
//        return queryFactory
//                .select(Projections.fields(ShopResponse.ShopMenuListSearchResponse.class,
//                        ))
//                .from(shop)
//                .join(shop.menuList, menu).fetchJoin()
//                .where(shop.id.eq(shopId))
//                .fetch();
//    }
}
