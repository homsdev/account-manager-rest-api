package com.homs.account_rest_api.categories.repository;


import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.mocks.CategoryMockFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class CategoryJPARepositoryImplIntegrationTest {

    @Mock
    EntityManager em;

    @Mock
    private TypedQuery<Category> query;

    @InjectMocks
    CategoryJPARepositoryImpl categoryRepository;

    @Test
    public void getAllCategoriesShouldReturnAllCategories() {
        log.info("Executing getAllCategoriesShouldReturnAllCategories Test");
        CategoryMockFactory categoryMockFactory = new CategoryMockFactory();
        when(em.createQuery(anyString(),eq(Category.class)))
                .thenReturn(query);
        when(categoryRepository.getAllCategories())
                .thenReturn(categoryMockFactory.all());
        List<Category> result = categoryRepository.getAllCategories();
        log.info(result.toString());
        assertEquals(4, result.size());
    }

    @Test
    public void getCategoryShouldReturnReqCategory() {
        log.info("Executing getCategoryShouldReturnReqCategory Test");
        CategoryMockFactory categoryMockFactory = new CategoryMockFactory();

        when(em.find(eq(Category.class),anyString()))
                .thenReturn(categoryMockFactory.salary());
        Optional<Category> result = categoryRepository.getCategory("cat-salary");
        assertTrue(result.isPresent());
        assertEquals("Salary",result.get().getName());
    }

    @Test
    public void getCategoryShouldReturnEmptyOptionalIfNotFound(){
        log.info("Executing getCategoryShouldReturnEmptyOptionalIfNotFound Test");
        when(em.find(eq(Category.class),anyString()))
                .thenReturn(null);
        Optional<Category> result = categoryRepository.getCategory("cat-notexistent");
        assertTrue(result.isEmpty());
    }

    @Test
    public void getByNameShouldReturnReqCategory() {
        log.info("Executing getByNameShouldReturnReqCategory Test");
        CategoryMockFactory categoryMockFactory = new CategoryMockFactory();
        when(em.createQuery(anyString(),eq(Category.class)))
                .thenReturn(query);
        when(query.getResultStream())
                .thenReturn(Stream.of(categoryMockFactory.salary()));
        Optional<Category> result = categoryRepository.getByName("salary");
        assertTrue(result.isPresent());
        log.info("{}",result.get());
    }

    @Test
    public void getByNameShouldReturnEmptyOptionalForInvalidName() {
        log.info("Executing getByNameShouldReturnEmptyOptionalForInvalidName Test");
        when(em.createQuery(anyString(),eq(Category.class)))
                .thenReturn(query);
        when(query.getResultStream())
                .thenReturn(Stream.empty());
        Optional<Category> result = categoryRepository.getByName("invalid-name");
        assertTrue(result.isEmpty());
    }
}