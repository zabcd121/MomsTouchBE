package com.momstouch.momstouchbe.domain.discountpolicy.utis.convertor;

import com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.TimeDiscountPolicy;

import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse.*;

public class TimeDiscountPolicyConvertor extends DiscountPolicyConvertor{
    @Override
    protected DiscountPolicyResponse convertResponse(DiscountPolicy discountPolicy) {

        if(isAssignable(discountPolicy)){
            TimeDiscountPolicy timeDiscountPolicy = (TimeDiscountPolicy) discountPolicy;
            return TimeDiscountPolicyResponse.of(timeDiscountPolicy);
        }
        return null;
    }

    @Override
    protected boolean isAssignable(DiscountPolicy discountPolicy) {
        return discountPolicy.getClass().isAssignableFrom(TimeDiscountPolicy.class);
    }
}
