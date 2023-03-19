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
public class OrderReadyEvent {
    private String orderId;
    private Instant startTime;
    private Instant endTime;
}
