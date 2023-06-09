package com.momstouch.momstouchbe.domain.shop.model;

import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import lombok.*;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.global.domain.Money;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="menu_id")
    private Long id;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="menu_id")
    private List<OptionGroupSpecification> optionGroupList = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="discount_policy_id")
    private DiscountPolicy discountPolicy;

    private String name;
    private String description;
    private Money price;
    private String imageURL;

    @Enumerated(EnumType.STRING)
    private Category category;


}
