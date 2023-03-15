package com.etspplbo50.ticketservice.model;

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

@Document(value = "ticket")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ticket {
    @Id
    private String id;
    private String orderId;
    private String status;
    @CreatedDate
    private Date startTime;
    @LastModifiedDate
    private Date endTime;
    @Version
    private Integer version;
}
