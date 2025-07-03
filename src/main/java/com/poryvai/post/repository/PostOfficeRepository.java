package com.poryvai.post.repository;

import com.poryvai.post.model.PostOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link PostOffice} entity.
 * Provides standard CRUD operations and custom query capabilities for PostOffice.
 */
@Repository
public interface PostOfficeRepository extends JpaRepository<PostOffice, Long> {
}
