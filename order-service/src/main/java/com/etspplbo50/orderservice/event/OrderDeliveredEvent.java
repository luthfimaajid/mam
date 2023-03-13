package com.etspplbo50.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDeliveredEvent {
    private String orderId;
    private String employeeId;
    private Date startTime;
    private Date endTime;
}
