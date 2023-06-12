package com.momstouch.momstouchbe.domain.order.service;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MenuInfo {

    //TODO : 내일 수정하자
    private Long menuId;
    private List<OptionGroupSelectInfo> optionGroupSelectInfoList;
    private Integer count;
}
