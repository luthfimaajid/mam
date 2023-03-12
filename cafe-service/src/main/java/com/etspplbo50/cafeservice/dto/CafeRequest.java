package com.etspplbo50.cafeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CafeRequest {
    private String name;
    private String address;
    private String description;
    private double latitude;
    private double longitude;
}
