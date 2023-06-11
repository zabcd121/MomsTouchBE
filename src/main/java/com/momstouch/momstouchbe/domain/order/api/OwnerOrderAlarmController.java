package com.momstouch.momstouchbe.domain.order.api;

import com.momstouch.momstouchbe.domain.order.application.OrderAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerOrderAlarmController {

    private final OrderAlarmService ownerOrderAlarmService;

    // Last-Event-ID: sse 연결이 끊어졌을 때 마지막으로 받은 이벤트의 id를 저장하면 다시 연결했을 때 중복된 이벤트를 받지 않고 이어서 받을 수 있다.
    @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long id,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return ownerOrderAlarmService.subscribe(id, lastEventId);
    }
}
