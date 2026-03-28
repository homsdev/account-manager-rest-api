package com.homs.account_rest_api.categories.service.impl;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.mapper.CategoryMapper;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.mocks.CategoryMockFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;

    @Spy
    CategoryMapper categoryMapper;

    @InjectMocks
    CategoryServiceImpl categoryService;

    CategoryMockFactory categoryMockFactory = new CategoryMockFactory();

    @Test
    void getAllCategoriesShouldReturnEmptyListWhenNoCategoriesFound() {
        when(categoryRepository.getAllCategories()).
                thenReturn(Collections.emptyList());

        List<CategoryDTO> result = categoryService.getAllCategories();
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllCategoriesShouldReturnFoundCategories() {
        List<Category> mockCategories = categoryMockFactory.all();
        when(categoryRepository.getAllCategories()).
                thenReturn(mockCategories);

        List<CategoryDTO> result = categoryService.getAllCategories();
        assertEquals(mockCategories.size(), result.size());
    }

    @Test
    void getCategoryById() {
        Category mockCategory = categoryMockFactory.createFoodCategory();

        when(categoryRepository.getCategory(anyLong()))
                .thenReturn(Optional.of(mockCategory));

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertEquals(mockCategory.getId(), result.getId());
        assertEquals(mockCategory.getName(), result.getName());
    }
}