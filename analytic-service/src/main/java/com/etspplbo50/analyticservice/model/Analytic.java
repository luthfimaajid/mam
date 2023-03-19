package com.etspplbo50.analyticservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(value = "analytic")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Analytic {
    @Id
    private String id;
    private String orderId;
    private Integer distance;
    private Integer deliveryTime;
    private Integer prepareTime;
}
