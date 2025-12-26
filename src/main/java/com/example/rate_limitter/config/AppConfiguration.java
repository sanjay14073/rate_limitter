package com.example.rate_limitter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.rate_limitter.model.MemoryModel;
import com.example.rate_limitter.rateLimitter.CustomRateLimitter;

@Configuration
public class AppConfiguration {

    @Bean
    public MemoryModel memoryModel() {
        return new MemoryModel();
    }

    @Bean 
    public CustomRateLimitter rateLimitter() {
        return new CustomRateLimitter();
    }
}
