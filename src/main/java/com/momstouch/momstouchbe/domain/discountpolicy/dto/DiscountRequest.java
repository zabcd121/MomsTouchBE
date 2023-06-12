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

        private Long shopId;
        private Integer baseAmount;
        private Integer discountAmount;
        private Double discountRate;
        private int hour;
        private int minute;
        private int second;

        public LocalTime getBaseTime() {
            return LocalTime.of(hour,minute,second);
        }

        public void setBaseTime(LocalTime localTime) {
            hour = localTime.getHour();
            minute = localTime.getMinute();
            second = localTime.getSecond();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UpdateDiscountPolicyRequest {

        private Long menuId;
    }


}
