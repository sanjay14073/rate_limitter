package com.example.rate_limitter.rateLimitter;

public abstract class RateLimit {
    public abstract boolean isAllowed(String apiKey);
}
