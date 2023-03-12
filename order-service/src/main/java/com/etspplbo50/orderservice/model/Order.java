package com.etspplbo50.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "order")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order {
    @Id
    private String id;
    private String cafeId;
    private String customerId;
    private String status;
    private Integer totalPrice;
    private String note;
    private List<OrderLineItems> orderLineItemsList;
}
