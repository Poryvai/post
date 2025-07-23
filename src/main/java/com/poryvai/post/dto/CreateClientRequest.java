package com.poryvai.post.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating a Client entity.
 * Includes validation constraints for data integrity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequest {

    /**
     * The first name of the client.
     * Must not be blank.
     */
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 64, message = "First name must be less than or equal to 64 characters")
    private String firstName;

    /**
     * The last name of the client.
     * Must not be blank.
     */
    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 64, message = "Last name must be less than or equal to 64 characters")
    private String lastName;

    /**
     * The email of the client.
     * Must not be blank.
     */
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must be less than or equal to 255 characters")
    private String email;

    /**
     * The phone of the client.
     * Must not be blank.
     */
    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid (e.g., +380991234567 or 0991234567)")
    @Size(max = 20, message = "Phone number must be less than or equal to 20 characters")
    private String phone;
}
