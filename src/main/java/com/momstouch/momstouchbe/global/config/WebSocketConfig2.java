//package com.momstouch.momstouchbe.global.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebSocket
//@RequiredArgsConstructor
//public class WebSocketConfig2 implements WebSocketConfigurer {
//
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        System.out.println("WebSocketConfig.registerWebSocketHandlers");
//
//        registry.addHandler(socketHandler, "/ws")
//                .setAllowedOriginPatterns("*");
//
//        registry.addHandler(socketHandler, "/ws")
//                .setAllowedOriginPatterns("*")
//                .withSockJS(); //Client의 web socket 지원 여부에 따라 Long Polling or Polling으로 통신한다.
//        System.out.println("WebSocketConfig.registerWebSocketHandlers");
//    }
//}
