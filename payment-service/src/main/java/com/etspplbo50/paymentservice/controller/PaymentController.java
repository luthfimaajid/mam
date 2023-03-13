package com.etspplbo50.paymentservice.controller;

import com.etspplbo50.paymentservice.dto.PaymentRequest;
import com.etspplbo50.paymentservice.dto.PaymentResponse;
import com.etspplbo50.paymentservice.event.OrderPlacedEvent;
import com.etspplbo50.paymentservice.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.json.JsonObject;
import org.json.JSONObject;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @KafkaListener(
            topics = "orderPlacedTopic",
            groupId = "paymentId"
    )
    public void createPayment(OrderPlacedEvent orderPlacedEvent) {
        JSONObject obj = new JSONObject(orderPlacedEvent.getOrderId());
        String orderId = obj.getString("orderId");
        paymentService.createPayment(orderId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse getPayment(@PathVariable("id") String id) {
        return paymentService.getPayment(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentResponse> getAllPayment() {
        return paymentService.getAllPayment();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse updatePayment(@PathVariable("id") String id, @RequestBody PaymentRequest paymentRequest) {
        return paymentService.updatePayment(id, paymentRequest);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse updatePaymentStatus(@PathVariable("id") String id, @RequestBody PaymentRequest paymentRequest) {
        return paymentService.updatePaymentStatus(id, paymentRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePayment(@PathVariable("id") String id) {
        return paymentService.deletePayment(id);
    }
}
