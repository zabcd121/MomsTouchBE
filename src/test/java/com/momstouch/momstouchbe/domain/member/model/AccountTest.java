package com.momstouch.momstouchbe.domain.member.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountTest {


    @Test
    public void builder_테스트() {
        Account acc = Account.builder()
                .loginId("loginId")
                .password(UUID.randomUUID().toString())
                .role("ROLE_USER")
                .name("김현석 바보")
                .build();

        Assertions.assertThatThrownBy(() -> {
            Account.builder()
                    .loginId("loginId")
                    .password(UUID.randomUUID().toString())
                    .role("NOT_ROLE_USER")
                    .name("김현석 바보")
                    .build();
        }).isInstanceOf(IllegalArgumentException.class);

    }

}