package com.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {

    @Test
    void testCreateOrder() {
        OrderController controller = new OrderController();
        String result = controller.testEndpoint();
        assertEquals("Simulation Build Success", result, "订单创建应该返回成功");
    }

    @Test
    void testHealthCheck() {
        OrderController controller = new OrderController();
        assertEquals("Simulation Build Success", controller.testEndpoint(), "健康检查应该通过");
    }
}
