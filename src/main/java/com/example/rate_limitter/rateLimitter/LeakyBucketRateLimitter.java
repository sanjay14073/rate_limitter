package com.example.rate_limitter.rateLimitter;

import com.example.rate_limitter.model.MemoryModel;


public class LeakyBucketRateLimitter extends RateLimit {

    private final MemoryModel memoryModel;
    private final int capacity;          // max bucket size
    private final double leakRatePerMs;  // leak rate per millisecond


    public LeakyBucketRateLimitter(MemoryModel memoryModel, int capacity, double leakRatePerSecond) {
        this.memoryModel = memoryModel;
        this.capacity = capacity;
        this.leakRatePerMs = leakRatePerSecond / 1000.0; // convert per second to per ms
    }

    @Override
    public boolean isAllowed(String apiKey) {
        long now = System.currentTimeMillis();
        memoryModel.getLeakyBuckets().putIfAbsent(apiKey, new LeakyBucket(0, now));
        LeakyBucket bucket = memoryModel.getLeakyBuckets().get(apiKey);

        synchronized (bucket) {
            // Leak requests over time
            double leaked = (now - bucket.lastUpdated) * leakRatePerMs;
            bucket.water = Math.max(0, bucket.water - leaked);
            bucket.lastUpdated = now;

            if (bucket.water < capacity) {
                // Accept request
                bucket.water += 1;

                // Optional: track current bucket fill in memory model
                memoryModel.getRequestCountsFromApiKey().put(apiKey, (int) bucket.water);
                return true;
            } else {
                // Bucket full → reject
                memoryModel.getRequestCountsFromApiKey().put(apiKey, (int) bucket.water);
                return false;
            }
        }
    }

    // Inner class to store bucket state
    public static class LeakyBucket {
        double water;          // current "fill" level of the bucket
        long lastUpdated;      // last time bucket was updated

        LeakyBucket(double water, long lastUpdated) {
            this.water = water;
            this.lastUpdated = lastUpdated;
        }
    }
}
