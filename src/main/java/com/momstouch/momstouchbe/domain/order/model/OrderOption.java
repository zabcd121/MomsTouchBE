package com.momstouch.momstouchbe.domain.order.model;

import javax.persistence.Embeddable;

@Embeddable
public class OrderOption {

    private String name;
    private Integer price;
}
