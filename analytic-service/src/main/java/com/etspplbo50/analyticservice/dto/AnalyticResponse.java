package com.etspplbo50.analyticservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnalyticResponse {
    private String id;
    private String orderId;
    private Integer distance;
    private Integer deliveryTime;
    private Integer prepareTime;
}
