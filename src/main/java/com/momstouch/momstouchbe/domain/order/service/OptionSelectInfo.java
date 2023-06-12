package com.momstouch.momstouchbe.domain.order.service;

import com.momstouch.momstouchbe.global.vo.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class OptionSelectInfo {

    private Long id;
    private String name;
    private Money price;
}
