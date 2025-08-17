package com.homs.account_rest_api.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableValidation {

    public static void validateTable(
            String repository,
            String tableName,
            EntityManager em) {
        log.info("Validating {} repository with table: {}",
                repository, tableName);

        try {
            Long result = (Long) em.createNativeQuery("""
                            SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES 
                            WHERE TABLE_NAME = ?1 
                            AND TABLE_SCHEMA = SCHEMA()
                            """, Long.class)
                    .setParameter(1, tableName.toUpperCase())
                    .getSingleResult();
            log.info("Retrieved result: {}",result);
            if (result != 1L) {
                throw new IllegalStateException("Table " + tableName + " does not exist");
            }
        } catch (PersistenceException e) {
            throw new IllegalStateException("Table validation failed: " + e.getMessage(), e);
        }
    }
}
