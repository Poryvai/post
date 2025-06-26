package com.poryvai.post.service.parcel.price;

import com.poryvai.post.dto.CreateParcelRequest;
import com.poryvai.post.model.DeliveryType;

/**
 * Interface for defining strategies to calculate the price of a parcel based on its attributes.
 * Implementations of this interface provide specific pricing logic for different delivery types.
 */
public interface PriceCalculator {

    /**
     * Calculates the delivery price for a parcel based on the provided request details.
     *
     * @param request The {@link CreateParcelRequest} containing parcel details like weight and potentially delivery type.
     * @return The calculated price for the parcel.
     */
    double calculatePrice(CreateParcelRequest request);

    /**
     * Returns the specific {@link DeliveryType} that this calculator strategy handles.
     * This method is used to select the appropriate calculator for a given delivery type.
     *
     * @return The {@link DeliveryType} associated with this price calculator.
     */
    DeliveryType getDeliveryType();
}
