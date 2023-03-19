package com.etspplbo50.deliveryservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDeliveredEvent {
    private String orderId;
    private String employeeId;
    private Integer distance;
    private Instant startTime;
    private Instant endTime;
}
