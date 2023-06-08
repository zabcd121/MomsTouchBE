package com.momstouch.momstouchbe.domain.DiscountPolicy.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Entity
@DiscriminatorValue("AMOUNT_DISCOUNT_POLICY")
//@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AmountDiscountPolicy extends DiscountPolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    private Integer baseAmount;
    @Positive
    private Integer discountAmount;

    @Builder
    public AmountDiscountPolicy(Integer baseAmount, Integer discountAmount) {
        if(discountAmount > baseAmount) {
            throw new IllegalArgumentException();
        }
        this.baseAmount = baseAmount;
        this.discountAmount = discountAmount;
    }

    @Override
    public int discount(int price) {
        if (price >= baseAmount) {
            return price - discountAmount;
        }
        return price;
    }
}
