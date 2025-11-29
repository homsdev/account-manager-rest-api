package com.homs.account_rest_api.mocks;

import com.homs.account_rest_api.categories.model.Category;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CategoryMockFactory {

    private final Map<String, Category> namedCategories = new HashMap<>();

    public Category gamesCategory() {
        return namedCategories.computeIfAbsent("Games",
                c -> Category.builder().id("cat-games").name("Games").build());
    }

    public Category foodCategory() {
        return namedCategories.computeIfAbsent("Food",
                c -> Category.builder().id("cat-food").name("Food").build());
    }

    public Category transportation() {
        return namedCategories.computeIfAbsent("Transportation",
                c -> Category.builder().id("cat-transportation").name("Transportation").build());
    }

    public Category salary() {
        return namedCategories.computeIfAbsent("Salary",
                c -> Category.builder().id("cat-salary").name("Salary").build());
    }

    /**
     * Returns 4 dummy categories for testing purposes
     * @return 4 Categories
     */
    public List<Category> all() {
        gamesCategory();
        foodCategory();
        transportation();
        salary();
        return namedCategories.values().stream().toList();
    }

}
