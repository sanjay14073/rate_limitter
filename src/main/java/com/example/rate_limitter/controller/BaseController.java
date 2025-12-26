package com.example.rate_limitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rate_limitter.rateLimitter.CustomRateLimitter;
import com.example.rate_limitter.services.BaseService;
import com.example.rate_limitter.utils.ConvertStringToEnums;

@RestController
@RequestMapping("/api/v1")
public class BaseController {

    //Autowire the service
    @Autowired
    private BaseService baseService;

    @Autowired
    private CustomRateLimitter rateLimitter;

    @Value("${rateLimitter.algoType:FIXED_WINDOW}")
    public String algoType;

    
    @GetMapping("/health")
    public String healthCheck() {
        return "Service is up and running!";
    }
        
    @PostMapping("/")
    public String rateLimitter(){
        try{
            if(!rateLimitter.isAllowed("default-api-key",ConvertStringToEnums.convertStringToRateLimitterType(algoType))){
                return "Rate limit exceeded. Try again later.";
            }
            String status = baseService.getServiceStatus();
            return status;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "Request processed successfully.";
    }
}
