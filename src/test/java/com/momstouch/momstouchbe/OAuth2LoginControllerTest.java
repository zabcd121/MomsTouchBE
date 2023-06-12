package com.momstouch.momstouchbe;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import com.momstouch.momstouchbe.setup.MemberSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class OAuth2LoginControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired
    MemberSetup memberSetup;


    @Test
    @WithMockUser(username = "loginId",roles = "MEMBER")
    void 회원_가입() throws Exception {
        Member member = memberSetup.saveMember("loginId", "인증번호", "이름", "ROLE_MEMBER");
        Authentication authentication = memberSetup.getAuthentication(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResultActions result = mockMvc.perform(
                get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        ).andDo(print());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(member.getEmail()))
                .andExpect(jsonPath("$.name").value(member.getAccount().getName()))
                .andExpect(jsonPath("$.roles[0]").value(member.getRole()));
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void 어드민_테스트() throws Exception {
        mockMvc.perform(get("/admins/1234"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 어드민_접속_테스트() throws Exception {
        mockMvc.perform(get("/admins"))
                .andExpect(status().isOk());
    }







}
