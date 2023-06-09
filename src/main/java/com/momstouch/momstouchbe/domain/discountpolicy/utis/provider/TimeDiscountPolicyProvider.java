package com.momstouch.momstouchbe.domain.discountpolicy.utis.provider;

import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

import static com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand.*;

public class TimeDiscountPolicyProvider extends DiscountPolicyProvider {

    public TimeDiscountPolicyProvider(DiscountPolicyService discountPolicyService) {
        super(discountPolicyService);
    }

    @Override
    protected Long provide(DiscountPolicyCreateCommand command) {

        if (isAssignable(command)) {
            TimeDiscountPolicyCreateCommand create = (TimeDiscountPolicyCreateCommand) command;
            LocalTime baseTime = create.getBaseTime();
            Integer discountAmount = create.getDiscountAmount();
            return discountPolicyService.createTimeDiscountPolicy(baseTime, discountAmount);
        }

        return null;
    }

    @Override
    protected boolean isAssignable (DiscountPolicyCreateCommand command){
        return command.getClass().isAssignableFrom(TimeDiscountPolicyCreateCommand.class);
    }
}
