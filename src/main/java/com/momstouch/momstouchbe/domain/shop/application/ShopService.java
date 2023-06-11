package com.momstouch.momstouchbe.domain.shop.application;

import com.momstouch.momstouchbe.domain.shop.dto.ShopResponse;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopSearchableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.momstouch.momstouchbe.domain.shop.dto.ShopResponse.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShopService {

    private final ShopSearchableRepository shopSearchableRepository;

    public ShopListResponse searchAllShop(Long memberId) {
        List<Shop> shopList = shopSearchableRepository.findAllByMemberId(memberId);
        return ShopListResponse.of(shopList);

    }
}
