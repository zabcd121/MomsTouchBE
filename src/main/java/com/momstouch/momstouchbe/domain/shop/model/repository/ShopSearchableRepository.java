package com.momstouch.momstouchbe.domain.shop.model.repository;

import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.domain.shop.model.Shop;

public interface ShopSearchableRepository {

//    public List<ShopResponse.ShopMenuListSearchResponse> findMenuListByShopId(Long shopId);
    Shop findWithMenuListByShopId(Long shopId);
    Menu findMenuByMenuId(Long menuId);

    Menu findMenuWithOptionGroupByMenuId(Long menuId);
}
