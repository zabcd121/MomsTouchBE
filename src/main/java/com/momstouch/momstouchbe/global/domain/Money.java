package com.momstouch.momstouchbe.global.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
public class Money {

    public static final Money ZERO = Money.of(0);

    @Column(insertable = false,updatable = false)
    private BigDecimal amount;


    Money(Integer amount) {
        if(amount < 0) throw new IllegalArgumentException();
        this.amount = BigDecimal.valueOf(amount);
    }

    Money(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException();
        this.amount = amount;
    }

    public static Money of(int amount) {
        return new Money(amount);
    }

    public Money minus(Money money) {
        return new Money(amount.subtract(money.amount));
    }

    public Money plus(Money money) {
        return new Money(amount.add(money.amount));
    }

    public Money times(double percent) {
        return new Money(amount.multiply(BigDecimal.valueOf(percent)));
    }

    public Money divide(double percent) {
        return new Money(amount.divide(BigDecimal.valueOf(percent)));
    }

    public boolean lessThan(Money o) {
        return amount.compareTo(o.amount) < 0;
    }

    public boolean moreThan(Money o) {
        return amount.compareTo(o.amount) > 0;
    }

    public boolean equalOrLess(Money o) {
        return amount.compareTo(o.amount) <= 0;
    }

    public boolean equalsOrMore(Money o) {
        return amount.compareTo(o.amount) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount.toBigInteger(), money.amount.toBigInteger());
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}
