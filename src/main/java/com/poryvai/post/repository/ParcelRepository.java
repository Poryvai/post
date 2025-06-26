package com.poryvai.post.repository;

import com.poryvai.post.model.Parcel;
import com.poryvai.post.model.DeliveryType;
import com.poryvai.post.model.ParcelStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Parcel} entities.
 * Provides standard CRUD (Create, Read, Update, Delete) operations out-of-the-box
 * and supports querying using JPA Specifications for dynamic filtering.
 */
@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long>,
        JpaSpecificationExecutor<Parcel> {

    /**
     * Finds a {@link Parcel} by its unique tracking number.
     *
     * @param trackingNumber The unique tracking number of the parcel as a String.
     * @return An {@link Optional} containing the found {@link Parcel}, or an empty Optional if no parcel is found.
     */
    Optional<Parcel> findByTrackingNumber(String trackingNumber);


}
