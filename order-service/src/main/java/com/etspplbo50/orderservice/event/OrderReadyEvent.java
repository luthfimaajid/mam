package com.etspplbo50.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReadyEvent {
    private String orderId;
    private Date startTime;
    private Date endTime;
}
