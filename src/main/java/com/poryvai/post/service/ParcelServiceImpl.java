package com.poryvai.post.service;

import com.poryvai.post.common.ComponentForProduceCycleDependency;
import com.poryvai.post.dto.*;
import com.poryvai.post.exception.NotFoundException;
import com.poryvai.post.model.*;
import com.poryvai.post.repository.*;
import com.poryvai.post.service.parcel.price.PriceCalculator;
import com.poryvai.post.util.CommonGenerator;
import com.poryvai.post.util.ParcelSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
@Slf4j
public class ParcelServiceImpl implements ParcelService {

    private final ParcelRepository parcelRepository;
    private final PostOfficeRepository postOfficeRepository;
    private final CommonGenerator generator;
    private final List<PriceCalculator> priceCalculators; // Injected list of price calculation strategies
    private final ComponentForProduceCycleDependency componentForProduceCycleDependency;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ParcelLogEntryRepository parcelLogEntryRepository;

    /**
     * Retrieves a parcel by its unique tracking number.
     *
     * @param trackingNumber The unique tracking number (String representation) used to identify the parcel.
     * @return The {@link ParcelResponse} object corresponding to the given tracking number.
     * @throws NotFoundException if no parcel with the specified tracking number is found.
     */
    @Override
    @Transactional(readOnly = true)
    public ParcelResponse getByTrackingNumber(String trackingNumber) {
        log.info("Retrieving parcel by tracking number: {}", trackingNumber);
        Parcel parcel = parcelRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new NotFoundException("Parcel with tracking number " + trackingNumber + " not found"));

