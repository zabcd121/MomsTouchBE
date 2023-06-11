package com.momstouch.momstouchbe.domain.order.service;

import com.momstouch.momstouchbe.global.domain.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class OptionGroupSelectInfo {

    private String name;
    private List<OptionSelectInfo> optionSelectInfoList;
}
