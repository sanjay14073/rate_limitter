package com.example.rate_limitter.rateLimitter;
import com.example.rate_limitter.enums.RateLimitterType;
import com.example.rate_limitter.model.MemoryModel;

public class RateLimitterFactory {
    public static RateLimit getInstance(RateLimitterType rateLimitterType,MemoryModel memoryModel, int param1, long param2) {
        switch (rateLimitterType) {
            case SLIDING_WINDOW:
                return new SlidingWindowRateLimitter(memoryModel, param1, param2);
            case FIXED_WINDOW:
                return new FixedWindowRateLimitter(memoryModel, param1, param2);
            case TOKEN_BUCKET:
                return new TokenBucketRateLimitter(memoryModel, param1, param2); 
            case LEAKY_BUCKET:
                return new LeakyBucketRateLimitter(memoryModel, param1, param2); 
            default:
                throw new IllegalArgumentException("Invalid Rate Limiter Type");
        }

    }
}
