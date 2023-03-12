package com.etspplbo50.customerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerResponse {
    private String id;
    private String name;
    private String phoneNumber;
    private String address;
    private Double latitude;
    private Double longitude;
}