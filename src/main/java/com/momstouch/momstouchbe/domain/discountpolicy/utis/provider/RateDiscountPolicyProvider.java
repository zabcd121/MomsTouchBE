package com.momstouch.momstouchbe.domain.discountpolicy.utis.provider;

import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import org.springframework.stereotype.Component;

import static com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand.*;

public class RateDiscountPolicyProvider extends DiscountPolicyProvider {

    public RateDiscountPolicyProvider(DiscountPolicyService discountPolicyService) {
        super(discountPolicyService);
    }

    @Override
    protected Long provide(DiscountPolicyCreateCommand command) {
        if(isAssignable(command)) {
            RateDiscountPolicyCreateCommand createCommand = (RateDiscountPolicyCreateCommand) command;
            double discountRate = createCommand.getDiscountRate();
            Integer baseAmount = createCommand.getBaseAmount();
            return discountPolicyService.createRateDiscountPolicy(baseAmount, discountRate);
        }
        return null;
    }

    @Override
    protected boolean isAssignable(DiscountPolicyCreateCommand command) {
        return command.getClass().isAssignableFrom(RateDiscountPolicyCreateCommand.class);
    }
}
