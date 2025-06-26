package com.poryvai.post.model;

/**
 * Represents the different types of delivery services available for parcels.
 * Each type has an associated impact on price calculation.
 */
public enum DeliveryType {

    /**
     * Standard delivery type.
     */
    EXPRESS,
    /**
     * Expedited delivery type, typically costing more.
     */
    DEFAULT,
    /**
     * Economical delivery type, typically costing less.
     */
    ECONOM,
}
