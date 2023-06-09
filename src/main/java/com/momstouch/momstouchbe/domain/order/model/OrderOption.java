package com.momstouch.momstouchbe.domain.order.model;

import com.momstouch.momstouchbe.global.domain.Money;

import javax.persistence.Embeddable;
import javax.validation.constraints.Positive;

@Embeddable
public class OrderOption {

    private String name;

    private Money price;
}
