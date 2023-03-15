package com.etspplbo50.deliveryservice.controller;

import com.etspplbo50.deliveryservice.dto.DeliveryRequest;
import com.etspplbo50.deliveryservice.dto.DeliveryResponse;
import com.etspplbo50.deliveryservice.event.OrderReadyEvent;
import com.etspplbo50.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {
    private final DeliveryService deliveryService;

    @KafkaListener(
            topics = "orderReadyTopic",
            groupId = "deliveryId",
            containerFactory = "orderReadyEventListenerFactory"
    )
    public void orderReadyTopicHandler(OrderReadyEvent orderReadyEvent) {
        log.info(orderReadyEvent.toString());
        deliveryService.orderReadyTopicHandler(orderReadyEvent.getOrderId());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryResponse getDelivery(@PathVariable("id") String id) {
        return deliveryService.getDelivery(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<DeliveryResponse> getAllDelivery() {
        return deliveryService.getAllDelivery();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryResponse updateDelivery(@PathVariable("id") String id, @RequestBody DeliveryRequest deliveryRequest) {
        return deliveryService.updateDelivery(id, deliveryRequest);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryResponse updateDeliveryStatus(@PathVariable("id") String id) {
        return deliveryService.updateDeliveryStatus(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteDelivery(@PathVariable("id") String id) {
        return deliveryService.deleteDelivery(id);
    }
}
