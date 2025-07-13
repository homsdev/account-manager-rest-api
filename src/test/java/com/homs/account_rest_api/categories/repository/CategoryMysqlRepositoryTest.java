package com.homs.account_rest_api.categories.repository;

import com.homs.account_rest_api.categories.mapper.CategoryRowMapper;
import com.homs.account_rest_api.categories.model.Category;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("Test")
public class CategoryMysqlRepositoryTest extends TestCase {

    @MockBean
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category food, videogames, streaming;
    private List<Category> all;

    @Override
    @Before
    public void setUp() throws Exception {
        food = Category.builder()
                .id(UUID.randomUUID().toString())
                .name("food")
                .build();
        videogames = Category.builder()
                .id(UUID.randomUUID().toString())
                .name("videogames")
                .build();
        streaming = Category.builder()
                .id(UUID.randomUUID().toString())
                .name("streaming")
                .build();
        all = List.of(food, videogames, streaming);
    }

    @Test
    public void shouldReturnAllValues() {
        when(jdbcTemplate.query(anyString(), any(CategoryRowMapper.class)))
                .thenReturn(all);

        List<Category> allCategories = categoryRepository.getAllCategories();
        int expectedValue = 3;

        assertEquals(expectedValue, allCategories.size());
    }

    @Test
    public void shouldReturnEmptyList() {
        when(jdbcTemplate.query(anyString(),any(CategoryRowMapper.class)))
                .thenReturn(Collections.emptyList());

        List<Category> allCategories = categoryRepository.getAllCategories();

        assertTrue(allCategories.isEmpty());
    }

    @Test
    public void shouldReturnRequestedValue() {
        when(jdbcTemplate.query(anyString(),anyMap(),any(CategoryRowMapper.class)))
                .thenReturn(Collections.singletonList(food));

        Optional<Category> result = categoryRepository.getCategory("foodId");

        assertTrue(result.isPresent());
    }

    @Test
    public void shouldReturnValueNotFound() {
        when(jdbcTemplate.query(anyString(),anyMap(),any(CategoryRowMapper.class)))
                .thenReturn(Collections.emptyList());

        Optional<Category> result = categoryRepository.getCategory("restaurantsId");

        assertTrue(result.isEmpty());
    }
}