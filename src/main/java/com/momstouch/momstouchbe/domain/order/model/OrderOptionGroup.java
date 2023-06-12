package com.momstouch.momstouchbe.domain.order.model;


import com.momstouch.momstouchbe.global.vo.Money;
import com.momstouch.momstouchbe.global.vo.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderOptionGroup extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_option_group_id")
    private Long id;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name="order_option", joinColumns = @JoinColumn(name="order_option_group_id"))
    private List<OrderOption> orderOptionList = new ArrayList<>();

    private String name;

    public void addOrderOption(OrderOption orderOption) {
        orderOptionList.add(orderOption);
    }

    public Money totalPrice() {
        Money zero = Money.ZERO;
        for (OrderOption orderOption : orderOptionList) {
            zero = zero.plus(orderOption.getPrice());
        }

        return zero;
    }

}
