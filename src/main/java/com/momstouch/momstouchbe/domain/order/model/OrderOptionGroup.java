package com.momstouch.momstouchbe.domain.order.model;


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
public class OrderOptionGroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_option_group_id")
    private Long id;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name="order_option", joinColumns = @JoinColumn(name="order_option_group_id"))
    private List<OrderOption> orderOptionList = new ArrayList<>();

    private String name;


}
