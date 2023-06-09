package com.momstouch.momstouchbe.domain.shop.repository;

import com.momstouch.momstouchbe.domain.menu.model.QMenu;
import com.momstouch.momstouchbe.domain.shop.dto.ShopResponse;
import com.momstouch.momstouchbe.domain.shop.model.QShop;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopSearchableRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.momstouch.momstouchbe.domain.menu.model.QMenu.*;
import static com.momstouch.momstouchbe.domain.shop.model.QShop.*;

@Repository
@RequiredArgsConstructor
public class ShopSearchRepository implements ShopSearchableRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Shop findMenuListByShopId(Long shopId) {
        return queryFactory
                .select(shop)
                .join(shop.menuList, menu).fetchJoin()
                .where(shop.id.eq(shopId))
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
