package com.homs.account_rest_api.summary.controller;

import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.summary.dto.CategoriesSummaryDto;
import com.homs.account_rest_api.summary.dto.SummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Summary", description = "Provides aggregated transaction overviews with filtering capabilities.")
public interface SummaryController {

    @Operation(
            summary = "Generate transaction summary for current month",
            description = "Calculates aggregated transaction data for the current month",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ApiResponseDTO.class,
                                            subTypes = {SummaryDto.class}
                                    ),
                                    examples = @ExampleObject(
                                            value = "{\n  \"totalBalance\": 20000.75,\n  \"creditCardExpenses\": 10000.00,\n  \"savingsGoal\": 8500,\n  \"count\": 10,\n  \"largestExpense\": {\n    \"id\": \"123\",\n    \"amount\": 4500.00,\n    \"type\": \"EXPENSE\",\n    \"date\": \"2025-07-10\",\n    \"alias\": \"Vacations\"\n  },\n  \"metadata\": {\n    \"totalExpenses\": 661.25,\n    \"count\": 2\n  },\n  \"_links\": {}\n}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No data available to show for current month",
                            content = @Content(
                                    schema = @Schema(hidden = true)
                            )
                    )
            }
    )
    public ResponseEntity<ApiResponseDTO<SummaryDto>> getSummary();

    @Operation(
            summary = "Generate expenses summary grouped by category for current month ",
            description = "Calculates aggregated expense totals grouped by category for the current month.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ApiResponseDTO.class,
                                            subTypes = {SummaryDto.class}
                                    ),
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\n  \"categorySummaries\": [\n    {\n      \"category\": \"Groceries\",\n      \"total\": 450.75\n    },\n    {\n      \"category\": \"Utilities\",\n      \"total\": 210.50\n    }\n  ],\n  \"metadata\": {\n    \"totalExpenses\": 661.25,\n    \"count\": 2\n  },\n  \"_links\": {}\n}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No data available to show for current month",
                            content = @Content(
                                    schema = @Schema(hidden = true)
                            )
                    ),
            }
    )
    public ResponseEntity<ApiResponseDTO<CategoriesSummaryDto>> getCategorySummary();
}
