package com.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping("/test")
    public String testEndpoint() {
        String message = "Hello from fixed build";
        return message
    }
}
