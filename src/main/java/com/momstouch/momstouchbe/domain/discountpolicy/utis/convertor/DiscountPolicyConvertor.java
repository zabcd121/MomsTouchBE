package com.momstouch.momstouchbe.domain.discountpolicy.utis.convertor;

import com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse.DiscountPolicyResponse;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import org.springframework.stereotype.Component;

public abstract class DiscountPolicyConvertor {


    protected abstract DiscountPolicyResponse convertResponse(DiscountPolicy discountPolicy);

    protected abstract boolean isAssignable(DiscountPolicy discountPolicy);

}
