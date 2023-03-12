package com.etspplbo50.menuservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MenuResponse {
    private String id;
    private String name;
    private String description;
    private String category;
    private Integer price;
}