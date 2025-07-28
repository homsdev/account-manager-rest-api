package com.homs.account_rest_api.summary.controller;

import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.summary.dto.SummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses/summary")
@RequiredArgsConstructor
public class SummaryControllerImpl implements SummaryController {

    @GetMapping
    @Override
    public ResponseEntity<ApiResponseDTO<SummaryDto>> getSummary() {
        return null;
    }

    @GetMapping("/categories")
    @Override
    public ResponseEntity<ApiResponseDTO<SummaryDto>> getCategorySummary() {
        return null;
    }
}
