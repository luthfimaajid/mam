package com.etspplbo50.orderservice.controller;

import com.etspplbo50.orderservice.dto.OrderResponse;
import com.etspplbo50.orderservice.event.OrderReadyToDeliverEvent;
import com.etspplbo50.orderservice.event.PaymentSettledEvent;
import com.etspplbo50.orderservice.service.OrderService;
import com.etspplbo50.orderservice.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

    @KafkaListener(
            topics = "paymentSettledTopic",
            groupId = "orderId"
    )
    public void paymentSettledTopicHandler(PaymentSettledEvent paymentSettledEvent) {
        JSONObject obj = new JSONObject(paymentSettledEvent.getOrderId());
        String orderId = obj.getString("orderId");
        orderService.paymentSettledTopicHandler(orderId);
    }

    @KafkaListener(
            topics = "orderReadyToDeliverTopic",
            groupId = "orderId"
    )
    public void orderReadyToDeliverTopicHandler(OrderReadyToDeliverEvent orderReadyToDeliverEvent) {
        JSONObject obj = new JSONObject(orderReadyToDeliverEvent.getOrderId());
        String orderId = obj.getString("orderId");
        orderService.orderReadyToDeliverTopicHandler(orderId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrder(@PathVariable("id") String id) {
        return orderService.getOrder(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrder() {
        return orderService.getAllOrder();
    }
}
