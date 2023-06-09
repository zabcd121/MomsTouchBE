package com.momstouch.momstouchbe.domain.discountpolicy.service;

import com.momstouch.momstouchbe.domain.discountpolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.RateDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.TimeDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.repository.DiscountPolicyRepository;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DiscountPolicyService {

    private final DiscountPolicyRepository discountPolicyRepository;

    private void enroll(Shop shop, DiscountPolicy discountPolicy) {
        shop.addDiscountPolicy(discountPolicy);
    }

    @Transactional
    public Long createAmountDiscountPolicy(Shop shop, int baseAmount, int discountAmount) {
        AmountDiscountPolicy discountPolicy = AmountDiscountPolicy.builder()
                .baseAmount(baseAmount)
                .discountAmount(discountAmount)
                .build();

        enroll(shop,discountPolicy);
        return discountPolicyRepository.createAmountDiscountPolicy(discountPolicy);
    };

    @Transactional
    public Long createRateDiscountPolicy(Shop shop, int baseAmount, double discountRate) {
        RateDiscountPolicy rateDiscountPolicy = RateDiscountPolicy.builder()
                .baseAmount(baseAmount)
                .discountRate(discountRate)
                .build();

        enroll(shop,rateDiscountPolicy);
        return discountPolicyRepository.createRateDiscountPolicy(rateDiscountPolicy);
    };

    @Transactional
    public Long createTimeDiscountPolicy(Shop shop, LocalTime baseTime, int discountAmount) {
        TimeDiscountPolicy discountPolicy = TimeDiscountPolicy.builder()
                .baseTime(baseTime)
                .discountAmount(discountAmount)
                .build();

        enroll(shop,discountPolicy);
        return discountPolicyRepository.createTimeDiscountPolicy(discountPolicy);
    };

    public Optional<DiscountPolicy> findById(Long id) {
         return discountPolicyRepository.findById(id);
    }

    public List<DiscountPolicy> findAll() {
        return discountPolicyRepository.findAll();
    }

    @Transactional
    public Long delete(Long id) {
        Optional<DiscountPolicy> byId = discountPolicyRepository.findById(id);
        DiscountPolicy discountPolicy = byId.orElseThrow(NoSuchElementException::new);
        discountPolicyRepository.delete(discountPolicy);
        return discountPolicy.getId();
    }


}
