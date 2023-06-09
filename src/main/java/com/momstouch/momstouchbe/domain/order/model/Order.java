package com.momstouch.momstouchbe.domain.order.model;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.global.domain.Money;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private List<OrderMenu> orderMenuList = new ArrayList<>();

    private String address;
    private String phoneNumber;

    private Money totalPrice;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDER;

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
