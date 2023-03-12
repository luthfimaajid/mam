package com.etspplbo50.orderservice.DTO;

import com.etspplbo50.orderservice.model.OrderLineItems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponse {
    private String id;
    private String cafeId;
    private String customerId;
    private String status;
    private Integer totalPrice;
    private String note;
    private List<OrderLineItems> orderLineItemsList;
}
