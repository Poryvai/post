package com.poryvai.post.dto;

import com.poryvai.post.model.DeliveryType;
import com.poryvai.post.model.ParcelDescription;
import com.poryvai.post.model.ParcelStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing full Parcel information in API responses.
 * Includes details of the associated origin and destination Post Offices.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelResponse {
    private Long id;
    private String trackingNumber;
    private String sender;
    private String recipient;
    private Double weight;
    private Double price;
    private ParcelStatus status;
    private DeliveryType deliveryType;
    private ParcelDescription parcelDescription;
    private PostOfficeDto originPostOffice;
    private PostOfficeDto destinationPostOffice;
}
