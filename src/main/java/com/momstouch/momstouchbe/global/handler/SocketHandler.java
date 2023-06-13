package com.momstouch.momstouchbe.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.momstouch.momstouchbe.global.handler.MessageType.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<WebSocketSession> ownerSessionIdList = new ArrayList<>();
    private List<WebSocketSession> customerSessionIdList = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        System.out.println("session.getId() = " + session.getId());
        sendMessage(session, new WebSocketMessage("server", "socket connect"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
        switch(message.getFrom()) {
            case "join-owner":
                ownerSessionIdList.add(session);
                break;
            case "join-customer":
                customerSessionIdList.add(session);
                break;
            case "owner":
                sendMessage(customerSessionIdList.get(0), message);
                break;
            case "customer":
                sendMessage(ownerSessionIdList.get(0), message);
                break;
            default:
                break;
        }
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        log.info("[ws] Session has been closed with status {}", status);

    }

    public void sendMessage(WebSocketSession session, WebSocketMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.info("An error occured: {}", e.getMessage());
        }
    }
}
