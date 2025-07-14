package com.homs.account_rest_api.categories.mapper;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.categories.dto.CategoryListResponseDTO;
import com.homs.account_rest_api.categories.model.Category;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("Test")
public class CategoryMapperTest extends TestCase {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void shouldConvertToCategoryDto() {
        Category food = Category.builder()
                .id("foodId")
                .name("food")
                .build();
        CategoryDTO result = categoryMapper.toCategoryDto(food);

        assertEquals(food.getId(), result.getId());
        assertEquals(food.getName(), result.getName());
    }

    @Test
    public void shouldReturnNullWhenPassedNullCategory() {
        CategoryDTO result = categoryMapper.toCategoryDto(null);
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

        CategoryListResponseDTO result = categoryMapper.toCategoryListResponseDTO(categories);
        log.info(result.toString());

        assertEquals(3, result.getItems().size());
    }

    @Test
    public void shouldReturnAnEmptyList() {
        List<Category> empty = Collections.emptyList();

        CategoryListResponseDTO result = categoryMapper.toCategoryListResponseDTO(empty);

        assertTrue(result.getItems().isEmpty());
    }
}