package com.etspplbo50.menuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MenuRequest {
    private String name;
    private String description;
    private String category;
    private Integer price;
}
