package com.momstouch.momstouchbe.domain.discountpolicy.model;

import com.momstouch.momstouchbe.global.domain.Money;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DiscountPolicy {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="discount_policy_id")
    private Long id;

    public abstract Money discount(Money price);
}
