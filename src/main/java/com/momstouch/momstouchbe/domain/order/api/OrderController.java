package com.momstouch.momstouchbe.domain.order.api;

import com.momstouch.momstouchbe.domain.order.application.OrderAppService;
import com.momstouch.momstouchbe.domain.order.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.momstouch.momstouchbe.domain.order.dto.OrderRequest.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderAppService orderAppService;
    @GetMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //TODO : Member 클래스로 변환
        orderAppService.order(createOrderRequest,authentication);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
