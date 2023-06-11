package com.momstouch.momstouchbe.domain.discountpolicy.api;

import com.momstouch.momstouchbe.domain.discountpolicy.annotation.DiscountPolicyType;
import com.momstouch.momstouchbe.domain.discountpolicy.application.DiscountAppService;
import com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountRequest.*;
import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse.*;

@Tag(name = "할인 정책", description = "할인 정책 API")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class DiscountPolicyApiController {

    private final DiscountAppService discountAppService;

    @Operation(summary = "모든 할인 정책 조회")
    @GetMapping("/discountPolicy")
    public ResponseEntity<DiscountListResponse> searchAllDiscountPolicy() {
        DiscountListResponse discountListResponse = discountAppService.searchAll();
        return new ResponseEntity<>(discountListResponse, HttpStatus.OK);
    }

    @Operation(summary = "할인 정책 추가")
    @PostMapping("/discountPolicy")
    public ResponseEntity<Long> saveDiscountPolicy(@RequestBody CreateDiscountPolicyRequest request,
                                                @DiscountPolicyType @RequestParam String type) {
        Long discountPolicyId = discountAppService.saveDiscountPolicy(request, type);
        return new ResponseEntity<>(discountPolicyId,HttpStatus.OK);
    }

    @Operation(summary = "할인 정책 삭제")
    @DeleteMapping("/discountPolicy/{id}")
    public ResponseEntity<Long> removeDiscountPolicy(@PathVariable Long id) {
        discountAppService.removeDiscountPolicy(id);
        return new ResponseEntity<>(id,HttpStatus.OK);
    }

    @Operation(summary = "할인 정책 적용")
    @PostMapping("/discountPolicy/{discountPolicyId}")
    public ResponseEntity updateDiscountPolicyOfMenu(@RequestBody UpdateDiscountPolicyRequest request,@PathVariable Long discountPolicyId) {
        discountAppService.updateDiscountPolicyOfMenu(discountPolicyId,request.getMenuId());
        return ResponseEntity.ok().build();
    }
}
