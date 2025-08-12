package com.poryvai.post.dto;

import com.poryvai.post.model.EmployeePosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for representing an employee's information in API responses.
 * This DTO is used to send employee data to clients, typically after a successful retrieval operation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private EmployeePosition position;
    private PostOfficeDto postOffice;

}
