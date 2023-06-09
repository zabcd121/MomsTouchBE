package com.momstouch.momstouchbe.domain.discountpolicy.utis.provider;

import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopRepository;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

import static com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand.*;

public class AmountDiscountPolicyProvider extends DiscountPolicyProvider {

    public AmountDiscountPolicyProvider(DiscountPolicyService discountPolicyService, ShopRepository shopRepository) {
        super(discountPolicyService,shopRepository);
    }

    @Override
    protected Long provide(DiscountPolicyCreateCommand command) {

        if (isAssignable(command)) {
            AmountDiscountPolicyCreateCommand create = (AmountDiscountPolicyCreateCommand) command;
            Integer baseAmount = create.getBaseAmount();
            Integer discountAmount = create.getDiscountAmount();
            Shop shop = shopRepository.findById(command.getShopId()).orElseThrow(NoSuchElementException::new);
            return discountPolicyService.createAmountDiscountPolicy(shop,baseAmount, discountAmount);
        }

        return null;
    }

    @Override
    protected boolean isAssignable (DiscountPolicyCreateCommand command){
        return command.getClass().isAssignableFrom(AmountDiscountPolicyCreateCommand.class);
    }
}
