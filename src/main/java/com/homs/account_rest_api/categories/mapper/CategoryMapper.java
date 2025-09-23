package com.homs.account_rest_api.categories.mapper;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.dto.CategoryListResponseDTO;
import com.homs.account_rest_api.categories.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Collections;
import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    @Mapping(source = "name", target = "name")
    CategoryDTO toCategoryDto(Category category);

    default CategoryListResponseDTO toCategoryListResponseDTO(List<Category> categories) {
        return CategoryListResponseDTO.builder()
                .items(toCategoryDtoList(categories))
                .build();
    }

    default List<CategoryDTO> toCategoryDtoList(List<Category> categories) {
        if (categories.isEmpty()) return Collections.emptyList();
        return categories.stream()
                .map(this::toCategoryDto)
                .toList();
    }
}
