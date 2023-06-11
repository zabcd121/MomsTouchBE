package com.momstouch.momstouchbe.domain.shop.dto;

import com.momstouch.momstouchbe.domain.shop.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class StatisticResponse {

    private OrderStatisticResponse orderStatistic;
    private List<OrderMenuStatisticResponse> menuStatistic;

    @Data
    public static class OrderStatisticResponse {
        private Long totalOrderCount;
        private BigDecimal totalOrderPrice;

        public OrderStatisticResponse(Long totalOrderCount, BigDecimal totalOrderPrice) {
            this.totalOrderCount = totalOrderCount;
            this.totalOrderPrice = totalOrderPrice;
        }

    }

    @Data
    public static class OrderMenuStatisticResponse {
        private Long menuId;
        private String menuName;
        private String description;
        private Integer count;
        private Category category;

        public OrderMenuStatisticResponse(Long menuId, String menuName, String description, Integer count, Category category) {
            this.menuId = menuId;
            this.menuName = menuName;
            this.description = description;
            this.count = count;
            this.category = category;
        }
    }
}
