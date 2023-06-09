package com.momstouch.momstouchbe.domain.order.service;

import com.momstouch.momstouchbe.global.domain.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class OptionGroupSelectInfo {

    private String name;
    private List<OptionSelectInfo> optionSelectInfoList;

    public Money getTotalPrice() {
        Money total = Money.ZERO;

        for (OptionSelectInfo optionSelectInfo : optionSelectInfoList) {
            total = total.plus(optionSelectInfo.getPrice());
        }

        return total;
    }
}
