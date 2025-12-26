package com.example.rate_limitter.utils;
import com.example.rate_limitter.enums.RateLimitterType;
public class ConvertStringToEnums {

    public static RateLimitterType convertStringToRateLimitterType(String type) {
        try {
            return RateLimitterType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Rate Limitter Type: " + type);
        }
    }
    
}
