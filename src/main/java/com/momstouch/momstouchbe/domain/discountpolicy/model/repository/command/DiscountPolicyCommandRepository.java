package com.momstouch.momstouchbe.domain.discountpolicy.model.repository.command;

import com.momstouch.momstouchbe.domain.discountpolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.RateDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.TimeDiscountPolicy;

public interface DiscountPolicyCommandRepository {

    Long createAmountDiscountPolicy(AmountDiscountPolicy amountDiscountPolicy);
    Long createRateDiscountPolicy(RateDiscountPolicy rateDiscountPolicy);
    Long createTimeDiscountPolicy(TimeDiscountPolicy discountPolicy);
}
