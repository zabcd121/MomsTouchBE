package com.momstouch.momstouchbe.domain.order.service;

import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.global.domain.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class MenuInfo {
    private Menu menu;
    private List<OptionGroupSelectInfo> optionGroupSelectInfoList;
    private Integer count;

    public Money getTotalPrice() {
        Money total = menu.getPrice();

        for (OptionGroupSelectInfo optionGroupSelectInfo : optionGroupSelectInfoList) {
            total = total.plus(optionGroupSelectInfo.getTotalPrice());
        }
        return total.times(count);
    }
}
