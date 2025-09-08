package com.poryvai.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing employee information.
 * This DTO is used to transfer employee data and is not tied to any persistence context,
 * making it safe for serialization.
 */

/**
 * DTO for representing Post Office information in API responses.
 * Provides a simplified view of the PostOffice entity,
 * suitable for inclusion in other response DTOs (e.g., ParcelResponse, EmployeeResponse).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;
}
