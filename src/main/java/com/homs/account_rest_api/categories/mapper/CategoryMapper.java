package com.homs.account_rest_api.categories.mapper;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
