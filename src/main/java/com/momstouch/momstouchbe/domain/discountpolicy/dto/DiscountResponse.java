package com.momstouch.momstouchbe.domain.discountpolicy.dto;

import com.momstouch.momstouchbe.domain.discountpolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.RateDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.TimeDiscountPolicy;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiscountResponse {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DiscountListResponse {

        private List<AmountDiscountPolicyResponse> amountDiscountPolicyList;
        private List<RateDiscountPolicyResponse> rateDiscountPolicyList;
        private List<TimeDiscountPolicyResponse> timeDiscountPolicyList;

        public DiscountListResponse() {
            this.amountDiscountPolicyList = new ArrayList<>();
            this.rateDiscountPolicyList = new ArrayList<>();
            this.timeDiscountPolicyList = new ArrayList<>();
        }

        public static DiscountListResponse of(List<AmountDiscountPolicy> amountDiscountPolicyList,
                                              List<RateDiscountPolicy> rateDiscountPolicyList,
                                              List<TimeDiscountPolicy> timeDiscountPolicyList) {
            List<AmountDiscountPolicyResponse> amountDiscountPolicyResponseList = amountDiscountPolicyList.stream()
                    .map(AmountDiscountPolicyResponse::of)
                    .toList();

            List<RateDiscountPolicyResponse> rateDiscountPolicyResponses = rateDiscountPolicyList.stream()
                    .map(RateDiscountPolicyResponse::of).toList();

            List<TimeDiscountPolicyResponse> timeDiscountPolicyResponses = timeDiscountPolicyList.stream()
                    .map(TimeDiscountPolicyResponse::of)
                    .toList();

            return new DiscountListResponse(amountDiscountPolicyResponseList,rateDiscountPolicyResponses,timeDiscountPolicyResponses);
        }
    }
    public abstract static class DiscountPolicyResponse {

        public static DiscountPolicyResponse of(DiscountPolicy discountPolicy) {
            Class<? extends DiscountPolicy> discountPolicyClass = discountPolicy.getClass();
            if(discountPolicyClass.isAssignableFrom(AmountDiscountPolicy.class)) {

                return AmountDiscountPolicyResponse.of((AmountDiscountPolicy)discountPolicy);
            } else if(discountPolicyClass.isAssignableFrom(RateDiscountPolicy.class)) {
                return RateDiscountPolicyResponse.of((RateDiscountPolicy)discountPolicy);
            } else if(discountPolicyClass.isAssignableFrom(TimeDiscountPolicy.class)) {
                return TimeDiscountPolicyResponse.of((TimeDiscountPolicy)discountPolicy);
            } else {
                return null;
            }
        }
    }
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class AmountDiscountPolicyResponse extends DiscountPolicyResponse {
        private Long id;
        private BigDecimal baseAmount;
        private BigDecimal discountAmount;

        public static AmountDiscountPolicyResponse of(AmountDiscountPolicy amountDiscountPolicy) {
            return new AmountDiscountPolicyResponse(amountDiscountPolicy.getId(),
                    amountDiscountPolicy.getBaseAmount().getAmount(),
                    amountDiscountPolicy.getDiscountAmount().getAmount());
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class TimeDiscountPolicyResponse extends DiscountPolicyResponse {
        private Long id;
        private LocalTime baseTime;
        private BigDecimal discountAmount;

        public static TimeDiscountPolicyResponse of(TimeDiscountPolicy discountPolicy) {
            return new TimeDiscountPolicyResponse(discountPolicy.getId(),
                    discountPolicy.getBaseTime(),
                    discountPolicy.getDiscountAmount().getAmount());
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class RateDiscountPolicyResponse extends DiscountPolicyResponse {
        private Long id;
        private BigDecimal baseAmount;
        private double discountRate;

        public static RateDiscountPolicyResponse of(RateDiscountPolicy discountPolicy) {
            return new RateDiscountPolicyResponse(discountPolicy.getId(),
                    discountPolicy.getBaseAmount().getAmount(),
                    discountPolicy.getDiscountRate());
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DiscountPolicyValueResponse {
        private Long id;
        private String type;
        private Integer baseAmount;
        private Integer discountAmount;
        private Double discountRate;
        private LocalTime baseTime;

        public static DiscountPolicyValueResponse of(DiscountPolicy discountPolicy) {
            if (discountPolicy instanceof AmountDiscountPolicy) {
                AmountDiscountPolicy policy = (AmountDiscountPolicy) discountPolicy;
                return new DiscountPolicyValueResponse(
                        policy.getId(),
                        "amount",
                        policy.getBaseAmount().getAmount().intValueExact(),
                        policy.getDiscountAmount().getAmount().intValueExact(),
                        null,
                        null);
            } else if (discountPolicy instanceof RateDiscountPolicy) {
                RateDiscountPolicy policy = (RateDiscountPolicy) discountPolicy;
                return new DiscountPolicyValueResponse(
                        discountPolicy.getId(),
                        "rate",
                        policy.getBaseAmount().getAmount().intValueExact(),
                        null,
                        policy.getDiscountRate(),
                        null);
            } else if (discountPolicy instanceof TimeDiscountPolicy) {
                TimeDiscountPolicy policy = (TimeDiscountPolicy) discountPolicy;
                return new DiscountPolicyValueResponse(
                        policy.getId(),
                        "time",
                        null,
                        policy.getDiscountAmount().getAmount().intValueExact(),
                        null,
                        policy.getBaseTime());
            } else return null;
        }
    }
}
