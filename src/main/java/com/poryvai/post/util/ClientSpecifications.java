package com.poryvai.post.util;

import com.poryvai.post.dto.ClientSearchParams;
import com.poryvai.post.model.Client;
import com.poryvai.post.model.Parcel;
import com.poryvai.post.model.ParcelLogEntry;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building dynamic {@link Specification} objects for querying {@link Client} entities.
 * This class provides a flexible way to construct database queries based on various search criteria
 * defined in {@link ClientSearchParams}. It allows for combining multiple optional filters
 * into a single query.
 */
public class ClientSpecifications {

    /**
     * Creates a {@link Specification} that dynamically applies filters to a {@link Client} query
     * based on the provided {@link ClientSearchParams}.
     * This method constructs a list of {@link Predicate} objects, each representing a
     * specific search condition, and combines them using a logical AND operator.
     *
     * @param params The {@link ClientSearchParams} object containing various criteria for filtering clients.
     * Fields with non-null and non-empty values will be used to build the query.
     * @return A {@link Specification} instance that can be passed to a JPA repository
     * to retrieve a filtered set of clients.
     */
    public static Specification<Client> withDynamicQuery(ClientSearchParams params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> havingPredicates = new ArrayList<>();

            // 1. Filter by basic client attributes
            if (params.getClientId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), params.getClientId()));
            }
            if (params.getFirstName() != null && !params.getFirstName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + params.getFirstName() + "%"));
            }
            if (params.getLastName() != null && !params.getLastName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + params.getLastName() + "%"));
            }
            if (params.getEmail() != null && !params.getEmail().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + params.getEmail() + "%"));
            }
            if (params.getPhone() != null && !params.getPhone().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + params.getPhone() + "%"));
            }

            // 2. Filter by sent and received parcel counts
            if (params.getMinSentParcels() != null || params.getMaxSentParcels() != null) {
                Join<Client, Parcel> sentParcelsJoin = root.join("sentParcels");
                query.groupBy(root.get("id")); // Group by client to count parcels

                if (params.getMinSentParcels() != null) {
                    havingPredicates.add(criteriaBuilder.ge(criteriaBuilder.count(sentParcelsJoin), params.getMinSentParcels()));
                }
                if (params.getMaxSentParcels() != null) {
                    havingPredicates.add(criteriaBuilder.le(criteriaBuilder.count(sentParcelsJoin), params.getMaxSentParcels()));
                }
            }

            if (params.getMinReceivedParcels() != null || params.getMaxReceivedParcels() != null) {
                Join<Client, Parcel> receivedParcelsJoin = root.join("receivedParcels");
                query.groupBy(root.get("id"));

                if (params.getMinReceivedParcels() != null) {
                    havingPredicates.add(criteriaBuilder.ge(criteriaBuilder.count(receivedParcelsJoin), params.getMinReceivedParcels()));
                }
                if (params.getMaxReceivedParcels() != null) {
                    havingPredicates.add(criteriaBuilder.le(criteriaBuilder.count(receivedParcelsJoin), params.getMaxReceivedParcels()));
                }
            }

            // 3. Filter by specific sender/recipient
            if (params.getSpecificRecipientClientId() != null) {
                Join<Client, Parcel> sentParcelsJoin = root.join("sentParcels");
                predicates.add(criteriaBuilder.equal(sentParcelsJoin.get("recipientClient").get("id"), params.getSpecificRecipientClientId()));
            }

            if (params.getSpecificSenderClientId() != null) {
                Join<Client, Parcel> receivedParcelsJoin = root.join("receivedParcels");
                predicates.add(criteriaBuilder.equal(receivedParcelsJoin.get("senderClient").get("id"), params.getSpecificSenderClientId()));
            }

            // 4. Filter by last activity date range
            if (params.getLastActivityAfter() != null || params.getLastActivityBefore() != null) {
                query.distinct(true);

                // Create a single predicate for "sent or received"
                 Join<Client, Parcel> sentParcelsJoin = root.join("sentParcels");
                 Join<Client, Parcel> receivedParcelsJoin = root.join("receivedParcels");

                Predicate sentActivityPredicate = criteriaBuilder.conjunction();
                Predicate receivedActivityPredicate = criteriaBuilder.conjunction();

                if (params.getLastActivityAfter() != null) {
                    Join<Parcel, ParcelLogEntry> sentLogEntryJoin = sentParcelsJoin.join("parcelLogEntries");
                    sentActivityPredicate = criteriaBuilder.and(sentActivityPredicate, criteriaBuilder.greaterThanOrEqualTo(sentLogEntryJoin.get("timestamp"), params.getLastActivityAfter()));

                    Join<Parcel, ParcelLogEntry> receivedLogEntryJoin = receivedParcelsJoin.join("parcelLogEntries");
                    receivedActivityPredicate = criteriaBuilder.and(receivedActivityPredicate, criteriaBuilder.greaterThanOrEqualTo(receivedLogEntryJoin.get("timestamp"), params.getLastActivityAfter()));
                }

                if (params.getLastActivityBefore() != null) {
                    Join<Parcel, ParcelLogEntry> sentLogEntryJoin = sentParcelsJoin.join("parcelLogEntries");
                    sentActivityPredicate = criteriaBuilder.and(sentActivityPredicate, criteriaBuilder.lessThanOrEqualTo(sentLogEntryJoin.get("timestamp"), params.getLastActivityBefore()));

                    Join<Parcel, ParcelLogEntry> receivedLogEntryJoin = receivedParcelsJoin.join("parcelLogEntries");
                    receivedActivityPredicate = criteriaBuilder.and(receivedActivityPredicate, criteriaBuilder.lessThanOrEqualTo(receivedLogEntryJoin.get("timestamp"), params.getLastActivityBefore()));
                }

                // Add the combined activity predicates to the main predicates list
                predicates.add(criteriaBuilder.or(sentActivityPredicate, receivedActivityPredicate));
            }

            // Combine all collected predicates
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            if (!havingPredicates.isEmpty()) {
                query.having(criteriaBuilder.and(havingPredicates.toArray(new Predicate[0])));
            }

            return query.getRestriction();
        };
    }
}
