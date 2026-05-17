package com.demo;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {

    private OrderController controller;

    @BeforeEach
    void setUp() {
        controller = new OrderController();
    }

    @Test
    void createOrder() {
        OrderController.CreateOrderRequest req = new OrderController.CreateOrderRequest();
        req.setItemName("iPhone 15");
        req.setQuantity(2);
        var order = controller.create(req);
        assertNotNull(order.getId());
        assertEquals("iPhone 15", order.getItemName());
        assertEquals(2, order.getQuantity());
        assertEquals("PENDING", order.getStatus());
    }

    @Test
    void listOrders() {
        OrderController.CreateOrderRequest req = new OrderController.CreateOrderRequest();
        req.setItemName("MacBook");
        req.setQuantity(1);
        controller.create(req);
        controller.create(req);
        assertEquals(2, controller.list().size());
    }

    @Test
    void updateOrderStatus() {
        OrderController.CreateOrderRequest req = new OrderController.CreateOrderRequest();
        req.setItemName("AirPods");
        req.setQuantity(1);
        var order = controller.create(req);

        OrderController.UpdateStatusRequest statusReq = new OrderController.UpdateStatusRequest();
        statusReq.setStatus("SHIPPED");
        var updated = controller.updateStatus(order.getId(), statusReq);
        assertEquals("SHIPPED", updated.getStatus());
    }

    @Test
    void getOrderNotFound() {
        assertThrows(OrderNotFoundException.class, () -> controller.getById(999L));
    }
}
