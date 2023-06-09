package com.momstouch.momstouchbe.domain.DiscountPolicy.model;

import com.momstouch.momstouchbe.global.domain.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TimeDiscountPolicyTest {

    private Validator validator;

    @BeforeEach
    public void init(){
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    public void 할인금액_양수_테스트() {
        assertThatThrownBy(() -> {
            TimeDiscountPolicy timeDiscountPolicy = TimeDiscountPolicy.builder()
                    .discountAmount(-1)
                    .baseTime(LocalTime.now())
                    .build();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 할인가능시간_null_테스트() {
        TimeDiscountPolicy timeDiscountPolicy = TimeDiscountPolicy.builder()
                .discountAmount(2000)
                .baseTime(null)
                .build();
        Set<ConstraintViolation<DiscountPolicy>> violations = validator.validate(timeDiscountPolicy);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void 할인_테스트() {

        TimeDiscountPolicy timeDiscountPolicy = TimeDiscountPolicy.builder()
                .discountAmount(2000)
                .baseTime(LocalTime.now().plus(1, ChronoUnit.SECONDS))
                .build();

        assertThat(timeDiscountPolicy.discount(Money.of(12000))).isEqualTo(Money.of(10000));
        assertThat(timeDiscountPolicy.discount(Money.of(10000))).isEqualTo(Money.of(8000));
        assertThat(timeDiscountPolicy.discount(Money.of(0))).isEqualTo(Money.of(0));
    }

}