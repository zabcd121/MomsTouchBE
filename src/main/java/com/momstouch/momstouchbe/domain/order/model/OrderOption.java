package com.momstouch.momstouchbe.domain.order.model;

import com.momstouch.momstouchbe.global.domain.Money;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Positive;

@Getter
@Embeddable
public class OrderOption {

    private String name;

    private Money price;
}
