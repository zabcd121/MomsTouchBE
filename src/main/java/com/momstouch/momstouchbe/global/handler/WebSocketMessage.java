package com.momstouch.momstouchbe.global.handler;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode
public class WebSocketMessage {
    private String from;
    private Object data;

    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "data=" + data +
                '}';
    }
}