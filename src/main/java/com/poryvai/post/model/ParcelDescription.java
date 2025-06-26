package com.poryvai.post.model;

/**
 * Represents the category or type of contents within a parcel.
 * This helps in classifying parcels for various purposes like logistics,
 * customs declarations, or statistical analysis.
 */
public enum ParcelDescription {

    /**
     * The parcel primarily contains clothing items.
     */
    CLOTHES,
    /**
     * The parcel primarily contains automotive or machinery spare parts.
     */
    SPARE_PARTS,
    /**
     * The parcel primarily contains food items, groceries, or perishable goods.
     */
    GROCERIES,
    /**
     * The parcel primarily contains books, magazines, or other printed materials.
     */
    BOOKS,
    /**
     * The parcel primarily contains pharmaceutical products or medical supplies.
     */
    MEDICATIONS,
    /**
     * The parcel primarily contains large or small appliances for home use.
     */
    HOME_APPLIANCES,
    /**
     * The parcel contains miscellaneous items not covered by other specific categories.
     */
    MISCELLANEOUS
}
