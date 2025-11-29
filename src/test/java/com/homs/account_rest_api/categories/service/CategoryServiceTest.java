package com.homs.account_rest_api.categories.service;

import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.mocks.CategoryMockFactory;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest extends TestCase {


    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category food;
    private List<Category> dummyCategories;

    @Override
    @Before
    public void setUp() throws Exception {
        CategoryMockFactory categoryMockFactory = new CategoryMockFactory();
        dummyCategories = categoryMockFactory.all();
        food = categoryMockFactory.foodCategory();
    }

    @Test
    public void shouldRetrieveAllCategories() {
        when(categoryRepository.getAllCategories())
                .thenReturn(dummyCategories);

        List<Category> result = categoryService.getAllCategories();
        log.info("Retrieved categories: {}",result);
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