package com.example.rate_limitter.rateLimitter;
import com.example.rate_limitter.model.MemoryModel;


public class TokenBucketRateLimitter extends RateLimit {

    private final MemoryModel memoryModel;
    private final int capacity;           // max tokens
    private final double refillRatePerMs; // tokens added per millisecond

    public TokenBucketRateLimitter(MemoryModel memoryModel, int capacity, double refillRatePerSecond) {
        this.memoryModel = memoryModel;
        this.capacity = capacity;
        this.refillRatePerMs = refillRatePerSecond / 1000.0; // convert per second to per millisecond
    }

    @Override
    public boolean isAllowed(String apiKey) {
        long now = System.currentTimeMillis();
        memoryModel.getBuckets().putIfAbsent(apiKey, new Bucket(capacity, now));
        Bucket bucket = memoryModel.getBuckets().get(apiKey);

        synchronized (bucket) {
            // Refill tokens
            double tokensToAdd = (now - bucket.lastRefillTime) * refillRatePerMs;
            bucket.tokens = Math.min(capacity, bucket.tokens + tokensToAdd);
            bucket.lastRefillTime = now;

            if (bucket.tokens >= 1) {
                bucket.tokens -= 1;

                // Optional: track token count in MemoryModel for monitoring
                memoryModel.getRequestCountsFromApiKey().put(apiKey, (int) bucket.tokens);

                return true;
            } else {
                memoryModel.getRequestCountsFromApiKey().put(apiKey, (int) bucket.tokens);
                return false;
            }
        }
    }

    // Inner class to store bucket state
    public static class Bucket {
        double tokens;
        long lastRefillTime;

        Bucket(double tokens, long lastRefillTime) {
            this.tokens = tokens;
            this.lastRefillTime = lastRefillTime;
        }
    }
}
