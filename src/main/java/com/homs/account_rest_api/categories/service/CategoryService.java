package com.homs.account_rest_api.categories.service;

import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Category Service
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Retrieves all categories
     *
     * @return An immutable {@link List} of {@link Category} objects,
     * returns an empty list if no categories exist
     */
    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    /**
     * Retrieves the requested category
     *
     * @param id {@link String} the unique identifier of the category to retrieve
     * @return A {@link Category}
     */
    public Category getCategoryById(String id) {
        Optional<Category> category = categoryRepository.getCategory(id);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException(id);
        }

        return category.get();
    }
}
