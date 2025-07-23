package com.poryvai.post.repository;

import com.poryvai.post.model.Client;
import com.poryvai.post.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Employee} entities.
 * Provides standard CRUD operations for Employee entities.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
