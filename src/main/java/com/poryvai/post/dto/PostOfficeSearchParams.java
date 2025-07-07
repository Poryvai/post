package com.poryvai.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for searching PostOffice entities with dynamic criteria.
 * Allows filtering by name, city, postcode, and street.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostOfficeSearchParams {

    /**
     * Name of the post office for case-insensitive search.
     */
    private String name;

    /**
     * City name where the post office is located, for case-insensitive search.
     */
    private String city;

    /**
     * Postcode of the post office for case-insensitive search.
     */
    private String postcode;

    /**
     * Street address of the post office for case-insensitive search.
     */
    private String street;
}
