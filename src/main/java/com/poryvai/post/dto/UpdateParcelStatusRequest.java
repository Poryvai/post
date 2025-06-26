package com.poryvai.post.dto;

import com.poryvai.post.model.ParcelStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) used for requests to update the status of an existing parcel.
 * It encapsulates the new {@link ParcelStatus} to be applied to a parcel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateParcelStatusRequest {

    /**
     * The new status to be set for the parcel.
     * This field typically represents the target state for a parcel's lifecycle (e.g., DELIVERED, RETURNED).
     */
    private ParcelStatus status;
}
