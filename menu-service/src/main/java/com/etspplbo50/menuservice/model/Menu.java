package com.etspplbo50.menuservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "menu")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Menu {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private Integer price;

}
