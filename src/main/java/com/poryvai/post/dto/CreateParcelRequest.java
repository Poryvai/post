package com.poryvai.post.dto;

import com.poryvai.post.model.DeliveryType;
import com.poryvai.post.model.ParcelDescription;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new parcel.
 * Contains all necessary information provided by the client to register a parcel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateParcelRequest {
    /**
     * The name or identifier of the sender. Must not be blank.
     */
    @NotBlank(message = "Sender cannot be blank")
    @Size(max = 128, message = "Sender name must be less than or equal to 128 characters")
    private String sender;

    /**
     * The name or identifier of the recipient. Must not be blank.
     */
    @NotBlank(message = "Recipient cannot be blank")
    @Size(max = 128, message = "Recipient name must be less than or equal to 128 characters")
    private String recipient;

    /**
     * The weight of the parcel in kilograms. Must be a positive value.
     */
    @NotNull(message = "Weight cannot be null")
    @DecimalMin(value = "0.01", message = "Weight must be greater than 0")
    private double weight;

    /**
     * The requested delivery type for the parcel. Can be null, defaulting to DEFAULT.
     */
    private DeliveryType deliveryType;

    /**
     * The description category of the parcel's contents (e.g., CLOTHES, BOOKS).
     * This field is required when creating a new parcel.
     */
    @NotNull(message = "Parcel description cannot be null")
    private ParcelDescription parcelDescription;

    /**
     * The ID of the post office where the parcel originated.
     * Must be a positive number and cannot be null.
     */
    @NotNull(message = "Origin post office ID cannot be null")
    @DecimalMin(value = "1", message = "Origin post office ID must be a positive number")
    private Long originPostOfficeId;

    /**
     * The ID of the post office where the parcel is destined to be delivered.
     * Must be a positive number and cannot be null.
     */
    @NotNull(message = "Destination post office ID cannot be null")
    @DecimalMin(value = "1", message = "Destination post office ID must be a positive number")
    private Long destinationPostOfficeId;
}
