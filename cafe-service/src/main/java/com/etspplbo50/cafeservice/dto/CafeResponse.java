package com.etspplbo50.cafeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CafeResponse {
    private String id;
    private String name;
    private String description;
    private String address;
    private double latitude;
    private double longitude;
}