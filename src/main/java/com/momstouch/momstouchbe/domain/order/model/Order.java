package com.momstouch.momstouchbe.domain.order.model;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.global.domain.Money;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    @NotNull
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @NotNull
    private Shop shop;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="order_id")
    private List<OrderMenu> orderMenuList = new ArrayList<>();

    @NotNull
    private String address;

    @NotNull
    private String phoneNumber;

    @NotNull
    private Money totalPrice;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDER;

    public void addOrderMenu(OrderMenu orderMenu) {
        if(!orderMenuList.contains(orderMenu)) {
            orderMenuList.add(orderMenu);
            orderMenu.order(this);
        }
    }

    public Money getTotalPrice() {
        Money price = Money.ZERO;
        for (OrderMenu orderMenu : orderMenuList) {
            price = price.plus(orderMenu.getTotalPrice());
        }

        return price;
    };

    public void setTotalPrice(Money price) {
        this.totalPrice = price;
    }

    public void accept() {orderStatus = OrderStatus.ACCEPT;}
    public void cancel() {
        orderStatus = OrderStatus.CANCEL;
    }

    public void deliver() {
        orderStatus = OrderStatus.DELIVERY;
    }

    public void complete() {
        orderStatus = OrderStatus.COMPLETE;
    }
}
