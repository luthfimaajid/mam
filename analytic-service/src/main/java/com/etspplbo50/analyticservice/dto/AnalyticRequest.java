package com.etspplbo50.analyticservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnalyticRequest {
    private String orderId;
    private Integer distance;
    private Integer deliveryTime;
    private Integer prepareTime;
}
