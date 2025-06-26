package com.poryvai.post.service.parcel.price;

import com.poryvai.post.dto.CreateParcelRequest;
import com.poryvai.post.model.DeliveryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implements the {@link PriceCalculator} interface for the {@link DeliveryType#ECONOM} delivery option.
 * This calculator provides a cost-effective pricing logic, specifically designed for
 * economical parcel delivery based on weight.
 */
@Component
@RequiredArgsConstructor
public class EconomDeliveryPriceCalculator implements PriceCalculator {

    /**
     * Calculates the price for a parcel using the econom delivery method.
     * The price is determined by multiplying the parcel's weight by a factor of 0.1 and adding a base fee of 200.
     *
     * @param request The {@link CreateParcelRequest} containing the parcel's weight.
     * @return The calculated price for econom delivery.
     */
    @Override
    public double calculatePrice(CreateParcelRequest request) {
        // Price calculation logic for Econom delivery.
        return request.getWeight() * 0.1 + 200;
    }


    /**
     * Returns the delivery type handled by this calculator, which is {@link DeliveryType#ECONOM}.
     * This method helps in selecting the appropriate price calculator for a given delivery type.
     *
     * @return {@link DeliveryType#ECONOM}.
     */
    @Override
    public DeliveryType getDeliveryType() {
        return DeliveryType.ECONOM;
    }
}
