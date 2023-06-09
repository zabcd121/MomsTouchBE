package com.momstouch.momstouchbe.domain.discountpolicy.application;

import com.momstouch.momstouchbe.domain.discountpolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.RateDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.TimeDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.provider.DelegationDiscountPolicyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountRequest.*;
import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse.*;
import static com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand.*;

@RequiredArgsConstructor
@Service
public class DiscountAppService {

    private final DiscountPolicyService discountPolicyService;
    private final DelegationDiscountPolicyProvider delegationDiscountPolicyProvider;

    public DiscountListResponse searchAll() {
        List<DiscountPolicy> discountPolicyList = discountPolicyService.findAll();

        List<AmountDiscountPolicy> amountDiscountPolicyList = discountPolicyList.stream()
                .filter((policy) -> policy instanceof AmountDiscountPolicy)
                .map((policy) -> (AmountDiscountPolicy)policy)
                .toList();

        List<RateDiscountPolicy> rateDiscountPolicyList = discountPolicyList.stream()
                .filter((policy -> policy instanceof RateDiscountPolicy))
                .map((policy) -> (RateDiscountPolicy)policy)
                .toList();

        List<TimeDiscountPolicy> timeDiscountPolicyList = discountPolicyList.stream()
                .filter((policy -> policy instanceof TimeDiscountPolicy))
                .map((policy) -> (TimeDiscountPolicy)policy)
                .toList();

        return DiscountListResponse.of(amountDiscountPolicyList,rateDiscountPolicyList,timeDiscountPolicyList);
    }

    public Long saveDiscountPolicy(CreateDiscountPolicyRequest createRequest, String type) {

        DiscountPolicyCreateCommand createCommand;

        if(type.equals("AMOUNT")) {
            createCommand = new AmountDiscountPolicyCreateCommand(createRequest.getBaseAmount(), createRequest.getDiscountAmount());
        } else if(type.equals("RATE")) {
            createCommand = new RateDiscountPolicyCreateCommand(createRequest.getBaseAmount(),createRequest.getDiscountRate());
        } else if(type.equals("TIME")) {
            createCommand = new TimeDiscountPolicyCreateCommand(createRequest.getBaseTime(),createRequest.getDiscountAmount());
        } else {
            throw new UnsupportedOperationException();
        }

        return delegationDiscountPolicyProvider.createDiscountPolicyProvider(createCommand);
    }


    @Transactional
    public Long removeDiscountPolicy(Long id) {
        //TODO : MENU 검색 레포지토리에서 연관 메뉴 조회하기
        return discountPolicyService.delete(id);
    }
}
