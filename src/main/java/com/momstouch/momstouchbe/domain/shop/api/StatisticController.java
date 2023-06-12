package com.momstouch.momstouchbe.domain.shop.api;

import com.momstouch.momstouchbe.domain.shop.application.StatisticAppService;
import com.momstouch.momstouchbe.domain.shop.dto.StatisticResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "통계")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class StatisticController {

    private final StatisticAppService statisticAppService;

    @GetMapping("/statistic/shop/{shopId}")
    public ResponseEntity<StatisticResponse> statistic(@PathVariable Long shopId) {
        return new ResponseEntity<>(statisticAppService.statisticPerShop(shopId), HttpStatus.OK);
    }

}
