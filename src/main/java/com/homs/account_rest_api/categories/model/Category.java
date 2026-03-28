package com.homs.account_rest_api.categories.model;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
public class Category {
    private Long id;
    private String name;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
