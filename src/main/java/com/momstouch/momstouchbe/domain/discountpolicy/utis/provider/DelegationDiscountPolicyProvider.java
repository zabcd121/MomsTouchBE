package com.momstouch.momstouchbe.domain.discountpolicy.utis.provider;

import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.provider.AmountDiscountPolicyProvider;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.provider.DiscountPolicyProvider;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.provider.RateDiscountPolicyProvider;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.provider.TimeDiscountPolicyProvider;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DelegationDiscountPolicyProvider {

    private final List<DiscountPolicyProvider> providerList;

    public DelegationDiscountPolicyProvider(DiscountPolicyService discountPolicyService, ShopRepository shopRepository) {
        providerList = List.of(
                new AmountDiscountPolicyProvider(discountPolicyService,shopRepository),
                new RateDiscountPolicyProvider(discountPolicyService,shopRepository),
                new TimeDiscountPolicyProvider(discountPolicyService,shopRepository)
        );
    }

    public Long createDiscountPolicyProvider(DiscountPolicyCreateCommand createCommand) {
        for (DiscountPolicyProvider discountPolicyProvider : providerList) {
            Long discountPolicy = discountPolicyProvider.provide(createCommand);
            if(discountPolicy != null) return discountPolicy;
        }

        throw new UnsupportedOperationException();
    }
}
