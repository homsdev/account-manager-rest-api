package com.homs.account_rest_api.categories.service;

import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CategoryServiceTest extends TestCase {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    private Category food, games, travels;
    private List<Category> dummyCategories;

    @Override
    @Before
    public void setUp() throws Exception {
        food = Category.builder()
                .id(UUID.randomUUID().toString())
                .name("food")
                .build();
        games = Category.builder()
                .id(UUID.randomUUID().toString())
                .name("games")
                .build();
        travels = Category.builder()
                .id(UUID.randomUUID().toString())
                .name("travels")
                .build();
        dummyCategories = List.of(food, games, travels);
    }

    @Test
    public void shouldRetrieveAllCategories() {
        when(categoryRepository.getAllCategories())
                .thenReturn(dummyCategories);

        List<Category> result = categoryService.getAllCategories();
        assertEquals(dummyCategories, result);
    }

    @Test
    public void shouldReturnAnEmptyListOfCategories() {
        when(categoryRepository.getAllCategories())
                .thenReturn(Collections.emptyList());

        List<Category> result = categoryService.getAllCategories();

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnRequestedCategory() {
        when(categoryRepository.getCategory(anyString()))
                .thenReturn(Optional.of(food));

        Category result = categoryService.getCategoryById("foodId");

        assertEquals(food, result);
    }

    @Test
    public void shouldThrowNotFoundException() {
        when(categoryRepository.getCategory(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getCategoryById("invalidId"));

    }
}