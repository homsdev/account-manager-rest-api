package com.homs.account_rest_api.mocks;

import com.homs.account_rest_api.categories.model.Category;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CategoryMockFactory {

    private final Map<String, Category> namedCategories = new HashMap<>();

    public Category createGamesCategory() {
        return namedCategories.computeIfAbsent("Games",
                c -> Category.builder().id(1L).name("Games").build());
    }

    public Category createFoodCategory() {
        return namedCategories.computeIfAbsent("Food",
                c -> Category.builder().id(2L).name("Food").build());
    }

    public Category createTransportationCategory() {
        return namedCategories.computeIfAbsent("Transportation",
                c -> Category.builder().id(3L).name("Transportation").build());
    }

    public Category createSalaryCategory() {
        return namedCategories.computeIfAbsent("Salary",
                c -> Category.builder().id(4L).name("Salary").build());
    }

    /**
     * Returns 4 dummy categories for testing purposes
     * @return 4 Categories
     */
    public List<Category> all() {
        createGamesCategory();
        createFoodCategory();
        createTransportationCategory();
        createSalaryCategory();
        return namedCategories.values().stream().toList();
    }

}
