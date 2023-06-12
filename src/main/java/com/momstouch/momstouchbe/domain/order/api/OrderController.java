package com.momstouch.momstouchbe.domain.order.api;

import com.momstouch.momstouchbe.domain.order.application.OrderAppService;
import com.momstouch.momstouchbe.domain.order.dto.OrderResponse;
import com.momstouch.momstouchbe.domain.order.dto.OrderResponse.OrderListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.momstouch.momstouchbe.domain.order.dto.OrderRequest.*;

@Tag(name = "주문")
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderAppService orderAppService;

    @Operation(summary = "고객이 주문을 요청")
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //TODO : Member 클래스로 변환
            orderAppService.order(createOrderRequest,authentication);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "주문의 상세 정보를 조회")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> findOrder(@PathVariable Long orderId) {
        OrderResponse orderRes = orderAppService.findOrderById(orderId);
        return new ResponseEntity<>(orderRes,HttpStatus.OK);
    }

    @Operation(summary = "점주가 주문을 승낙함")
    @PostMapping("/order/{orderId}")
    public ResponseEntity<?> acceptOrder(@PathVariable Long orderId) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            orderAppService.accept(orderId,authentication);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "점주가 주문의 상태를 배달로 변경")
    @PutMapping("/order/{orderId}/delivery")
    public ResponseEntity<?> deliverOrder(@PathVariable Long orderId) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            orderAppService.deliver(orderId,authentication);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "점주가 주문을 거절함")
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            orderAppService.cancel(orderId,authentication);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "점주가 주문의 상태를 완료로 변경")
    @PutMapping("/order/{orderId}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable Long orderId) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            orderAppService.complete(orderId,authentication);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @Operation(summary = "고객이 본인의 주문 내역을 조회")
    @GetMapping("/order")
    public ResponseEntity<OrderListResponse> findOrderList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OrderListResponse myOrderList = orderAppService.findMyOrderList(authentication);
        return new ResponseEntity<>(myOrderList,HttpStatus.OK);
    }

    @Operation(summary = "점주가 가게에 들어온 주문 목록을 확인")
    @GetMapping("/order/shop/{shopId}")
    public ResponseEntity<OrderListResponse> findAllOrderInShop(@PathVariable Long shopId) {

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            OrderListResponse response = orderAppService.findOrderListByShopId(authentication, shopId);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}