package com.homs.account_rest_api.dto;

import lombok.Builder;

@Builder
public class PaginationDTO {
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalItems;
    private Integer totalPages;
}
