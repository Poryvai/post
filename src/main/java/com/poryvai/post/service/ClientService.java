package com.poryvai.post.service;

import com.poryvai.post.dto.ClientResponse;
import com.poryvai.post.dto.ClientSearchParams;
import com.poryvai.post.dto.CreateClientRequest;
import com.poryvai.post.exception.NotFoundException;
import com.poryvai.post.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @throws NotFoundException if no client with the specified ID is found.
     */
    Client getById(Long id);

    /**
     * Retrieves all clients.
     *
     * @return A list of all {@link Client} objects.
     */
    List<ClientResponse> getAll();

    /**
     * Updates an existing client identified by its ID.
     *
     * @param id      The unique ID of the client to be updated.
     * @param request The {@link CreateClientRequest} containing the updated details.
     * @return The updated {@link Client} object.
     * @throws NotFoundException if no client with the specified ID is found.
     */
    Client update(Long id, CreateClientRequest request);

    /**
     * Deletes a client by its unique ID.
     *
     * @param id The unique ID of the client to be deleted.
     * @throws NotFoundException if no client with the specified ID is found.
     */
    void delete(Long id);

    /**
     * Searches for clients based on dynamic criteria.
     *
     * @param params   The search parameters as defined in {@link ClientSearchParams}.
     * @param pageable The pagination and sorting information.
     * @return A {@link Page} of clients that match the search criteria.
     */
    Page<ClientResponse> searchClients(ClientSearchParams params, Pageable pageable);
}

