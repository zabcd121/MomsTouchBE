package com.momstouch.momstouchbe.setup;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopRepository;
import com.momstouch.momstouchbe.domain.shop.model.repository.ShopSearchableRepository;
import com.momstouch.momstouchbe.global.domain.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class ShopSetup {

    @Autowired
    private ShopRepository shopRepository;

    public Shop saveMenu(Member member, String name, String description, String address, String phoneNumber, LocalTime openTime, LocalTime closedTime, Integer minOrderPrice) {
        return shopRepository.save(
                Shop.builder()
                        .owner(member)
                        .name(name)
                        .address(address)
                        .phoneNumber(phoneNumber)
                        .openTime(openTime)
                        .closedTime(closedTime)
                        .minOrderPrice(Money.of(minOrderPrice))
                        .build()
        );
    }

}
