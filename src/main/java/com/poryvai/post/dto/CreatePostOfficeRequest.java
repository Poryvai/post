package com.poryvai.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new {@link com.poryvai.post.model.PostOffice}.
 * This object carries the data required to initialize a new PostOffice record.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostOfficeRequest {

    @NotBlank(message = "Post office name cannot be blank")
    @Size(max = 255, message = "Post office name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 80, message = "City cannot exceed 80 characters")
    private String city;

    @NotBlank(message = "PostCode cannot be blank")
    @Size(max = 10, message = "PostCode cannot exceed 10 characters")
    private String postcode;

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 255, message = "Street cannot exceed 255 characters")
    private String street;
}
