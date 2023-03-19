package com.etspplbo50.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeliveryResponse {
    private String id;
    private String orderId;
    private String employeeId;
    private String status;
    private Integer distance;
    private Instant startTime;
    private Instant endTime;
}