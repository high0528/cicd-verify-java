package com.demo;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private long idCounter = 1;

    @GetMapping
    public List<Order> list() {
        return new ArrayList<>(orders.values());
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        Order order = orders.get(id);
        if (order == null) throw new OrderNotFoundException(id);
        return order;
    }

    @PostMapping
    public Order create(@RequestBody CreateOrderRequest req) {
        Order order = new Order(idCounter++, req.getItemName(), req.getQuantity(), "PENDING");
        orders.put(order.getId(), order);
        return order;
    }

    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest req) {
        Order order = orders.get(id);
        if (order == null) throw new OrderNotFoundException(id);
        order.setStatus(req.getStatus());
        return order;
    }

    public static class Order {
        private Long id;
        private String itemName;
        private int quantity;
        private String status;

        public Order() {}
        public Order(Long id, String itemName, int quantity, String status) {
            this.id = id; this.itemName = itemName; this.quantity = quantity; this.status = status;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class CreateOrderRequest {
        private String itemName;
        private int quantity;
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public static class UpdateStatusRequest {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}

class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) { super("Order not found: " + id); }
}
