package com.homs.account_rest_api.categories.mapper;

import com.homs.account_rest_api.categories.model.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CategoryRowMapper implements RowMapper<Category> {

    /**
     * Maps result row to a {@link Category} object
     * @param rs result row
     * @param rowNum result row number
     * @return A {@link Category} object
     * @throws SQLException
     */
    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Category.builder()
                .id(rs.getString("category_id"))
                .name(rs.getString("category_name"))
                .build();
    }
}
