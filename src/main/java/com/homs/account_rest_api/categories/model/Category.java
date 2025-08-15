package com.homs.account_rest_api.categories.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class Category {
    private String id;
    private String name;
}
