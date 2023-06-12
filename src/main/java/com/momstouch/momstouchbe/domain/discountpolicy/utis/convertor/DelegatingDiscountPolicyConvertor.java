package com.momstouch.momstouchbe.domain.discountpolicy.utis.convertor;

import com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse.*;

public class DelegatingDiscountPolicyConvertor extends DiscountPolicyConvertor{

    private static DelegatingDiscountPolicyConvertor delegatingDiscountPolicyConvertor;
    private final List<DiscountPolicyConvertor> convertors;

    private DelegatingDiscountPolicyConvertor() {
        this.convertors = List.of(new AmountDiscountPolicyConvertor(),
                new RateDiscountPolicyConvertor(),
                new TimeDiscountPolicyConvertor());
    }


    @Override
    protected DiscountPolicyResponse convertResponse(DiscountPolicy discountPolicy) {
        for (DiscountPolicyConvertor convertor : convertors) {
            DiscountPolicyResponse discountPolicyResponse = convertor.convertResponse(discountPolicy);
            if(discountPolicyResponse != null) return discountPolicyResponse;
        }

        return null;
    }

    @Override
    protected boolean isAssignable(DiscountPolicy discountPolicy) {
        throw new UnsupportedOperationException();
    }

    public static DiscountPolicyResponse convert(DiscountPolicy discountPolicy) {
        if(delegatingDiscountPolicyConvertor == null) {
            delegatingDiscountPolicyConvertor = new DelegatingDiscountPolicyConvertor();
        }

        return delegatingDiscountPolicyConvertor.convertResponse(discountPolicy);
    }
}
