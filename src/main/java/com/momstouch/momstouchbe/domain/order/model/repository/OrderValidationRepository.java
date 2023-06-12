package com.momstouch.momstouchbe.domain.order.model.repository;

import com.momstouch.momstouchbe.domain.order.model.OrderOption;
import com.momstouch.momstouchbe.domain.order.model.OrderOptionGroup;
import com.momstouch.momstouchbe.domain.shop.model.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.momstouch.momstouchbe.domain.shop.model.QOptionGroupSpecification.optionGroupSpecification;

@RequiredArgsConstructor
@Repository
public class OrderValidationRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<OptionGroupSpecification> findByOrderOptionGroupInMenu(OrderOptionGroup orderOptionGroup, Menu menu) {
         return jpaQueryFactory
                .select(optionGroupSpecification)
                .from(QMenu.menu)
                .leftJoin(QMenu.menu.optionGroupList,optionGroupSpecification)
                .where(QMenu.menu.id.eq(menu.getId())
                        .and(optionGroupSpecification.name.eq(orderOptionGroup.getName()))
                ).fetch();
    }

    public boolean existOrderOptionInOptionGroupSpecification(OrderOption orderOption, OptionGroupSpecification optionGroupSpecification) {
        QOptionSpecification option = new QOptionSpecification("optionSpecification");
        QOptionGroupSpecification group = new QOptionGroupSpecification("optionGroupSpecification");
        return jpaQueryFactory
                .from(group)
                .leftJoin(group.optionList,option).fetchJoin()
                .where(option.name.eq(orderOption.getName())
                        .and(option.price.eq(orderOption.getPrice()))
                        .and(group.id.eq(optionGroupSpecification.getId())))
                .fetchFirst() != null; //TODO: 테스트
    }

}
