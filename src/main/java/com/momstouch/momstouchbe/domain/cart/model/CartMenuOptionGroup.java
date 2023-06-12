package com.momstouch.momstouchbe.domain.cart.model;

import com.momstouch.momstouchbe.global.vo.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartMenuOptionGroup extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menuOptionGroupId;

    private String menuOptionGroupName;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_menu_option_group_id")
    private List<CartMenuOption> cartMenuOptionList = new ArrayList<>();
}
