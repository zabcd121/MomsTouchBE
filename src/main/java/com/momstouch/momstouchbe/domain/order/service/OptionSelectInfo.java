package com.momstouch.momstouchbe.domain.order.service;

import com.momstouch.momstouchbe.global.vo.Money;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OptionSelectInfo {

    private Long id;
    private String name;
    private Money price;
}
