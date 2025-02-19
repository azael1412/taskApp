package com.azael.taskapp.services;

import org.springframework.stereotype.Service;

@Service
public interface RateLimitService {
    boolean isAllowed(String ip);
    void reset();
}