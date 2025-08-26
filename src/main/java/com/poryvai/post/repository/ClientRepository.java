package com.poryvai.post.repository;

import com.poryvai.post.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Client} entities.
 * Provides standard CRUD operations for Client entities,
 * and also enables dynamic search capabilities using {@link JpaSpecificationExecutor}.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
}
