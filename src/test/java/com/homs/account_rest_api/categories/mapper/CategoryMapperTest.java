package com.homs.account_rest_api.categories.mapper;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.dto.CategoryListResponseDTO;
import com.homs.account_rest_api.categories.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class CategoryMapperTest {

    @Test
    public void shouldConvertToCategoryDto() {
        Category food = Category.builder()
                .id("foodId")
                .name("food")
                .build();
        CategoryDTO result = CategoryMapper.toCategoryDto(food);

        assertEquals(food.getName(), result.getName());
    }

    @Test
    public void shouldReturnNullWhenPassedNullCategory() {
        CategoryDTO result = CategoryMapper.toCategoryDto(null);
        assertNull(result);
    }

    @Test
    public void shouldConvertToCategoryListResponseDTO() {
        Category food = Category.builder()
                .id("foodId")
                .name("food")
                .build();
        Category games = Category.builder()
                .id("gamesId")
                .name("games")
                .build();
        Category movies = Category.builder()
                .id("moviesId")
                .name("movies")
                .build();
        List<Category> categories = List.of(food, games, movies);

        CategoryListResponseDTO result = CategoryMapper.toCategoryListResponseDTO(categories);
        log.info(result.toString());

        assertEquals(3, result.getItems().size());
    }

    @Test
    public void shouldReturnAnEmptyList() {
        List<Category> empty = Collections.emptyList();

        CategoryListResponseDTO result = CategoryMapper.toCategoryListResponseDTO(empty);

        assertTrue(result.getItems().isEmpty());
    }
}