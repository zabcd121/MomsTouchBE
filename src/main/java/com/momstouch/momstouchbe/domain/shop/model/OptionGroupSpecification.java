package com.momstouch.momstouchbe.domain.shop.model;


import com.momstouch.momstouchbe.global.domain.Money;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OptionGroupSpecification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="option_group_id")
    private Long id;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="option_group_id")
    private List<OptionSpecification> optionList = new ArrayList<>();

    @NotNull
    private String name;

    public Money getTotalPrice() {
        Money total = Money.ZERO;

        for (OptionSpecification optionSpecification : optionList) {
            total = total.plus(optionSpecification.getPrice());
        }

        return total;
    }

    public void update(List<OptionSpecification> optionList, String name) {
        this.optionList.clear();
        this.optionList.addAll(optionList);
        this.name = name;
    }

}
