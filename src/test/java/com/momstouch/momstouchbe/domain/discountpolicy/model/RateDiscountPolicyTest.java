package com.momstouch.momstouchbe.domain.discountpolicy.model;

import com.momstouch.momstouchbe.global.vo.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RateDiscountPolicyTest {

    private Validator validator;

    @BeforeEach
    public void init(){
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    public void 할인율_음수_최소값_테스트() {

        RateDiscountPolicy build = RateDiscountPolicy.builder()
                .discountRate(-1.1)
                .baseAmount(10000)
                .build();
        Set<ConstraintViolation<DiscountPolicy>> violations = validator.validate(build);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void 할인율_최댓값_테스트() {

        RateDiscountPolicy build = RateDiscountPolicy.builder()
                .discountRate(120.123)
                .baseAmount(10000)
                .build();

        Set<ConstraintViolation<DiscountPolicy>> violations = validator.validate(build);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void 할인_테스트() {
        RateDiscountPolicy discountPolicy = RateDiscountPolicy.builder()
                .discountRate(8.0)
                .baseAmount(10000)
                .build();

        assertThat(discountPolicy.discount(Money.of(12000))).isEqualTo(Money.of(11040));
        assertThat(discountPolicy.discount(Money.of(10000))).isEqualTo(Money.of(9200));
        assertThat(discountPolicy.discount(Money.of(0))).isEqualTo(Money.of(0));
    }
}