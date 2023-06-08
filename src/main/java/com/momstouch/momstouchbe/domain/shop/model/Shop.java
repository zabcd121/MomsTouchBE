package com.momstouch.momstouchbe.domain.shop.model;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.menu.model.Menu;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shop_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_id")
    private Member owner;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="shop_id")
    private List<Menu> menuList = new ArrayList<>();

    private String name;
    private String phoneNumber;
    private String address;
    private LocalTime openTime;
    private LocalTime closedTime;

    @Positive
    private Integer minOrderPrice;

}