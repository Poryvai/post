package com.poryvai.post.service.parcel.price;

import com.poryvai.post.dto.CreateParcelRequest;
import com.poryvai.post.model.DeliveryType;
import org.springframework.stereotype.Component;

/**
 * Implements the {@link PriceCalculator} interface for the {@link DeliveryType#DEFAULT} delivery option.
 * This calculator provides the standard pricing logic based solely on the parcel's weight.
 */
@Component
public class DefaultDeliveryPriceCalculator implements PriceCalculator {

    /**
     * Calculates the price for a parcel using the default delivery method.
     * The price is determined by multiplying the parcel's weight by a factor of 0.2 and adding a base fee of 400.
     *
     * @param request The {@link CreateParcelRequest} containing the parcel's weight.
     * @return The calculated price for default delivery.
     */
    @Override
    public double calculatePrice(CreateParcelRequest request) {
        // Price calculation logic for default delivery.
        return request.getWeight() * 0.2 + 400;
    }


    /**
     * Returns the delivery type handled by this calculator, which is {@link DeliveryType#DEFAULT}.
     *
     * @return {@link DeliveryType#DEFAULT}.
     */
    @Override
    public DeliveryType getDeliveryType() {
        return DeliveryType.DEFAULT;
    }
}
