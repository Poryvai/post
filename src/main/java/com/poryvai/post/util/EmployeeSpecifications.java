package com.poryvai.post.util;

import com.poryvai.post.dto.EmployeeSearchParams;
import com.poryvai.post.dto.ParcelSearchParams;
import com.poryvai.post.model.Employee;
import com.poryvai.post.model.Parcel;
import com.poryvai.post.model.ParcelLogEntry;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building dynamic {@link Specification} objects for querying {@link Employee} entities.
 * This class provides a flexible way to construct database queries based on various search criteria
 * defined in {@link EmployeeSearchParams}. It allows for combining multiple optional filters
 * into a single query.
 */
public class EmployeeSpecifications {
    /**
     * Creates a {@link Specification} that dynamically applies filters to an {@link Employee} query
     * based on the provided {@link EmployeeSearchParams}.
     * This method constructs a list of {@link Predicate} objects, each representing a
     * specific search condition, and combines them using a logical AND operator.
     *
     * @param params The {@link EmployeeSearchParams} object containing various criteria for filtering employees.
     * Fields with non-null and non-empty values will be used to build the query.
     * @return A {@link Specification} instance that can be passed to a JPA repository
     * to retrieve a filtered set of employees.
     */
    public static Specification<Employee> withDynamicQuery(EmployeeSearchParams params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> havingPredicates = new ArrayList<>();
            Join<Employee, ParcelLogEntry> logEntriesJoin = null;

            // 1. Filter by basic employee attributes
            if (params.getEmployeeId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), params.getEmployeeId()));
            }
            if (params.getFirstName() != null && !params.getFirstName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + params.getFirstName() + "%"));
            }
            if (params.getLastName() != null && !params.getLastName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + params.getLastName() + "%"));
            }
            if (params.getPosition() != null) {
                predicates.add(criteriaBuilder.equal(root.get("position"), params.getPosition()));
            }
            if (params.getPostOfficeId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("postOffice").get("id"), params.getPostOfficeId()));
            }

            // 2. Logic for filtering by count and/or activity time
            boolean hasCountFilter = params.getMinProcessedParcels() != null || params.getMaxProcessedParcels() != null;
            boolean hasDateFilter = params.getLastActivityAfter() != null || params.getLastActivityBefore() != null;

            if (hasCountFilter || hasDateFilter) {
                logEntriesJoin = root.join("parcelLogEntries");

                if (params.getLastActivityAfter() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(logEntriesJoin.get("timestamp"), params.getLastActivityAfter()));
                }
                if (params.getLastActivityBefore() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(logEntriesJoin.get("timestamp"), params.getLastActivityBefore()));
                }
            }

            // 3.  Count and filter (GROUP BY and HAVING)
            if (hasCountFilter) {
                query.groupBy(root.get("id"));

                if (params.getMinProcessedParcels() != null) {
                    havingPredicates.add(criteriaBuilder.ge(criteriaBuilder.countDistinct(logEntriesJoin.get("parcel")), params.getMinProcessedParcels()));
                }
                if (params.getMaxProcessedParcels() != null) {
                    havingPredicates.add(criteriaBuilder.le(criteriaBuilder.countDistinct(logEntriesJoin.get("parcel")), params.getMaxProcessedParcels()));
                }
            }

            if (!hasCountFilter && hasDateFilter) {
                query.distinct(true);
            }

            // 4. Combine all predicate
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            if (!havingPredicates.isEmpty()) {
                query.having(criteriaBuilder.and(havingPredicates.toArray(new Predicate[0])));
            }

            return query.getRestriction();
        };
    }
}
