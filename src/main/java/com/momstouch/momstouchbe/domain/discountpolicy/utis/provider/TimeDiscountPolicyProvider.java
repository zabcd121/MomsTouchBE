package com.momstouch.momstouchbe.domain.discountpolicy.utis.provider;

import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopRepository;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

import static com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand.*;

public class TimeDiscountPolicyProvider extends DiscountPolicyProvider {

    public TimeDiscountPolicyProvider(DiscountPolicyService discountPolicyService, ShopRepository shopRepository) {
        super(discountPolicyService,shopRepository);
    }

    @Override
    protected Long provide(DiscountPolicyCreateCommand command) {

        if (isAssignable(command)) {
            TimeDiscountPolicyCreateCommand create = (TimeDiscountPolicyCreateCommand) command;
            LocalTime baseTime = create.getBaseTime();
            Integer discountAmount = create.getDiscountAmount();
            Shop shop = shopRepository.findById(command.getShopId()).orElseThrow(NoClassDefFoundError::new);
            return discountPolicyService.createTimeDiscountPolicy(shop,baseTime, discountAmount);
        }

        return null;
    }

    @Override
    protected boolean isAssignable (DiscountPolicyCreateCommand command){
        return command.getClass().isAssignableFrom(TimeDiscountPolicyCreateCommand.class);
    }
}
