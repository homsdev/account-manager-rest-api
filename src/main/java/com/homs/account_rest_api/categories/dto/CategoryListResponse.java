package com.homs.account_rest_api.categories.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Schema(description = "Category list response")
public class CategoryListResponse extends RepresentationModel<CategoryListResponse> {

    @Schema(description = "List of categories")
    private List<CategoryDTO> data;

    @Schema(description = "Timestamp of the response")
    LocalDateTime timestamp;

    @Schema(hidden = true)
    @Override
    public Links getLinks() {
        return super.getLinks();
    }
}
