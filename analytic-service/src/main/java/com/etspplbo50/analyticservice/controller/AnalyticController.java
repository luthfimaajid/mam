package com.etspplbo50.analyticservice.controller;

import com.etspplbo50.analyticservice.dto.AnalyticResponse;
import com.etspplbo50.analyticservice.event.OrderDeliveredEvent;
import com.etspplbo50.analyticservice.event.OrderReadyEvent;
import com.etspplbo50.analyticservice.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytic")
@RequiredArgsConstructor
@Slf4j
public class AnalyticController {
    private final AnalyticService analyticService;

    @KafkaListener(
            topics = "orderReadyTopic",
            groupId = "analyticId",
            containerFactory = "orderReadyEventListenerFactory"
    )
    public void orderReadyToDeliverTopicHandler(OrderReadyEvent orderReadyEvent) {
        analyticService.orderReadyToDeliverTopicHandler(orderReadyEvent);
    }


    @KafkaListener(
            topics = "orderDeliveredTopic",
            groupId = "analyticId",
            containerFactory = "orderDeliveredEventListenerFactory"
    )
    public void analyticDeliveredTopicHandler(OrderDeliveredEvent orderDeliveredEvent) {
        analyticService.orderDeliveredTopicHandler(orderDeliveredEvent);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnalyticResponse getAnalytic(@PathVariable("id") String id) {
        return analyticService.getAnalytic(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<AnalyticResponse> getAllAnalytic() {
        return analyticService.getAllAnalytic();
    }
}
