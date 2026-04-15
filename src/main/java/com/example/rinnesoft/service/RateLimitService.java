package com.example.rinnesoft.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private final Map<String, RequestInfo> requestCounts =
        new ConcurrentHashMap<>();
    private final int MAX_REQUESTS = 5;

    public boolean isAllowed(String ip) {
        LocalDateTime now = LocalDateTime.now();

        requestCounts
            .entrySet()
            .removeIf(entry ->
                entry.getValue().firstRequestTime.isBefore(now.minusHours(1))
            );

        RequestInfo info = requestCounts.computeIfAbsent(ip, k ->
            new RequestInfo(new AtomicInteger(0), now)
        );

        if (info.count.get() >= MAX_REQUESTS) {
            return false;
        }

        info.count.incrementAndGet();
        return true;
    }

    private static class RequestInfo {

        AtomicInteger count;
        LocalDateTime firstRequestTime;

        RequestInfo(AtomicInteger count, LocalDateTime time) {
            this.count = count;
            this.firstRequestTime = time;
        }
    }
}
