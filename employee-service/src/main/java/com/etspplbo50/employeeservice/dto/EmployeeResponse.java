package com.etspplbo50.employeeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeResponse {
    private String id;
    private String cafeId;
    private String name;
    private Boolean isAbleDelivery;
}