package com.momstouch.momstouchbe.global.vo;

import com.momstouch.momstouchbe.global.vo.Money;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


class MoneyTest {

    @Test
    void 값객체_동등성_테스트() {
        Money x = Money.of(3000);
        Money y = Money.of(3000);
        Integer price = 3000;

        Assertions.assertThat(x.equals(x)).isTrue();
        Assertions.assertThat(x.equals(null)).isFalse();
        Assertions.assertThat(x.equals(price)).isFalse();
        Assertions.assertThat(x.equals(y)).isTrue();

        Assertions.assertThatThrownBy(() -> {
            Money.of(-100);
        }).isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> {
            new Money(BigDecimal.valueOf(-100));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}