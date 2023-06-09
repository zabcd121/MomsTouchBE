package com.momstouch.momstouchbe.domain.discountpolicy.utis.provider;

import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopRepository;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

import static com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand.*;

public class RateDiscountPolicyProvider extends DiscountPolicyProvider {

    public RateDiscountPolicyProvider(DiscountPolicyService discountPolicyService, ShopRepository shopRepository) {
        super(discountPolicyService,shopRepository);
    }

    @Override
    protected Long provide(DiscountPolicyCreateCommand command) {
        if(isAssignable(command)) {
            RateDiscountPolicyCreateCommand createCommand = (RateDiscountPolicyCreateCommand) command;
            double discountRate = createCommand.getDiscountRate();
            Integer baseAmount = createCommand.getBaseAmount();
            Shop shop = shopRepository.findById(command.getShopId()).orElseThrow(NoSuchElementException::new);
            return discountPolicyService.createRateDiscountPolicy(shop, baseAmount, discountRate);
        }
        return null;
    }

    @Override
    protected boolean isAssignable(DiscountPolicyCreateCommand command) {
        return command.getClass().isAssignableFrom(RateDiscountPolicyCreateCommand.class);
    }
}
