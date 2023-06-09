package com.momstouch.momstouchbe.domain.discountpolicy.utis.command;

import lombok.Getter;

import java.time.LocalTime;

public abstract class DiscountPolicyCreateCommand {

    @Getter
    public static class AmountDiscountPolicyCreateCommand extends DiscountPolicyCreateCommand {
        private Integer baseAmount;
        private Integer discountAmount;

        public AmountDiscountPolicyCreateCommand(Integer baseAmount, Integer discountAmount) {
            this.baseAmount = baseAmount;
            this.discountAmount = discountAmount;
        }
    }

    @Getter
    public static class RateDiscountPolicyCreateCommand extends DiscountPolicyCreateCommand {
        private Integer baseAmount;
        private double discountRate;

        public RateDiscountPolicyCreateCommand(Integer baseAmount, double discountRate) {
            this.baseAmount = baseAmount;
            this.discountRate = discountRate;
        }
    }

    @Getter
    public static class TimeDiscountPolicyCreateCommand extends DiscountPolicyCreateCommand {
        private LocalTime baseTime;
        private Integer discountAmount;

        public TimeDiscountPolicyCreateCommand(LocalTime baseTime, Integer discountAmount) {
            this.baseTime = baseTime;
            this.discountAmount = discountAmount;
        }
    }
}
