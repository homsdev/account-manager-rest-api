package com.homs.account_rest_api.categories.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class CategoryDTO {
    private String id;
    private String name;
}
