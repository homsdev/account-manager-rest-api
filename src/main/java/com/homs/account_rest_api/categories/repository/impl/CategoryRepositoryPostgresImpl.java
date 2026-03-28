package com.homs.account_rest_api.categories.repository.impl;

import com.homs.account_rest_api.categories.mapper.CategoryRowMapper;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryPostgresImpl implements CategoryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Category> getAllCategories() {
        String sql = "SELECT * FROM cli_category";
        return jdbcTemplate.query(sql, new CategoryRowMapper());
    }

    @Override
    public Optional<Category> getCategory(Long id) {
        String sql = "SELECT * FROM cli_category WHERE category_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(sql, params, new CategoryRowMapper()).stream().findFirst();
    }

    @Override
    public Optional<Category> getByName(String name) {
        String sql = "SELECT * FROM cli_category WHERE category_name = :name";
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        return jdbcTemplate.query(sql, params, new CategoryRowMapper()).stream().findFirst();
    }
}
