package com.example.rate_limitter.rateLimitter;

import com.example.rate_limitter.model.MemoryModel;

import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowRateLimitter extends RateLimit {

    private final MemoryModel memoryModel;
    private final int maxRequests;       // max requests allowed per window
    private final long windowSizeMs;     // window size in milliseconds



    public FixedWindowRateLimitter(MemoryModel memoryModel, int maxRequests, long windowSizeMs) {
        this.memoryModel = memoryModel;
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
    }

    @Override
    public boolean isAllowed(String apiKey) {
        long now = System.currentTimeMillis();
        memoryModel.getWindows().putIfAbsent(apiKey, new Window(now, new AtomicInteger(0)));
        Window window = memoryModel.getWindows().get(apiKey);

        synchronized (window) {
            // Reset window if expired
            if (now - window.startTime > windowSizeMs) {
                window.startTime = now;
                window.counter.set(0);
            }

            if (window.counter.incrementAndGet() <= maxRequests) {
                // Optional: track count in MemoryModel
                memoryModel.getRequestCountsFromApiKey().put(apiKey, window.counter.get());
                return true;
            } else {
                memoryModel.getRequestCountsFromApiKey().put(apiKey, window.counter.get());
                return false;
            }
        }
    }

    // Inner class to store window state
    public static class Window {
        long startTime;
        AtomicInteger counter;

        Window(long startTime, AtomicInteger counter) {
            this.startTime = startTime;
            this.counter = counter;
        }
    }
}

