package com.momstouch.momstouchbe;

import com.momstouch.momstouchbe.domain.member.Service.TestService;
import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import com.momstouch.momstouchbe.domain.member.Service.CustomOAuth2UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.momstouch.momstouchbe.domain.member.web.OAuth2LoginController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;




import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class OAuth2LoginControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean private MemberRepository memberRepository;

    @MockBean TestService testService;



    @Test
    @WithMockUser(roles = "MEMBER")
    void 회원_가입() throws Exception {
        Member member =  Member.createMember("인증키","인증번호","이름","ROLE_MEMBER","email");
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", 1L));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(member.getEmail()))
                .andExpect(jsonPath("$.name").value(member.getAccount().getName()))
                .andExpect(jsonPath("$.role").value(member.getRole()));
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void 어드민_테스트() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admins/1234"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 어드민_접속_테스트() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admins"))
                .andExpect(status().isOk());
    }







}