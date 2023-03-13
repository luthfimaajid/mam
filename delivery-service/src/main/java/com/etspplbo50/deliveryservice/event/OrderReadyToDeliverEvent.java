package com.etspplbo50.deliveryservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReadyToDeliverEvent {
    private String orderId;
}
