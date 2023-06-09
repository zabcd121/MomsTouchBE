package com.momstouch.momstouchbe.domain.shop.application;

import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.DiscountPolicyRepository;
import com.momstouch.momstouchbe.domain.shop.dto.MenuRequest;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopSearchableRepository;
import com.momstouch.momstouchbe.global.util.FileUploadUtil;
import com.momstouch.momstouchbe.global.vo.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.momstouch.momstouchbe.domain.shop.dto.MenuRequest.*;
import static com.momstouch.momstouchbe.domain.shop.dto.ShopResponse.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final ShopSearchableRepository shopSearchableRepository;
    private final DiscountPolicyRepository discountPolicyRepository;
    private final FileUploadUtil fileUploadUtil;

    public ShopMenuListResponse searchAllMenuBy(Long shopId) {
        return ShopMenuListResponse.of(shopSearchableRepository.findMenuListByShopId(shopId));
    }

    @Transactional
    public Shop addMenu(Long shopId, MultipartFile image, MenuCreateRequest menuRequest) {
        Shop shop = null;
        try {
            shop = shopSearchableRepository.findMenuListByShopId(shopId);
            shop.getDiscountPolicyList();
            String imageURL = fileUploadUtil.uploadMenuImage(image);
            shop.addMenu(
                    menuRequest.toEntity(
                            imageURL,
                            discountPolicyRepository.getReferenceById(menuRequest.getDiscountPolicyId())
                    ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shop;
    }
}
