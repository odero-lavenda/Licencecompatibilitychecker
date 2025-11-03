package com.stagethree.controller;

import com.stagethree.utility.HttpUtils;
import org.json.JSONObject;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String healthCheck() {
        JSONObject health = new JSONObject();
        health.put("status", "healthy");
        health.put("agent", "License Compatibility Checker");
        health.put("version", "1.0.0");
        health.put("timestamp", LocalDateTime.now().toString());
        return health.toString(2);
    }
}
