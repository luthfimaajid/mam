package com.etspplbo50.ticketservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TicketResponse {
    private String id;
    private String orderId;
    private String status;
    private Instant startTime;
    private Instant endTime;
}