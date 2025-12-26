package com.example.rate_limitter.rateLimitter;

import com.example.rate_limitter.model.MemoryModel;

import java.util.*;

public class SlidingWindowRateLimitter extends RateLimit {

    private final MemoryModel memoryModel;
    private final int maxRequests;       // max requests allowed
    private final long windowSizeMs;     // window size in milliseconds


    public SlidingWindowRateLimitter(MemoryModel memoryModel, int maxRequests, long windowSizeMs) {
        this.memoryModel = memoryModel;
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
    }

    @Override
    public boolean isAllowed(String apiKey) {
        long now = System.currentTimeMillis();

        // Initialize the timestamps list if absent
        memoryModel.getRequestTimestamps().putIfAbsent(apiKey, new LinkedList<>());
        Deque<Long> timestamps = memoryModel.getRequestTimestamps().get(apiKey);

        synchronized (timestamps) {
            // Remove timestamps older than the sliding window
            while (!timestamps.isEmpty() && (now - timestamps.peekFirst()) > windowSizeMs) {
                timestamps.pollFirst();
            }

            // Check if under the limit
            if (timestamps.size() < maxRequests) {
                timestamps.addLast(now);

                // Optional: track count in memory model
                memoryModel.getRequestCountsFromApiKey()
                           .put(apiKey, timestamps.size());

                return true;
            } else {
                // Optional: update count even if request rejected
                memoryModel.getRequestCountsFromApiKey()
                           .put(apiKey, timestamps.size());

                return false;
            }
        }
    }
}
