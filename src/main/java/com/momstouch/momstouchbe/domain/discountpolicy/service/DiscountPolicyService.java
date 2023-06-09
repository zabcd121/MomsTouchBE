package com.momstouch.momstouchbe.domain.DiscountPolicy.service;

import com.momstouch.momstouchbe.domain.DiscountPolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.DiscountPolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.DiscountPolicy.model.RateDiscountPolicy;
import com.momstouch.momstouchbe.domain.DiscountPolicy.model.TimeDiscountPolicy;
import com.momstouch.momstouchbe.domain.DiscountPolicy.model.repository.DiscountPolicyRepository;
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

    @Transactional
    public Long createAmountDiscountPolicy(int baseAmount, int discountAmount) {
        AmountDiscountPolicy discountPolicy = AmountDiscountPolicy.builder()
                .baseAmount(baseAmount)
                .discountAmount(discountAmount)
                .build();

        return discountPolicyRepository.createAmountDiscountPolicy(discountPolicy);
    };

    @Transactional
    public Long createRateDiscountPolicy(int baseAmount, double discountRate) {
        RateDiscountPolicy rateDiscountPolicy = RateDiscountPolicy.builder()
                .baseAmount(baseAmount)
                .discountRate(discountRate)
                .build();

        return discountPolicyRepository.createRateDiscountPolicy(rateDiscountPolicy);
    };

    @Transactional
    public Long createTimeDiscountPolicy(LocalTime baseTime, int discountAmount) {
        TimeDiscountPolicy discountPolicy = TimeDiscountPolicy.builder()
                .baseTime(baseTime)
                .discountAmount(discountAmount)
                .build();

        return discountPolicyRepository.createTimeDiscountPolicy(discountPolicy);
    };

    public Optional<DiscountPolicy> findById(Long id) {
         return discountPolicyRepository.findById(id);
    }

    public List<DiscountPolicy> findAll() {
        return discountPolicyRepository.findAll();
    }

    public Long delete(Long id) {
        Optional<DiscountPolicy> byId = discountPolicyRepository.findById(id);
        DiscountPolicy discountPolicy = byId.orElseThrow(NoSuchElementException::new);
        discountPolicyRepository.delete(discountPolicy);
        return discountPolicy.getId();
    }


}
