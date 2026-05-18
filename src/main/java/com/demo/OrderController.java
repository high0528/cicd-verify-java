package com.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    // 故意制造编译错误：缺少分号、错误类型
    @GetMapping("/test")
    public String testEndpoint() {
        String message = "Hello from broken build"
        int broken = "this is not an int";
        return message;
    }
}
