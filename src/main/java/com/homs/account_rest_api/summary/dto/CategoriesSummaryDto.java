package com.homs.account_rest_api.summary.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CategoriesSummaryDto {
    private List<CategorySummaryDto> categories;
    private MetadataDto metadata;
}
