package com.momstouch.momstouchbe.domain.discountpolicy.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momstouch.momstouchbe.domain.discountpolicy.application.DiscountAppService;
import com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountRequest;
import com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountResponse.DiscountListResponse;
import com.momstouch.momstouchbe.domain.discountpolicy.model.AmountDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.DiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.RateDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.model.TimeDiscountPolicy;
import com.momstouch.momstouchbe.domain.discountpolicy.service.DiscountPolicyService;
import com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.shop.model.Shop;
import com.momstouch.momstouchbe.setup.ShopSetup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.momstouch.momstouchbe.domain.discountpolicy.dto.DiscountRequest.*;
import static com.momstouch.momstouchbe.domain.discountpolicy.utis.command.DiscountPolicyCreateCommand.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DiscountPolicyApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ShopSetup shopSetup;

    @Autowired
    private DiscountPolicyService discountPolicyService;

    @Test
    public void 할인정책_목록조회_테스트() throws Exception {
        LocalTime now = LocalTime.of(1,1,1);
        Member member = Member.createMember("loginId", UUID.randomUUID().toString(),"김현석","ROLE_USER");
        Shop shop = shopSetup.saveShop(member,"shop","description","address","phoneNumber",LocalTime.now(),LocalTime.now(),10000);

        discountPolicyService.createAmountDiscountPolicy(shop,10000,1000);
        discountPolicyService.createRateDiscountPolicy(shop,10000,9.00);
        discountPolicyService.createTimeDiscountPolicy(shop,now,10000);

        ResultActions actions = mvc.perform(
                get("/api/discountPolicy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        actions.andDo(print())
            .andExpect(jsonPath("$.amountDiscountPolicyList").exists())
                .andExpect(jsonPath("$.amountDiscountPolicyList.size()").value(1))
                .andExpect(jsonPath("$.amountDiscountPolicyList[0].baseAmount").value(10000))
                .andExpect(jsonPath("$.amountDiscountPolicyList[0].discountAmount").value(1000))

            .andExpect(jsonPath("$.rateDiscountPolicyList").exists())
                .andExpect(jsonPath("$.rateDiscountPolicyList.size()").value(1))
                .andExpect(jsonPath("$.rateDiscountPolicyList[0].baseAmount").value(10000))
                .andExpect(jsonPath("$.rateDiscountPolicyList[0].discountRate").value(9.00))

            .andExpect(jsonPath("$.timeDiscountPolicyList").exists())
                .andExpect(jsonPath("$.timeDiscountPolicyList.size()").value(1))
                .andExpect(jsonPath("$.timeDiscountPolicyList[0].baseTime").isNotEmpty())
                .andExpect(jsonPath("$.timeDiscountPolicyList[0].discountAmount").value(10000))
            .andExpect(status().isOk());
    }

    @Test
    public void 정량_할인정책_등록_테스트() throws Exception {

        CreateDiscountPolicyRequest createDiscountPolicyRequest = new CreateDiscountPolicyRequest();
        createDiscountPolicyRequest.setBaseAmount(100000);
        createDiscountPolicyRequest.setDiscountAmount(10000);

        List<DiscountPolicy> beforeCreate = discountPolicyService.findAll();
        Assertions.assertThat(beforeCreate.size()).isEqualTo(0);

        ResultActions actions = mvc.perform(
                post("/api/discountPolicy")
                        .queryParam("type","AMOUNT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDiscountPolicyRequest))
                        .with(csrf())
        );

        actions.andDo(print())
            .andExpect(status().isOk());

        List<DiscountPolicy> all = discountPolicyService.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);
        DiscountPolicy discountPolicy = all.get(0);
        Assertions.assertThat(discountPolicy instanceof AmountDiscountPolicy).isTrue();
    }

    @Test
    public void 정률_할인정책_등록_테스트() throws Exception {

        CreateDiscountPolicyRequest createDiscountPolicyRequest = new CreateDiscountPolicyRequest();
        createDiscountPolicyRequest.setBaseAmount(100000);
        createDiscountPolicyRequest.setDiscountRate(10.00);

        List<DiscountPolicy> beforeCreate = discountPolicyService.findAll();
        Assertions.assertThat(beforeCreate.size()).isEqualTo(0);

        ResultActions actions = mvc.perform(
                post("/api/discountPolicy")
                        .queryParam("type","RATE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDiscountPolicyRequest))
                        .with(csrf())
        );

        actions.andDo(print())
                .andExpect(status().isOk());

        List<DiscountPolicy> all = discountPolicyService.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);
        DiscountPolicy discountPolicy = all.get(0);
        Assertions.assertThat(discountPolicy instanceof RateDiscountPolicy).isTrue();
    }

    @Test
    public void 조조_할인정책_등록_테스트() throws Exception {

        CreateDiscountPolicyRequest createDiscountPolicyRequest = new CreateDiscountPolicyRequest();
        createDiscountPolicyRequest.setBaseTime(LocalTime.now());
        createDiscountPolicyRequest.setDiscountAmount(10000);

        List<DiscountPolicy> beforeCreate = discountPolicyService.findAll();
        Assertions.assertThat(beforeCreate.size()).isEqualTo(0);

        ResultActions actions = mvc.perform(
                post("/api/discountPolicy")
                        .queryParam("type","TIME")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDiscountPolicyRequest))
                        .with(csrf())
        );

        actions.andDo(print())
                .andExpect(status().isOk());

        List<DiscountPolicy> all = discountPolicyService.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);
        DiscountPolicy discountPolicy = all.get(0);
        Assertions.assertThat(discountPolicy instanceof TimeDiscountPolicy).isTrue();
    }

    @Test
    public void 할인정책_삭제_테스트() throws Exception {
        Member member = Member.createMember("loginId", UUID.randomUUID().toString(),"김현석","ROLE_USER");
        Shop shop = shopSetup.saveShop(member,"shop","description","address","phoneNumber",LocalTime.now(),LocalTime.now(),10000);
        Long amountDiscountPolicyId = discountPolicyService.createAmountDiscountPolicy(shop,10000, 1000);
        Optional<DiscountPolicy> beforeDelete = discountPolicyService.findById(amountDiscountPolicyId);
        Assertions.assertThat(beforeDelete.isPresent()).isTrue();

        ResultActions actions = mvc.perform(
                delete("/api/discountPolicy/{id}",amountDiscountPolicyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        actions.andDo(print())
                .andExpect(status().isOk());

        Optional<DiscountPolicy> afterDelete = discountPolicyService.findById(amountDiscountPolicyId);
        Assertions.assertThat(afterDelete.isEmpty()).isTrue();
    }
}