package com.poryvai.post.service.parcel.price;

import com.poryvai.post.dto.CreateParcelRequest;
import com.poryvai.post.model.DeliveryType;
import org.springframework.stereotype.Component;

/**
 * Implements the {@link PriceCalculator} interface for the {@link DeliveryType#EXPRESS} delivery option.
 * This calculator provides a premium pricing logic, typically reflecting a higher cost
 * due to expedited service.
 */
@Component
public class ExpressDeliveryPriceCalculator implements PriceCalculator {

    /**
     * Calculates the price for a parcel using the express delivery method.
     * The price is determined by multiplying the parcel's weight by a factor of 0.3 and adding a base fee of 600.
     *
     * @param request The {@link CreateParcelRequest} containing the parcel's weight.
     * @return The calculated price for express delivery.
     */
    @Override
    public double calculatePrice(CreateParcelRequest request) {
        // Price calculation logic for Express delivery.
        return request.getWeight() * 0.3 + 600;
    }

    /**
     * Returns the delivery type handled by this calculator, which is {@link DeliveryType#EXPRESS}.
     * This method helps in selecting the appropriate price calculator for a given delivery type.
     *
     * @return {@link DeliveryType#EXPRESS}.
     */
    @Override
    public DeliveryType getDeliveryType() {
        return DeliveryType.EXPRESS;
    }
}
