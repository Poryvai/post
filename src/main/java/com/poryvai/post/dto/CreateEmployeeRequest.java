package com.poryvai.post.dto;

import com.poryvai.post.model.EmployeePosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for requests to create a new employee.
 * Contains fields required for employee creation, along with validation constraints.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {

    /**
     * The first name of the employee.
     * Must not be blank.
     */
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 64, message = "First name must be less than or equal to 64 characters")
    private String firstName;

    /**
     * The last name of the employee.
     * Must not be blank.
     */
    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 64, message = "Last name must be less than or equal to 64 characters")
    private String lastName;

    /**
     * The position or role of the employee.
     * Must not be null and must be a valid {@link EmployeePosition} enum value.
     */
    @NotNull(message = "Position cannot be null")
    private EmployeePosition position;
}
