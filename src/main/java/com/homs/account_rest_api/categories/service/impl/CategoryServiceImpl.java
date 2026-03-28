package com.homs.account_rest_api.categories.service.impl;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.mapper.CategoryMapper;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.categories.service.CategoryService;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getAllCategories(){
        List<Category> categories = categoryRepository.getAllCategories();

        if(categories.isEmpty()){
            return Collections.emptyList();
        }

        return categories.stream().map(categoryMapper::toCategoryDto).toList();

    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.getCategory(id)
                .orElseThrow(ResourceNotFoundException::new);

        return categoryMapper.toCategoryDto(category);
    }
}
