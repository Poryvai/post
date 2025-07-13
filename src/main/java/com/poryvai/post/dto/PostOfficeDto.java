package com.poryvai.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing Post Office information in API responses.
 * Provides a simplified view of the PostOffice entity,
 * suitable for inclusion in other response DTOs (e.g., ParcelResponse, EmployeeResponse).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostOfficeDto {
    private Long id;
    private String name;
    private String city;
    private String postcode;
    private String street;
}
