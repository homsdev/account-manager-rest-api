package com.homs.account_rest_api.categories.mapper;

import com.homs.account_rest_api.categories.model.Category;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CategoryRowMapper implements RowMapper<Category> {

    @Override
    public @Nullable Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        LocalDateTime createdDate = rs.getTimestamp("creation_date").toLocalDateTime();
        LocalDateTime updatedDate = rs.getTimestamp("updated_date").toLocalDateTime();

        return Category.builder()
                .id(rs.getLong("category_id"))
                .name(rs.getString("category_name"))
                .createdAt(createdDate)
                .updatedAt(updatedDate)
                .build();
    }
}
