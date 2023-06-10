package com.momstouch.momstouchbe.domain.order.service.validation;

import com.momstouch.momstouchbe.domain.order.model.Order;
import com.momstouch.momstouchbe.domain.order.model.OrderMenu;
import com.momstouch.momstouchbe.domain.order.model.OrderOption;
import com.momstouch.momstouchbe.domain.order.model.OrderOptionGroup;
import com.momstouch.momstouchbe.domain.order.model.repository.OrderValidationRepository;
import com.momstouch.momstouchbe.domain.order.service.OrderService;
import com.momstouch.momstouchbe.domain.shop.model.Menu;
import com.momstouch.momstouchbe.domain.shop.model.OptionGroupSpecification;
import com.momstouch.momstouchbe.domain.shop.model.OptionSpecification;
import com.momstouch.momstouchbe.domain.shop.model.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderValidationService {

    private final MenuRepository menuRepository;
    private final OrderValidationRepository orderValidationRepository;

    public void validate(Order order) {
        List<OrderMenu> orderMenuList = order.getOrderMenuList();
    }

    public void validate(OrderMenu orderMenu) {
        List<OrderOptionGroup> orderOptionGroupList = orderMenu.getOrderOptionGroupList();
        Menu menu = orderMenu.getMenu();

        for (OrderOptionGroup orderOptionGroup : orderOptionGroupList) {
            validate(menu,orderOptionGroup);
        }
    }

    public void validate(Menu menu, OrderOptionGroup orderOptionGroup) {
        //menu에 orderOptionGroup이 데이터로 OptionGroupSpecification 조회 쿼리
        List<OptionGroupSpecification> optionGroupList = orderValidationRepository.findByOrderOptionGroupInMenu(orderOptionGroup,menu);

        for (OptionGroupSpecification optionGroupSpecification : optionGroupList) {

        }

    }
//
//    private boolean equals(OptionGroupSpecification optionGroupSpecification, OrderOptionGroup orderOptionGroup) {
//
//        List<OptionSpecification> optionList = optionGroupSpecification.getOptionList();
//        List<OrderOption> orderOptionList = orderOptionGroup.getOrderOptionList();
//
//
//    }
//
//
//
//    public void validate(OptionGroupSpecification optionGroupSpecification, OrderOption orderOption) {
//        //TODO : optionGroupSpecification에 orderOption으로 조회하는 쿼리
//
//
//
//    }
//

}
