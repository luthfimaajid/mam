package com.etspplbo50.cafeservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "cafe")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Cafe {
    @Id
    private String id;
    private String name;
    private String description;
    private String address;
    private double latitude;
    private double longitude;

}
