package com.example.rate_limitter.model;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import com.example.rate_limitter.rateLimitter.FixedWindowRateLimitter.Window;
import com.example.rate_limitter.rateLimitter.LeakyBucketRateLimitter.LeakyBucket;
import com.example.rate_limitter.rateLimitter.TokenBucketRateLimitter.Bucket;

import lombok.Getter;


public class MemoryModel {
    // Map to store request counts per API key
    @Getter
    private ConcurrentHashMap<String, Integer> requestCountsFromApiKey = new ConcurrentHashMap<>();
    // Map to store request timestamps per API key
    @Getter
    private final ConcurrentHashMap<String, LinkedList<Long>> requestTimestamps = new ConcurrentHashMap<>();
    // Map to store bucket info per API key
    @Getter
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    // Map to store window info per API key
    @Getter
    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();
    // Map to store leaky bucket info per API key
    @Getter
    private final ConcurrentHashMap<String, LeakyBucket> leakyBuckets = new ConcurrentHashMap<>();
}
