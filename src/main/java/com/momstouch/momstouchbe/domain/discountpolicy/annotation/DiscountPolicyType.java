package com.momstouch.momstouchbe.domain.discountpolicy.annotation;

import com.momstouch.momstouchbe.domain.discountpolicy.api.DiscountAmountTypeValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DiscountAmountTypeValidator.class)
public @interface DiscountPolicyType {

    String message() default "잘못된 타입입니다.";

}
