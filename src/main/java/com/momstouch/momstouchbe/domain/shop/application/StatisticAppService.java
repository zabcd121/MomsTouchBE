package com.momstouch.momstouchbe.domain.shop.application;

import com.momstouch.momstouchbe.domain.shop.dto.StatisticResponse;
import com.momstouch.momstouchbe.domain.shop.repository.ShopStatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.momstouch.momstouchbe.domain.shop.dto.StatisticResponse.*;

@Service
@RequiredArgsConstructor
public class StatisticAppService {

    private final ShopStatisticRepository shopStatisticRepository;

    public StatisticResponse statisticPerShop(Long shopId) {
        OrderStatisticResponse orderStatistic = shopStatisticRepository.orderStatisticByShopId(shopId);
        List<OrderMenuStatisticResponse> orderMenuStatisticResponses = shopStatisticRepository.orderMenuStatisticByOrderIdList(shopId);

        return new StatisticResponse(orderStatistic,orderMenuStatisticResponses);

    }


}
