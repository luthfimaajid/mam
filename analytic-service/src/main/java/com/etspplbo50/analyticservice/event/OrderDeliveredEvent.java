package com.etspplbo50.analyticservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDeliveredEvent {
    private String orderId;
    private String employeeId;
    private Instant startTime;
    private Instant endTime;
    private Integer distance;
}
