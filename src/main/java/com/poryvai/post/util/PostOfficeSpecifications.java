package com.poryvai.post.util;

import com.poryvai.post.model.PostOffice;
import com.poryvai.post.dto.PostOfficeSearchParams;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building JPA Specifications for PostOffice entities.
 * Provides methods to create dynamic queries based on PostOfficeSearchParams.
 */
public class PostOfficeSpecifications {

    /**
     * Creates a Specification for filtering PostOffice entities based on provided search parameters.
     *
     * @param params The {@link PostOfficeSearchParams} containing the criteria.
     * @return A {@link Specification} that can be used with Spring Data JPA repositories.
     */
    public static Specification<PostOffice> bySearchParams(PostOfficeSearchParams params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filter by name (case-insensitive, like)
            if (StringUtils.hasText(params.getName())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + params.getName().toLowerCase() + "%"));
            }

            // 2. Filter by city (case-insensitive, like)
            if (StringUtils.hasText(params.getCity())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("city")),
                        "%" + params.getCity().toLowerCase() + "%"));
            }

            // 3. Filter by postcode (case-insensitive, like)
            if (StringUtils.hasText(params.getPostcode())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("postcode")),
                        "%" + params.getPostcode().toLowerCase() + "%"));
            }

            // 4. Filter by street (case-insensitive, like)
            if (StringUtils.hasText(params.getStreet())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("street")),
                        "%" + params.getStreet().toLowerCase() + "%"));
            }

            // Combine all collected predicates with an AND logical operator.
            // If no predicates, it will result in a query that fetches all records (effectively 'true').
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
