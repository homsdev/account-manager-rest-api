package com.homs.account_rest_api.categories.controller;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.dto.CategoryResponse;
import com.homs.account_rest_api.categories.service.CategoryService;
import com.homs.account_rest_api.categories.dto.CategoryListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryControllerV1Impl implements CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Override
    public ResponseEntity<CategoryListResponse> getAllCategories() {

        List<CategoryDTO> allCategories = categoryService.getAllCategories();

        if (allCategories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        CategoryListResponse res = CategoryListResponse.builder().data(allCategories).timestamp(LocalDateTime.now()).build();


        Link link = Link.of("/api/categories/{id}")
                .withRel("find")
                .withType("GET");

        res.add(link);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {

        CategoryDTO category = categoryService.getCategoryById(id);

        CategoryResponse res = CategoryResponse.builder().data(category).timestamp(LocalDate.now()).build();

        res.add(linkTo(
                methodOn(CategoryControllerV1Impl.class).getAllCategories()
        ).withRel("collection").withType("GET"));

        return ResponseEntity.ok(res);
    }


}
