package com.momstouch.momstouchbe.domain.order.model;

import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.global.domain.Money;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_id")
    private Menu menu;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="order_menu_id")
    private List<OrderOptionGroup> orderOptionGroupList = new ArrayList<>();

    private Integer count;

    public Money getTotalPrice() {
        Money menuPrice = menu.getPrice();
        for (OrderOptionGroup orderOptionGroup : orderOptionGroupList) {
            menuPrice = menuPrice.plus(orderOptionGroup.totalPrice());
        }

        return menuPrice.times(count);
    }
}
