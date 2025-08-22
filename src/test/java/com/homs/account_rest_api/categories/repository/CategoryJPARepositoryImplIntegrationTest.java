package com.homs.account_rest_api.categories.repository;

import com.homs.account_rest_api.AccountRestApiApplication;
import com.homs.account_rest_api.categories.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountRestApiApplication.class)
@ActiveProfiles("dev")
@Slf4j
public class CategoryJPARepositoryImplIntegrationTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void getAllCategoriesShouldReturnAllCategories() {
        List<Category> result = categoryRepository.getAllCategories();
        log.info(result.toString());
        assertEquals(3, result.size());
    }

    @Test
    public void getCategoryShouldReturnReqCategory() {
        Optional<Category> result = categoryRepository.getCategory("cat-salary");
        assertTrue(result.isPresent());
        assertEquals("salary",result.get().getName());
    }

    @Test
    public void getCategoryShouldReturnEmptyOptionalIfNotFound(){
        Optional<Category> result = categoryRepository.getCategory("cat-notexistent");
        assertTrue(result.isEmpty());
    }

    @Test
    public void getByNameShouldReturnReqCategory() {
        Optional<Category> result = categoryRepository.getByName("salary");
        assertTrue(result.isPresent());
    }

    @Test
    public void getByNameShouldReturnEmptyOptionalForInvalidName() {
        Optional<Category> result = categoryRepository.getByName("invalid-name");
        assertTrue(result.isEmpty());
    }
}