package com.homs.account_rest_api.categories.repository.impl;

import com.homs.account_rest_api.categories.mapper.CategoryRowMapper;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.mocks.CategoryMockFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CategoryRepositoryPostgresImplTest {

    @Mock
    NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private CategoryRepositoryPostgresImpl categoryRepository;

    private CategoryMockFactory categoryMockFactory = new CategoryMockFactory();

    @Test
    void getAllCategories() {
        List<Category> categories = categoryMockFactory.all();

        when(jdbcTemplate.query(anyString(), any(CategoryRowMapper.class)))
                .thenReturn(categories);

        List<Category> result = categoryRepository.getAllCategories();
        assertNotNull(result);
        assertEquals(categories.size(), result.size());
    }

    @Test
    void getCategory() {
        Category category = categoryMockFactory.createFoodCategory();

        when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(CategoryRowMapper.class)))
                .thenReturn(Collections.singletonList(category));

        categoryRepository.getCategory(1L).ifPresentOrElse(cat -> {
            assertEquals(category.getId(), cat.getId());
            assertEquals(category.getName(), cat.getName());
        }, () -> fail("Expected category to be found"));
    }

    @Test
    void getByName() {
        Category category = categoryMockFactory.createFoodCategory();

        when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(CategoryRowMapper.class)))
                .thenReturn(Collections.singletonList(category));

        categoryRepository.getByName("Food").ifPresentOrElse(cat -> {
            assertEquals(category.getId(), cat.getId());
            assertEquals(category.getName(), cat.getName());
        }, () -> fail("Expected category to be found"));
    }
}