package com.poryvai.post.dto;

import com.poryvai.post.model.DeliveryType;
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
    private String sender;

    /**
     * The name or identifier of the recipient. Must not be blank.
     */
    private String recipient;

    /**
     * The weight of the parcel in kilograms. Must be a positive value.
     */
    private double weight;

    /**
     * The requested delivery type for the parcel. Can be null, defaulting to DEFAULT.
     */
    private DeliveryType deliveryType;
}
