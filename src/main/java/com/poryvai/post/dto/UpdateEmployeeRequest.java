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
 * Data Transfer Object (DTO) for requests to update an existing employee.
 * Contains fields that can be modified for an employee, along with validation constraints.
 * This DTO assumes a PUT operation, requiring all fields for a complete replacement.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequest {

    /**
     * The first name of the employee.
     * Must not be blank.
     */
    @NotBlank(message = "First name cannot be blank for update")
    @Size(max = 64, message = "First name must be less than or equal to 64 characters for update")
    private String firstName;

    /**
     * The last name of the employee.
     * Must not be blank.
     */
    @NotBlank(message = "Last name cannot be blank for update")
    @Size(max = 64, message = "Last name must be less than or equal to 64 characters for update")
    private String lastName;

    /**
     * The position or role of the employee.
     * Must not be null and must be a valid {@link EmployeePosition} enum value.
     */
    @NotNull(message = "Position cannot be null for update")
    private EmployeePosition position;

    /**
     * The ID of the PostOffice where the employee now works.
     * Must not be null.
     */
    @NotNull(message = "Post office ID cannot be null for update")
    private Long postOfficeId;
}
