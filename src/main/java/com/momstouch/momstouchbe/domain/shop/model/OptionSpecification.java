package com.momstouch.momstouchbe.domain.shop.model;

import com.momstouch.momstouchbe.global.domain.Money;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionSpecification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Money price;

}
