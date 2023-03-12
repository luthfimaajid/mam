package com.etspplbo50.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderRequest {
    private String cafeId;
    private String customerId;
    private String note;
    private List<OrderLineItemsRequest> orderLineItemsList;
}
