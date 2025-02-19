package com.azael.taskapp.filter;

import org.springframework.stereotype.Component;

import com.azael.taskapp.exceptions.RateLimitException;
import com.azael.taskapp.services.RateLimitService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();

        if (!rateLimitService.isAllowed(clientIp)) {
            throw new RateLimitException("Too many requests. Please try again later.");
        }

        filterChain.doFilter(request, response);
    }
}