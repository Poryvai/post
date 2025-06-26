package com.poryvai.post.controller;

import com.poryvai.post.dto.CreateParcelRequest;
import com.poryvai.post.dto.ParcelSearchParams;
import com.poryvai.post.dto.ParcelStatistic;
import com.poryvai.post.dto.UpdateParcelStatusRequest;
import com.poryvai.post.model.Parcel;
import com.poryvai.post.model.ParcelStatus;
import com.poryvai.post.service.ParcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing parcel-related operations.
 * Provides endpoints for creating, retrieving, updating parcel statuses,
 * and generating statistics.
 */
@RestController
@RequestMapping("/parcels")
@RequiredArgsConstructor
public class ParcelController {

    private final ParcelService parcelService;

    /**
     * Retrieves a parcel by its unique tracking number.
     *
     * @param trackingNumber The unique tracking number of the parcel, extracted from the URL path.
     * @return The {@link Parcel} object matching the tracking number.
     * This method relies on {@link com.poryvai.post.controller.RestExceptionHandler}
     * to handle {@link com.poryvai.post.exception.NotFoundException}
     * if no parcel is found, returning an HTTP 404.
     */
    @GetMapping("/{trackingNumber}")
    public Parcel findByTrackingNumber(@PathVariable("trackingNumber") String trackingNumber) {
        return parcelService.getByTrackingNumber(trackingNumber);
    }

    /**
     * Retrieves a paginated list of parcels, allowing for dynamic filtering based on various criteria.
     *
     * @param params   {@link ParcelSearchParams} containing optional filters such as tracking number, sender, recipient,
     * weight/price ranges, and lists of statuses or delivery types.
     * These parameters are typically passed as request parameters (query string).
     * @param pageable {@link Pageable} object for pagination and sorting (e.g., page=0&size=10&sort=id,asc).
     * Automatically resolved by Spring from request parameters.
     * @return A {@link Page} of {@link Parcel} objects matching the search criteria.
     */
    @GetMapping
    public Page<Parcel> findAll(ParcelSearchParams params, Pageable pageable) {
        return parcelService.findAll(params, pageable);
    }

    /**
     * Generates and retrieves statistical data about parcels based on provided search criteria.
     * This endpoint aggregates data like total parcels, averages, and counts by status/delivery type.
     *
     * @param params {@link ParcelSearchParams} containing optional filters to scope the statistics.
     * These parameters are typically passed as request parameters (query string).
     * @return A {@link ParcelStatistic} object containing aggregated data.
     */
    @GetMapping("/statistic")
    public ParcelStatistic findAll(ParcelSearchParams params) {
        return parcelService.buildStatistic(params);
    }

    /**
     * Creates a new parcel record in the system.
     * The parcel's tracking number will be automatically generated, and its price calculated
     * based on the provided weight and delivery type.
     *
     * @param request The {@link CreateParcelRequest} object containing details for the new parcel.
     * This object is expected in the request body (JSON).
     * It should be valid according to its validation annotations (e.g., @NotBlank, @Positive).
     * @return The created {@link Parcel} object, including its generated tracking number and calculated price.
     * This method relies on {@link com.poryvai.post.controller.RestExceptionHandler}
     * to handle validation errors (HTTP 400).
     */
    @PostMapping
    public Parcel create(@RequestBody CreateParcelRequest request) {
        return parcelService.create(request);
    }

    /**
     * Updates the status of an existing parcel identified by its tracking number.
     *
     * @param trackingNumber The unique tracking number of the parcel to update, extracted from the URL path.
     * @param request        The {@link UpdateParcelStatusRequest} object containing the new {@link ParcelStatus}.
     * This object is expected in the request body (JSON).
     * @return The updated {@link Parcel} object with its new status.
     * This method relies on {@link com.poryvai.post.controller.RestExceptionHandler}
     * to handle {@link com.poryvai.post.exception.NotFoundException}
     * if no parcel is found, returning an HTTP 404.
     */
    @PatchMapping("/{trackingNumber}")
    public Parcel updateStatus(@PathVariable("trackingNumber") String trackingNumber,
                               @RequestBody UpdateParcelStatusRequest request) {
        return parcelService.updateStatus(trackingNumber, request.getStatus());
    }
}
