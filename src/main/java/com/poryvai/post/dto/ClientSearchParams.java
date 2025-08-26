package com.poryvai.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for specifying search parameters when querying clients.
 * Allows for dynamic filtering based on various client attributes
 * and their parcel activity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientSearchParams {
    /**
     * The ID of the client to search for.
     */
    private Long clientId;

    /**
     * The first name of the client to search for.
     */
    private String firstName;

    /**
     * The last name of the client to search for.
     */
    private String lastName;

    /**
     * The email address of the client to search for.
     */
    private String email;

    /**
     * The phone number of the client to search for.
     */
    private String phone;

    /**
     * Minimum number of parcels sent by the client (inclusive).
     */
    private Integer minSentParcels;

    /**
     * Maximum number of parcels sent by the client (inclusive).
     */
    private Integer maxSentParcels;

    /**
     * Minimum number of parcels received by the client (inclusive).
     */
    private Integer minReceivedParcels;

    /**
     * Maximum number of parcels received by the client (inclusive).
     */
    private Integer maxReceivedParcels;

    /**
     * Search for clients who were senders for a specific recipient client ID.
     */
    private Long specificRecipientClientId;

    /**
     * Search for clients who were recipients from a specific sender client ID.
     */
    private Long specificSenderClientId;

    /**
     * Filter clients based on the time of their last activity (e.g., last sent or received parcel).
     * The last activity should be after this timestamp.
     */
    private LocalDateTime lastActivityAfter;

    /**
     * Filter clients based on the time of their last activity.
     * The last activity should be before this timestamp.
     */
    private LocalDateTime lastActivityBefore;
}
