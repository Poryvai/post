package com.poryvai.post.repository;

import com.poryvai.post.model.Client;
import com.poryvai.post.model.Employee;
import com.poryvai.post.model.EmployeePosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


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

    /**
     * Finds the first {@link Employee} entity with a specified position.
     * The method returns an {@link Optional} containing the employee if found,
     * or an empty {@link Optional} otherwise.
     *
     * @param position The position of the employee to find, from {@link EmployeePosition}.
     * @return An {@link Optional} containing the found employee, or an empty optional.
     */
    Optional<Employee> findFirstByPosition(EmployeePosition position);
}
