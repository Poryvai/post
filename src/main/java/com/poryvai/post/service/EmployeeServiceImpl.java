package com.poryvai.post.service;

import com.poryvai.post.dto.CreateEmployeeRequest;
import com.poryvai.post.dto.EmployeeResponse;
import com.poryvai.post.dto.PostOfficeDto;
import com.poryvai.post.dto.UpdateEmployeeRequest;
import com.poryvai.post.exception.NotFoundException;
import com.poryvai.post.model.Employee;
import com.poryvai.post.model.PostOffice;
import com.poryvai.post.repository.EmployeeRepository;
import com.poryvai.post.repository.PostOfficeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link EmployeeService} interface.
 * Provides the business logic for managing employee entities,
 * interacting with the {@link EmployeeRepository}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final PostOfficeRepository postOfficeRepository;

    /**
     * Creates a new employee based on the provided request.
     *
     * @param request The {@link CreateEmployeeRequest} containing all necessary data for employee creation.
     * @return The newly created and persisted {@link EmployeeResponse} DTO representing the newly created employee.
     */
    @Override
    @Transactional
    public EmployeeResponse create(CreateEmployeeRequest request) {
        log.info("Creating new employee with first name: {}, last name: {}", request.getFirstName(), request.getLastName());

        PostOffice postOffice = postOfficeRepository.findById(request.getPostOfficeId())
                .orElseThrow(() -> new NotFoundException("PostOffice not found with ID: " + request.getPostOfficeId()));

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .position(request.getPosition())
                .postOffice(postOffice)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());
        return mapEmployeeToResponse(savedEmployee);
    }

    /**
     * Retrieves an employee by its unique ID.
     *
     * @param id The unique ID of the employee.
     * @return The {@link EmployeeResponse} DTO corresponding to the given ID.
     * @throws NotFoundException if no employee with the specified ID is found.
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getById(Long id) {
        log.info("Fetching employee by ID: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + id));
        log.info("Employee with ID: {} fetched successfully.", id);
        return mapEmployeeToResponse(employee);
    }

    /**
     * Retrieves a paginated list of all employees.
     * This method supports pagination and sorting
     *
     * @param pageable An object defining pagination (page number, size) and sorting options.
     * @return A {@link Page} of {@link EmployeeResponse} DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAll(Pageable pageable) {
        log.info("Fetching all employees with pageable: {}", pageable);
        Page<Employee> employeesPage = employeeRepository.findAll(pageable);
        Page<EmployeeResponse> responsePage = employeesPage.map(this::mapEmployeeToResponse);
        log.info("Fetched {} employees on page {} of {}.",
                responsePage.getNumberOfElements(), responsePage.getNumber() + 1, responsePage.getTotalPages());
        return responsePage;
    }


    /**
     * This implementation delegates the query to the {@link EmployeeRepository}
     * using the {@code findByPostOfficeId} method, and then maps the resulting
     * {@link Page} of {@link Employee} entities to a {@link Page} of {@link EmployeeResponse} DTOs.
     *
     * @param postOfficeId The unique ID of the post office to search for.
     * @param pageable     An object defining pagination (page number, size) and sorting options.
     * @return A {@link Page} of {@link EmployeeResponse} DTOs containing employees from the specified post office.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getEmployeesByPostOffice(Long postOfficeId, Pageable pageable) {
        log.info("Fetching employees for post office with ID: {} with pageable: {}", postOfficeId, pageable);
        Page<Employee> employeesPage = employeeRepository.findByPostOfficeId(postOfficeId, pageable);
        Page<EmployeeResponse> responsePage = employeesPage.map(this::mapEmployeeToResponse);
        log.info("Fetched {} employees on page {} of {} for post office ID: {}",
                responsePage.getNumberOfElements(), responsePage.getNumber() + 1, responsePage.getTotalPages(), postOfficeId);
        return responsePage;
    }

    /**
     * Updates an existing employee identified by its ID.
     *
     * @param id      The unique ID of the employee to be updated.
     * @param request The {@link UpdateEmployeeRequest} containing the updated employee data.
     * @return The updated {@link EmployeeResponse} DTO representing the updated employee.
     * @throws NotFoundException        if no employee with the specified ID is found.
     * @throws IllegalArgumentException if validation fails or business rules are violated.
     */
    @Override
    @Transactional
    public EmployeeResponse update(Long id, UpdateEmployeeRequest request) {
        log.info("Updating employee with ID {}: {} {}", id, request.getFirstName(), request.getLastName());
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee with ID " + id + " not found"));

        existingEmployee.setFirstName(request.getFirstName());
        existingEmployee.setLastName(request.getLastName());
        existingEmployee.setPosition(request.getPosition());

        if (request.getPostOfficeId() != null && !request.getPostOfficeId().equals(existingEmployee.getPostOffice().getId())) {
            PostOffice newPostOffice = postOfficeRepository.findById(request.getPostOfficeId())
                    .orElseThrow(() -> new NotFoundException("PostOffice not found with ID: " + request.getPostOfficeId()));
            existingEmployee.setPostOffice(newPostOffice);
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        log.info("Employee with ID: {} updated successfully.", updatedEmployee.getId());
        return mapEmployeeToResponse(updatedEmployee);
    }

    /**
     * Deletes an employee by its unique ID.
     *
     * @param id The unique ID of the employee to be deleted.
     * @throws NotFoundException if no employee with the specified ID is found.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting employee by ID: {}", id);
        if (!employeeRepository.existsById(id)) {
            throw new NotFoundException("Employee with ID: " + id + " not found");
        }
        employeeRepository.deleteById(id);
        log.info("Employee with ID: {} deleted successfully.", id);
    }

    /**
     * Helper method to map an Employee entity to an EmployeeResponse DTO.
     *
     * @param employee The Employee entity to map.
     * @return An EmployeeResponse DTO.
     */
    private EmployeeResponse mapEmployeeToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .position(employee.getPosition())
                .postOffice(mapPostOfficeToDto(employee.getPostOffice()))
                .build();
    }

    /**
     * Helper method to map a PostOffice entity to a PostOfficeDto.
     * @param postOffice The PostOffice entity to map.
     * @return A PostOfficeDto.
     */
    private PostOfficeDto mapPostOfficeToDto(PostOffice postOffice) {
        if (postOffice == null) {
            return null;
        }
        return PostOfficeDto.builder()
                .id(postOffice.getId())
                .name(postOffice.getName())
                .city(postOffice.getCity())
                .postcode(postOffice.getPostcode())
                .street(postOffice.getStreet())
                .build();
    }
}

