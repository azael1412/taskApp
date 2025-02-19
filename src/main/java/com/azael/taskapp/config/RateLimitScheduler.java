package com.azael.taskapp.config;

import com.azael.taskapp.services.RateLimitService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateLimitScheduler {

    private final RateLimitService rateLimitService;

    public RateLimitScheduler(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Scheduled(fixedRate = 60000) // Cada 60 segundos
    public void resetRequestCounts() {
        rateLimitService.reset();
    }
}
