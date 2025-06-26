package com.poryvai.post.util;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * A base interface for integration tests that require a running PostgreSQL database.
 * This interface leverages the Testcontainers library to automatically provision and manage
 * a PostgreSQL container, ensuring a clean and isolated database instance for each test run.
 * Test classes can implement this interface to inherit the Testcontainers setup.
 */
@Testcontainers
public interface BasePgSqlIT {

    @Container
    PostgreSQLContainer<?> PG_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void setPgSqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PG_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", PG_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", PG_SQL_CONTAINER::getPassword);
    }
}
