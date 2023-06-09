package com.momstouch.momstouchbe.domain.order.service;

import com.momstouch.momstouchbe.global.domain.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class OptionSelectInfo {

    private Long id;
    private String name;
    private Money price;
}
