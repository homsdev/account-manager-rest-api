package com.homs.account_rest_api.categories.repository;

import com.homs.account_rest_api.categories.mapper.CategoryRowMapper;
import com.homs.account_rest_api.categories.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Category Repository Mysql Implementation
 */
@Repository
@RequiredArgsConstructor
public class CategoryMysqlRepository implements CategoryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${category.findAll}")
    private String findAllQuery;

    @Value("${category.findById}")
    private String findByIdQuery;

    /**
     * Retrieves all category records from the database.
     *
     * <p>This method executes a SQL query to fetch all available categories,
     * mapping each result row to a {@link Category} object using {@link CategoryRowMapper}.
     *
     * @return An immutable {@link List} of {@link Category} objects,
     * guaranteed non-null. Returns an empty list if no categories exist.
     *
     * @throws org.springframework.dao.DataAccessException if there's any problem executing the query or
     * mapping results (e.g., SQL syntax error, connection issues)
     */
    @Override
    public List<Category> getAllCategories() {
        return jdbcTemplate.query(findAllQuery, new CategoryRowMapper());
    }

    /**
     * Retrieves a specific category record from the database.
     *
     * <p>This methods executes a SQL query to fetch an specific category,
     * mapping the result row to a {@link Category} using {@link CategoryRowMapper}.</p>
     *
     * @param id {@link String} the unique identifier of the category to retrieve
     * @return An {@link Optional} of {@link Category} returns an empty optional if no match
     *
     * @throws org.springframework.dao.DataAccessException if there's any problem executing the query or
     * mapping results (e.g., SQL syntax error, connection issues)
     */
    @Override
    public Optional<Category> getCategory(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbcTemplate.query(findByIdQuery, params, new CategoryRowMapper())
                .stream().findFirst();
    }
}
