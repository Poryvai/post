package com.poryvai.post.model;

/**
 * Enum representing the types of actions an employee can perform on a parcel.
 */
public enum ParcelLogEntryActionType {

    /**
     * The parcel has been received by an employee at a post office.
     */
    RECEIVED,

    /**
     * The parcel has been sent from one post office to another.
     */
    SENT,

    /**
     * The parcel has been successfully delivered to the recipient.
     */
    DELIVERED,
}
