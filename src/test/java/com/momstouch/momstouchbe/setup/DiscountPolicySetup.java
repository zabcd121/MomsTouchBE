package com.momstouch.momstouchbe.setup;

import com.momstouch.momstouchbe.domain.discountpolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.DiscountPolicyRepository;
import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscountPolicySetup {

    @Autowired
    private DiscountPolicyService discountPolicyService;

    public Long saveAmountDiscountPolicy(Shop shop, Integer baseAmount, Integer discountAmount) {
        return discountPolicyService.createAmountDiscountPolicy(shop, baseAmount, discountAmount);
    }

}
