package com.momstouch.momstouchbe.domain.order.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class OptionGroupSelectInfo {

    private String name;
    private List<OptionSelectInfo> optionSelectInfoList;

}
