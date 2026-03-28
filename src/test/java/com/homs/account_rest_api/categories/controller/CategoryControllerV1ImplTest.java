package com.homs.account_rest_api.categories.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.mapper.CategoryMapper;
import com.homs.account_rest_api.categories.service.CategoryService;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.mocks.CategoryMockFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(CategoryControllerV1Impl.class)
class CategoryControllerV1ImplTest {

    @MockitoBean
    CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    CategoryMockFactory categoryMockFactory = new CategoryMockFactory();
    CategoryMapper categoryMapper = new CategoryMapper();

    private final String baseUrl = "/api/v1/categories";

    @Test
    void getAllCategoriesShouldReturnAllCategories() throws Exception{

       List<CategoryDTO> dummyCategories =
               categoryMockFactory.all().stream().map(categoryMapper::toCategoryDto).toList();

       when(categoryService.getAllCategories())
               .thenReturn(dummyCategories);

        MvcResult result = mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(dummyCategories.size())))
                .andReturn();

        log.info("MockMvc result: {}", result.getResponse().getContentAsString());
    }

    @Test
    void getAllCategoriesShouldHandleNotCategoriesFound() throws Exception{
        when(categoryService.getAllCategories())
                .thenReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(get(baseUrl))
                .andExpect(status().isNoContent())
                .andReturn();

        log.info("MockMvc result: {}", result.getResponse().getContentAsString());
    }

    @Test
    void getCategoryByIdShouldReturnCategory() throws Exception {
        CategoryDTO category = categoryMapper.toCategoryDto(categoryMockFactory.createFoodCategory());

        when(categoryService.getCategoryById(anyLong()))
                .thenReturn(category);

        MvcResult result = mockMvc.perform(get(baseUrl + "/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(category.getName()))
                .andExpect(jsonPath("$.data.id").value(category.getId()))
                .andReturn();

        log.info("MockMvc result: {}", result.getResponse().getContentAsString());
    }

    @Test
    void getCategoryByIdShouldHandleNotFoundCategory() throws Exception{
        when(categoryService.getCategoryById(anyLong()))
                .thenThrow(new ResourceNotFoundException());

        MvcResult result = mockMvc.perform(get(baseUrl + "/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Requested Resource was not found"))
                .andReturn();

        log.info("MockMvc result: {}", result.getResponse().getContentAsString());
    }
}