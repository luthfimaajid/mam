package com.etspplbo50.orderservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsRequest {
    private  String menuId;
    private Integer quantity;
}
