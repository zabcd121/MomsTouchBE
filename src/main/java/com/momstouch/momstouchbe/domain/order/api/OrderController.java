package com.momstouch.momstouchbe.domain.order.api;

import com.momstouch.momstouchbe.domain.order.application.OrderAppService;
import com.momstouch.momstouchbe.domain.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> findOrder(@PathVariable Long orderId) {
        OrderResponse orderRes = orderAppService.findOrderById(orderId);
        return new ResponseEntity<>(orderRes,HttpStatus.OK);
    }


    @PostMapping("/order/{orderId}")
    public ResponseEntity<?> acceptOrder(@PathVariable Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderAppService.accept(orderId,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/order/{orderId}/delivery")
    public ResponseEntity<?> deliverOrder(@PathVariable Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderAppService.deliver(orderId,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderAppService.cancel(orderId,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/order/{orderId}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderAppService.complete(orderId,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
