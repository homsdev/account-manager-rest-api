package com.homs.account_rest_api.categories.service;

import com.homs.account_rest_api.categories.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    public List<CategoryDTO> getAllCategories();
    public CategoryDTO getCategoryById(Long id);
}
