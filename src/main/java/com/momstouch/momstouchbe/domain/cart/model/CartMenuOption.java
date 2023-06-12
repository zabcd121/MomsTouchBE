package com.momstouch.momstouchbe.domain.cart.model;

import com.momstouch.momstouchbe.global.vo.Money;
import com.momstouch.momstouchbe.global.vo.BaseTime;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartMenuOption extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_menu_option_id")
    private Long id;

    private Long menuOptionId;
    private Money price;
}
