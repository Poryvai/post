package com.poryvai.post.model;

/**
 * Represents the current status of a parcel in the delivery lifecycle.
 */
public enum ParcelStatus {

    /**
     * The parcel has been created and is awaiting pickup or initial processing.
     */
    CREATED,
    /**
     * The parcel is currently being transported to its destination.
     */
    IN_TRANSIT,
    /**
     * The parcel has successfully reached its recipient.
     */
    DELIVERED,
}
