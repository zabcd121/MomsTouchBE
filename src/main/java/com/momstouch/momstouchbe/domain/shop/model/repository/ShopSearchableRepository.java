package com.momstouch.momstouchbe.domain.shop.model.repository;

import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.domain.shop.model.Shop;

import java.util.List;

public interface ShopSearchableRepository {

//    public List<ShopResponse.ShopMenuListSearchResponse> findMenuListByShopId(Long shopId);
    Shop findWithMenuListByShopId(Long shopId);
    List<Shop> findAllByMemberId(Long memberId);

    Menu findMenuByMenuId(Long menuId);
    Menu findMenuWithOptionGroupByMenuId(Long menuId);
}
