package com.poryvai.post.repository;

import com.poryvai.post.model.Client;
import com.poryvai.post.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for {@link Employee} entities.
 * Provides standard CRUD operations for Employee entities.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Finds all {@link Employee} entities associated with a specific post office.
     * This method retrieves a list of employees by matching the ID of their
     * assigned post office.
     *
     * @param postOfficeId The unique identifier of the post office.
     * @param pageable     An object defining pagination and sorting options.
     * @return A {@link Page} of {@link Employee} entities found for the given post office ID.
     */
    Page<Employee> findByPostOfficeId(Long postOfficeId, Pageable pageable);
}
