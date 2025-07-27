package com.homs.account_rest_api.categories.repository;

import com.homs.account_rest_api.categories.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    public List<Category> getAllCategories();

    public Optional<Category> getCategory(String id);

    public Optional<Category> getByName(String name);
}
