package com.homs.account_rest_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @deprecated
 * @param <T>
 */
@SuppressWarnings("unused")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Deprecated(since = "0.2",forRemoval = true)
public class ApiResponseDTO<T> {
    private List<T> data;
    private String message;
    private Integer code;
}
