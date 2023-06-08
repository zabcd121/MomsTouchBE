package com.momstouch.momstouchbe.domain.order.model;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
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
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


}
