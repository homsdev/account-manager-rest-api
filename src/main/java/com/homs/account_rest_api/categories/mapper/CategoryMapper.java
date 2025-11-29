package com.homs.account_rest_api.categories.mapper;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.dto.CategoryListResponseDTO;
import com.homs.account_rest_api.categories.model.Category;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryMapper {

    public static CategoryDTO toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static CategoryListResponseDTO toCategoryListResponseDTO(List<Category> categories) {
        return CategoryListResponseDTO.builder()
                .items(toCategoryDtoList(categories))
                .build();
    }

    public static List<CategoryDTO> toCategoryDtoList(List<Category> categories) {
        if (categories.isEmpty()) {
            return Collections.emptyList();
        }

        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

}
