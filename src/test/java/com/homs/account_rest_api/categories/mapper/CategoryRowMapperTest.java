package com.homs.account_rest_api.categories.mapper;

import com.homs.account_rest_api.categories.model.Category;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class CategoryRowMapperTest {

    private ResultSet rs;
    private CategoryRowMapper categoryRowMapper;

    @Before
    public void setUp() {
        rs = mock(ResultSet.class);
        categoryRowMapper = new CategoryRowMapper();
    }

    @Test
    public void shouldMapResultSetToEntity() throws SQLException {
        when(rs.getString("category_id"))
                .thenReturn("categoryId");

        when(rs.getString("category_name"))
                .thenReturn("categoryName");

        Category category = categoryRowMapper.mapRow(rs, 42);
        assertNotNull(category);
        assertEquals("categoryId", category.getId());
        assertEquals("categoryName", category.getName());
    }
}