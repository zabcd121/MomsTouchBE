package com.momstouch.momstouchbe.domain.discountpolicy.model;

import com.momstouch.momstouchbe.global.domain.Money;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@DiscriminatorValue("AMOUNT_DISCOUNT_POLICY")
//@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AmountDiscountPolicy extends DiscountPolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Money baseAmount;

    @NotNull
    private Money discountAmount;

    @Builder
    public AmountDiscountPolicy(int baseAmount, int discountAmount) {
        if(discountAmount > baseAmount) {
            throw new IllegalArgumentException();
        }
        this.baseAmount = Money.of(baseAmount);
        this.discountAmount = Money.of(discountAmount);
    }

    @Override
    public Money discount(Money price) {
        if (price.equalsOrMore(baseAmount)) {
            return price.minus(discountAmount);
        }
        return price;
    }
}
