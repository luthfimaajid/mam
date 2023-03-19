package com.etspplbo50.analyticservice.service;


import com.etspplbo50.analyticservice.dto.AnalyticResponse;
import com.etspplbo50.analyticservice.event.OrderDeliveredEvent;
import com.etspplbo50.analyticservice.event.OrderReadyEvent;
import com.etspplbo50.analyticservice.model.Analytic;
import com.etspplbo50.analyticservice.repository.AnalyticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AnalyticService {
    private final AnalyticRepository analyticRepository;

    public List<AnalyticResponse> getAllAnalytic() {
        List<Analytic> analyticList = analyticRepository.findAll();

        return analyticList.stream().map(analytic -> mapAnalyticToAnalyticResponse(analytic)).toList();
    }

    public AnalyticResponse getAnalytic(String id) {
        Optional<Analytic> analytic = analyticRepository.findById(id);

        if (analytic.isPresent()) {
            return mapAnalyticToAnalyticResponse(analytic.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Analytic data not found");
        }
    }

    public void orderReadyToDeliverTopicHandler(OrderReadyEvent orderReadyEvent) {
        Integer prepareTime = (int) Duration.between(orderReadyEvent.getStartTime(), orderReadyEvent.getEndTime()).toMinutes();

        Analytic analytic = Analytic.builder()
                .orderId(orderReadyEvent.getOrderId())
                .prepareTime(prepareTime)
                .build();

        analyticRepository.save(analytic);

        log.info("Added analytic data for order {}", orderReadyEvent.getOrderId());
    }

    public void orderDeliveredTopicHandler(OrderDeliveredEvent orderDeliveredEvent) {
        Optional<Analytic> analytic = analyticRepository.findByOrderId(orderDeliveredEvent.getOrderId());

        if (analytic.isPresent()) {
            Analytic updatedAnalytic = analytic.get();
            Integer deliveryTime = (int) Duration.between(orderDeliveredEvent.getStartTime(), orderDeliveredEvent.getEndTime()).toMinutes();

            updatedAnalytic.setDeliveryTime(deliveryTime);
            updatedAnalytic.setDistance(orderDeliveredEvent.getDistance());

            log.info("Updated analytic data for order {}", updatedAnalytic.getOrderId());

            analyticRepository.save(updatedAnalytic);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Analytic data not found");
        }
    }

    private AnalyticResponse mapAnalyticToAnalyticResponse(Analytic analytic) {
        return AnalyticResponse.builder()
                .id(analytic.getId())
                .orderId(analytic.getOrderId())
                .prepareTime(analytic.getPrepareTime())
                .deliveryTime(analytic.getDeliveryTime())
                .distance(analytic.getDistance())
                .build();
    }
}
