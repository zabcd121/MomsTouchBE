package com.momstouch.momstouchbe.domain.discountpolicy.utis.convertor;

import com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse.DiscountPolicyResponse;
import com.momstouch.momstouchbe.domain.discountpolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;

public class AmountDiscountPolicyConvertor extends DiscountPolicyConvertor {


    @Override
    protected DiscountPolicyResponse convertResponse(DiscountPolicy discountPolicy) {
        if(isAssignable(discountPolicy)) {

        }

        return null;
    }

    @Override
    protected boolean isAssignable(DiscountPolicy discountPolicy) {
        return discountPolicy.getClass().isAssignableFrom(AmountDiscountPolicy.class);
    }
}
