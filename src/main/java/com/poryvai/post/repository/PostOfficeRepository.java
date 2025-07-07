package com.poryvai.post.repository;

import com.poryvai.post.model.PostOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for {@link PostOffice} entities.
 * Provides standard CRUD operations and allows for execution of {@link org.springframework.data.jpa.domain.Specification} based queries.
 */
@Repository
public interface PostOfficeRepository extends JpaRepository<PostOffice, Long>, JpaSpecificationExecutor<PostOffice> {
}
