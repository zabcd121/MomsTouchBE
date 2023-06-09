package com.momstouch.momstouchbe.domain.discountpolicy.utis.provider;

import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import org.springframework.stereotype.Component;

import static com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand.*;

public class AmountDiscountPolicyProvider extends DiscountPolicyProvider {

    public AmountDiscountPolicyProvider(DiscountPolicyService discountPolicyService) {
        super(discountPolicyService);
    }

    @Override
    protected Long provide(DiscountPolicyCreateCommand command) {

        if (isAssignable(command)) {
            AmountDiscountPolicyCreateCommand create = (AmountDiscountPolicyCreateCommand) command;
            Integer baseAmount = create.getBaseAmount();
            Integer discountAmount = create.getDiscountAmount();
            return discountPolicyService.createAmountDiscountPolicy(baseAmount, discountAmount);
        }

        return null;
    }

    @Override
    protected boolean isAssignable (DiscountPolicyCreateCommand command){
        return command.getClass().isAssignableFrom(AmountDiscountPolicyCreateCommand.class);
    }
}
