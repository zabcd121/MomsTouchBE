package com.momstouch.momstouchbe.domain.order.api;

import com.momstouch.momstouchbe.domain.member.model.Member;
import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
import com.momstouch.momstouchbe.domain.order.application.OrderAlarmService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
public class OrderAlarmController {

    private final OrderAlarmService ownerOrderAlarmService;
    private final MemberRepository memberRepository;

    // Last-Event-ID: sse 연결이 끊어졌을 때 마지막으로 받은 이벤트의 id를 저장하면 다시 연결했을 때 중복된 이벤트를 받지 않고 이어서 받을 수 있다.
    @Operation(summary = "알림 구독")
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) throw new AccessDeniedException("로그인이 필요합니다.");
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Member member = memberRepository.findBySub(principal.getUsername()).orElseThrow();
        return ownerOrderAlarmService.subscribe(member.getId(), lastEventId);
    }
}
