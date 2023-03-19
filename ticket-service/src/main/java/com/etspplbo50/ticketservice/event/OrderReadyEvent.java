package com.etspplbo50.ticketservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReadyEvent {
    private String orderId;
    private Instant startTime;
    private Instant endTime;
}
