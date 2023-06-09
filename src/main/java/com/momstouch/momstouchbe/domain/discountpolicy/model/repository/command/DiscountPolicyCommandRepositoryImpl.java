package com.momstouch.momstouchbe.domain.discountpolicy.model.repository.command;


import com.momstouch.momstouchbe.domain.discountpolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.RateDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.TimeDiscountPolicy;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DiscountPolicyCommandRepositoryImpl implements DiscountPolicyCommandRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long createAmountDiscountPolicy(AmountDiscountPolicy amountDiscountPolicy) {
        entityManager.persist(amountDiscountPolicy);
        return amountDiscountPolicy.getId();
    }

    @Override
    public Long createRateDiscountPolicy(RateDiscountPolicy rateDiscountPolicy) {
        entityManager.persist(rateDiscountPolicy);
        return rateDiscountPolicy.getId();
    }

    @Override
    public Long createTimeDiscountPolicy(TimeDiscountPolicy discountPolicy) {
        entityManager.persist(discountPolicy);
        return  discountPolicy.getId();
    }
}
