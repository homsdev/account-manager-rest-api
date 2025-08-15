package com.homs.account_rest_api.categories.controller;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.dto.CategoryListResponseDTO;
import com.homs.account_rest_api.dto.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ApiResponseDTO.class,
                                            subTypes = {CategoryListResponseDTO.class}
                                    ),
                                    examples = @ExampleObject(
                                            value = "{\"data\":{\"items\":[{\"id\":\"1\",\"name\":\"Food\"}]},\"timestamp\":\"2023-11-20T15:30:45.00Z\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "There are no available categories for retrieval(no content)",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    ResponseEntity<ApiResponseDTO<CategoryListResponseDTO>> getAllCategories();

    @Operation(
            summary = "Retrieve a single category",
            description = "Fetches the category that matches with provided id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved category",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ApiResponseDTO.class,
                                            subTypes = {CategoryDTO.class}
                                    ),
                                    examples = @ExampleObject(
                                            value = "{\n  \"data\": {\n    \"id\": 1,\n    \"name\": \"Food\"\n  },\n  \"timestamp\": \"2023-11-20T15:30:45.00Z\"\n}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Requested category not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ApiResponseDTO.class
                                    ),
                                    examples = @ExampleObject(
                                            value = "{\n  \"message\":[\"Category not found\"],\n  \"timestamp\":\"12:00:00 MM-YY-DD\"\n}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<ApiResponseDTO<CategoryDTO>> getCategoryById(String id);


}
