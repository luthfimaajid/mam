package com.etspplbo50.deliveryservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
