package com.momstouch.momstouchbe.domain.shop.model;


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
public class OptionGroupSpecification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="option_group_id")
    private Long id;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="option_group_id")
    private List<OptionSpecification> optionList = new ArrayList<>();

    private String name;


}
