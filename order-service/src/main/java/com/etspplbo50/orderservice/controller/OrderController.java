package com.etspplbo50.orderservice.controller;

import com.etspplbo50.orderservice.dto.OrderResponse;
import com.etspplbo50.orderservice.event.OrderDeliveredEvent;
import com.etspplbo50.orderservice.event.OrderReadyEvent;
import com.etspplbo50.orderservice.event.OrderShippedEvent;
import com.etspplbo50.orderservice.event.PaymentSettledEvent;
import com.etspplbo50.orderservice.service.OrderService;
import com.etspplbo50.orderservice.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

    @KafkaListener(
            topics = "paymentSettledTopic",
            groupId = "orderId",
            containerFactory = "paymentSettledEventListenerFactory"
    )
    public void paymentSettledTopicHandler(PaymentSettledEvent paymentSettledEvent) {
        orderService.paymentSettledTopicHandler(paymentSettledEvent.getOrderId());
    }

    @KafkaListener(
            topics = "orderReadyTopic",
            groupId = "orderId",
            containerFactory = "orderReadyEventListenerFactory"
    )
    public void orderReadyToDeliverTopicHandler(OrderReadyEvent orderReadyEvent) {
        log.info(orderReadyEvent.toString());
        orderService.orderReadyToDeliverTopicHandler(orderReadyEvent.getOrderId());
    }

    @KafkaListener(
            topics = "orderShippedTopic",
            groupId = "orderId",
            containerFactory = "orderShippedEventListenerFactory"
    )
    public void orderShippedTopicHandler(OrderShippedEvent orderShippedEvent) {
        orderService.orderShippedTopicHandler(orderShippedEvent.getOrderId());
    }

    @KafkaListener(
            topics = "orderDeliveredTopic",
            groupId = "orderId",
            containerFactory = "orderDeliveredEventListenerFactory"
    )
    public void orderDeliveredTopicHandler(OrderDeliveredEvent orderDeliveredEvent) {
        orderService.orderDeliveredTopicHandler(orderDeliveredEvent.getOrderId());
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
