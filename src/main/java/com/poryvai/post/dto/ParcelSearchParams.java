package com.poryvai.post.dto;

import com.poryvai.post.model.Client;
import com.poryvai.post.model.DeliveryType;
import com.poryvai.post.model.ParcelDescription;
import com.poryvai.post.model.ParcelStatus;
import com.poryvai.post.model.PostOffice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for specifying search parameters when querying parcels.
 * Allows for dynamic filtering based on various parcel attributes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelSearchParams {

    /**
     * The exact tracking number of the parcel to search for.
     */
    private String trackingNumber;

    /**
     * The ID of the sender {@link Client} to filter by.
     */
    private Long senderClientId;;

    /**
     * The ID of the recipient {@link Client} to filter by.
     */
    private Long recipientClientId;

    /**
     * Minimum weight of the parcel to filter by (inclusive).
     */
    private Double fromWeight;

    /**
     * Maximum weight of the parcel to filter by (inclusive).
     */
    private Double toWeight;

    /**
     * Minimum price of the parcel to filter by (inclusive).
     */
    private Double fromPrice;

    /**
     * Maximum price of the parcel to filter by (inclusive).
     */
    private Double toPrice;

    /**
     * A list of parcel statuses to include in the search.
     * Parcels with any of the specified statuses will be returned.
     */
    @Builder.Default
    private List<ParcelStatus> statuses = new ArrayList<>();

    /**
     * A list of delivery types to include in the search.
     * Parcels with any of the specified delivery types will be returned.
     */
    @Builder.Default
    private List<DeliveryType> deliveryTypes = new ArrayList<>();

    /**
     * A list of parcel descriptions to include in the search.
     * Parcels with any of the specified delivery types will be returned.
     */
    @Builder.Default
    private List<ParcelDescription> parcelDescriptions = new ArrayList<>();

    /**
     * The ID of the origin {@link PostOffice} to filter by.
     */
    private Long originPostOfficeId;

    /**
     * The ID of the destination {@link PostOffice} to filter by.
     */
    private Long destinationPostOfficeId;
}
