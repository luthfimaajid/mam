package com.etspplbo50.inventoryservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InventoryResponse {
    private String id;
    private String cafeId;
    private String menuId;
    private Boolean isAvailable;
}