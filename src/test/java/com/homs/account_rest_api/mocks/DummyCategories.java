package com.homs.account_rest_api.mocks;

import com.homs.account_rest_api.categories.model.Category;
import lombok.Getter;

import java.util.List;

@Getter
public class DummyCategories {
    private final Category food;
    private final Category games;
    private final Category movies;
    private final Category dummy;
    private final List<Category> dummyCategoriesList;

    public DummyCategories() {
        food = Category.builder()
                .id("foodId")
                .name("food")
                .build();
        games = Category.builder()
                .id("gamesId")
                .name("games")
                .build();
        movies = Category.builder()
                .id("moviesId")
                .name("movies")
                .build();
        dummy = Category.builder()
                .id("dummyId")
                .name("dummyName")
                .build();
        dummyCategoriesList = List.of(food, games, movies);
    }
}
