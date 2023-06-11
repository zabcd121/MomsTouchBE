package com.momstouch.momstouchbe.domain.shop.model;

import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.member.model.Member;
import lombok.*;
import com.momstouch.momstouchbe.global.domain.Money;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shop_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_id")
    private Member owner;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="shop_id")
    private List<Menu> menuList = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shop_id")
    private List<DiscountPolicy> discountPolicyList = new ArrayList<>();

    private String name;
    private String phoneNumber;
    private String address;
    private LocalTime openTime;
    private LocalTime closedTime;

    private Money minOrderPrice;

    public void addMenu(Menu menu) {
        menuList.add(menu);
    }

    public void addDiscountPolicy(DiscountPolicy discountPolicy) {
        discountPolicyList.add(discountPolicy);
    }

    public boolean isOwn(Member member) {
        return member.getId() == owner.getId();
    }


}