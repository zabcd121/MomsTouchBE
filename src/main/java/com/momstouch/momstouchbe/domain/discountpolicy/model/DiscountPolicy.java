package com.momstouch.momstouchbe.domain.discountpolicy.model;

import com.momstouch.momstouchbe.global.vo.Money;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DiscountPolicy {

    public static final List<String> DISCOUNT_POLICY_TYPES = List.of("AMOUNT","RATE","TIME");

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="discount_policy_id")
    private Long id;

    public abstract Money discount(Money price);
}
