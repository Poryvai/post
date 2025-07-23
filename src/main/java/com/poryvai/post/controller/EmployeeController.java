package com.poryvai.post.controller;

import com.poryvai.post.dto.CreateEmployeeRequest;
import com.poryvai.post.dto.EmployeeResponse;
import com.poryvai.post.dto.UpdateEmployeeRequest;
import com.poryvai.post.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing employee entities.
 * Provides API endpoints for basic CRUD operations on employees.
 */
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Creates a new employee record in the system.
     *
     * @param request The {@link CreateEmployeeRequest} object containing details for the new employee.
     * @return The created {@link EmployeeResponse} object.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse create(@Valid @RequestBody CreateEmployeeRequest request){
        log.info("Received request to create employee: {}", request);
        EmployeeResponse response = employeeService.create(request);
        log.info("Employee created successfully with ID: {}", response.getId());
        return response;
    }

    /**
     * Retrieves an employee by their unique ID.
     *
     * @param id The unique ID of the employee to retrieve, extracted from the URL path.
     * @return The {@link EmployeeResponse} object corresponding to the given ID.
     * @throws com.poryvai.post.exception.NotFoundException if no employee is found with the given ID.
     */
    @GetMapping("/{id}")
    public EmployeeResponse getById(@PathVariable Long id){
        log.info("Received request to get employee by ID: {}", id);
        EmployeeResponse response = employeeService.getById(id);
        log.info("Employee with ID: {} fetched successfully.", response.getId());
        return response;
    }

    /**
     * Retrieves a paginated list of all employees.
     *
     * @param pageable {@link Pageable} object containing pagination and sorting information.
     * Automatically resolved from request parameters like {@code ?page=0&size=10&sort=firstName,asc}.
     * @return A {@link Page} of {@link EmployeeResponse} objects.
     */
    @GetMapping
    public Page<EmployeeResponse> getAll(Pageable pageable){
        log.info("Received request to get all employees with pageable: {}", pageable);
        Page<EmployeeResponse> responsePage = employeeService.getAll(pageable);
        log.info("Fetched {} employees on page {} of {}.",
                responsePage.getNumberOfElements(), responsePage.getNumber() + 1, responsePage.getTotalPages());
        return responsePage;
    }

    /**
     * Updates an existing employee identified by their ID.
     *
     * @param id The unique ID of the employee to update, extracted from the URL path.
     * @param request The {@link UpdateEmployeeRequest} object containing the updated employee data.
     * Validated using {@code @Valid}.
     * @return The updated {@link EmployeeResponse} object.
     * @throws com.poryvai.post.exception.NotFoundException if no employee is found with the given ID.
     */
    @PutMapping("/{id}")
    public EmployeeResponse update(@PathVariable  Long id, @Valid @RequestBody UpdateEmployeeRequest request){
        log.info("Received request to update employee with ID {}: {}", id, request);
        EmployeeResponse response = employeeService.update(id, request);
        log.info("Employee with ID: {} updated successfully.", response.getId());
        return response;
    }

    /**
     * Deletes an employee by its unique ID.
     *
     * @param id The unique ID of the employee to delete, extracted from the URL path.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        log.info("Received request to delete employee by ID: {}", id);
        employeeService.delete(id);
        log.info("Employee with ID: {} deleted successfully.", id);
    }
}
