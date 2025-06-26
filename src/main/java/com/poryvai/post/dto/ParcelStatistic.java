package com.poryvai.post.dto;

import com.poryvai.post.model.DeliveryType;
import com.poryvai.post.model.Parcel;
import com.poryvai.post.model.ParcelStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.Map;

/**
 * DTO representing a comprehensive statistical summary of parcels.
 * This includes counts, averages, and extreme values based on applied filters.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ParcelStatistic {

    /**
     * The total number of parcels matching the search criteria.
     */
    private long totalParcels;

    /**
     * The average weight of all parcels matching the search criteria.
     */
    private double averageWeight;

    /**
     * The average price of all parcels matching the search criteria.
     */
    private double averagePrice;

    /**
     * A map showing the count of parcels for each status type (e.g., CREATED, IN_TRANSIT, DELIVERED).
     */
    private Map<ParcelStatus, Long> parcelsCountByStatus;

    /**
     * A map showing the count of parcels for each delivery type (e.g., DEFAULT, EXPRESS, ECONOM).
     */
    private Map<DeliveryType, Long> parcelsCountByDeliveryType;

    /**
     * The parcel with the highest price among those matching the search criteria.
     * Can be null if no parcels are found.
     */
    private Parcel mostExpensiveParcel;

    /**
     * The parcel with the lowest price among those matching the search criteria.
     * Can be null if no parcels are found.
     */
    private Parcel cheapestParcel;

    /**
     * The parcel with the highest weight among those matching the search criteria.
     * Can be null if no parcels are found.
     */
    private Parcel heaviestParcel;

    /**
     * The parcel with the lowest weight among those matching the search criteria.
     * Can be null if no parcels are found.
     */
    private Parcel lightestParcel;
}
