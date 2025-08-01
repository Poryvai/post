package com.poryvai.post.dto;

import com.poryvai.post.model.Client;
import com.poryvai.post.model.DeliveryType;
import com.poryvai.post.model.ParcelDescription;
import com.poryvai.post.model.PostOffice;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
     * The unique ID of the {@link Client} who is the sender of the parcel.
     * Must be a positive number and cannot be null.
     */
    @NotNull(message = "Sender client ID cannot be null")
    @DecimalMin(value = "1", message = "Sender client ID must be a positive number")
    private Long senderClientId;

    /**
     * The unique ID of the {@link Client} who is the recipient of the parcel.
     * Must be a positive number and cannot be null.
     */
    @NotNull(message = "Recipient client ID cannot be null")
    @DecimalMin(value = "1", message = "Recipient client ID must be a positive number")
    private Long recipientClientId;

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
     * The ID of the {@link PostOffice} where the parcel originated.
     * Must be a positive number and cannot be null.
     */
    @NotNull(message = "Origin post office ID cannot be null")
    @DecimalMin(value = "1", message = "Origin post office ID must be a positive number")
    private Long originPostOfficeId;

    /**
     * The ID of the {@link PostOffice} where the parcel is destined to be delivered.
     * Must be a positive number and cannot be null.
     */
    @NotNull(message = "Destination post office ID cannot be null")
    @DecimalMin(value = "1", message = "Destination post office ID must be a positive number")
    private Long destinationPostOfficeId;
}
