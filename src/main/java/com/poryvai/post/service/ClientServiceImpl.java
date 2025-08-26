package com.poryvai.post.service;

import com.poryvai.post.dto.ClientResponse;
import com.poryvai.post.dto.ClientSearchParams;
import com.poryvai.post.dto.CreateClientRequest;
import com.poryvai.post.exception.NotFoundException;
import com.poryvai.post.model.Client;
import com.poryvai.post.repository.ClientRepository;
import com.poryvai.post.util.ClientSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing {@link Client} entities.
 * Provides concrete implementations for basic CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;

    /**
     * Creates a new client based on the provided request.
     *
     * @param request The {@link CreateClientRequest} containing details for the new client.
     * @return The newly created and persisted {@link Client} object.
     */
    @Override
    @Transactional
    public Client create(CreateClientRequest request) {
        log.info("Creating new client with first name: {}, last name: {}", request.getFirstName(), request.getLastName());
        Client client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        Client savedClient = clientRepository.save(client);
        log.info("Client created successfully with ID: {}", savedClient.getId());
        return savedClient;
    }

    /**
     * Retrieves a client by its unique ID.
     *
     * @param id The unique ID of the client.
     * @return The {@link Client} object corresponding to the given ID.
     * @throws NotFoundException if no client with the specified ID is found.
     */
    @Override
    @Transactional(readOnly = true)
    public Client getById(Long id) {
        log.info("Fetching client by ID: {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client with ID " + id + " not found"));
    }

    /**
     * Retrieves all clients.
     *
     * @return A list of all {@link Client} objects.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClientResponse> getAll() {
        log.info("Fetching all clients");
        return clientRepository.findAll().stream()
                .map(ClientResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing client identified by its ID.
     *
     * @param id      The unique ID of the client to be updated.
     * @param request The {@link CreateClientRequest} containing the updated details.
     * @return The updated {@link Client} object.
     * @throws NotFoundException if no client with the specified ID is found.
     */
    @Override
    @Transactional
    public Client update(Long id, CreateClientRequest request) {
        log.info("Updating client with ID {}: {} {}", id, request.getFirstName(), request.getLastName());
        return clientRepository.findById(id)
                .map(existingClient -> {
                    existingClient.setFirstName(request.getFirstName());
                    existingClient.setLastName(request.getLastName());
                    existingClient.setEmail(request.getEmail());
                    existingClient.setPhone(request.getPhone());

                    log.info("Client with ID: {} updated successfully.", id);
                    return clientRepository.save(existingClient);
                })
                .orElseThrow(() -> new NotFoundException("Client with ID " + id + " not found for update"));
    }

    /**
     * Deletes a client by its unique ID.
     *
     * @param id The unique ID of the client to be deleted.
     * @throws NotFoundException if no client with the specified ID is found.
     */
    @Override
    public void delete(Long id) {
        log.info("Deleting client by ID: {}", id);
        if (!clientRepository.existsById(id)) {
            throw new NotFoundException("Client with ID " + id + " not found");
        }
        clientRepository.deleteById(id);
        log.info("Client with ID: {} deleted successfully.", id);
    }

    /**
     * Searches for clients based on dynamic criteria.
     *
     * @param params   The search parameters as defined in {@link ClientSearchParams}.
     * @param pageable The pagination and sorting information.
     * @return A {@link Page} of clients that match the search criteria.
     */
    @Override
    public Page<ClientResponse> searchClients(ClientSearchParams params, Pageable pageable) {
        log.info("Searching clients with parameters: {}", params);
        Page<Client> clientPage = clientRepository.findAll(ClientSpecifications.withDynamicQuery(params), pageable);
        return clientPage.map(ClientResponse::fromEntity);
    }
}
