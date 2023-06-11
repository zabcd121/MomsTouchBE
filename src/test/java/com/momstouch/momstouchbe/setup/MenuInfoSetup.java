package com.momstouch.momstouchbe.setup;

import com.momstouch.momstouchbe.domain.order.service.MenuInfo;
import com.momstouch.momstouchbe.domain.order.service.OptionGroupSelectInfo;
import com.momstouch.momstouchbe.domain.order.service.OptionSelectInfo;
import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.domain.shop.model.OptionGroupSpecification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuInfoSetup {

    public MenuInfo of(Menu menu, List<OptionGroupSpecification> optionGroupSpecificationList , Integer count) {

        List<OptionGroupSelectInfo> optionGroupSelectInfoList = optionGroupSpecificationList.stream()
                .map(optionGroupSpecification -> {
                    return OptionGroupSelectInfo.builder()
                            .name(optionGroupSpecification.getName())
                            .optionSelectInfoList(optionGroupSpecification.getOptionList().stream()
                                    .map(optionSpecification -> {
                                        return OptionSelectInfo.builder()
                                                .name(optionSpecification.getName())
                                                .price(optionSpecification.getPrice())
                                                .build();
                                    })
                                    .toList()
                            ).build();
                })
                .toList();
        return MenuInfo
                .builder()
                .menu(menu)
                .optionGroupSelectInfoList(optionGroupSelectInfoList)
                .count(count)
                .build();
    }
}
