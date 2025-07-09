package com.poryvai.post.service;

import com.poryvai.post.dto.CreateClientRequest;
import com.poryvai.post.model.Client;

import java.util.List;

/**
 * Service interface for managing {@link Client} entities.
 * Defines the contract for basic CRUD operations.
 */
public interface ClientService {

    /**
     * Creates a new client based on the provided request.
     *
     * @param request The {@link CreateClientRequest} containing details for the new client.
     * @return The newly created and persisted {@link Client} object.
     */
    Client create(CreateClientRequest request);

    /**
     * Retrieves a client by its unique ID.
     *
     * @param id The unique ID of the client.
     * @return The {@link Client} object corresponding to the given ID.
     * @throws com.poryvai.post.exception.NotFoundException if no client with the specified ID is found.
     */
    Client getById(Long id);

    /**
     * Retrieves all clients.
     *
     * @return A list of all {@link Client} objects.
     */
    List<Client> getAll();

    /**
     * Updates an existing client identified by its ID.
     *
     * @param id      The unique ID of the client to be updated.
     * @param request The {@link CreateClientRequest} containing the updated details.
     * @return The updated {@link Client} object.
     * @throws com.poryvai.post.exception.NotFoundException if no client with the specified ID is found.
     */
    Client update(Long id, CreateClientRequest request);

    /**
     * Deletes a client by its unique ID.
     *
     * @param id The unique ID of the client to be deleted.
     * @throws com.poryvai.post.exception.NotFoundException if no client with the specified ID is found.
     */
    void delete(Long id);
}
