package com.momstouch.momstouchbe.domain.discountpolicy.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DiscountPolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="discount_policy_id")
    private Long id;

    public abstract int discount(int price);
}
