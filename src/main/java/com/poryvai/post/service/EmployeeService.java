package com.poryvai.post.service;

import com.poryvai.post.dto.CreateEmployeeRequest;
import com.poryvai.post.dto.EmployeeResponse;
import com.poryvai.post.dto.UpdateEmployeeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing employee-related business logic.
 * Defines the contract for basic CRUD operations.
 */
public interface EmployeeService {

    /**
     * Creates a new employee based on the provided request.
     *
     * @param request The {@link CreateEmployeeRequest} containing all necessary data for employee creation.
     * @return The newly created and persisted {@link EmployeeResponse} DTO representing the newly created employee.
     */
    EmployeeResponse create(CreateEmployeeRequest request);

    /**
     * Retrieves an employee by its unique ID.
     *
     * @param id The unique ID of the employee.
     * @return The {@link EmployeeResponse} DTO corresponding to the given ID.
     * @throws com.poryvai.post.exception.NotFoundException if no employee with the specified ID is found.
     */
    EmployeeResponse getById(Long id);

    /**
     * Retrieves a paginated list of all employees.
     * This method supports pagination and sorting
     *
     * @param pageable An object defining pagination (page number, size) and sorting options.
     * @return A {@link Page} of {@link EmployeeResponse} DTOs.
     */
    Page<EmployeeResponse> getAll(Pageable pageable);

    /**
     * Retrieves a paginated list of all employees working at a specific post office.
     * This method is used to find employees by the ID of their assigned post office,
     * with support for pagination and sorting.
     *
     * @param postOfficeId The unique ID of the post office to search for.
     * @param pageable     An object defining pagination (page number, size) and sorting options.
     * @return A {@link Page} of {@link EmployeeResponse} DTOs.
     */
    Page<EmployeeResponse> getEmployeesByPostOffice(Long postOfficeId, Pageable pageable);

    /**
     * Updates an existing employee identified by its ID.
     *
     * @param id      The unique ID of the employee to be updated.
     * @param request The {@link UpdateEmployeeRequest} containing the updated employee data.
     * @return The updated {@link EmployeeResponse} DTO representing the updated employee.
     * @throws com.poryvai.post.exception.NotFoundException if no employee with the specified ID is found.
     * @throws IllegalArgumentException if validation fails or business rules are violated.
     */
    EmployeeResponse update(Long id, UpdateEmployeeRequest request);

    /**
     * Deletes an employee by its unique ID.
     *
     * @param id The unique ID of the employee to be deleted.
     * @throws com.poryvai.post.exception.NotFoundException if no employee with the specified ID is found.
     */
    void delete(Long id);
}
