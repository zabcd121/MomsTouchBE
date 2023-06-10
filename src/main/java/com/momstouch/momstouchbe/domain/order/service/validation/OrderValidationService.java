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

    public boolean validate(Order order) {
        List<OrderMenu> orderMenuList = order.getOrderMenuList();
        for (OrderMenu orderMenu : orderMenuList) {
            validate(orderMenu);
        }

        return true;
    }

    private void validate(OrderMenu orderMenu) {
        List<OrderOptionGroup> orderOptionGroupList = orderMenu.getOrderOptionGroupList();
        Menu menu = orderMenu.getMenu();

        for (OrderOptionGroup orderOptionGroup : orderOptionGroupList) {
            validate(menu,orderOptionGroup);
        }
    }

    private void validate(Menu menu, OrderOptionGroup orderOptionGroup) {
        List<OptionGroupSpecification> optionGroupList = orderValidationRepository.findByOrderOptionGroupInMenu(orderOptionGroup,menu);
        List<OrderOption> orderOptionList = orderOptionGroup.getOrderOptionList();

        for (OptionGroupSpecification optionGroupSpecification : optionGroupList) {
            for (OrderOption orderOption : orderOptionList) {
                validate(optionGroupSpecification,orderOption);
            }
        }
    }

    private void validate(OptionGroupSpecification optionGroupSpecification, OrderOption orderOption) {
        //TODO : optionGroupSpecification에 orderOption으로 조회하는 쿼리
        boolean exist = orderValidationRepository.existOrderOptionInOptionGroupSpecification(orderOption, optionGroupSpecification);

        if(!exist) throw new IllegalStateException();
    }

}
