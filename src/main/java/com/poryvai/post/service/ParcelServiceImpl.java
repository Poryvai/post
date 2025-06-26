package com.poryvai.post.service;

import com.poryvai.post.common.ComponentForProduceCycleDependency;
import com.poryvai.post.dto.CreateParcelRequest;
import com.poryvai.post.dto.ParcelSearchParams;
import com.poryvai.post.dto.ParcelStatistic;
import com.poryvai.post.exception.NotFoundException;
import com.poryvai.post.model.DeliveryType;
import com.poryvai.post.model.Parcel;
import com.poryvai.post.model.ParcelStatus;
import com.poryvai.post.repository.ParcelRepository;
import com.poryvai.post.service.parcel.price.PriceCalculator;
import com.poryvai.post.util.CommonGenerator;
import com.poryvai.post.util.ParcelSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ParcelService} interface.
 * Handles the core business logic for parcel management, including creation, retrieval,
 * status updates, and statistical analysis. This service also demonstrates handling
 * potential circular dependencies.
 */
@Service
@RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {

    private final ParcelRepository parcelRepository;
    private final CommonGenerator generator;
    private final List<PriceCalculator> priceCalculators; // Injected list of price calculation strategies
    private final ComponentForProduceCycleDependency componentForProduceCycleDependency;

    /**
     * Retrieves a parcel by its unique tracking number.
     *
     * @param trackingNumber The unique tracking number (String representation) used to identify the parcel.
     * @return The {@link Parcel} object corresponding to the given tracking number.
     * @throws NotFoundException if no parcel with the specified tracking number is found.
     */
    @Override
    public Parcel getByTrackingNumber(String trackingNumber) {
        return parcelRepository.findByTrackingNumber(trackingNumber).orElseThrow(
                () -> new NotFoundException("Parcel with tracking number " + trackingNumber + " not found"));
    }

    /**
     * Retrieves a paginated and filtered list of parcels.
     * This method leverages JPA Specifications to build dynamic queries based on
     * various search parameters provided in {@link ParcelSearchParams}.
     *
     * @param params   An object containing optional search criteria (e.g., sender, recipient, status, weight/price ranges).
     * @param pageable An object defining pagination (page number, size) and sorting options.
     * @return A {@link Page} of {@link Parcel} objects that match the specified criteria.
     */
    @Override
    public Page<Parcel> findAll(ParcelSearchParams params, Pageable pageable) {
        // Dynamic search based on provided parameters using JPA Specifications.
        return parcelRepository.findAll(ParcelSpecifications.withDynamicQuery(params), pageable);
    }

    /**
     * Builds and returns a comprehensive statistical summary of parcels.
     * The statistics are calculated based on a filtered list of parcels, determined by
     * the provided {@link ParcelSearchParams}. This includes total counts, average weight and price,
     * counts by status and delivery type, and identification of extreme parcels (most/least expensive/heavy).
     * Ensures all enum values for status and delivery type are present in the counts, even if zero.
     *
     * @param params An object containing optional filters to scope the statistics calculation.
     * @return A {@link ParcelStatistic} object containing the aggregated statistical data.
     */
    @Override
    public ParcelStatistic buildStatistic(ParcelSearchParams params) {
        // Fetch all parcels matching the dynamic filters for statistics calculation.
        List<Parcel> parcels = parcelRepository.findAll(ParcelSpecifications.withDynamicQuery(params));

        long totalParcels = parcels.size();
        double totalWeight = 0;
        double totalPrice = 0;

        // Initialize maps with zero counts for all enum values to ensure complete statistics.
        Map<ParcelStatus, Long> parcelsCountByStatus = Arrays.stream(ParcelStatus.values())
                .collect(Collectors.toMap(status -> status, status -> 0L));

        Map<DeliveryType, Long> parcelsCountByDeliveryType = Arrays.stream(DeliveryType.values())
                .collect(Collectors.toMap(type -> type, type -> 0L));

        // Variables to track min/max parcels by price and weight.
        Parcel mostExpensiveParcel = null;
        Parcel cheapestParcel = null;
        Parcel heaviestParcel = null;
        Parcel lightestParcel = null;

        if (!parcels.isEmpty()) {
            for (Parcel parcel : parcels) {
                totalWeight += parcel.getWeight();
                totalPrice += parcel.getPrice();

                // Aggregate counts for status and delivery type.
                parcelsCountByStatus.merge(parcel.getStatus(), 1L, Long::sum);
                parcelsCountByDeliveryType.merge(parcel.getDeliveryType(), 1L, Long::sum);

                // Update min/max parcel details.
                if (mostExpensiveParcel == null || parcel.getPrice() > mostExpensiveParcel.getPrice()) {
                    mostExpensiveParcel = parcel;
                }
                if (cheapestParcel == null || parcel.getPrice() < cheapestParcel.getPrice()) {
                    cheapestParcel = parcel;
                }
                if (heaviestParcel == null || parcel.getWeight() > heaviestParcel.getWeight()) {
                    heaviestParcel = parcel;
                }
                if (lightestParcel == null || parcel.getWeight() < lightestParcel.getWeight()) {
                    lightestParcel = parcel;
                }
            }
        }

        // Calculating average values.
        double averageWeight = totalParcels > 0 ? totalWeight / totalParcels : 0;
        double averagePrice = totalParcels > 0 ? totalPrice / totalParcels : 0;

        return ParcelStatistic.builder()
                .totalParcels(totalParcels)
                .averageWeight(averageWeight)
                .averagePrice(averagePrice)
                .parcelsCountByStatus(parcelsCountByStatus)
                .parcelsCountByDeliveryType(parcelsCountByDeliveryType)
                .mostExpensiveParcel(mostExpensiveParcel)
                .cheapestParcel(cheapestParcel)
                .heaviestParcel(heaviestParcel)
                .lightestParcel(lightestParcel)
                .build();
    }

    /**
     * Creates a new parcel record based on the provided request.
     * This involves generating a unique tracking number, setting the initial status to {@link ParcelStatus#CREATED},
     * calculating the delivery price, and persisting the parcel.
     * If {@code deliveryType} is not specified in the request, it defaults to {@link DeliveryType#DEFAULT}.
     *
     * @param request The {@link CreateParcelRequest} containing details for the new parcel.
     * @return The newly created and persisted {@link Parcel} object.
     * @throws NotFoundException if a specific delivery type is requested but no corresponding
     * price calculator is found.
     */
    @Override
    public Parcel create(CreateParcelRequest request) {
        Parcel parcel = new Parcel();
        parcel.setTrackingNumber(generator.uuid());
        parcel.setSender(request.getSender());
        parcel.setRecipient(request.getRecipient());
        parcel.setWeight(request.getWeight());
        parcel.setStatus(ParcelStatus.CREATED);

        // Logic to determine the actual DeliveryType, defaulting to DEFAULT if null.
        DeliveryType initialDeliveryType = request.getDeliveryType();
        final DeliveryType actualDeliveryType;

        if (initialDeliveryType == null) {
            actualDeliveryType = DeliveryType.DEFAULT;
        } else {
            actualDeliveryType = initialDeliveryType;
        }

        parcel.setDeliveryType(actualDeliveryType);

        // Find and use the appropriate price calculator from the injected list.
        Optional<PriceCalculator> calculatorOptional = priceCalculators.stream()
                .filter(c -> c.getDeliveryType() == actualDeliveryType)
                .findFirst();

        // Calculate and set the parcel price using the selected calculator
        if (calculatorOptional.isPresent()) {
            parcel.setPrice(calculatorOptional.get().calculatePrice(request));
        } else {
            throw new NotFoundException("No price calculator found for delivery type: " + actualDeliveryType);
        }
        return parcelRepository.save(parcel);
    }

    /**
     * Updates the status of an existing parcel identified by its tracking number.
     * After updating the status, the parcel is saved and then re-retrieved from the database.
     *
     * @param trackingNumber The unique tracking number of the parcel to be updated.
     * @param status         The new {@link ParcelStatus} to set for the parcel.
     * @return The updated {@link Parcel} object, re-fetched from the database.
     * @throws NotFoundException if no parcel with the specified tracking number is found.
     */
    @Override
    public Parcel updateStatus(String trackingNumber, ParcelStatus status) {
        Parcel parcel = getByTrackingNumber(trackingNumber);
        parcel.setStatus(status);
        save(parcel);
        return getByTrackingNumber(trackingNumber);
    }

    /**
     * Saves a {@link Parcel} entity to the database.
     * This method is transactional, ensuring that the save operation is atomic.
     *
     * @param parcel The {@link Parcel} object to be saved.
     * @return The saved {@link Parcel} entity.
     */
    @Transactional
    public Parcel save(Parcel parcel) {
        return parcelRepository.save(parcel);
    }

    /**
     * A utility method primarily used to demonstrate dependency injection and resolve
     * potential circular dependency issues by being part of the Spring context.
     * It calls a method on an injected component that has no side effects.
     */
    public void methodForTrick() {
        componentForProduceCycleDependency.doNothing();
    }
}
