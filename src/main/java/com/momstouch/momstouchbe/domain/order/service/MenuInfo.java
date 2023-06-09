package com.momstouch.momstouchbe.domain.order.service;

import com.momstouch.momstouchbe.domain.shop.model.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MenuInfo {
    private Menu menu;
    private OptionGroupSelectInfo menuSelectInfo;
}
