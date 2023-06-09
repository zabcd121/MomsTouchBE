package com.momstouch.momstouchbe.domain.discountpolicy.api;

import com.momstouch.momstouchbe.domain.discountpolicy.annotation.DiscountPolicyType;
import com.momstouch.momstouchbe.domain.discountpolicy.application.DiscountAppService;
import com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountRequest.*;
import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class DiscountPolicyApiController {

    private final DiscountAppService discountAppService;
    @GetMapping("/discountPolicy")
    public ResponseEntity<DiscountListResponse> searchAllDiscountPolicy() {
        DiscountListResponse discountListResponse = discountAppService.searchAll();
        return new ResponseEntity<>(discountListResponse, HttpStatus.OK);
    }

    @PostMapping("/discountPolicy")
    public ResponseEntity<Long> saveDiscountPolicy(@RequestBody CreateDiscountPolicyRequest request,
                                                @DiscountPolicyType @RequestParam String type) {
        Long discountPolicyId = discountAppService.saveDiscountPolicy(request, type);
        return new ResponseEntity<>(discountPolicyId,HttpStatus.OK);
    }

    @DeleteMapping("/discountPolicy/{id}")
    public ResponseEntity<Long> removeDiscountPolicy(@PathVariable Long id) {
        discountAppService.removeDiscountPolicy(id);
        return new ResponseEntity<>(id,HttpStatus.OK);
    }
}
