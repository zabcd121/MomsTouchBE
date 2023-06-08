package com.momstouch.momstouchbe.domain.menu.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="menu_id")
    private Long id;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_id")
    private List<OptionGroupSpecification> optionGroupList = new ArrayList<>();

    private String name;
    private String description;
    private int price;
    private String imageURL;


}
