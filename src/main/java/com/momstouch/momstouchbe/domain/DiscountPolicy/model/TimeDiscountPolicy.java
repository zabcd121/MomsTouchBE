package com.momstouch.momstouchbe.domain.DiscountPolicy.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@DiscriminatorValue("TIME_DISCOUNT_POLICY")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeDiscountPolicy extends DiscountPolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime baseTime;
    private Integer discountAmount;

    @Override
    public int discount(int price) {
        if (LocalTime.now().isAfter(baseTime)) {
            return price - discountAmount;
        }
        return price;
    }
}
