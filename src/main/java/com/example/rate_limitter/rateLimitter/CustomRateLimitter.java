package com.example.rate_limitter.rateLimitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.example.rate_limitter.enums.RateLimitterType;
import com.example.rate_limitter.model.MemoryModel;

@Component
public class CustomRateLimitter  {

    @Autowired
    private MemoryModel memoryModel;

    @Value("${rateLimitter.param1}")
    public int param1;
    @Value("${rateLimitter.param2}")
    public long param2;

    public boolean isAllowed(String apiKey,RateLimitterType rateLimitterType) {
        return RateLimitterFactory.getInstance(rateLimitterType, memoryModel, param1, param2).isAllowed(apiKey);
    }
}
