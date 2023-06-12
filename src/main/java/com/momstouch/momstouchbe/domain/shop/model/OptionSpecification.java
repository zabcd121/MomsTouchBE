package com.momstouch.momstouchbe.domain.shop.model;

import com.momstouch.momstouchbe.global.vo.Money;
import com.momstouch.momstouchbe.global.vo.BaseTime;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionSpecification extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Embedded
    private Money price;


//    public void update(String name, Money price) {
//        this.name = name;
//        this.price = price;
//    }

}
