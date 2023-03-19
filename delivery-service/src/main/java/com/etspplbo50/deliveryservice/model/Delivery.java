package com.etspplbo50.deliveryservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Date;
import java.time.Instant;

@Document(value = "delivery")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Delivery {
    @Id
    private String id;
    private String orderId;
    private String employeeId;
    private String status;
    private Integer distance;
    @CreatedDate
    private Instant startTime;
    @LastModifiedDate
    private Instant endTime;
}
