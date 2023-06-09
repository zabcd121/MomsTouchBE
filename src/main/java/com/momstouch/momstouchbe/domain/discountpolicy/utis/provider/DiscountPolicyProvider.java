package com.momstouch.momstouchbe.domain.discountpolicy.utis.provider;

import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;


public abstract class DiscountPolicyProvider {

    protected final DiscountPolicyService discountPolicyService;

    public DiscountPolicyProvider(DiscountPolicyService discountPolicyService) {
        this.discountPolicyService = discountPolicyService;
    }

    //    public DiscountPolicy createDiscountPolicy(DiscountPolicyCreateCommand createCommand) {
//

//    }

    protected abstract Long provide(DiscountPolicyCreateCommand command);

    protected abstract boolean isAssignable(DiscountPolicyCreateCommand command);
}
