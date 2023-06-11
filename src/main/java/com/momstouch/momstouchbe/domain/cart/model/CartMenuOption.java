package com.momstouch.momstouchbe.domain.cart.model;

import com.momstouch.momstouchbe.global.domain.Money;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartMenuOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_menu_option_id")
    private Long id;

    private Long menuOptionId;
    private Money price;
}
