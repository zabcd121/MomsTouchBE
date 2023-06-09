package com.momstouch.momstouchbe.domain.discountpolicy.dto;

import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import lombok.*;

import java.time.LocalTime;

public class DiscountRequest {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CreateDiscountPolicyRequest {
        private Integer baseAmount;
        private Integer discountAmount;
        private Double discountRate;
        private LocalTime baseTime;
    }

}
