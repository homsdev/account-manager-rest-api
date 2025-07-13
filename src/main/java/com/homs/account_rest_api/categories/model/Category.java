package com.homs.account_rest_api.categories.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category {
    private String id;
    private String name;
}
