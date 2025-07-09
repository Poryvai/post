package com.poryvai.post.controller;

import com.poryvai.post.dto.CreateClientRequest;
import com.poryvai.post.model.Client;
import com.poryvai.post.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing client entities.
 * Provides API endpoints for basic CRUD operations on clients.
 */
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {
    private final ClientService clientService;

    /**
     * Creates a new client record in the system.
     *
     * @param request The {@link CreateClientRequest} object containing details for the new client.
     * @return The created {@link Client} object.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@Valid @RequestBody CreateClientRequest request){
        log.info("Received request to create client: {} {}", request.getFirstName(), request.getLastName());
        return clientService.create(request);
    }

    @GetMapping("/{id}")
    public Client getById(@PathVariable Long id){
        log.info("Received request to get client by ID: {}", id);
        return clientService.getById(id);
    }

    /**
     * Retrieves all clients.
     *
     * @return A list of all {@link Client} objects.
     */
    @GetMapping
    public List<Client> getAll(){
        log.info("Received request to get all clients");
        return clientService.getAll();
    }

    /**
     * Updates an existing client identified by its ID.
     *
     * @param id      The unique ID of the client to update, extracted from the URL path.
     * @param request The {@link CreateClientRequest} object containing the updated details.
     * @return The updated {@link Client} object.
     */
    @PutMapping("/{id}")
    public Client update(@PathVariable Long id, @Valid @RequestBody CreateClientRequest request){
        log.info("Received request to update client with ID {}: {} {}", id, request.getFirstName(), request.getLastName());
        return clientService.update(id, request);
    }

    /**
     * Deletes a client by its unique ID.
     *
     * @param id The unique ID of the client to delete, extracted from the URL path.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        log.info("Received request to delete client by ID: {}", id);
        clientService.delete(id);
    }
}
