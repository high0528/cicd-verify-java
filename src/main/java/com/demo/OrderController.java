package com.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping("/test")
    public String testEndpoint() {
        NonExistentService service = new NonExistentService();
        return service.process();
    }
}
