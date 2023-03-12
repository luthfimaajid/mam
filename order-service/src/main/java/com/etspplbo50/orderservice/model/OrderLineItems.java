package com.etspplbo50.orderservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "orderLineItems")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderLineItems {
    @Id
    private String id;
    private String menuId;
    private Integer quantity;

}
