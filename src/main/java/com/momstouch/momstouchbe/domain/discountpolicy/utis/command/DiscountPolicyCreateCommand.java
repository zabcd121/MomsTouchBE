package com.momstouch.momstouchbe.domain.discountpolicy.utis.command;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public abstract class DiscountPolicyCreateCommand {

    private Long shopId;

    public DiscountPolicyCreateCommand(Long shopId) {
        this.shopId = shopId;
    }

    @Getter
    public static class AmountDiscountPolicyCreateCommand extends DiscountPolicyCreateCommand {
        private Integer baseAmount;
        private Integer discountAmount;

        public AmountDiscountPolicyCreateCommand(Long shopId, Integer baseAmount, Integer discountAmount) {
            super(shopId);
            this.baseAmount = baseAmount;
            this.discountAmount = discountAmount;
        }
    }

    @Getter
    public static class RateDiscountPolicyCreateCommand extends DiscountPolicyCreateCommand {
        private Integer baseAmount;
        private double discountRate;

        public RateDiscountPolicyCreateCommand(Long shopId, Integer baseAmount, double discountRate) {
            super(shopId);
            this.baseAmount = baseAmount;
            this.discountRate = discountRate;
        }
    }

    @Getter
    public static class TimeDiscountPolicyCreateCommand extends DiscountPolicyCreateCommand {
        private LocalTime baseTime;
        private Integer discountAmount;

        public TimeDiscountPolicyCreateCommand(Long shopId,LocalTime baseTime, Integer discountAmount) {
            super(shopId);
            this.baseTime = baseTime;
            this.discountAmount = discountAmount;
        }
    }
}
