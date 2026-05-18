package com.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping("/test")
    public String testEndpoint() {
        // [Push Simulation] Triggering build pipeline
        String message = "Simulation Build Success";
        return message;
    }
}
