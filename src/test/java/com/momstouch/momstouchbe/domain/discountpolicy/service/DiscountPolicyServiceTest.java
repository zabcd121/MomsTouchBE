package com.momstouch.momstouchbe.domain.DiscountPolicy.service;

import com.momstouch.momstouchbe.domain.DiscountPolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.DiscountPolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.DiscountPolicy.model.RateDiscountPolicy;
import com.momstouch.momstouchbe.domain.DiscountPolicy.model.TimeDiscountPolicy;
import com.momstouch.momstouchbe.global.domain.Money;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DiscountPolicyServiceTest {

    @Autowired
    private DiscountPolicyService discountPolicyService;
    @Test
    public void 할인정책_생성_테스트() {
        LocalTime baseTime = LocalTime.of(12, 12);
        Long amountDiscountPolicyId = discountPolicyService.createAmountDiscountPolicy(1000, 200);
        Long rateDiscountPolicyId = discountPolicyService.createRateDiscountPolicy(1000, 10.0);
        Long timeDiscountPolicyId = discountPolicyService.createTimeDiscountPolicy(baseTime, 200);

        Optional<DiscountPolicy> amountOptional = discountPolicyService.findById(amountDiscountPolicyId);
        Optional<DiscountPolicy> rateDiscountOptional = discountPolicyService.findById(rateDiscountPolicyId);
        Optional<DiscountPolicy> timeDiscountOptional = discountPolicyService.findById(timeDiscountPolicyId);

        assertThat(amountOptional.isPresent()).isTrue();
        assertThat(rateDiscountOptional.isPresent()).isTrue();
        assertThat(timeDiscountOptional.isPresent()).isTrue();

        AmountDiscountPolicy amountDiscountPolicy = (AmountDiscountPolicy) amountOptional.get();
        RateDiscountPolicy rateDiscountPolicy = (RateDiscountPolicy) rateDiscountOptional.get();
        TimeDiscountPolicy timeDiscountPolicy = (TimeDiscountPolicy) timeDiscountOptional.get();

        assertThat(amountDiscountPolicy.getBaseAmount()).isEqualTo(Money.of(1000)); assertThat(amountDiscountPolicy.getDiscountAmount()).isEqualTo(Money.of(200));
        assertThat(rateDiscountPolicy.getBaseAmount()).isEqualTo(Money.of(1000)); assertThat(rateDiscountPolicy.getDiscountRate()).isEqualTo(10.0);
        assertThat(timeDiscountPolicy.getBaseTime()).isEqualTo(baseTime); assertThat(timeDiscountPolicy.getDiscountAmount()).isEqualTo(Money.of(200));
    }

    @Test
    public void 할인정책_삭제_테스트() {
        LocalTime baseTime = LocalTime.of(12, 12);
        Long amountDiscountPolicyId = discountPolicyService.createAmountDiscountPolicy(1000, 200);
        Long rateDiscountPolicyId = discountPolicyService.createRateDiscountPolicy(1000, 10.0);
        Long timeDiscountPolicyId = discountPolicyService.createTimeDiscountPolicy(baseTime, 200);

        assertThat(discountPolicyService.delete(amountDiscountPolicyId)).isEqualTo(amountDiscountPolicyId);
        assertThat(discountPolicyService.delete(rateDiscountPolicyId)).isEqualTo(rateDiscountPolicyId);
        assertThat(discountPolicyService.delete(timeDiscountPolicyId)).isEqualTo(timeDiscountPolicyId);
    }

}