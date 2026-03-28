package com.homs.account_rest_api.categories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@Schema(
        description = "Category DTO"
)
public class CategoryDTO {

    @Schema(
            description = "Category ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Category name",
            example = "Transportation"
    )
    private String name;
}
