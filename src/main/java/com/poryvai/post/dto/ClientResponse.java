package com.poryvai.post.dto;

import com.poryvai.post.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for representing a client's information in API responses.
 * This DTO is used to send client data to the client-side, excluding sensitive or
 * lazy-loaded information like associated parcels.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    /**
     * Creates a ClientResponse DTO from a Client entity.
     *
     * @param client The Client entity to convert.
     * @return A new ClientResponse object.
     */
    public static ClientResponse fromEntity(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .build();
    }
}
