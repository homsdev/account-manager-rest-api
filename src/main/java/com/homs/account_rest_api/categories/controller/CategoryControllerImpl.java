package com.homs.account_rest_api.categories.controller;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.dto.CategoryListResponseDTO;
import com.homs.account_rest_api.categories.mapper.CategoryMapper;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.service.CategoryService;
import com.homs.account_rest_api.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
            return ResponseEntity.noContent().build();
        }

        CategoryListResponseDTO categoriesDto = categoryMapper.toCategoryListResponseDTO(allCategories);

        ApiResponseDTO<CategoryListResponseDTO> res = ApiResponseDTO
                .<CategoryListResponseDTO>builder()
                .data(categoriesDto)
                .timestamp(Instant.now())
                .build();

        Link link = Link.of("/api/categories/{id}")
                .withRel("find")
                .withType("GET");
        res.add(link);

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

        res.add(linkTo(
                methodOn(CategoryControllerImpl.class).getAllCategories()
        ).withRel("collection").withType("GET"));

        return ResponseEntity.ok(res);
    }


}
