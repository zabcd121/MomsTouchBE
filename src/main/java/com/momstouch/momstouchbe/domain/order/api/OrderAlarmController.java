//package com.momstouch.momstouchbe.domain.order.api;
//
//import com.momstouch.momstouchbe.domain.member.model.Member;
//import com.momstouch.momstouchbe.domain.member.repository.MemberRepository;
//import com.momstouch.momstouchbe.global.jwt.JwtTokenProvider;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//@RequiredArgsConstructor
//public class OrderAlarmController {
//
//    //private final OrderAlarmService ownerOrderAlarmService;
//    private final MemberRepository memberRepository;
//
//    private final SimpMessageSendingOperations messagingTemplate;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    // Last-Event-ID: sse 연결이 끊어졌을 때 마지막으로 받은 이벤트의 id를 저장하면 다시 연결했을 때 중복된 이벤트를 받지 않고 이어서 받을 수 있다.
////    @Operation(summary = "알림 구독")
////    @GetMapping(value = "/subscribe", produces = "text/event-stream")
////    public SseEmitter subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        if(authentication == null) throw new AccessDeniedException("로그인이 필요합니다.");
////        UserDetails principal = (UserDetails) authentication.getPrincipal();
////        System.out.println("userDetail 다음");
////        Member member = memberRepository.findBySub(principal.getUsername()).orElseThrow();
////        return ownerOrderAlarmService.subscribe(member.getId(), lastEventId);
////    }
//
//    @Operation(summary = "알림 구독")
//    @MessageMapping("/sub")
//    public void subscribe(HttpServletRequest request) {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        if(authentication == null) throw new AccessDeniedException("로그인이 필요합니다.");
////        UserDetails principal = (UserDetails) authentication.getPrincipal();
//
//        //Member member = memberRepository.findBySub(principal.getUsername()).orElseThrow();
//        String resolvedToken = jwtTokenProvider.resolveToken(request.getHeader("Authorization"));
//        Long memberId = Long.parseLong(jwtTokenProvider.getSubject(resolvedToken));
//        messagingTemplate.convertAndSend("/sub/" + memberId, "알림 소켓 구독 연결 성공");
//    }
//}
