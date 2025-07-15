package com.homs.account_rest_api.categories.controller;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.dto.CategoryListResponseDTO;
import com.homs.account_rest_api.categories.mapper.CategoryMapper;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.service.CategoryService;
import com.homs.account_rest_api.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    @Override
    public ResponseEntity<ApiResponseDTO<CategoryListResponseDTO>> getAllCategories() {

        List<Category> allCategories = categoryService.getAllCategories();

        if (allCategories.isEmpty()) {
            return ResponseEntity.status(204).body(null);
        }

        CategoryListResponseDTO categoriesDto = categoryMapper.toCategoryListResponseDTO(allCategories);

        ApiResponseDTO<CategoryListResponseDTO> res = ApiResponseDTO
                .<CategoryListResponseDTO>builder()
                .data(categoriesDto)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseDTO<CategoryDTO>> getCategoryById(@PathVariable String id) {

        Category category = categoryService.getCategoryById(id);

        CategoryDTO categoryDto = categoryMapper.toCategoryDto(category);

        ApiResponseDTO<CategoryDTO> res = ApiResponseDTO.<CategoryDTO>builder()
                .data(categoryDto)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(res);
    }
}
