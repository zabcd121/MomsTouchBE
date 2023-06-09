package com.momstouch.momstouchbe.domain.shop.model.repository;

import com.momstouch.momstouchbe.domain.shop.dto.ShopResponse;
import com.momstouch.momstouchbe.domain.shop.model.Shop;

import java.util.List;

public interface ShopSearchableRepository {

//    public List<ShopResponse.ShopMenuListSearchResponse> findMenuListByShopId(Long shopId);
    Shop findMenuListByShopId(Long shopId);
}
