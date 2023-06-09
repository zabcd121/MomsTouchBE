package com.momstouch.momstouchbe.domain.DiscountPolicy.model;

import com.momstouch.momstouchbe.global.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class AmountDiscountPolicyTest {
    @Test
    public void 할인금액_양수_테스트() {
        assertThatThrownBy(()-> {
            AmountDiscountPolicy build = AmountDiscountPolicy.builder()
                    .discountAmount(-1)
                    .baseAmount(1)
                    .build();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 기준금액_양수_테스트() {
        assertThatThrownBy(()-> {
            AmountDiscountPolicy build = AmountDiscountPolicy.builder()
                    .discountAmount(1)
                    .baseAmount(0)
                    .build();
        }).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void 할인_테스트() {
        AmountDiscountPolicy discountPolicy = AmountDiscountPolicy.builder()
                .discountAmount(300)
                .baseAmount(10000)
                .build();

        assertThat(discountPolicy.discount(Money.of(12000))).isEqualTo(Money.of(11700));
        assertThat(discountPolicy.discount(Money.of(10000))).isEqualTo(Money.of(9700));
        assertThat(discountPolicy.discount(Money.of(0))).isEqualTo(Money.of(0));
    }
}