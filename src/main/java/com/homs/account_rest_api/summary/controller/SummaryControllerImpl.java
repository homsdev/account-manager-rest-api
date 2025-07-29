package com.homs.account_rest_api.summary.controller;

import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.summary.dto.CategoriesSummaryDto;
import com.homs.account_rest_api.summary.dto.SummaryDto;
import com.homs.account_rest_api.summary.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/expenses/summary")
@RequiredArgsConstructor
public class SummaryControllerImpl implements SummaryController {

    private final SummaryService summaryService;

    @GetMapping
    @Override
    public ResponseEntity<ApiResponseDTO<SummaryDto>> getSummary() {
        Optional<SummaryDto> summary = summaryService.getSummary();

        if (summary.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ApiResponseDTO<SummaryDto> res = ApiResponseDTO.<SummaryDto>builder()
                .data(summary.get())
                .timestamp(Instant.now())
                .build();

        res.add(linkTo(methodOn(SummaryControllerImpl.class).getSummary())
                .withSelfRel()
        );

        res.add(linkTo(methodOn(SummaryControllerImpl.class).getCategorySummary())
                .withRel("category-summary")
                .withTitle("View summary by categories")
        );

        return ResponseEntity.ok(res);
    }

    @GetMapping("/categories")
    @Override
    public ResponseEntity<ApiResponseDTO<CategoriesSummaryDto>> getCategorySummary() {
        Optional<CategoriesSummaryDto> categoriesSummary = summaryService.getCategoriesSummary();

        if (categoriesSummary.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ApiResponseDTO<CategoriesSummaryDto> res = ApiResponseDTO.<CategoriesSummaryDto>builder()
                .data(categoriesSummary.get())
                .timestamp(Instant.now())
                .build();

        res.add(linkTo(methodOn(SummaryControllerImpl.class).getCategorySummary())
                .withSelfRel()
        );

        res.add(linkTo(methodOn(SummaryControllerImpl.class).getSummary())
                .withRel("general-summary")
                .withTitle("View general summary")
        );

        return ResponseEntity.ok(res);
    }
}
