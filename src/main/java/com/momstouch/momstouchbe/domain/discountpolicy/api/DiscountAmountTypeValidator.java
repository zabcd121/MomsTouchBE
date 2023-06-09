package com.momstouch.momstouchbe.domain.discountpolicy.api;

import com.momstouch.momstouchbe.domain.discountpolicy.annotation.DiscountPolicyType;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DiscountAmountTypeValidator implements ConstraintValidator<DiscountPolicyType,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && DiscountPolicy.DISCOUNT_POLICY_TYPES.contains(value);
    }
}
