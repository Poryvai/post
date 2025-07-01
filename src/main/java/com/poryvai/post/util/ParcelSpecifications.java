package com.poryvai.post.util;

import com.poryvai.post.dto.ParcelSearchParams;
import com.poryvai.post.model.DeliveryType;
import com.poryvai.post.model.Parcel;
import com.poryvai.post.model.ParcelStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building dynamic {@link Specification} objects for querying {@link Parcel} entities.
 * This class provides a flexible way to construct database queries based on various search criteria
 * defined in {@link ParcelSearchParams}. It allows for combining multiple optional filters
 * (like tracking number, sender, recipient, weight/price ranges, statuses, and delivery types)
 * into a single query.
 */
public class ParcelSpecifications {

    /**
     * Creates a {@link Specification} that dynamically applies filters to a {@link Parcel} query
     * based on the provided {@link ParcelSearchParams}.
     * This method constructs a list of {@link Predicate} objects, each representing a
     * specific search condition, and combines them using a logical AND operator.
     *
     * @param params The {@link ParcelSearchParams} object containing various criteria for filtering parcels.
     * Fields with non-null and non-empty values will be used to build the query.
     * @return A {@link Specification} instance that can be passed to a JPA repository
     * (e.g., to methods provided by {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor JpaSpecificationExecutor})
     * to retrieve a filtered set of parcels.
     */
    public static Specification<Parcel> withDynamicQuery(ParcelSearchParams params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filter by trackingNumber
            if (params.getTrackingNumber() != null && !params.getTrackingNumber().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("trackingNumber"), params.getTrackingNumber()));
            }

            // 2. Filter by sender
            if (params.getSender() != null && !params.getSender().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("sender")), "%" + params.getSender().toLowerCase() + "%"));
            }

            // 3.Filter by recipient
            if (params.getRecipient() != null && !params.getRecipient().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("recipient")), "%" + params.getRecipient().toLowerCase() + "%"));
            }

            // 4. Filter by weight (fromWeight, toWeight)
            if (params.getFromWeight() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("weight"), params.getFromWeight()));
            }
            if (params.getToWeight() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("weight"), params.getToWeight()));
            }

            // 5. Filter by price (fromPrice, toPrice)
            if (params.getFromPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), params.getFromPrice()));
            }
            if (params.getToPrice() != null) { // <-- ИЗМЕНЕНО: getPriceTo() на getToPrice()
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), params.getToPrice()));
            }

            // 6. Filter by statuses
            if (params.getStatuses() != null && !params.getStatuses().isEmpty()) {
                predicates.add(root.get("status").in(params.getStatuses()));
            }

            // 7. Filter by deliveryTypes
            if (params.getDeliveryTypes() != null && !params.getDeliveryTypes().isEmpty()) {
                predicates.add(root.get("deliveryType").in(params.getDeliveryTypes()));
            }

            // 8. Filter by parcel descriptions
            if (params.getParcelDescriptions() != null && !params.getParcelDescriptions().isEmpty()) {
                predicates.add(root.get("parcelDescription").in(params.getParcelDescriptions()));
            }

            // Combine all collected predicates with an AND logical operator.
            // If no predicates, it will result in a query that fetches all records (effectively 'true').
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}