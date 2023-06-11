package com.momstouch.momstouchbe.domain.order.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {
    private static final Map<String, SseEmitter> CLIENTS = new ConcurrentHashMap<>();

    public SseEmitter save(String id, SseEmitter emitter) {
        CLIENTS.put(id, emitter);
        return emitter;
    }

    public void deleteById(String id) {
        CLIENTS.remove(id);
    }

    public Map<String, Object> findAllEventCacheStartWithId(String id) {
        Map<String, Object> events = new ConcurrentHashMap<>();
        CLIENTS.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .forEach(entry -> events.put(entry.getKey(), entry.getValue()));
        return events;
    }

    public Map<String, SseEmitter> findAllStartWithById(String id) {
        Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
        CLIENTS.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(id))
                .forEach(entry -> emitters.put(entry.getKey(), entry.getValue()));
        return emitters;
    }
}
