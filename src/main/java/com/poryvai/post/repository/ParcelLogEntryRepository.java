package com.poryvai.post.repository;

import com.poryvai.post.model.ParcelLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link ParcelLogEntry} entities.
 * Provides standard CRUD operations for ParcelLogEntry entities.
 */
public interface ParcelLogEntryRepository extends JpaRepository<ParcelLogEntry, Long> {
}
