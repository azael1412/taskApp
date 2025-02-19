package com.azael.taskapp.services.impl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azael.taskapp.services.RateLimitService;

@Component
public class RateLimiterServiceImpl implements RateLimitService {
    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    @Value("${api.security.max-requests-per-minute}")
    private int MAX_REQUESTS_PER_MINUTE;

    public boolean isAllowed(String ip) {
        int count = requestCounts.getOrDefault(ip, 0);
        if (count >= MAX_REQUESTS_PER_MINUTE) {
            return false;
        }
        requestCounts.put(ip, count + 1);
        return true;
    }

    public void reset() {
        requestCounts.clear();
    }
}