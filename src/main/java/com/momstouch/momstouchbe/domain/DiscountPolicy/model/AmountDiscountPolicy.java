package com.momstouch.momstouchbe.domain.DiscountPolicy.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@DiscriminatorValue("AMOUNT_DISCOUNT_POLICY")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AmountDiscountPolicy extends DiscountPolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer baseAmount;

    private Integer discountAmount;

    @Override
    public int discount(int price) {
        if (price >= baseAmount) {
            return price - discountAmount;
        }
        return price;
    }
}
