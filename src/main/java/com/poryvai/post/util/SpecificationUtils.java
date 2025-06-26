package com.poryvai.post.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

/**
 * A utility class providing static helper methods to easily create common {@link Specification} predicates
 * for use with Spring Data JPA's {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}.
 * This class encapsulates common filtering logic, making dynamic query construction more concise and reusable.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecificationUtils {

    /**
     * Creates a {@link Specification} that filters entities where a specified string field
     * contains a given value, performing a case-insensitive "like" comparison.
     * If the value is null, empty, or blank, it returns a null specification (effectively no filter).
     *
     * @param field The name of the entity's string field to apply the 'like' filter on.
     * @param value The string value to search for within the field (case-insensitive).
     * @param <S> The type of the entity for which the Specification is created.
     * @return A {@link Specification} for a case-insensitive 'like' comparison, or a null specification if the value is invalid.
     */
    public static <S> Specification<S> iLike(String field, String value) {
        if (value == null || value.isBlank()) {
            return Specification.where(null);
        }
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    /**
     * Creates a {@link Specification} that filters entities where a specified field's value
     * is greater than or equal to a given comparable value.
     * If the value is null, it returns a null specification (effectively no filter).
     *
     * @param field The name of the entity's field to apply the 'greater than or equal to' filter on.
     * @param value The comparable value to check against (e.g., Integer, Double, Date).
     * @param <S> The type of the entity for which the Specification is created.
     * @param <T> The type of the comparable value.
     * @return A {@link Specification} for a 'greater than or equal to' comparison, or a null specification if the value is null.
     */
    public static <S, T extends Comparable<? super T>> Specification<S> gte(String field, T value) {
        if (value == null) {
            return Specification.where(null);
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(field), value);
    }

    /**
     * Creates a {@link Specification} that filters entities where a specified field's value
     * is less than or equal to a given comparable value.
     * If the value is null, it returns a null specification (effectively no filter).
     *
     * @param field The name of the entity's field to apply the 'less than or equal to' filter on.
     * @param value The comparable value to check against (e.g., Integer, Double, Date).
     * @param <S> The type of the entity for which the Specification is created.
     * @param <T> The type of the comparable value.
     * @return A {@link Specification} for a 'less than or equal to' comparison, or a null specification if the value is null.
     */
    public static <S, T extends Comparable<? super T>> Specification<S> lte(String field, T value) {
        if (value == null) {
            return Specification.where(null);
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(field), value);
    }

    /**
     * Creates a {@link Specification} that filters entities where a specified field's value
     * is present in a given collection of values. This is equivalent to a SQL 'IN' clause.
     * If the collection is null or empty, it returns a null specification (effectively no filter).
     *
     * @param field The name of the entity's field to apply the 'in' filter on.
     * @param values A collection of values to check for inclusion.
     * @param <S> The type of the entity for which the Specification is created.
     * @param <T> The type of the values in the collection.
     * @return A {@link Specification} for an 'in' comparison, or a null specification if the collection is invalid.
     */
    public static <S, T> Specification<S> in(String field, Collection<? extends T> values) {
        if (values == null || values.isEmpty()) {
            return Specification.where(null);
        }
        return (root, query, cb) -> root.get(field).in(values);
    }
}
