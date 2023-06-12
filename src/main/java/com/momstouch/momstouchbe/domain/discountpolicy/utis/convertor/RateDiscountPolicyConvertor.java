package com.momstouch.momstouchbe.domain.discountpolicy.utis.convertor;

import com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.RateDiscountPolicy;

import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse.*;

public class RateDiscountPolicyConvertor extends DiscountPolicyConvertor {

    @Override
    protected DiscountPolicyResponse convertResponse(DiscountPolicy discountPolicy) {
        if(isAssignable(discountPolicy)) {
            RateDiscountPolicy rateDiscountPolicy = (RateDiscountPolicy) discountPolicy;

            return RateDiscountPolicyResponse.of(rateDiscountPolicy);
        }

        return null;
    }

    @Override
    protected boolean isAssignable(DiscountPolicy discountPolicy) {
        return discountPolicy.getClass().isAssignableFrom(RateDiscountPolicy.class);
    }
}
