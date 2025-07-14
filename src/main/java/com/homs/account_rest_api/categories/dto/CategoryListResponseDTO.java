package com.homs.account_rest_api.categories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Schema(
        description = "Category list response wrapper"
)
@Builder
@Getter
@ToString
public class CategoryListResponseDTO {

    @Schema(
            description = "List of category items",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<CategoryDTO> items;
}
