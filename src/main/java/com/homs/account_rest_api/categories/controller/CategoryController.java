package com.homs.account_rest_api.categories.controller;

import com.homs.account_rest_api.categories.dto.CategoryResponse;
import com.homs.account_rest_api.categories.dto.CategoryListResponse;
import com.homs.account_rest_api.dto.ErrorApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@Tag(name = "Category", description = "API endpoints for managing expense categories")
public interface CategoryController {

    @Operation(
            summary = "Retrieve all categories",
            description = "Fetches a complete list of available expense categories.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved category list.",
                            content = @Content(
                                    schema = @Schema(implementation = CategoryListResponse.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "There are no available categories for retrieval(no content)",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    ResponseEntity<CategoryListResponse> getAllCategories();

    @Operation(
            summary = "Retrieve a single category",
            description = "Fetches the category that matches with provided id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved category",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CategoryResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Requested category not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorApiResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<CategoryResponse> getCategoryById(Long id);


}
