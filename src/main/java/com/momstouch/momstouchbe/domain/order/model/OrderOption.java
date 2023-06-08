package com.momstouch.momstouchbe.domain.order.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.Positive;

@Embeddable
public class OrderOption {

    private String name;

    @Positive
    private Integer price;
}
