package com.momstouch.momstouchbe.domain.order.model.repository;

import com.momstouch.momstouchbe.domain.order.model.OrderOption;
import com.momstouch.momstouchbe.domain.order.model.OrderOptionGroup;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.momstouch.momstouchbe.domain.shop.model.QMenu.menu;
import static com.momstouch.momstouchbe.domain.shop.model.QOptionGroupSpecification.optionGroupSpecification;

@RequiredArgsConstructor
@Repository
public class OrderValidationRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<OptionGroupSpecification> findByOrderOptionGroupInMenu(OrderOptionGroup orderOptionGroup, Menu menu) {
         return jpaQueryFactory
                .select(optionGroupSpecification)
                .from(QMenu.menu)
                .leftJoin(QMenu.menu.optionGroupList)
                .where(QMenu.menu.id.eq(menu.getId())
                        .and(optionGroupSpecification.name.eq(orderOptionGroup.getName()))
                ).fetch();
    }

    public boolean existOrderOptionInOptionGroupSpecification(OrderOption orderOption, OptionGroupSpecification optionGroupSpecification) {
        QOptionSpecification option = new QOptionSpecification("option");
        QOptionGroupSpecification group = new QOptionGroupSpecification("group");
        return jpaQueryFactory
                .from(group)
                .leftJoin(group.optionList,option).fetchJoin()
                .where(option.name.eq(orderOption.getName())
                        .and(option.price.eq(orderOption.getPrice()))
                        .and(group.name.eq(optionGroupSpecification.getName())))
                .fetchFirst() != null;
    }

    public List<OptionSpecification> test(OrderOption orderOption, OptionGroupSpecification optionGroupSpecification) {
        QOptionSpecification qOptionSpecification = new QOptionSpecification("option");
        QOptionGroupSpecification qOptionGroupSpecification = new QOptionGroupSpecification("group");
        List<OptionSpecification> fetch = jpaQueryFactory
                .select(qOptionSpecification)
                .from(qOptionSpecification)
                .leftJoin(qOptionGroupSpecification)
                .on(qOptionGroupSpecification.id.eq(qOptionSpecification.id))
                .fetch();
        return fetch;
    }

}
