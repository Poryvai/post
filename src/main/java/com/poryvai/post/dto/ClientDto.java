package com.poryvai.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing client information in API responses,
 * typically as a nested object within other DTOs (e.g., ParcelResponse).
 * Includes essential client details without sensitive information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
