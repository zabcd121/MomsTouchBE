package com.momstouch.momstouchbe.domain.DiscountPolicy.model.repository.command;

import com.momstouch.momstouchbe.domain.DiscountPolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.DiscountPolicy.model.RateDiscountPolicy;
import com.momstouch.momstouchbe.domain.DiscountPolicy.model.TimeDiscountPolicy;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;

public interface DiscountPolicyCommandRepository {

    Long createAmountDiscountPolicy(AmountDiscountPolicy amountDiscountPolicy);
    Long createRateDiscountPolicy(RateDiscountPolicy rateDiscountPolicy);
    Long createTimeDiscountPolicy(TimeDiscountPolicy discountPolicy);
}
