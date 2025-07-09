package com.poryvai.post.service;

import com.poryvai.post.dto.CreateClientRequest;
import com.poryvai.post.exception.NotFoundException;
import com.poryvai.post.model.Client;
import com.poryvai.post.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Client create(CreateClientRequest request) {
        log.info("Creating new client: {} {}", request.getFirstName(), request.getLastName());
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        return clientRepository.save(client);
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
    public List<Client> getAll() {
        log.info("Fetching all clients");
        return clientRepository.findAll();
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
    }
}
