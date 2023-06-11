//package com.momstouch.momstouchbe.global.util;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class SocketHandler extends TextWebSocketHandler {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private Map<String, WebSocketSession> memberSessionMap = new HashMap<>();
//
//    @Override
//    public void afterConnectionEstablished(final WebSocketSession session) {
//        System.out.println("session.getId() = " + session.getId());
//        sendMessage(session, new WebSocketMessage("Server", MSG_TYPE_SESSION_CONNECTED, Boolean.toString(sessionIdMap.containsKey(session.getId())), null, null));
//    }
//
//
//    @Override
//    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
//        log.info("[ws] Session has been closed with status {}", status);
//        memberSessionMap.remove(session.getId());
//    }
//
//    private void sendMessage(WebSocketSession session, WebSocketMessage message) {
//        try {
//            String json = objectMapper.writeValueAsString(message);
//            session.sendMessage(new TextMessage(json));
//        } catch (IOException e) {
//            log.info("An error occured: {}", e.getMessage());
//        }
//    }
//}
