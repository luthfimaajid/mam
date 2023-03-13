package com.etspplbo50.deliveryservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeliveredEvent {
    private String orderId;
    private String employeeId;
    private Date startTime;
    private Date finishTime;
}
