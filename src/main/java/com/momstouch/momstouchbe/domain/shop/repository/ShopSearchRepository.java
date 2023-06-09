package com.momstouch.momstouchbe.domain.shop.repository;

import com.momstouch.momstouchbe.domain.shop.model.QMenu;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopSearchableRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.momstouch.momstouchbe.domain.shop.model.QMenu.*;
import static com.momstouch.momstouchbe.domain.shop.model.QShop.*;


@Repository
@RequiredArgsConstructor
public class ShopSearchRepository implements ShopSearchableRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Shop findMenuListByShopId(Long shopId) {
        return queryFactory
                .selectFrom(shop)
                .distinct()
                .leftJoin(shop.menuList, menu).fetchJoin()
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
