package com.momstouch.momstouchbe;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.persistence.EntityManager;

@SpringBootApplication
@EnableJpaAuditing
@EnableWebSocket
public class MomsTouchBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MomsTouchBeApplication.class, args);
    }

    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

}
