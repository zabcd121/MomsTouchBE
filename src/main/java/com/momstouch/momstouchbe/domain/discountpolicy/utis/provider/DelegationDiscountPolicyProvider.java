package com.momstouch.momstouchbe.domain.discountpolicy.utis.provider;

import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.provider.AmountDiscountPolicyProvider;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.provider.DiscountPolicyProvider;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.provider.RateDiscountPolicyProvider;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.provider.TimeDiscountPolicyProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DelegationDiscountPolicyProvider {

    private final List<DiscountPolicyProvider> providerList;

    public DelegationDiscountPolicyProvider(DiscountPolicyService discountPolicyService) {
        providerList = List.of(
                new AmountDiscountPolicyProvider(discountPolicyService),
                new RateDiscountPolicyProvider(discountPolicyService),
                new TimeDiscountPolicyProvider(discountPolicyService)
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
