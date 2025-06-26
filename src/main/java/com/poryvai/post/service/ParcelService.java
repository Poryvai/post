package com.poryvai.post.service;

import com.poryvai.post.dto.CreateParcelRequest;
import com.poryvai.post.model.Parcel;
import com.poryvai.post.dto.ParcelSearchParams;
import com.poryvai.post.dto.ParcelStatistic;
import com.poryvai.post.model.ParcelStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing parcel-related business logic.
 * Defines the contract for operations such as creating, retrieving,
 * updating parcel statuses, and generating statistics.
 */
public interface ParcelService {

    /**
     * Retrieves a parcel by its unique tracking number.
     *
     * @param trackingNumber The unique tracking number used to identify the parcel.
     * @return The {@link Parcel} object corresponding to the given tracking number.
     * @throws com.poryvai.post.exception.NotFoundException if no parcel with the specified tracking number is found.
     * This exception is typically handled by a global exception handler (e.g., RestExceptionHandler).
     */
    Parcel getByTrackingNumber(String trackingNumber);

    /**
     * Retrieves a paginated and filtered list of parcels.
     * This method supports dynamic queries based on various search parameters.
     *
     * @param params   An object containing optional search criteria (e.g., sender, recipient, status, parcel descriptions).
     * @param pageable An object defining pagination (page number, size) and sorting options.
     * @return A {@link Page} of {@link Parcel} objects that match the criteria.
     */
    Page<Parcel> findAll(ParcelSearchParams params, Pageable pageable);

    /**
     * Builds and returns a statistical summary of parcels based on specified search parameters.
     * This includes aggregate data such as total counts, averages, and counts by different categories,
     * and counts by parcel description types.
     *
     * @param params An object containing optional filters to scope the statistics calculation.
     * @return A {@link ParcelStatistic} object containing the aggregated statistical data.
     */
    ParcelStatistic buildStatistic(ParcelSearchParams params);

    /**
     * Creates a new parcel based on the provided request data.
     * This method handles the generation of tracking numbers, price calculation,
     * and initial status setting, and assigning the parcel's description.
     *
     * @param request The {@link CreateParcelRequest} containing all necessary data for parcel creation.
     * @return The newly created {@link Parcel} object, persisted in the database.
     * @throws IllegalArgumentException if an invalid delivery type is provided or a price calculator is not found.
     */
    Parcel create(CreateParcelRequest request);

    /**
     * Updates the status of an existing parcel.
     * The parcel is identified by its tracking number, and its status is updated to the new provided value.
     *
     * @param trackingNumber The unique tracking number of the parcel to be updated.
     * @param status         The new {@link ParcelStatus} to set for the parcel.
     * @return The updated {@link Parcel} object.
     * @throws com.poryvai.post.exception.NotFoundException if no parcel with the specified tracking number is found.
     * This exception is typically handled by a global exception handler (e.g., RestExceptionHandler).
     */
    Parcel updateStatus(String trackingNumber, ParcelStatus status);
}
