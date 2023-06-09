package com.momstouch.momstouchbe.domain.discountpolicy.model;

import com.momstouch.momstouchbe.global.domain.Money;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalTime;

@Entity
@DiscriminatorValue("TIME_DISCOUNT_POLICY")
//@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TimeDiscountPolicy extends DiscountPolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalTime baseTime;

    private Money discountAmount;

    @Builder
    public TimeDiscountPolicy(LocalTime baseTime, int discountAmount) {
        this.baseTime = baseTime;
        this.discountAmount = Money.of(discountAmount);
    }




    @Override
    public Money discount(Money price) {
        if (applicable()) {
//            return price.minus(discountAmount);
            return discountAmount.equalsOrMore(price) ? Money.ZERO : price.minus(discountAmount);
        }
        return price;
    }

    public boolean applicable() {
        return LocalTime.now().isBefore(baseTime);
    }
}
