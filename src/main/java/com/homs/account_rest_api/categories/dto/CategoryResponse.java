package com.homs.account_rest_api.categories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Builder
@Getter
@Schema(
        description = "Category response DTO"
)
public class CategoryResponse extends RepresentationModel<CategoryResponse> {

    @Schema(
            description = "Category data"
    )
    private CategoryDTO data;

    @Schema(
            description = "The timestamp of the response"
    )
    private LocalDate timestamp;

    @Schema(hidden = true)
    @Override
    public Links getLinks() {
        return super.getLinks();
    }
}
