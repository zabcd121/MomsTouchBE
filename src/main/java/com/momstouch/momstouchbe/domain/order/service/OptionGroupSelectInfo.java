package com.momstouch.momstouchbe.domain.order.service;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OptionGroupSelectInfo {

    private String name;
    private List<OptionSelectInfo> optionSelectInfoList;
}
