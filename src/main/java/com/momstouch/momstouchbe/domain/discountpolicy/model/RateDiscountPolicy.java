package com.momstouch.momstouchbe.domain.discountpolicy.model;

import com.momstouch.momstouchbe.global.domain.Money;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@DiscriminatorValue("RATE_DISCOUNT_POLICY")
//@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RateDiscountPolicy extends DiscountPolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Money baseAmount;
    @DecimalMin(value = "0.0", message = "할인율을 0% 이상")
    @DecimalMax(value = "100.0", message = "할인율을 100% 이하")
    @NotNull
    private Double discountRate;

    @Builder
    private RateDiscountPolicy(int baseAmount, double discountRate) {
        this.baseAmount = Money.of(baseAmount);
        this.discountRate = discountRate;
    }

    @Override
    public Money discount(Money price) {
        if (price.equalsOrMore(baseAmount)) {
            return (price.times((100 - discountRate)/100));
        }
        return price;
    }
}
