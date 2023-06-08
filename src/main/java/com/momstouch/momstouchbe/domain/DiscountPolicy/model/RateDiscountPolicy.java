package com.momstouch.momstouchbe.domain.DiscountPolicy.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@DiscriminatorValue("RATE_DISCOUNT_POLICY")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RateDiscountPolicy extends DiscountPolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer baseAmount;

    private Double discountRate;


    @Override
    public int discount(int price) {
        if (price >= baseAmount) {
            return (int) (price - (price * discountRate));
        }
        return price;
    }
}
