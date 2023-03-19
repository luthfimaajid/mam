package com.etspplbo50.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderShippedEvent {
    private String orderId;
    private String employeeId;
    private Integer distance;
}
