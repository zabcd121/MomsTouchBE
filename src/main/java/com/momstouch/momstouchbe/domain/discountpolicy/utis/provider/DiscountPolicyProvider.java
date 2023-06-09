package com.momstouch.momstouchbe.domain.discountpolicy.utis.provider;

import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopRepository;

public abstract class DiscountPolicyProvider {

    protected final DiscountPolicyService discountPolicyService;
    protected final ShopRepository shopRepository;

    public DiscountPolicyProvider(DiscountPolicyService discountPolicyService, ShopRepository shopRepository) {
        this.discountPolicyService = discountPolicyService;
        this.shopRepository = shopRepository;
    }


    protected abstract Long provide(DiscountPolicyCreateCommand command);

    protected abstract boolean isAssignable(DiscountPolicyCreateCommand command);
}
