package com.example.rate_limitter.services;

import org.springframework.stereotype.Service;

@Service
public class BaseService {
    public String getServiceStatus() {
        return "Service is running";
    }
}
