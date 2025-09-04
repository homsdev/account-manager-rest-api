package com.homs.account_rest_api.categories.repository;

import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.utils.TableValidation;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
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
    public void init() {
        log.info("Currently using: {}", this.getClass().getSimpleName());
        TableValidation.tableExists(em,"Category");
    }

    @Override
    public List<Category> getAllCategories() {
        return em.createQuery("SELECT c FROM Category c", Category.class)
                .getResultList();
    }

    @Override
    public Optional<Category> getCategory(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidParametersException("Invalid category data");
        }
        return Optional.ofNullable(em.find(Category.class, id));
    }

    @Override
    @Transactional
    public Optional<Category> getByName(String name) {
        TypedQuery<Category> query = em.createQuery(
                "SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name)", Category.class);
        query.setParameter("name", name);
        return query.getResultStream().findFirst();
    }
}
