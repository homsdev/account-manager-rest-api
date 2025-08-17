package com.homs.account_rest_api.categories.repository;

import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.utils.TableValidation;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * CategoryRepository JPA with EntityManager implementation class
 * Profile: This implementation is only active when the dev profile is enabled.
 */
@Slf4j
@Repository
@Profile({"dev"})
@RequiredArgsConstructor
public class CategoryJPARepositoryImpl implements CategoryRepository {

    private final EntityManager em;

    @PostConstruct
    public void init(){
        log.info("Currently using: {}",this.getClass().getSimpleName());
        TableValidation.validateTable(
                this.getClass().getSimpleName(),
                "cli_transaction",
                this.em
        );
    }

    @Override
    public List<Category> getAllCategories() {
        return Collections.emptyList();
    }

    @Override
    public Optional<Category> getCategory(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Category> getByName(String name) {
        return Optional.empty();
    }
}