        return mapParcelToResponse(parcel);
    }

    /**
     * Retrieves a paginated and filtered list of parcels.
     * This method leverages JPA Specifications to build dynamic queries based on
     * various search parameters provided in {@link ParcelSearchParams}.
     *
     * @param params   An object containing optional search criteria (e.g., sender, recipient, status, weight/price ranges).
     * @param pageable An object defining pagination (page number, size) and sorting options.
     * @return A {@link Page} of {@link ParcelResponse} objects that match the specified criteria.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ParcelResponse> findAll(ParcelSearchParams params, Pageable pageable) {
        log.info("Fetching all parcels with params: {} and pageable: {}", params, pageable);
        Page<Parcel> parcelPage = parcelRepository.findAll(ParcelSpecifications.withDynamicQuery(params), pageable);

        return parcelPage.map(this::mapParcelToResponse);
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
    @Transactional(readOnly = true)
    public ParcelStatistic buildStatistic(ParcelSearchParams params) {
        log.info("Building statistics for parcels with params: {}", params);

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

        Map<ParcelDescription, Long> parcelsCountByDescription = Arrays.stream(ParcelDescription.values())
                .collect(Collectors.toMap(description -> description, description -> 0L));

        // Variables to track min/max parcels by price and weight.
        Parcel mostExpensiveParcel = null;
        Parcel cheapestParcel = null;
        Parcel heaviestParcel = null;
        Parcel lightestParcel = null;

        if (!parcels.isEmpty()) {
            for (Parcel parcel : parcels) {
                totalWeight += parcel.getWeight();
                totalPrice += parcel.getPrice();

                // Aggregate counts for status, delivery type, and parcel description
                parcelsCountByStatus.merge(parcel.getStatus(), 1L, Long::sum);
                parcelsCountByDeliveryType.merge(parcel.getDeliveryType(), 1L, Long::sum);
                parcelsCountByDescription.merge(parcel.getParcelDescription(), 1L, Long::sum);

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
                .parcelsCountByDescription(parcelsCountByDescription)
                .mostExpensiveParcel(mostExpensiveParcel != null ? mapParcelToResponse(mostExpensiveParcel) : null)
                .cheapestParcel(cheapestParcel != null ? mapParcelToResponse(cheapestParcel) : null)
                .heaviestParcel(heaviestParcel != null ? mapParcelToResponse(heaviestParcel) : null)
                .lightestParcel(lightestParcel != null ? mapParcelToResponse(lightestParcel) : null)
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
    @Transactional
    public Parcel create(CreateParcelRequest request) {
        log.info("Creating new parcel for sender: {}, recipient: {} from PostOffice ID {} to PostOffice ID {}",
                request.getSenderClientId(), request.getRecipientClientId(), request.getOriginPostOfficeId(), request.getDestinationPostOfficeId());

        PostOffice originPostOffice = postOfficeRepository.findById(request.getOriginPostOfficeId())
                .orElseThrow(() -> new NotFoundException("Origin Post Office with ID " + request.getOriginPostOfficeId() + " not found"));

        PostOffice destinationPostOffice = postOfficeRepository.findById(request.getDestinationPostOfficeId())
                .orElseThrow(() -> new NotFoundException("Destination Post Office with ID " + request.getDestinationPostOfficeId() + " not found"));

        Client senderClient = clientRepository.findById(request.getSenderClientId())
                .orElseThrow(() -> new NotFoundException("Sender Client with ID " + request.getSenderClientId() + " not found"));

        Client recipientClient = clientRepository.findById(request.getRecipientClientId())
                .orElseThrow(() -> new NotFoundException("Recipient Client with ID " + request.getRecipientClientId() + " not found"));

        // Logic to determine the actual DeliveryType, defaulting to DEFAULT if null.
        DeliveryType initialDeliveryType = request.getDeliveryType();
        final DeliveryType actualDeliveryType = Optional.ofNullable(initialDeliveryType)
                .orElse(DeliveryType.DEFAULT);

        // Find and use the appropriate price calculator from the injected list.
        PriceCalculator calculator = priceCalculators.stream()
                .filter(c -> c.getDeliveryType() == actualDeliveryType)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No price calculator found for delivery type: " + actualDeliveryType));



        Parcel parcel = Parcel.builder()
                .trackingNumber(generator.uuid())
                .senderClient(senderClient)
                .recipientClient(recipientClient)
                .weight(request.getWeight())
                .status(ParcelStatus.CREATED)
                .originPostOffice(originPostOffice)
                .destinationPostOffice(destinationPostOffice)
                .deliveryType(actualDeliveryType)
                .parcelDescription(request.getParcelDescription())
                .price(calculator.calculatePrice(request))
                .build();

        Parcel savedParcel = parcelRepository.save(parcel);
        log.info("Parcel created successfully with tracking number: {}", savedParcel.getTrackingNumber());

        logParcelAction(savedParcel, ParcelLogEntryActionType.RECEIVED, EmployeePosition.CLERK, originPostOffice.getId());
        return savedParcel;
    }

    /**
     * Updates the status of an existing parcel identified by its tracking number.
     * After updating the status, the parcel is saved and then re-retrieved from the database.
     *
     * @param trackingNumber The unique tracking number of the parcel to be updated.
     * @param status         The new {@link ParcelStatus} to set for the parcel.
     * @return The updated {@link ParcelResponse} object, representing the fetched from the database.
     * @throws NotFoundException if no parcel with the specified tracking number is found.
     */
    @Override
    @Transactional
    public ParcelResponse updateStatus(String trackingNumber, ParcelStatus status) {
        log.info("Updating status for parcel with tracking number {} to {}", trackingNumber, status);
        Parcel parcel = parcelRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new NotFoundException("Parcel with tracking number " + trackingNumber + " not found"));

        parcel.setStatus(status);

        Parcel updatedParcel = parcelRepository.save(parcel);

        if (status == ParcelStatus.IN_TRANSIT) {
            logParcelAction(updatedParcel, ParcelLogEntryActionType.SENT, EmployeePosition.CLERK, updatedParcel.getOriginPostOffice().getId());
        } else if(status == ParcelStatus.DELIVERED) {
            logParcelAction(updatedParcel, ParcelLogEntryActionType.DELIVERED, EmployeePosition.CLERK, updatedParcel.getDestinationPostOffice().getId());
        }

        return mapParcelToResponse(updatedParcel);
    }

    /**
     * Initiates the process of sending a parcel from its current post office.
     * This method validates the parcel's status, updates it to {@link ParcelStatus#IN_TRANSIT}
     * if necessary, and logs a 'SENT' event.
     *
     * @param trackingNumber The unique tracking number of the parcel to be sent.
     * @return The updated {@link ParcelResponse} object.
     * @throws NotFoundException if the parcel with the specified tracking number is not found.
     * @throws IllegalArgumentException if the parcel is not in a valid status for sending.
     */
    @Override
    @Transactional
    public ParcelResponse sendParcel(String trackingNumber) {
        log.info("Sending parcel with tracking number: {}", trackingNumber);
        Parcel parcel = parcelRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new NotFoundException("Parcel with tracking number " + trackingNumber + " not found"));

        if (parcel.getStatus() != ParcelStatus.CREATED && parcel.getStatus() != ParcelStatus.IN_TRANSIT) {
            throw new IllegalArgumentException("Parcel with tracking number " + trackingNumber + " cannot be sent from its current status: " + parcel.getStatus());
        }

        // Updates the parcel's status to IN_TRANSIT, if it's not already in transit
        if (parcel.getStatus() == ParcelStatus.CREATED) {
            parcel.setStatus(ParcelStatus.IN_TRANSIT);
        }

        Parcel updatedParcel = parcelRepository.save(parcel);

        logParcelAction(updatedParcel, ParcelLogEntryActionType.SENT, EmployeePosition.CLERK, updatedParcel.getOriginPostOffice().getId());

        return mapParcelToResponse(updatedParcel);
    }

    /**
     * Creates and saves a new log entry for a specific action performed on a parcel.
     * This method is a helper to record an employee's interaction with a parcel,
     * such as receiving or delivering it.
     *
     * @param parcel      The {@link Parcel} entity that the action was performed on.
     * @param actionType  The type of action that was performed, from {@link ParcelLogEntryActionType}.
     * @param employeePosition  The unique ID of the {@link Employee} who performed the action.
     * @throws NotFoundException if the employee with the specified ID does not exist.
     */
    private void logParcelAction(Parcel parcel, ParcelLogEntryActionType actionType, EmployeePosition employeePosition, Long postOfficeId) {
        Employee employee = employeeRepository.findFirstByPosition(employeePosition)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + employeePosition));

        PostOffice postOffice = postOfficeRepository.findById(postOfficeId)
                .orElseThrow(() -> new NotFoundException("Post Office not found with ID: " + postOfficeId));

        ParcelLogEntry logEntry = ParcelLogEntry.builder()
                .timestamp(LocalDateTime.now())
                .actionType(actionType)
                .parcel(parcel)
                .employee(employee)
                .postOffice(postOffice)
                .build();

        parcelLogEntryRepository.save(logEntry);
        log.info("Logged action '{}' for parcel '{}' by employee '{}'", actionType, parcel.getTrackingNumber(), EmployeePosition.CLERK);
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
        log.info("Saving parcel with tracking number: {}", parcel.getTrackingNumber());
        return parcelRepository.save(parcel);
    }

    /**
     * A utility method primarily used to demonstrate dependency injection and resolve
     * potential circular dependency issues by being part of the Spring context.
     * It calls a method on an injected component that has no side effects.
     */
    public void methodForTrick() {
        log.info("Calling methodForTrick to demonstrate cyclic dependency handling");
        componentForProduceCycleDependency.doNothing();
    }

    /**
     * Helper method to map a Parcel entity to a ParcelResponse DTO.
     *
     * @param parcel The Parcel entity to map.
     * @return A ParcelResponse DTO.
     */
    private ParcelResponse mapParcelToResponse(Parcel parcel) {
        // Helper to map Client to ClientDto
        ClientDto senderClientDto = null;
        if (parcel.getSenderClient() != null) {
            senderClientDto = ClientDto.builder()
                    .id(parcel.getSenderClient().getId())
                    .firstName(parcel.getSenderClient().getFirstName())
                    .lastName(parcel.getSenderClient().getLastName())
                    .email(parcel.getSenderClient().getEmail())
                    .phone(parcel.getSenderClient().getPhone())
                    .build();
        }

        ClientDto recipientClientDto = null;
        if (parcel.getRecipientClient() != null) {
            recipientClientDto = ClientDto.builder()
                    .id(parcel.getRecipientClient().getId())
                    .firstName(parcel.getRecipientClient().getFirstName())
                    .lastName(parcel.getRecipientClient().getLastName())
                    .email(parcel.getRecipientClient().getEmail())
                    .phone(parcel.getRecipientClient().getPhone())
                    .build();
        }
        return ParcelResponse.builder()
                .id(parcel.getId())
                .trackingNumber(parcel.getTrackingNumber())
                .senderClient(senderClientDto)
                .recipientClient(recipientClientDto)
                .weight(parcel.getWeight())
                .price(parcel.getPrice())
                .status(parcel.getStatus())
                .deliveryType(parcel.getDeliveryType())
                .parcelDescription(parcel.getParcelDescription())
                .originPostOffice(parcel.getOriginPostOffice() != null ? PostOfficeDto.builder()
                        .id(parcel.getOriginPostOffice().getId())
                        .name(parcel.getOriginPostOffice().getName())
                        .city(parcel.getOriginPostOffice().getCity())
                        .postcode(parcel.getOriginPostOffice().getPostcode())
                        .street(parcel.getOriginPostOffice().getStreet())
                        .build() : null)
                .destinationPostOffice(parcel.getDestinationPostOffice() != null ? PostOfficeDto.builder()
                        .id(parcel.getDestinationPostOffice().getId())
                        .name(parcel.getDestinationPostOffice().getName())
                        .city(parcel.getDestinationPostOffice().getCity())
                        .postcode(parcel.getDestinationPostOffice().getPostcode())
                        .street(parcel.getDestinationPostOffice().getStreet())
                        .build() : null)
                .build();
    }
}